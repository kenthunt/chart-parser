package com.robinhowlett.chartparser.charts.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.robinhowlett.chartparser.ChartParser.COPYRIGHT_PATTERN;

/**
 * Parses and stores the chart's race notes. The track attendance, handle etc. numbers are not
 * attempted to be parsed as they appear only on the final chart of a particular race day
 */
public class Footnotes {

    private static final Pattern FOOTNOTES = Pattern.compile("^Footnotes$");
    private static final Pattern HANDLE =
            Pattern.compile("^Track Attendance:|^Handle:|^ISW =|^ITW =|^Attendance:");

    public static String parse(List<List<ChartCharacter>> lines) {
        boolean footnotesFound = false;
        List<ChartCharacter> footnotes = new ArrayList<>();
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);

            Matcher matcher = COPYRIGHT_PATTERN.matcher(text);
            if (matcher.find()) {
                footnotesFound = false;
            }

            matcher = HANDLE.matcher(text);
            if (matcher.find()) {
                footnotesFound = false;
            }

            if (footnotesFound) {
                footnotes.addAll(line);
            }

            matcher = FOOTNOTES.matcher(text);
            if (matcher.find()) {
                footnotesFound = true;
            }
        }

        String notes = Chart.convertToText(footnotes);
        notes = notes.replaceAll("\nDenotes a Keeneland Sales Graduate", "")
                .replaceAll("\n", " ")
                .replaceAll("\\|", " ");

        return notes;
    }
}
