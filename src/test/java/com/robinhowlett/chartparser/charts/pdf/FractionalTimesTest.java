package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FractionalTimesTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void parse_WithRunningLines_ReturnsListOfFractions() throws Exception {
        List<String> expected = Arrays.asList("22.88", "46.50", "59.31", "1:12.98");
        List<String> fractions = FractionalTimes.parse(sampleCharts.getRunningLineLines(0));
        assertThat(fractions, equalTo(expected));
    }

    @Test
    public void parse_WithJustFinalTime_ReturnsListOfFractions() throws Exception {
        List<List<ChartCharacter>> runningLineLines = sampleCharts.getRunningLineLines(0);
        // remove the Fractional Times
        runningLineLines.set(8, runningLineLines.get(8).subList(31, 48));

        List<String> expected = Arrays.asList("1:12.98");
        List<String> fractions = FractionalTimes.parse(runningLineLines);
        assertThat(fractions, equalTo(expected));
    }
}
