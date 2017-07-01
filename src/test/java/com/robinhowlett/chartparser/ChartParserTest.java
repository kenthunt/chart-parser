package com.robinhowlett.chartparser;

import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.robinhowlett.chartparser.charts.pdf.Chart.convertToText;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChartParserTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void readChartCsv_WithSampleCsvChart_ParsesCsvCorrectly() throws Exception {
        ChartCharacter expected = sampleCharts.getSampleChartCharacter(9.92, 31.630, 'A');
        List<ChartCharacter> chartCharacters =
                ChartParser.readChartCsv(sampleCharts.getFirstCsvChart());

        assertThat(chartCharacters.size(), equalTo(2543));
        assertThat(chartCharacters.get(0), equalTo(expected));
    }

    @Test
    public void separateIntoLines_WithThreeCharacters_SeparatesIntoTwoLines() throws Exception {
        List<ChartCharacter> charactersOnDifferentLines = new ArrayList<>();
        ChartCharacter firstLineFirstCharacter = sampleCharts.getSampleChartCharacter(9.92,
                31.63, 'A');
        ChartCharacter secondLineFirstCharacter = sampleCharts.getSampleChartCharacter(9.92,
                40.63, 'B');
        ChartCharacter secondLineSecondCharacter = sampleCharts.getSampleChartCharacter(19.84,
                40.63, 'C');
        charactersOnDifferentLines.add(firstLineFirstCharacter);
        charactersOnDifferentLines.add(secondLineFirstCharacter);
        charactersOnDifferentLines.add(secondLineSecondCharacter);

        List<List<ChartCharacter>> expected = new ArrayList<>();
        expected.add(new ArrayList<ChartCharacter>() {{
            add(firstLineFirstCharacter);
        }});
        expected.add(new ArrayList<ChartCharacter>() {{
            add(secondLineFirstCharacter);
            add(secondLineSecondCharacter);
        }});

        List<List<ChartCharacter>> linesOfChartCharacters =
                ChartParser.separateIntoLines(charactersOnDifferentLines);

        assertThat(linesOfChartCharacters, is(expected));
    }

    @Test
    public void getRunningLines_WithSampleChartSections_ExtractsRunningLinesCorrectly()
            throws Exception {
        List<List<ChartCharacter>> runningLines =
                ChartParser.getRunningLines(sampleCharts.getSampleChartLines(0));
        assertThat(runningLines.size(), equalTo(11));
        assertThat(convertToText(runningLines.get(0)), equalTo("Last Raced|Pgm|Horse Name " +
                "(Jockey)|Wgt M/E|PP|Start|1/4|1/2|Str|Fin|Odds|Comments"));
        assertThat(convertToText(runningLines.get(10)), equalTo("Run-Up: 30 feet"));
    }

    @Test
    public void convertToCsv_WithSamplePdfChart_CreatesListOfCsvs() throws Exception {
        List<String> expected = sampleCharts.getCsvCharts();
        List<String> csvCharts = ChartParser.convertToCsv(sampleCharts.getPdfChartsFile());
        assertThat(csvCharts, is(expected));
    }
}