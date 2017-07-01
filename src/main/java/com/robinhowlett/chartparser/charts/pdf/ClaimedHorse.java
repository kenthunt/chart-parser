package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.exceptions.ChartParserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and stores the horse, trainer, and owner name(s) for {@link Starter}s that were claimed
 */
public class ClaimedHorse {

    private static final Pattern CLAIMED_HORSES = Pattern.compile("(\\d+) Claimed Horse\\(s\\).+");
    private static final Pattern CLAIMED_HORSE =
            Pattern.compile("([0-9a-zA-Z\\s\\-\\.\\'\\(\\)]+)[\\||\\s]New Trainer:( (.+))" +
                    "?[\\||\\s]New Owner:( (.+))?");

    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimedHorse.class);

    private final Horse horse;
    private final String newTrainerName;
    private String newOwnerName;

    public ClaimedHorse(Horse horse, String newTrainerName, String newOwnerName) {
        this.horse = horse;
        this.newTrainerName = newTrainerName;
        this.newOwnerName = newOwnerName;
    }

    public static List<ClaimedHorse> parse(List<List<ChartCharacter>> lines) {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            Matcher matcher = CLAIMED_HORSES.matcher(text);
            if (matcher.find()) {
                return parseClaimedHorses(text);
            }
        }
        return Collections.emptyList();
    }

    static List<ClaimedHorse> parseClaimedHorses(String text) {
        text = text.substring(text.indexOf('|') + 1);
        List<String> rawClaims = Arrays.asList(text.split(System.lineSeparator()));

        // claimed horses layout can be messy, so try to join them up together as best we can
        List<String> claims = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String claim : rawClaims) {
            sb = sb.append(claim);
            if (claim.contains("New Owner:")) {
                claims.add(sb.toString());
                sb = new StringBuilder();
            }
        }

        List<ClaimedHorse> claimedHorses = new ArrayList<>();
        ClaimedHorse previous = null;
        for (String claim : claims) {
            ClaimedHorse claimedHorse = parseClaimedHorse(claim, previous);
            if (claimedHorse != null && !claimedHorse.equals(previous)) {
                claimedHorses.add(claimedHorse);
            }
            previous = claimedHorse;
        }

        if (claimedHorses.isEmpty()) {
            LOGGER.warn(String.format("No claimed horses were successfully parsed from text: %s",
                    text));
        }

        return claimedHorses;
    }

    private static ClaimedHorse parseClaimedHorse(String claim, ClaimedHorse previous) {
        Matcher matcher = CLAIMED_HORSE.matcher(claim);
        if (matcher.find()) {
            String horseName = matcher.group(1);
            String newTrainerName = matcher.group(3);
            String newOwnerName = matcher.group(5);
            return new ClaimedHorse(new Horse(horseName), newTrainerName, newOwnerName);
        } else {
            // handle the claim information being wrapped and covering multiple lines
            if (previous != null) {
                previous.setNewOwnerName(previous.getNewOwnerName() + " " + claim);
                return previous;
            }
        }
        return null;
    }

    public Horse getHorse() {
        return horse;
    }

    public String getNewTrainerName() {
        return newTrainerName;
    }

    public String getNewOwnerName() {
        return newOwnerName;
    }

    public void setNewOwnerName(String newOwnerName) {
        this.newOwnerName = newOwnerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClaimedHorse that = (ClaimedHorse) o;

        if (horse != null ? !horse.equals(that.horse) : that.horse != null)
            return false;
        if (newTrainerName != null ? !newTrainerName.equals(that.newTrainerName) : that
                .newTrainerName != null)
            return false;
        return newOwnerName != null ? newOwnerName.equals(that.newOwnerName) : that.newOwnerName
                == null;
    }

    @Override
    public int hashCode() {
        int result = horse != null ? horse.hashCode() : 0;
        result = 31 * result + (newTrainerName != null ? newTrainerName.hashCode() : 0);
        result = 31 * result + (newOwnerName != null ? newOwnerName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClaimedHorse{" +
                "horse='" + horse + '\'' +
                ", newTrainerName='" + newTrainerName + '\'' +
                ", newOwnerName='" + newOwnerName + '\'' +
                '}';
    }
}
