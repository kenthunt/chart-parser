package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.Chart;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The value of the {@link Starter} (expressed within a {@link Double} {@code value} field) and a
 * boolean to note if the starter was favorite
 */
public class Odds {

    private static final Logger LOGGER = LoggerFactory.getLogger(Odds.class);

    private final Double value;
    private final boolean favorite;

    public Odds(final Double value, boolean favorite) {
        this.value = value;
        this.favorite = favorite;
    }

    public static Odds parse(List<ChartCharacter> chartCharacters) {
        String text = Chart.convertToText(chartCharacters);
        boolean isFavorite = false;
        Double odds = null;
        if (!text.isEmpty()) {
            if (text.contains("*")) {
                isFavorite = true;
                text = text.substring(0, text.length() - 1);
            }
            try {
                odds = Double.parseDouble(text);
            } catch (NumberFormatException e) {
                LOGGER.warn(String.format("Unable to parse value: %s, due to %s", text, e.getMessage()));
                odds = null;
            }
        }
        return new Odds(odds, isFavorite);
    }

    public Double getValue() {
        return value;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public String toString() {
        return "Odds{" +
                "value=" + value +
                ", favorite=" + favorite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Odds odds1 = (Odds) o;

        if (favorite != odds1.favorite) return false;
        return value != null ? value.equals(odds1.value) : odds1.value == null;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (favorite ? 1 : 0);
        return result;
    }
}
