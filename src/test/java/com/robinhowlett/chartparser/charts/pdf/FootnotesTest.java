package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FootnotesTest {

    private static final String EXPECTED =
            "PRATER SIXTY FOUR opened a big lead in he stretch and held on for the win. CANDY " +
                    "SWEETHEART closed well midstretch for place. MIDNIGHTWITHDRAWAL was well " +
                    "back early and finished with interest. PROWERS COUNTY off slow closed well " +
                    "mid track. AL BAZ (GB) was up close arly then shuffled back on the inside " +
                    "and then tired late. COOK INLET raced midpack and had no response in the " +
                    "stretch. ANTARES DREAM up clsoe early faded after three-quarters. TRAVELIN " +
                    "TREV was always well back and not a factor.";

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void parseFootnotes_WithSampleChartText_ExtractsFootnotesCorrectly() throws Exception {
        String footnotes = Footnotes.parse(sampleCharts.getSampleChartLines(8));

        assertThat(footnotes, equalTo(EXPECTED));
    }
}
