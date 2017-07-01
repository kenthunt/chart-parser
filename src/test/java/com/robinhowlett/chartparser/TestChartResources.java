package com.robinhowlett.chartparser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.robinhowlett.chartparser.charts.pdf.Breed;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.running_line.RunningLineColumnIndex;
import com.robinhowlett.chartparser.charts.pdf.running_line.RunningLineHeader;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.ColumnRange;
import com.robinhowlett.chartparser.charts.pdf.wagering.WageringColumn;
import com.robinhowlett.chartparser.charts.pdf.wagering.WageringTreeSet;
import com.robinhowlett.chartparser.exceptions.ChartParserException;
import com.robinhowlett.chartparser.fractionals.FractionalTreeSet;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall;
import com.robinhowlett.chartparser.points_of_call.PointsOfCallTreeSet;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.robinhowlett.chartparser.ChartParser.getObjectMapper;
import static com.robinhowlett.chartparser.charts.pdf.running_line.RunningLine
        .groupRunningLineCharactersByColumn;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestChartResources {

    public File getPdfChartsFile() {
        return getPdfChartsFile("ARP_2016-07-24_race-charts.pdf");
    }

    public File getPdfChartsFile(String filename) {
        return new File(getClass().getClassLoader().getResource(filename).getFile());
    }

    public List<File> getPdfChartsFiles() throws URISyntaxException, IOException {
        List<Path> files = Files.list(Paths.get("src/test/resources"))
                .filter(path -> path.toString().endsWith("pdf"))
                .collect(Collectors.toList());

        List<File> csvChartsFiles = new ArrayList<>();
        for (Path path : files) {
            csvChartsFiles.add(path.toFile());
        }
        return csvChartsFiles;
    }

    public List<File> getCsvChartsFiles() throws URISyntaxException, IOException {
        URI uri = getClass().getClassLoader().getResource("csv").toURI();
        List<Path> files = Files.list(Paths.get(uri)).collect(Collectors.toList());

        List<File> csvChartsFiles = new ArrayList<>();
        for (Path path : files) {
            csvChartsFiles.add(path.toFile());
        }
        return csvChartsFiles;
    }

    public List<String> getCsvCharts() throws IOException, URISyntaxException {
        List<String> csvCharts = new ArrayList<>();
        for (File csvChartFile : getCsvChartsFiles()) {
            csvCharts.add(new String(Files.readAllBytes(csvChartFile.toPath()), UTF_8));
        }
        return csvCharts;
    }

    public String getFirstCsvChart() throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(getCsvChartsFiles().get(0).toPath()), UTF_8);
    }

    public String getCsvChart(int chartIndex) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(getCsvChartsFiles().get(chartIndex).toPath()), UTF_8);
    }

    public PDDocument getCharts() throws IOException {
        return PDDocument.load(getPdfChartsFile());
    }

    public ChartCharacter getSampleChartCharacter(double xDirAdj, double yDirAdj, char unicode) {
        ChartCharacter chartCharacter = new ChartCharacter();
        chartCharacter.setxDirAdj(xDirAdj);
        chartCharacter.setyDirAdj(yDirAdj);
        chartCharacter.setFontSize(8.000);
        chartCharacter.setxScale(8.000);
        chartCharacter.setHeight(4.760);
        chartCharacter.setWidthOfSpace(2.224);
        chartCharacter.setWidthDirAdj(5.776);
        chartCharacter.setUnicode(unicode);
        return chartCharacter;
    }

    public List<List<ChartCharacter>> getSampleChartLines(int chartPageIndex) throws IOException,
            URISyntaxException, ChartParserException {
        List<ChartCharacter> chartCharacters =
                ChartParser.convertToChartCharacters(getCsvChart(chartPageIndex));
        return ChartParser.separateIntoLines(chartCharacters);
    }

    public List<List<ChartCharacter>> getRunningLineLines(int chartPageIndex) throws IOException,
            URISyntaxException, ChartParserException {
        return ChartParser.getRunningLines(getSampleChartLines(chartPageIndex));
    }

    public List<ChartCharacter> getAllWageringLines()
            throws IOException, URISyntaxException, ChartParserException {
        return getSampleChartLines(8).get(26);
    }

    public List<ChartCharacter> getGridWageringLines()
            throws IOException, URISyntaxException, ChartParserException {
        return getAllWageringLines().subList(19, 289);
    }

    public Map<Double, List<ChartCharacter>> getWageringLinesByLine() throws IOException,
            URISyntaxException, ChartParserException {
        List<ChartCharacter> wageringLines = getAllWageringLines();

        Map<Double, List<ChartCharacter>> wageringLinesByLine =
                new LinkedHashMap<Double, List<ChartCharacter>>() {{
                    List<ChartCharacter> headerLine = new ArrayList<>(wageringLines.subList(19,
                            39));
                    headerLine.addAll(wageringLines.subList(114, 147));
                    put(339.566, headerLine);

                    List<ChartCharacter> firstRow = new ArrayList<>(wageringLines.subList(39, 67));
                    firstRow.addAll(wageringLines.subList(147, 171));
                    put(347.748, firstRow);

                    List<ChartCharacter> secondRow = new ArrayList<>(wageringLines.subList(67, 91));
                    secondRow.addAll(wageringLines.subList(171, 197));
                    put(355.623, secondRow);

                    List<ChartCharacter> thirdRow = wageringLines.subList(91, 114);
                    thirdRow.addAll(wageringLines.subList(197, 226));
                    put(363.498, thirdRow);

                    List<ChartCharacter> fourthRow = wageringLines.subList(255, 290);
                    put(371.373, fourthRow);

                    List<ChartCharacter> fifthRow = wageringLines.subList(290, 318);
                    put(379.248, fifthRow);
                }};

        return wageringLinesByLine;
    }

    public LinkedHashMap<String, ColumnRange> getWageringGridColumnRanges() {
        return new LinkedHashMap<String, ColumnRange>() {{
            put("Pgm", new ColumnRange(8.199, 25.535));
            put("Horse", new ColumnRange(29.761, 52.433));
            put("Win", new ColumnRange(134.151, 148.815));
            put("Place", new ColumnRange(161.926, 182.83));
            put("Show", new ColumnRange(195.509, 216.845));
            put("WagerType", new ColumnRange(228.187, 273.531));
            put("WinningNumbers", new ColumnRange(307.557, 376.005));
            put("Payoff", new ColumnRange(410.667, 435.115));
            put("Pool", new ColumnRange(468.802, 486.138));
        }};
    }

    public WageringTreeSet getWageringGridColumnFloors() {
        return new WageringTreeSet() {{
            add(new WageringColumn("Pgm", 0.0));
            add(new WageringColumn("HorseWin", 29.761));
            add(new WageringColumn("Place", 148.815));
            add(new WageringColumn("Show", 182.83));
            add(new WageringColumn("WagerType", 228.187));
            add(new WageringColumn("WinningNumbersPayoff", 307.557));
            add(new WageringColumn("Pool", 435.115));
            add(new WageringColumn("Carryover", 486.138));
        }};
    }

    public Map<Double, Map<String, List<ChartCharacter>>> getWageringGridWithJustFirstRow()
            throws IOException, URISyntaxException, ChartParserException {
        List<ChartCharacter> wageringLines = getAllWageringLines();

        return new LinkedHashMap<Double, Map<String, List<ChartCharacter>>>() {{
            put(347.748, new LinkedHashMap<String, List<ChartCharacter>>() {{
                put("Pgm", wageringLines.subList(39, 40));
                put("HorseWin", wageringLines.subList(40, 59));
                put("Place", wageringLines.subList(59, 63));
                put("Show", wageringLines.subList(63, 67));
                put("WagerType", wageringLines.subList(147, 158));
                put("WinningNumbersPayoff", wageringLines.subList(158, 166));
                put("Pool", wageringLines.subList(166, 171));
            }});
        }};
    }

    public List<ChartCharacter> getWageringGridHeaderLine()
            throws IOException, URISyntaxException, ChartParserException {
        List<ChartCharacter> wageringLines = getAllWageringLines();
        List<ChartCharacter> wpsHeaders = wageringLines.subList(19, 39);
        List<ChartCharacter> exoticsHeaders = wageringLines.subList(114, 147);

        List<ChartCharacter> wageringHeaderLine = new ArrayList<>(wpsHeaders);
        wageringHeaderLine.addAll(exoticsHeaders);

        return wageringHeaderLine;
    }

    public Map<String, List<ChartCharacter>> getWinnersWinPlaceShowGridRow() throws IOException,
            URISyntaxException, ChartParserException {
        List<ChartCharacter> wageringLines = getAllWageringLines();

        Map<String, List<ChartCharacter>> winnerWinPlaceShowGridRow = new LinkedHashMap<>();
        winnerWinPlaceShowGridRow.put("Pgm", wageringLines.subList(39, 40));
        winnerWinPlaceShowGridRow.put("HorseWin", wageringLines.subList(40, 59));
        winnerWinPlaceShowGridRow.put("Place", wageringLines.subList(59, 63));
        winnerWinPlaceShowGridRow.put("Show", wageringLines.subList(63, 67));

        return winnerWinPlaceShowGridRow;
    }

    public Map<String, List<ChartCharacter>> getExactaGridRow() throws IOException,
            URISyntaxException, ChartParserException {
        List<ChartCharacter> wageringLines = getAllWageringLines();

        Map<String, List<ChartCharacter>> firstExotic = new LinkedHashMap<>();
        firstExotic.put("WagerType", wageringLines.subList(147, 158));
        firstExotic.put("WinningNumbersPayoff", wageringLines.subList(158, 166));
        firstExotic.put("Pool", wageringLines.subList(166, 171));

        return firstExotic;
    }

    public FractionalTreeSet getFractionalTimePoints() throws IOException {
        File fractionalTimesFile = new File(
                getClass().getClassLoader().getResource("fractional-times.json").getFile());
        FractionalTreeSet fractionalPoints = getObjectMapper().readValue(fractionalTimesFile,
                new TypeReference<FractionalTreeSet>() {
                });
        return fractionalPoints;
    }

    public List<RunningLineColumnIndex> getRunningLineColumnIndices() {
        List<RunningLineColumnIndex> runningLineColumnIndices = new ArrayList<>();
        runningLineColumnIndices.add(new RunningLineColumnIndex("LastRaced", 9.92));
        runningLineColumnIndices.add(new RunningLineColumnIndex("Pgm", 69.623));
        runningLineColumnIndices.add(new RunningLineColumnIndex("HorseName(Jockey)", 95.548));
        runningLineColumnIndices.add(new RunningLineColumnIndex("Wgt", 249.092));
        runningLineColumnIndices.add(new RunningLineColumnIndex("M/E", 266.807));
        runningLineColumnIndices.add(new RunningLineColumnIndex("PP", 287.476));
        runningLineColumnIndices.add(new RunningLineColumnIndex("Start", 304.955));
        runningLineColumnIndices.add(new RunningLineColumnIndex("1/4", 331.766));
        runningLineColumnIndices.add(new RunningLineColumnIndex("1/2", 367.198));
        runningLineColumnIndices.add(new RunningLineColumnIndex("Str", 402.63));
        runningLineColumnIndices.add(new RunningLineColumnIndex("Fin", 438.062));
        runningLineColumnIndices.add(new RunningLineColumnIndex("Odds", 463.919));
        runningLineColumnIndices.add(new RunningLineColumnIndex("Comments", 494.163));
        return runningLineColumnIndices;
    }

    public Map<String, List<ChartCharacter>> getSampleRunningLineCharsByColumn(
            int chartPageIndex, int starterIndex)
            throws IOException, URISyntaxException, ChartParserException {
        TreeSet<RunningLineColumnIndex> runningLineColumnIndices =
                new TreeSet<>(new RunningLineHeader.RunningLineHeaderComparator());
        runningLineColumnIndices.addAll(
                RunningLineHeader.createIndexOfRunningLineColumns(
                        getRunningLineLines(chartPageIndex).get(0)));
        List<ChartCharacter> winningRunningLine =
                getRunningLineLines(chartPageIndex).get(starterIndex);
        return groupRunningLineCharactersByColumn(runningLineColumnIndices, winningRunningLine);
    }

    public PointsOfCall getPointsOfCall(Breed breed, int raceDistanceInFeet) throws IOException {
        String fileName = "points_of_call/points-of-call.json";
        if (breed.equals(Breed.QUARTER_HORSE)) {
            fileName = "points_of_call/points-of-call_short-mixed.json";
        }
        File pointsOfCallFile =
                new File(getClass().getClassLoader().getResource(fileName).getFile());
        PointsOfCallTreeSet pointsOfCall = getObjectMapper().readValue(pointsOfCallFile,
                new TypeReference<PointsOfCallTreeSet>() {
                });
        return pointsOfCall.floor(new PointsOfCall("", raceDistanceInFeet, null));
    }

}
