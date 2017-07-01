package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import java.util.List;

import static com.robinhowlett.chartparser.charts.pdf.Chart.convertToText;
import static com.robinhowlett.chartparser.charts.pdf.SplitTimes.removeSplitTimesIfPresent;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SplitTimesTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void removeSplitTimesIfPresent_WithRunningLines_RemovesSplitTimesLine() throws
            Exception {
        List<List<ChartCharacter>> runningLineSections = sampleCharts.getRunningLineLines(0);
        assertThat(isSplitTimesIsPresent(runningLineSections), equalTo(true));

        removeSplitTimesIfPresent(runningLineSections);
        assertThat(isSplitTimesIsPresent(runningLineSections), equalTo(false));
    }

    private boolean isSplitTimesIsPresent(List<List<ChartCharacter>> runningLineSections) {
        for (List<ChartCharacter> runningLineSection : runningLineSections) {
            if (convertToText(runningLineSection).startsWith("Split Times:")) {
                return true;
            }
        }
        return false;
    }
}
