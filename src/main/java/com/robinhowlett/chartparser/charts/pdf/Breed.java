package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.exceptions.ChartParserException;

/**
 * Enum for Thoroughbred (TB), Quarter Horse (QH), Arabian, and Mixed (usually a race containing
 * both TB and QH)
 */
public enum Breed {
    THOROUGHBRED("Thoroughbred"),
    QUARTER_HORSE("Quarter Horse"),
    ARABIAN("Arabian"),
    MIXED("Mixed");

    private final String chartValue;

    Breed(String chartValue) {
        this.chartValue = chartValue;
    }

    // forChartValue("Thoroughbred") returns Breed.THOROUGHBRED
    public static Breed forChartValue(String text) throws NoMatchingBreedException {
        for (Breed breed : values()) {
            if (breed.getChartValue().equals(text)) {
                return breed;
            }
        }
        throw new NoMatchingBreedException(text);
    }

    public static boolean isBreed(String text) {
        for (Breed breed : values()) {
            if (breed.getChartValue().equals(text)) {
                return true;
            }
        }
        return false;
    }

    public String getChartValue() {
        return chartValue;
    }

    public String getBreed() {
        return chartValue;
    }

    @Override
    public String toString() {
        return "Breed{" +
                "chartValue='" + chartValue + '\'' +
                '}';
    }

    public static class NoMatchingBreedException extends ChartParserException {
        public NoMatchingBreedException(String message) {
            super(String.format("Did not match a breed for %s", message));
        }
    }
}
