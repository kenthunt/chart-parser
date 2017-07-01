package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and stores the {@link Starter}s who were disqualified and given a new finishing position
 */
public class Disqualification {

    private static final Pattern DISQUALIFICATIONS = Pattern.compile("Disqualification\\(s\\):.+");
    private static final Pattern DISQUALIFICATION =
            Pattern.compile("#\\s?(\\d+\\w?)? (.+) from (\\d+) to (\\d+)");

    private final String program;
    private final Horse horse;
    private final int originalPosition;
    private final int newPosition;

    public Disqualification(String program, Horse horse, int originalPosition,
            int newPosition) {
        this.program = program;
        this.horse = horse;
        this.originalPosition = originalPosition;
        this.newPosition = newPosition;
    }

    public static List<Disqualification> parse(List<List<ChartCharacter>> lines)
            throws ChartParserException {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            Matcher matcher = DISQUALIFICATIONS.matcher(text);
            if (matcher.find()) {
                return parseDisqualifications(text);
            }
        }
        return Collections.emptyList();
    }

    static List<Disqualification> parseDisqualifications(String text) throws ChartParserException {
        text = text.substring(text.indexOf('|') + 1);
        List<String> dqs = Arrays.asList(text.split(System.lineSeparator()));

        List<Disqualification> disqualifications = new ArrayList<>();
        for (String dq : dqs) {
            Disqualification disqualifiedHorse = parseDisqualification(dq);
            disqualifications.add(disqualifiedHorse);
        }

        return disqualifications;
    }

    static Disqualification parseDisqualification(String text) throws ChartParserException {
        Matcher matcher = DISQUALIFICATION.matcher(text);
        if (matcher.find()) {
            String program = matcher.group(1);
            String horseName = matcher.group(2);
            int originalPosition = Integer.parseInt(matcher.group(3));
            int newPosition = Integer.parseInt(matcher.group(4));
            return new Disqualification(program, new Horse(horseName), originalPosition,
                    newPosition);
        }
        throw new ChartParserException("Could not parse disqualified horse: " + text);
    }

    public String getProgram() {
        return program;
    }

    public Horse getHorse() {
        return horse;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public int getNewPosition() {
        return newPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Disqualification that = (Disqualification) o;

        if (originalPosition != that.originalPosition) return false;
        if (newPosition != that.newPosition) return false;
        if (program != null ? !program.equals(that.program) : that.program != null) return false;
        return horse != null ? horse.equals(that.horse) : that.horse == null;
    }

    @Override
    public int hashCode() {
        int result = program != null ? program.hashCode() : 0;
        result = 31 * result + (horse != null ? horse.hashCode() : 0);
        result = 31 * result + originalPosition;
        result = 31 * result + newPosition;
        return result;
    }

    @Override
    public String toString() {
        return "Disqualification{" +
                "program='" + program + '\'' +
                ", horse='" + horse + '\'' +
                ", originalPosition=" + originalPosition +
                ", newPosition=" + newPosition +
                '}';
    }
}
