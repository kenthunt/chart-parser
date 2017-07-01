package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.Chart;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Starter;
import com.robinhowlett.chartparser.exceptions.ChartParserException;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall.PointOfCall.RelativePosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Updates the {@link Starter}s with the total lengths behind at each point of call
 */
public class PastPerformanceRunningLinePreview {

    private static final Pattern PP_RUNNING_LINE_PREVIEW =
            Pattern.compile("Past Performance Running Line Preview");

    public static List<Starter> parse(List<List<ChartCharacter>> lines, List<Starter> starters)
            throws ChartParserException {
        List<ChartCharacter> ppRunningLinePreview = getRunningLinePreview(lines);
        List<List<ChartCharacter>> runningLinePreviews = separateIntoLines(ppRunningLinePreview);
        List<ChartCharacter> headerCharacters = runningLinePreviews.get(1);

        List<String> columns = new ArrayList<>(Arrays.asList("Pgm", "HorseName"));
        Map<String, ChartCharacter> headerColumns =
                RunningLineHeader.populateHeaderColumns(headerCharacters, columns);

        headerColumns = RunningLineHeader.populateHeaderColumnsWithInRaceRunningLine(
                headerCharacters, headerColumns);

        TreeSet<RunningLineColumnIndex> runningLineColumnIndices =
                RunningLineHeader.createRunningLineColumnIndices(headerColumns);

        runningLinePreviews = runningLinePreviews.subList(2, runningLinePreviews.size());

        for (List<ChartCharacter> runningLinePreview : runningLinePreviews) {
            Map<String, List<ChartCharacter>> runningLineCharactersByColumn =
                    RunningLine.groupRunningLineCharactersByColumn(runningLineColumnIndices,
                            runningLinePreview);

            String program = Chart.convertToText(runningLineCharactersByColumn.get("Pgm"));
            String horseName = Chart.convertToText(runningLineCharactersByColumn.get("HorseName"));

            for (String column : runningLineCharactersByColumn.keySet()) {
                List<ChartCharacter> chartCharacters = runningLineCharactersByColumn.get(column);
                switch (column) {
                    case "Pgm":
                    case "HorseName":
                        break;
                    default:
                        RelativePosition relativePosition =
                                PointOfCallPosition.parse(chartCharacters);
                        for (Starter starter : starters) {
                            if (starter.matchesProgramOrName(program, horseName)) {
                                starter.setTotalLengthsBehindAtPointOfCall(column,
                                        relativePosition);
                            }
                        }
                        break;
                }
            }
        }

        return starters;
    }

    static List<ChartCharacter> getRunningLinePreview(List<List<ChartCharacter>> lines) {
        List<ChartCharacter> ppRunningLinePreview = new ArrayList<>();

        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);

            Matcher matcher = PP_RUNNING_LINE_PREVIEW.matcher(text);
            if (matcher.find()) {
                ppRunningLinePreview = line;
                break;
            }
        }
        return ppRunningLinePreview;
    }

    static List<List<ChartCharacter>> separateIntoLines(List<ChartCharacter> data) {
        List<List<ChartCharacter>> lines = new ArrayList<>();
        List<ChartCharacter> line = new ArrayList<>();
        ChartCharacter previous = null;
        for (ChartCharacter d : data) {
            if (previous == null) {
                line.add(d);
            } else {
                if (d.getxDirAdj() < previous.getxDirAdj()) {
                    lines.add(line);
                    line = new ArrayList<>();
                }

                line.add(d);
            }
            previous = d;
        }
        lines.add(line);
        return lines;
    }

}
