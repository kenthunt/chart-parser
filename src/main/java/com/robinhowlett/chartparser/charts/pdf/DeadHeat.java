package com.robinhowlett.chartparser.charts.pdf;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and detects if a dead-heat was declared
 */
public class DeadHeat {

    private static final Pattern DEAD_HEATS = Pattern.compile("Dead Heats:.+");

    public static boolean parse(List<List<ChartCharacter>> lines) {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            if (parseDeadHeat(text)) return true;
        }
        return false;
    }

    static boolean parseDeadHeat(String text) {
        Matcher matcher = DEAD_HEATS.matcher(text);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}
