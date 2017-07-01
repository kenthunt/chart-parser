package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;

import java.util.List;

/**
 * Defines the valid suffixes and stores the suffix String and the {@link ChartCharacter}s detected
 */
public class RunningLineHeaderSuffix {
    // the list of valid header-columns-as-text suffixes to match against
    public static String[] SUFFIXES = new String[]{"FinOddsComments",
            "FinOddsInd.TimeSp.In.Comments", "FinComments", "FinInd.TimeSp.In.Comments"};

    private String headerSuffix;
    private List<ChartCharacter> headerSuffixCharacters;

    public RunningLineHeaderSuffix(String headerSuffix,
            List<ChartCharacter> headerSuffixCharacters) {
        this.headerSuffix = headerSuffix;
        this.headerSuffixCharacters = headerSuffixCharacters;
    }

    public String getHeaderSuffix() {
        return headerSuffix;
    }

    public List<ChartCharacter> getHeaderSuffixCharacters() {
        return headerSuffixCharacters;
    }

    @Override
    public String toString() {
        return "RunningLineHeaderSuffix{" +
                "headerSuffix='" + headerSuffix + '\'' +
                ", headerSuffixCharacters=" + headerSuffixCharacters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunningLineHeaderSuffix that = (RunningLineHeaderSuffix) o;

        if (headerSuffix != null ? !headerSuffix.equals(that.headerSuffix) : that.headerSuffix !=
                null)
            return false;
        return headerSuffixCharacters != null ? headerSuffixCharacters.equals(that
                .headerSuffixCharacters) : that.headerSuffixCharacters == null;
    }

    @Override
    public int hashCode() {
        int result = headerSuffix != null ? headerSuffix.hashCode() : 0;
        result = 31 * result + (headerSuffixCharacters != null ? headerSuffixCharacters.hashCode
                () : 0);
        return result;
    }
}
