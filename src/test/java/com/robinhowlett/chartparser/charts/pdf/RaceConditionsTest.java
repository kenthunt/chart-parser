package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.RaceConditions.ClaimingPriceRange;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class RaceConditionsTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void parse_WithSampleChart_ReturnsValidRaceDescription() throws Exception {
        RaceConditions expected = new RaceConditions("FOR " +
                "MAIDENS, FILLIES AND MARES THREE YEARS OLD AND UPWARD. Three Year Olds, 120 lbs" +
                ".; Older, 124 lbs.", null);

        RaceConditions raceConditions =
                RaceConditions.parse(sampleCharts.getSampleChartLines(0));
        assertThat(raceConditions, equalTo(expected));
    }

    @Test
    public void parse_WithClaimingPriceRanger_ParsesCorrectly() throws Exception {
        ClaimingPriceRange expected = new ClaimingPriceRange(22500, 25000);

        String raceConditions = "Maidens three years old and upward. Weights: 3-year-olds, " +
                "119 lbs; Older, 122 lbs. Claiming Price $25,000; if for $ 22,500 allowed 2 lbs. " +
                "Claiming Price: $25,000 - $22,500";

        ClaimingPriceRange claimingPriceRange = ClaimingPriceRange.parse(raceConditions);

        assertThat(claimingPriceRange, equalTo(expected));
    }
}
