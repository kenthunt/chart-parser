package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class RunUpTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void parse_WithSampleRunningLines_Returns30FeetRunUp() throws Exception {
        Integer expected = 30;
        Integer runUp = RunUp.parse(sampleCharts.getRunningLineLines(0));
        assertThat(runUp, equalTo(expected));
    }
}
