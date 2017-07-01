package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import static com.robinhowlett.chartparser.charts.pdf.Breed.forChartValue;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BreedTest {

    @Test
    public void forChartValue_WithThoroughbred_ReturnsCorrectBreedEnum() throws Exception {
        Breed breed = forChartValue("Thoroughbred");
        assertThat(breed, equalTo(Breed.THOROUGHBRED));
    }
}
