package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class PostTimeStartCommentsTimerTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void parse_WithSampleSections_ReturnsClearWeatherAndFastTrack() throws Exception {
        PostTimeStartCommentsTimer expected =
                new PostTimeStartCommentsTimer("1:01", "Good for all");
        Optional<PostTimeStartCommentsTimer> postTimeStartCommentsTimer =
                PostTimeStartCommentsTimer.parse(sampleCharts.getSampleChartLines(0));

        assertThat(postTimeStartCommentsTimer.get(), equalTo(expected));
    }

}
