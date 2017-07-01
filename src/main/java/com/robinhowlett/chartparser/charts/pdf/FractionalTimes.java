package com.robinhowlett.chartparser.charts.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * Parses and stores the textual descriptions of the times recorded by the leader at each fractional
 * point
 */
public class FractionalTimes {

    private static final Logger LOGGER = LoggerFactory.getLogger(FractionalTimes.class);
    private static final Pattern FRAC_TIMES_PATTERN =
            Pattern.compile("(Fractional Times: (.+)\\|)?Final Time: (\\d?\\d?:?\\d\\d.\\d\\d)");

    public static ArrayList<String> parse(List<List<ChartCharacter>> runningLines) {
        String fractionalTimesCandidate = null;
        ArrayList<String> fractions = new ArrayList<>();
        if (runningLines != null) {
            for (int i = 0; i < runningLines.size(); i++) {
                String text = Chart.convertToText(runningLines.get(i));
                if (text.startsWith("Fractional Times:") || text.startsWith("Final Time:")) {
                    runningLines.remove(i);
                    fractionalTimesCandidate = text.replaceAll(System.lineSeparator(), "");
                    Matcher fractionalTimes = FRAC_TIMES_PATTERN.matcher(fractionalTimesCandidate);
                    if (fractionalTimes.find()) {
                        if (fractionalTimes.group(1) != null) {
                            String timesAtFractions = fractionalTimes.group(2);
                            List<String> times = asList(timesAtFractions.split("\\|"));
                            fractions = new ArrayList<>(times);
                        }
                        String finalTime = fractionalTimes.group(3);

                        fractions.add(finalTime);
                        return fractions;
                    }
                }
            }
        }

        if (fractions.isEmpty() && fractionalTimesCandidate != null) {
            LOGGER.warn(String.format("No valid fractional times or final time available: %s",
                    fractionalTimesCandidate));
        }

        return fractions;
    }
}
