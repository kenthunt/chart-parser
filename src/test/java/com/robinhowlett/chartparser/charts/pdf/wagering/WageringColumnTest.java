package com.robinhowlett.chartparser.charts.pdf.wagering;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WageringColumnTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void calculateColumnFloors_WithSampleWageringGrid_CalculatesColumnFloors()
            throws Exception {
        WageringTreeSet expected = sampleCharts.getWageringGridColumnFloors();

        WageringTreeSet columnFloors =
                WageringColumn.calculateColumnFloors(sampleCharts.getWageringGridColumnRanges());

        assertThat(columnFloors, equalTo(expected));
    }
}
