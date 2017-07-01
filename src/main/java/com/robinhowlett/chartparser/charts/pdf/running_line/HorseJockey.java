package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.Chart;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Horse;
import com.robinhowlett.chartparser.charts.pdf.Jockey;
import com.robinhowlett.chartparser.charts.pdf.Starter;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The horse and jockey for this {@link Starter}
 */
public class HorseJockey {

    private static final Pattern HORSE_JOCKEY_PATTERN =
            Pattern.compile("(.+) \\(((.+),( (.+))?)?\\)");

    private final Horse horse;
    private final Jockey jockey;

    public HorseJockey(Horse horse, Jockey jockey) {
        this.horse = horse;
        this.jockey = jockey;
    }

    /**
     * Attempt to parse the horse and jockey names
     * @param chartCharacters the characters that should contain the horse and jockey names
     * @return HorseJockey instance containing the horse and jockey names
     * @throws MissingHorseJockeyException if unable to match the regex
     */
    public static HorseJockey parse(List<ChartCharacter> chartCharacters) throws MissingHorseJockeyException {
        String text = Chart.convertToText(chartCharacters);
        Matcher matcher = HORSE_JOCKEY_PATTERN.matcher(text);
        if (matcher.find()) {
            String horseName = matcher.group(1);
            // handle disqualifications
            if (horseName.startsWith("DQ-")) {
                horseName = horseName.substring(3);
            }

            Jockey jockey;
            if (matcher.group(2) != null) {
                String jockeyLastName = matcher.group(3);
                String jockeyFirstName = matcher.group(5);
                jockey = new Jockey(jockeyFirstName, jockeyLastName);
            } else {
                jockey = new Jockey(null, "unknown jockey");
            }

            return new HorseJockey(new Horse(horseName), jockey);
        }
        throw new MissingHorseJockeyException(text);
    }

    public Horse getHorse() {
        return horse;
    }

    public Jockey getJockey() {
        return jockey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HorseJockey that = (HorseJockey) o;

        if (horse != null ? !horse.equals(that.horse) : that.horse != null) return false;
        return jockey != null ? jockey.equals(that.jockey) : that.jockey == null;
    }

    @Override
    public int hashCode() {
        int result = horse != null ? horse.hashCode() : 0;
        result = 31 * result + (jockey != null ? jockey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HorseJockey{" +
                "horse=" + horse +
                ", jockey=" + jockey +
                '}';
    }

    public static class MissingHorseJockeyException extends ChartParserException {
        public MissingHorseJockeyException(String text) {
            super(String.format("Unable to parse horse and jockey names from: %s", text));
        }
    }
}
