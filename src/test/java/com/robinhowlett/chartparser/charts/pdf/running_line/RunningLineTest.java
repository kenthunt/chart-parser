package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.robinhowlett.chartparser.charts.pdf.Chart.convertToText;
import static com.robinhowlett.chartparser.charts.pdf.running_line.RunningLine
        .groupRunningLineCharactersByColumn;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class RunningLineTest {

    private TestChartResources sampleCharts = new TestChartResources();

    @Test
    public void groupRunningLineCharactersByColumn_WithWinningRunningLine_GroupsColumnsCorrectly()
            throws Exception {
        TreeSet<RunningLineColumnIndex> runningLineColumnIndices =
                new TreeSet<>(new RunningLineHeader.RunningLineHeaderComparator());
        runningLineColumnIndices.addAll(sampleCharts.getRunningLineColumnIndices());

        List<ChartCharacter> winningRunningLine = sampleCharts.getRunningLineLines(0).get(1);
        Map<String, List<ChartCharacter>> charactersByColumn =
                groupRunningLineCharactersByColumn(runningLineColumnIndices, winningRunningLine);

        assertThat(charactersByColumn.keySet().toString(), equalTo("[LastRaced, Pgm, HorseName" +
                "(Jockey), Wgt, M/E, PP, Start, 1/4, 1/2, Str, Fin, Odds, Comments]"));
        assertThat(convertToText(charactersByColumn.get("Wgt")), equalTo("124"));
        assertThat(convertToText(charactersByColumn.get("LastRaced")), equalTo("19Jun16 3ARP6"));
    }
}
