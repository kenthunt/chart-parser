package com.robinhowlett.chartparser.charts.pdf;

import java.util.List;

/**
 * Since split times are calculated from the parsed {@link FractionalTimes} (with the added benefit
 * of knowing the distance the split applies to), this information is not parsed or stored from the
 * chart
 */
public class SplitTimes {

    public static List<List<ChartCharacter>> removeSplitTimesIfPresent(
            List<List<ChartCharacter>> runningLines) {
        if (runningLines != null) {
            for (int i = 0; i < runningLines.size(); i++) {
                if (Chart.convertToText(runningLines.get(i)).startsWith("Split Times:")) {
                    runningLines.remove(i);
                    break;
                }
            }
        }
        return runningLines;
    }
}
