package com.robinhowlett.chartparser.charts.pdf;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The registered run-up, measured in feet (the distance between the starting stalls and when the
 * race timer is actually started - a longer run up means the horses are potentially traveling at a
 * much higher speed already versus a short run-up)
 */
public class RunUp {

    private static final Pattern RUN_UP_PATTERN = Pattern.compile("(\\d+) feet");

    public static Integer parse(List<List<ChartCharacter>> runningLines) {
        Integer runUpInFeet = null;
        if (runningLines != null) {
            for (int i = 0; i < runningLines.size(); i++) {
                String runUpCandidate = Chart.convertToText(runningLines.get(i));
                if (runUpCandidate.startsWith("Run-Up:")) {
                    runningLines.remove(i);

                    Matcher matcher = RUN_UP_PATTERN.matcher(runUpCandidate);
                    if (matcher.find()) {
                        runUpInFeet = Integer.parseInt(matcher.group(1));
                        break;
                    }
                }
            }
        }
        return runUpInFeet;
    }

}
