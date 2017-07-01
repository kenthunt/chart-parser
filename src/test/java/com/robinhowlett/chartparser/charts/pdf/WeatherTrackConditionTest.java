package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WeatherTrackConditionTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void parse_WithSampleSections_ReturnsClearWeatherAndFastTrack() throws Exception {
        WeatherTrackCondition expected = new WeatherTrackCondition("Clear", "Fast");
        Optional<WeatherTrackCondition> weatherTrackCondition =
                WeatherTrackCondition.parse(sampleCharts.getSampleChartLines(0));

        assertThat(weatherTrackCondition.get(), equalTo(expected));
    }

}
