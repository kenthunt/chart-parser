package com.robinhowlett.chartparser.fractionals;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.Breed;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FractionalServiceTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void calculateMillisecondsForFraction_WithVariousFractions_ConvertsToMillisCorrectly()
            throws Exception {
        Optional<Long> millis = FractionalService.calculateMillisecondsForFraction("22.88");
        assertThat(millis.get(), equalTo(22880L));

        millis = FractionalService.calculateMillisecondsForFraction("1:12.98");
        assertThat(millis.get(), equalTo(72980L));

        millis = FractionalService.calculateMillisecondsForFraction("invalid");
        assertThat(millis.isPresent(), equalTo(false));
    }

    @Test
    public void getFractionalPointsForDistance_WithSixFurlongFractions_ReturnsCorrectFractionals()
            throws Exception {
        List<FractionalPoint.Fractional> expected = new ArrayList<>();
        expected.add(new FractionalPoint.Fractional(1, "1/4", 1320, "22.88", 22880L));
        expected.add(new FractionalPoint.Fractional(2, "1/2", 2640, "46.50", 46500L));
        expected.add(new FractionalPoint.Fractional(3, "5/8", 3300, "59.31", 59310L));
        expected.add(new FractionalPoint.Fractional(6, "Fin", 3960, "1:12.98", 72980L));

        List<String> fractions = Arrays.asList("22.88", "46.50", "59.31", "1:12.98");

        FractionalPointRepository repository = mock(FractionalPointRepository.class);
        when(repository.findAll()).thenReturn(sampleCharts.getFractionalTimePoints());

        FractionalService fractionalService = new FractionalService(repository);
        List<FractionalPoint.Fractional> fractionals =
                fractionalService.getFractionalPointsForDistance(fractions, 3960,
                        Breed.THOROUGHBRED);

        verify(repository).findAll();

        assertThat(fractionals, equalTo(expected));
    }
}
