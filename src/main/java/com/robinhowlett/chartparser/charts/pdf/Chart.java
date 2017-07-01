package com.robinhowlett.chartparser.charts.pdf;

import java.math.BigDecimal;
import java.util.List;

/**
 * Utility class for dealing with text and numbers parsed from the chart
 */
public class Chart {

    public static final BigDecimal THREE = new BigDecimal(3);

    /**
     * Round half-up and to three decimal places
     *
     * @see #round(double, int)
     */
    public static BigDecimal round(double d) {
        return round(d, 3);
    }

    /**
     * Round half-up and up to the defined numer of decimal places. Used to try to avoid floating
     * point woes
     */
    public static BigDecimal round(double d, int newScale) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(newScale, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    /**
     * Converts the list of {@link ChartCharacter}s into a String, including inserting space, pipe,
     * and new line characters when the horizontal and vertical positions of adjacent characters
     * suggests it
     */
    public static String convertToText(List<ChartCharacter> line) {
        StringBuilder sb = new StringBuilder();
        ChartCharacter prev = null;
        if (line != null) {
            for (ChartCharacter curr : line) {
                if (prev != null) {
                    // add a whitespace (if required)
                    sb.append(addWhitespaceIfRequired(curr.getxDirAdj(), prev.getxDirAdj(),
                            prev.getWidthDirAdj()));
                    // add a new line (if required)
                    if (Math.abs(curr.getyDirAdj() - prev.getyDirAdj()) > 4) {
                        sb.append(System.lineSeparator());
                    }
                }
                // add the actual character
                sb.append(curr.getUnicode());
                prev = curr;
            }
        }
        return sb.toString();
    }

    /**
     * Adds a space or a tab marker (using a pipe character) when the positions of two characters
     * suggests it. Otherwise returns an empty String
     */
    public static String addWhitespaceIfRequired(double currXDir, double prevXDir,
            double prevWidthDirAdj) {
        BigDecimal spacing = round(currXDir).subtract(round(prevXDir).add(round(prevWidthDirAdj)));

        // changed to 0.001 from ZERO; found a few occurances of when it appeared there was
        // rounding up
        if (spacing.compareTo(new BigDecimal(0.001)) > 0 && spacing.compareTo(THREE) <= 0) {
            return " "; // a space
        } else if (spacing.compareTo(THREE) > 0) {
            return "|"; // used as a CSV separator
        }

        return ""; // no whitespace
    }
}
