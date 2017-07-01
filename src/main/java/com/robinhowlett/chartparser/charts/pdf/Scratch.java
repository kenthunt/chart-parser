package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonCreator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and stores the horse(s) scratched from the race and, if available, for what reason. If the
 * scratch still earned part of the purse for simply entering, that value is not stored.
 */
public class Scratch {

    private static final Pattern SCRATCHED_HORSES =
            Pattern.compile("Scratched Horse\\(s\\):.+");
    private static final Pattern SCRATCHED_HORSE =
            Pattern.compile("(.+?(\\s\\([A-Z]{2,3}\\))?)\\s\\((.*?)\\)(\\s\\(Earned .+\\))?");

    private static final Logger LOGGER = LoggerFactory.getLogger(Scratch.class);

    private final Horse horse;
    private final String reason;

    @JsonCreator
    public Scratch(Horse horse, String reason) {
        this.horse = horse;
        this.reason = reason;
    }

    public static List<Scratch> parse(List<List<ChartCharacter>> lines) {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            Matcher matcher = SCRATCHED_HORSES.matcher(text);
            if (matcher.find()) {
                return parseScratchedHorses(text);
            }
        }
        return Collections.emptyList();
    }

    static List<Scratch> parseScratchedHorses(String text) {
        text = text.substring(text.indexOf('|') + 1).replaceAll(System.lineSeparator(), " ");
        List<String> scratchedHorses = Arrays.asList(text.split(","));

        List<Scratch> scratches = new ArrayList<>();
        for (String scratchedHorse : scratchedHorses) {
            Scratch scratch = parseScratchedHorse(scratchedHorse);
            if (scratch != null) {
                scratches.add(scratch);
            }
        }

        return scratches;
    }

    static Scratch parseScratchedHorse(String scratchedHorse) {
        Matcher matcher = SCRATCHED_HORSE.matcher(scratchedHorse);
        if (matcher.find()) {
            String horseName = matcher.group(1).trim();
            String reason = matcher.group(3);
            if (reason == null || reason.isEmpty()) {
                reason = "N/A";
            } else {
                reason = reason.trim();
            }
            return new Scratch(new Horse(horseName), reason);
        }

        LOGGER.warn(String.format("Unable to parse a valid scratched horse from text: %s",
                scratchedHorse));
        return null;
    }

    public Horse getHorse() {
        return horse;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scratch scratch = (Scratch) o;

        if (horse != null ? !horse.equals(scratch.horse) : scratch.horse != null)
            return false;
        return reason != null ? reason.equals(scratch.reason) : scratch.reason == null;
    }

    @Override
    public String toString() {
        return "Scratch{" +
                "horse='" + horse + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = horse != null ? horse.hashCode() : 0;
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}
