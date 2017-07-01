package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.robinhowlett.chartparser.charts.pdf.running_line.RunningLineHeader
        .createIndexOfRunningLineColumns;
import static com.robinhowlett.chartparser.charts.pdf.running_line.RunningLineHeader
        .createPostRaceRunningLineHeaderColumns;
import static com.robinhowlett.chartparser.charts.pdf.running_line.RunningLineHeader
        .identifyHeaderSuffixCharactersForRegistry;
import static com.robinhowlett.chartparser.charts.pdf.running_line.RunningLineHeader
        .populateHeaderColumns;
import static com.robinhowlett.chartparser.charts.pdf.running_line.RunningLineHeader
        .populateHeaderColumnsWithInRaceRunningLine;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class RunningLineHeaderTest {

    private TestChartResources sampleCharts = new TestChartResources();
    private List<ChartCharacter> runningLineHeader;
    private List<String> preRaceHeaderColumnNames;

    @Before
    public void setUp() throws Exception {
        runningLineHeader = sampleCharts.getRunningLineLines(0).get(0);
        preRaceHeaderColumnNames = Arrays.asList("LastRaced", "Pgm", "HorseName(Jockey)", "Wgt",
                "M/E", "PP");
    }

    @Test
    public void createRunningLineColumnIndex_WithSampleRunningLine_IndexesColumnsCorrectly()
            throws Exception {
        List<RunningLineColumnIndex> expected = sampleCharts.getRunningLineColumnIndices();

        TreeSet<RunningLineColumnIndex> runningLineColumnIndex =
                createIndexOfRunningLineColumns(runningLineHeader);

        int counter = 0;
        for (RunningLineColumnIndex index : runningLineColumnIndex) {
            assertThat(index, equalTo(expected.get(counter)));
            counter++;
        }
    }

    @Test
    public void populateHeaderColumnsWithPreRaceRunningLine_AssignsCorrectChartCharacterToKey()
            throws Exception {
        Map<String, ChartCharacter> headerColumns =
                populateHeaderColumns(runningLineHeader, preRaceHeaderColumnNames);
        assertThat(headerColumns.keySet().toString(),
                equalTo("[LastRaced, Pgm, HorseName(Jockey), Wgt, M/E, PP]"));

        Iterator<Map.Entry<String, ChartCharacter>> iterator = headerColumns.entrySet().iterator();
        Map.Entry<String, ChartCharacter> firstColumn = iterator.next();
        assertThat(firstColumn.getKey(), equalTo("LastRaced"));
        assertThat(firstColumn.getValue().getxDirAdj(), equalTo(9.92));
        assertThat(firstColumn.getValue().getUnicode(), equalTo('L'));

        Map.Entry<String, ChartCharacter> secondColumn = iterator.next();
        assertThat(secondColumn.getKey(), equalTo("Pgm"));
        assertThat(secondColumn.getValue().getxDirAdj(), equalTo(69.623));
        assertThat(secondColumn.getValue().getUnicode(), equalTo('P'));
    }

    @Test
    public void createPostRaceRunningLineHeaderColumns_WithRunningLineHeader_CreatesColumns()
            throws Exception {
        Map<String, ChartCharacter> headerColumns =
                createPostRaceRunningLineHeaderColumns(runningLineHeader);
        assertThat(headerColumns.keySet().toString(), equalTo("[Fin, Odds, Comments]"));
        Iterator<Map.Entry<String, ChartCharacter>> iterator = headerColumns.entrySet().iterator();

        Map.Entry<String, ChartCharacter> firstColumn = iterator.next();
        assertThat(firstColumn.getKey(), equalTo("Fin"));
        assertThat(firstColumn.getValue().getxDirAdj(), equalTo(438.062));
        assertThat(firstColumn.getValue().getUnicode(), equalTo('F'));
    }

    @Test
    public void identifyHeaderSuffixCharactersForRegistry_WithRunningLineHeader_IdentifiesSuffix()
            throws Exception {
        RunningLineHeaderSuffix runningLineHeaderSuffix =
                identifyHeaderSuffixCharactersForRegistry(runningLineHeader);
        assertThat(runningLineHeaderSuffix.getHeaderSuffix(), equalTo("FinOddsComments"));
        assertThat(runningLineHeaderSuffix.getHeaderSuffixCharacters().size(), equalTo(15));
    }

    @Test
    public void populateHeaderColumnsWithInRaceRunningLine_WithTruncatedHeader_CreatesColumns()
            throws Exception {
        // simulate the pre- and post-race columns being already removed by earlier processing
        runningLineHeader = runningLineHeader.subList(37, 51);

        Map<String, ChartCharacter> columns =
                populateHeaderColumns(runningLineHeader, preRaceHeaderColumnNames);

        Map<String, ChartCharacter> headerColumns =
                populateHeaderColumnsWithInRaceRunningLine(runningLineHeader, columns);

        assertThat(headerColumns.keySet().toString(), equalTo("[Start, 1/4, 1/2, Str]"));

        Iterator<Map.Entry<String, ChartCharacter>> iterator = headerColumns.entrySet().iterator();
        Map.Entry<String, ChartCharacter> firstColumn = iterator.next();
        assertThat(firstColumn.getKey(), equalTo("Start"));
        assertThat(firstColumn.getValue().getxDirAdj(), equalTo(304.955));
        assertThat(firstColumn.getValue().getUnicode(), equalTo('S'));

        Map.Entry<String, ChartCharacter> secondColumn = iterator.next();
        assertThat(secondColumn.getKey(), equalTo("1/4"));
        assertThat(secondColumn.getValue().getxDirAdj(), equalTo(331.766));
        assertThat(secondColumn.getValue().getUnicode(), equalTo('1'));
    }
}
