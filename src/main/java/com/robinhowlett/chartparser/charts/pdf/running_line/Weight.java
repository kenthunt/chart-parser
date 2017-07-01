package com.robinhowlett.chartparser.charts.pdf.running_line;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import static java.lang.Integer.parseInt;

/**
 * Stores the weight carried by the horse (net) and the jockey allowance (due to apprenticeship)
 */
@JsonPropertyOrder({"carried", "jockeyAllowance"})
public class Weight {

    @JsonProperty("carried")
    private final int weightCarried;
    private final int jockeyAllowance;

    public Weight(int weightCarried, int jockeyAllowance) {
        this.weightCarried = weightCarried;
        this.jockeyAllowance = jockeyAllowance;
    }

    public static Weight parse(final List<ChartCharacter> chartCharacters)
            throws ChartParserException {
        StringBuilder carriedBuilder = new StringBuilder();
        int jockeyAllowance = 0;
        ChartCharacter prev = null;
        for (ChartCharacter columnCharacter : chartCharacters) {
            if (prev != null && (columnCharacter.getWidthOfSpace() != prev.getWidthOfSpace())) {
                // convert the unicode chars into their numeric equivalents (in lbs)
                switch (columnCharacter.getUnicode()) {
                    case '¹':
                        jockeyAllowance = 3;
                        break;
                    case '»':
                        jockeyAllowance = 5;
                        break;
                    case '½':
                        jockeyAllowance = 7;
                        break;
                    case '¶':
                        jockeyAllowance = 10;
                        break;
                    default:
                        throw new ChartParserException(String.format("Unknown Jockey Allowance " +
                                "character: %s", columnCharacter.getUnicode()));
                }
                break;
            }
            carriedBuilder.append(columnCharacter.getUnicode());
            prev = columnCharacter;
        }

        int weightCarried = parseInt(carriedBuilder.toString());

        return new Weight(weightCarried, jockeyAllowance);
    }

    public int getWeightCarried() {
        return weightCarried;
    }

    public int getJockeyAllowance() {
        return jockeyAllowance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weight weight = (Weight) o;

        if (weightCarried != weight.weightCarried) return false;
        return jockeyAllowance == weight.jockeyAllowance;
    }

    @Override
    public int hashCode() {
        int result = weightCarried;
        result = 31 * result + jockeyAllowance;
        return result;
    }

    @Override
    public String toString() {
        return "Weight{" +
                "weightCarried=" + weightCarried +
                ", jockeyAllowance=" + jockeyAllowance +
                '}';
    }
}
