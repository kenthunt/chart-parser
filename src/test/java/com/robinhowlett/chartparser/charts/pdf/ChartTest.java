package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.robinhowlett.chartparser.charts.pdf.Chart.addWhitespaceIfRequired;
import static com.robinhowlett.chartparser.charts.pdf.Chart.convertToText;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ChartTest {

    private static final String NO_SPACE = "";
    private static final String SPACE = " ";
    private static final String PIPE = "|";

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void round_WithBigDecimalToSixPlaces_RoundsCorrectly() throws Exception {
        BigDecimal rounded = Chart.round(31.630505);
        String expected = "31.631";
        assertThat(rounded.toString(), equalTo(expected));
    }

    @Test
    public void convertToText_WithSampleCharacters_PrintsTextCorrectly() throws Exception {
        List<ChartCharacter> characters = new ArrayList<>();
        characters.add(sampleCharts.getSampleChartCharacter(9.92, 31.63, 'A'));
        characters.add(sampleCharts.getSampleChartCharacter(12.144, 40.63, 'B'));
        characters.add(sampleCharts.getSampleChartCharacter(14.368, 40.63, 'C'));
        characters.add(sampleCharts.getSampleChartCharacter(43.256, 40.63, 'D'));
        characters.add(sampleCharts.getSampleChartCharacter(45.48, 42.63, 'E'));

        String text = convertToText(characters);
        assertThat(text, equalTo("A" + System.lineSeparator() + "BC|DE"));
    }

    @Test
    public void addWhitespaceIfRequired_WithAdjacentCharacters_ReturnsNoAdditionalSpacing()
            throws Exception {
        assertThat(addWhitespaceIfRequired(50.36, 44.136, 6.224), equalTo(NO_SPACE));
    }

    @Test
    public void addWhitespaceIfRequired_WithSpacingCharacters_ReturnsASpace() throws Exception {
        assertThat(addWhitespaceIfRequired(57.92, 50.36, 5.336), equalTo(SPACE));
    }

    @Test
    public void addWhitespaceIfRequired_WithSeparatedCharacters_ReturnsPipeSeparator()
            throws Exception {
        assertThat(addWhitespaceIfRequired(102.168, 95.72, 2.224), equalTo(PIPE));
    }
}
