package com.robinhowlett.chartparser.charts.pdf.running_line;

/**
 * The individual finishing time (for Quarter Horse races)
 */
public class IndividualTime {

    /**
     * Convert the finishing time to milliseconds
     *
     * @return the finishing time converted to milliseconds
     */
    public static Long parse(String indTime) {
        if (indTime != null && indTime.matches("\\d{1,3}\\.\\d{1,3}")) {
            return (long) (Double.parseDouble(indTime) * 1000);
        }
        return null;
    }
}
