package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.exceptions.ChartParserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The race winner, which provides additional breeding information about the winning horse. In the
 * case of dead heats, each individual winner is parsed.
 */
public class Winner {

    private static final Pattern WINNER_PATTERN =
            Pattern.compile("^Winner:\\|(.+), ((.+) )?(.+), by (.+) out of (.+), by (.+). Foaled " +
                    "(\\w{3} \\d{2}, \\d{4}) in (.+)\\.");
    private static final Pattern BREEDER_PATTERN =
            Pattern.compile("^Breeder:\\|(.+)\\|Winning Owner");
    private static final DateTimeFormatter MONTH_DAY_YEAR_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private static final Logger LOGGER = LoggerFactory.getLogger(Winner.class);

    private final String horseName;
    private final String horseColor;
    private final String horseSex;
    private final String sireName;
    private final String damName;
    private final String damSireName;
    private final LocalDate foalingDate;
    private final String foalingLocation;
    private Breeder breeder;

    public Winner(String horseName, String horseColor, String horseSex, String sireName, String
            damName, String damSireName, LocalDate foalingDate, String foalingLocation) {
        this.horseName = horseName;
        this.horseColor = horseColor;
        this.horseSex = horseSex;
        this.sireName = sireName;
        this.damName = damName;
        this.damSireName = damSireName;
        this.foalingDate = foalingDate;
        this.foalingLocation = foalingLocation;
    }

    public static List<Winner> parse(List<List<ChartCharacter>> lines)
            throws NoWinnersDeclaredException {
        List<Winner> winners = new ArrayList<>();
        // find the winner(s)
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            text = text.replaceAll(System.lineSeparator(), " ");

            Optional<Winner> winner = parseWinner(text);
            if (winner.isPresent()) {
                winners.add(winner.get());
            }
        }

        if (!winners.isEmpty()) {
            // find the winning breeder(s) and associate with the winner(s)
            winners = findAndAssociateWinningBreeder(lines, winners);
        } else {
            throw new NoWinnersDeclaredException();
        }

        return winners;
    }

    // the winner's breeder's details are listed immediately after the winner (this handles dead
    // heats)
    static List<Winner> findAndAssociateWinningBreeder(List<List<ChartCharacter>> lines,
            List<Winner> winners) {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            text = text.replaceAll(System.lineSeparator(), " ");

            Optional<String> breeder = parseBreeder(text);
            if (breeder.isPresent()) {
                for (Winner winner : winners) {
                    if (winner.getBreeder() == null) {
                        String breederName = breeder.get();
                        if (breederName.endsWith(".")) {
                            breederName = breederName.substring(0, breederName.length() - 1);
                        }
                        winner.setBreeder(new Breeder(breederName));
                        break;
                    }
                }
            }
        }

        return winners;
    }

    static Optional<Winner> parseWinner(String text) {
        Matcher matcher = WINNER_PATTERN.matcher(text);
        if (matcher.find()) {
            String horseName = matcher.group(1);
            String horseColor = matcher.group(3);
            String horseSex = matcher.group(4);
            String sireName = matcher.group(5);
            String damName = matcher.group(6);
            String damSireName = matcher.group(7);
            String foalingDateText = matcher.group(8);
            LocalDate foalingDate = LocalDate.parse(foalingDateText, MONTH_DAY_YEAR_FORMATTER);
            String foalingLocation = matcher.group(9);

            return Optional.of(new Winner(horseName, horseColor, horseSex, sireName, damName,
                    damSireName, foalingDate, foalingLocation));
        }
        return Optional.empty();
    }

    static Optional<String> parseBreeder(String text) {
        Matcher matcher = BREEDER_PATTERN.matcher(text);
        if (matcher.find()) {
            String breederName = matcher.group(1);

            return Optional.of(breederName);
        }
        return Optional.empty();
    }

    public String getHorseName() {
        return horseName;
    }

    public String getHorseColor() {
        return horseColor;
    }

    public String getHorseSex() {
        return horseSex;
    }

    public String getSireName() {
        return sireName;
    }

    public String getDamName() {
        return damName;
    }

    public String getDamSireName() {
        return damSireName;
    }

    public LocalDate getFoalingDate() {
        return foalingDate;
    }

    public String getFoalingLocation() {
        return foalingLocation;
    }

    public Breeder getBreeder() {
        return breeder;
    }

    public void setBreeder(Breeder breeder) {
        this.breeder = breeder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Winner winner = (Winner) o;

        if (horseName != null ? !horseName.equals(winner.horseName) : winner.horseName != null)
            return false;
        if (horseColor != null ? !horseColor.equals(winner.horseColor) : winner.horseColor != null)
            return false;
        if (horseSex != null ? !horseSex.equals(winner.horseSex) : winner.horseSex != null)
            return false;
        if (sireName != null ? !sireName.equals(winner.sireName) : winner.sireName != null)
            return false;
        if (damName != null ? !damName.equals(winner.damName) : winner.damName != null)
            return false;
        if (damSireName != null ? !damSireName.equals(winner.damSireName) : winner.damSireName !=
                null)
            return false;
        if (foalingDate != null ? !foalingDate.equals(winner.foalingDate) : winner.foalingDate !=
                null)
            return false;
        if (foalingLocation != null ? !foalingLocation.equals(winner.foalingLocation) : winner
                .foalingLocation != null)
            return false;
        return breeder != null ? breeder.equals(winner.breeder) : winner.breeder
                == null;
    }

    @Override
    public int hashCode() {
        int result = horseName != null ? horseName.hashCode() : 0;
        result = 31 * result + (horseColor != null ? horseColor.hashCode() : 0);
        result = 31 * result + (horseSex != null ? horseSex.hashCode() : 0);
        result = 31 * result + (sireName != null ? sireName.hashCode() : 0);
        result = 31 * result + (damName != null ? damName.hashCode() : 0);
        result = 31 * result + (damSireName != null ? damSireName.hashCode() : 0);
        result = 31 * result + (foalingDate != null ? foalingDate.hashCode() : 0);
        result = 31 * result + (foalingLocation != null ? foalingLocation.hashCode() : 0);
        result = 31 * result + (breeder != null ? breeder.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Winner{" +
                "horseName='" + horseName + '\'' +
                ", horseColor='" + horseColor + '\'' +
                ", horseSex='" + horseSex + '\'' +
                ", sireName='" + sireName + '\'' +
                ", damName='" + damName + '\'' +
                ", damSireName='" + damSireName + '\'' +
                ", foalingDate=" + foalingDate +
                ", foalingLocation='" + foalingLocation + '\'' +
                ", breeder='" + breeder + '\'' +
                '}';
    }

    public static class NoWinnersDeclaredException extends ChartParserException {
        public NoWinnersDeclaredException() {
            super("Unable to find a declared Winner");
        }
    }
}
