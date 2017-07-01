package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Starter;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall.PointOfCall.RelativePosition
        .LengthsAhead;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.robinhowlett.chartparser.charts.pdf.Chart.convertToText;

/**
 * The lengths ahead a {@link Starter} is (if applicable) of the next Starter at a particular point
 * of call
 */
public class ChartLengthsAhead {

    private static final Pattern TEXT_LENGTHS_AHEAD_PATTERN = Pattern.compile("Head|Neck|Nose");
    private static final Pattern LENGTHS_AND_FRACTION_AHEAD_PATTERN =
            Pattern.compile("((\\d+) )?((\\d+)\\/(\\d+))");
    private static final Pattern EVEN_LENGTHS_AHEAD_PATTERN = Pattern.compile("^(\\d+)$");

    /**
     * Convert the fraction-based lengths ahead text into a {@link LengthsAhead} instance
     *
     * @param lengthsAheadCharacters The {@link ChartCharacter}s representing the lengths ahead of
     *                               the next participant at a particular point of call
     * @return The {@link LengthsAhead} this starter was of the next at a particular point of call
     * (if applicable)
     */
    public static LengthsAhead parse(List<ChartCharacter> lengthsAheadCharacters) {
        if (lengthsAheadCharacters != null) {
            String lengthsText = convertToText(lengthsAheadCharacters);
            double lengths = 0;

            // calculate the number of lengths ahead by converting text, numbers with fractions,
            // and whole numbers into their respective values
            if (!lengthsText.isEmpty()) {
                lengths = calculateTextLengthsAhead(lengthsText, lengths);
                lengths = calculateLengthsAndFractionsAhead(lengthsText, lengths);
                lengths = calculateEvenLengthsAhead(lengthsText, lengths);
            }

            return new LengthsAhead(lengthsText, lengths);
        }

        return null;
    }

    // e.g. "2" or "12" lengths ahead
    static Double calculateEvenLengthsAhead(String chart, Double lengthsAhead) {
        Matcher evenLengthsMatcher = EVEN_LENGTHS_AHEAD_PATTERN.matcher(chart);
        if (evenLengthsMatcher.find()) {
            String wholeLengths = evenLengthsMatcher.group();
            if (wholeLengths != null) {
                int lengths = Integer.parseInt(wholeLengths);
                lengthsAhead += lengths;
            }
        }
        return lengthsAhead;
    }

    // e.g. "1 1/2" or "3/4" lengths ahead
    static Double calculateLengthsAndFractionsAhead(String chart, Double lengthsAhead) {
        Matcher lengthsAndFractionsMatcher = LENGTHS_AND_FRACTION_AHEAD_PATTERN.matcher(chart);
        if (lengthsAndFractionsMatcher.find()) {
            String lengthsAndFractions = lengthsAndFractionsMatcher.group();
            if (lengthsAndFractions != null) {
                String wholeLength = lengthsAndFractionsMatcher.group(2);
                if (wholeLength != null) {
                    int wholeLen = Integer.parseInt(wholeLength);
                    lengthsAhead += wholeLen;
                }

                String fraction = lengthsAndFractionsMatcher.group(3);
                if (fraction != null) {
                    String num = lengthsAndFractionsMatcher.group(4);
                    String denom = lengthsAndFractionsMatcher.group(5);
                    if (num != null && denom != null) {
                        int numerator = Integer.parseInt(num);
                        int denominator = Integer.parseInt(denom);
                        lengthsAhead += ((double) numerator / denominator);
                    }
                }
            }
        }
        return lengthsAhead;
    }

    // e.g. Ahead by a "Neck" or a "Nose"
    protected static Double calculateTextLengthsAhead(String chart, Double lengthsAhead) {
        Matcher lengthsAheadMatcher = TEXT_LENGTHS_AHEAD_PATTERN.matcher(chart);
        if (lengthsAheadMatcher.find()) {
            String textAhead = lengthsAheadMatcher.group();
            if (textAhead != null) {
                switch (textAhead) {
                    case "Nose":
                        lengthsAhead += 0.05;
                        break;
                    case "Head":
                        lengthsAhead += 0.10;
                        break;
                    case "Neck":
                        lengthsAhead += 0.25;
                        break;
                }
            }
        }
        return lengthsAhead;
    }
}
