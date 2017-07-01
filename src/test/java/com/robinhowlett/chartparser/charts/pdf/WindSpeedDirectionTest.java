package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WindSpeedDirectionTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void parse_WithSampleSections_ReturnsClearWeatherAndFastTrack() throws Exception {
        WindSpeedDirection expected = new WindSpeedDirection(3, "Cross");

        Optional<WindSpeedDirection> weatherTrackCondition =
                WindSpeedDirection.parse(sampleCharts.getSampleChartLines(5));

        assertThat(weatherTrackCondition.get(), equalTo(expected));
    }

}
