package com.robinhowlett.chartparser.charts.pdf.wagering;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Horse;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.ColumnRange;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.ExoticPayoffPool;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.HorseNameWin;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.WagerNameUnit;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.WinPlaceShowPayoffPool;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.WinPlaceShowPayoffPool
        .WinPlaceShowPayoff;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.WinningNumbersPayoff;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools
        .WAGERING_COLUMN_NAMES;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WagerPayoffPoolsTest {
    private static TestChartResources SAMPLE_CHARTS = new TestChartResources();

    @Test
    public void organizeWageringLinesByLine_WithWageringLines_GroupsByLine() throws Exception {
        Map<Double, List<ChartCharacter>> expected = SAMPLE_CHARTS.getWageringLinesByLine();

        Map<Double, List<ChartCharacter>> wageringLinesByLine =
                WagerPayoffPools.organizeWageringLinesByLine(SAMPLE_CHARTS.getGridWageringLines());

        assertThat(wageringLinesByLine, equalTo(expected));
    }

    @Test
    public void populateHeaderColumns_WithSampleHeaderLine_MapsCorrectly() throws Exception {
        Map<String, ColumnRange> expected = SAMPLE_CHARTS.getWageringGridColumnRanges();

        Map<String, ColumnRange> headerColumns = WagerPayoffPools.populateHeaderColumns(
                SAMPLE_CHARTS.getWageringGridHeaderLine(), WAGERING_COLUMN_NAMES);

        assertThat(headerColumns, equalTo(expected));
    }

    @Test
    public void createWageringGrid_WithFirstRow_AssignsCharactersToCorrectColumn()
            throws Exception {
        Map<Double, Map<String, List<ChartCharacter>>> expected =
                SAMPLE_CHARTS.getWageringGridWithJustFirstRow();

        Map<Double, List<ChartCharacter>> wageringLinesByLine =
                SAMPLE_CHARTS.getWageringLinesByLine();
        wageringLinesByLine.remove(339.566);
        WageringTreeSet columnFloors = SAMPLE_CHARTS.getWageringGridColumnFloors();

        Map<Double, Map<String, List<ChartCharacter>>> wageringGrid =
                WagerPayoffPools.createWageringGrid(wageringLinesByLine, columnFloors);

        assertThat(wageringGrid.get(347.748), equalTo(expected.get(347.748)));
    }

    @Test
    public void mergeColumnsSplitBetweenLines_WithSimpleData_CombinesRelatedRows()
            throws Exception {
        Map<Double, Map<String, List<ChartCharacter>>> expected =
                new LinkedHashMap<Double, Map<String, List<ChartCharacter>>>() {{
                    put(1.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", new ArrayList<ChartCharacter>() {{
                            ChartCharacter a = new ChartCharacter();
                            a.setUnicode('A');
                            add(a);
                            ChartCharacter c = new ChartCharacter();
                            c.setUnicode('C');
                            add(c);
                        }});
                        put("WinningNumbersPayoff", new ArrayList<ChartCharacter>() {{
                            ChartCharacter b = new ChartCharacter();
                            b.setUnicode('B');
                            add(b);
                        }});
                    }});
                    put(2.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", null);
                        put("WinningNumbersPayoff", null);
                    }});
                    put(3.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", new ArrayList<ChartCharacter>() {{
                            ChartCharacter d = new ChartCharacter();
                            d.setUnicode('D');
                            add(d);
                        }});
                        put("WinningNumbersPayoff", new ArrayList<ChartCharacter>() {{
                            ChartCharacter e = new ChartCharacter();
                            e.setUnicode('E');
                            add(e);
                            ChartCharacter f = new ChartCharacter();
                            f.setUnicode('F');
                            add(f);
                        }});
                    }});
                    put(4.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", null);
                        put("WinningNumbersPayoff", null);
                    }});
                }};

        Map<Double, Map<String, List<ChartCharacter>>> wageringGrid =
                new LinkedHashMap<Double, Map<String, List<ChartCharacter>>>() {{
                    put(1.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", new ArrayList<ChartCharacter>() {{
                            ChartCharacter a = new ChartCharacter();
                            a.setUnicode('A');
                            add(a);
                        }});
                        put("WinningNumbersPayoff", new ArrayList<ChartCharacter>() {{
                            ChartCharacter b = new ChartCharacter();
                            b.setUnicode('B');
                            add(b);
                        }});
                    }});
                    put(2.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", new ArrayList<ChartCharacter>() {{
                            ChartCharacter c = new ChartCharacter();
                            c.setUnicode('C');
                            add(c);
                        }});
                        put("WinningNumbersPayoff", null);
                    }});
                    put(3.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", new ArrayList<ChartCharacter>() {{
                            ChartCharacter d = new ChartCharacter();
                            d.setUnicode('D');
                            add(d);
                        }});
                        put("WinningNumbersPayoff", new ArrayList<ChartCharacter>() {{
                            ChartCharacter e = new ChartCharacter();
                            e.setUnicode('E');
                            add(e);
                        }});
                    }});
                    put(4.0, new LinkedHashMap<String, List<ChartCharacter>>() {{
                        put("WagerType", null);
                        put("WinningNumbersPayoff", new ArrayList<ChartCharacter>() {{
                            ChartCharacter f = new ChartCharacter();
                            f.setUnicode('F');
                            add(f);
                        }});
                    }});
                }};

        wageringGrid = WagerPayoffPools.mergeColumnsSplitBetweenLines(wageringGrid);

        assertThat(wageringGrid, equalTo(expected));
    }

    @Test
    public void parseWinPlaceShowPool_WithSevenFigures_ParsesCorrectly() throws Exception {
        Integer winPlaceShowPool =
                WagerPayoffPools.parseTotalWinPlaceShowPool("Total WPS Pool: $3,782,701");
        assertThat(winPlaceShowPool, equalTo(3782701));
    }

    @Test
    public void parse_WithFullWageringLines_ParsesWinPlaceShowAndExotics() throws Exception {
        WagerPayoffPools wagerPayoffPools =
                WagerPayoffPools.parsePayoffs(6334, SAMPLE_CHARTS.getGridWageringLines());

        assertThat(wagerPayoffPools, equalTo(expectedWagerPayoffPools()));
    }

    public static WagerPayoffPools expectedWagerPayoffPools() {
        List<WinPlaceShowPayoff> winPlaceShowPayoffs = new ArrayList<WinPlaceShowPayoff>() {{
            add(expectedWinPlaceAndShowPayoff());
            add(expectedPlaceAndShowPayoff());
            add(expectedShowPayoff());
        }};

        List<ExoticPayoffPool> exoticPayoffPools = new ArrayList<ExoticPayoffPool>() {{
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Exacta"),
                    new WinningNumbersPayoff("7-8", null, 23.4), 2892, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Quinella"),
                    new WinningNumbersPayoff("7-8", null, 17.6), 1239, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Trifecta"),
                    new WinningNumbersPayoff("7-8-3", null, 104.8), 3983, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Superfecta"),
                    new WinningNumbersPayoff("7-8-3-6", null, 1140.6), 1521, null));
            add(new ExoticPayoffPool(new WagerNameUnit(2.0, "Daily Double"),
                    new WinningNumbersPayoff("11-7", null, 16.4), 882, null));
        }};
        WinPlaceShowPayoffPool winPlaceShowPayoffPool =
                new WinPlaceShowPayoffPool(6334, winPlaceShowPayoffs);
        return new WagerPayoffPools(winPlaceShowPayoffPool, exoticPayoffPools);
    }

    public static WinPlaceShowPayoff expectedWinPlaceAndShowPayoff() {
        return new WinPlaceShowPayoff("7", new HorseNameWin(new Horse("Prater Sixty Four"), 3.8),
                2.8, 2.4);
    }

    public static WinPlaceShowPayoff expectedPlaceAndShowPayoff() {
        return new WinPlaceShowPayoff("8", new HorseNameWin(new Horse("Candy Sweetheart"), null),
                6.4, 3.8);
    }

    public static WinPlaceShowPayoff expectedShowPayoff() {
        return new WinPlaceShowPayoff("3", new HorseNameWin(new Horse("Midnightwithdrawal"),
                null), null, 3.4);
    }

    public static class WinPlaceShowPayoffTest {

        @Test
        public void parse_WithWinnersWinPlaceShowGridRow_ParsesWPSPayoffCorrectly() throws
                Exception {
            WinPlaceShowPayoff expected = new WinPlaceShowPayoff("7",
                    new HorseNameWin(new Horse("Prater Sixty Four"), 3.8), 2.8, 2.4);

            Map<String, List<ChartCharacter>> winnersWPSGridRow =
                    SAMPLE_CHARTS.getWinnersWinPlaceShowGridRow();

            WinPlaceShowPayoff winnersWPSPayoff = WinPlaceShowPayoff.parse(winnersWPSGridRow);

            assertThat(winnersWPSPayoff, equalTo(expected));
        }

        @RunWith(Parameterized.class)
        public static class HorseNameWinTest {
            private String horseNameWin;
            private HorseNameWin expected;

            public HorseNameWinTest(String horseNameWin, HorseNameWin expected) {
                this.horseNameWin = horseNameWin;
                this.expected = expected;
            }

            @Parameterized.Parameters(name = "{0}")
            public static Collection winnersAndWinPayoffs() throws IOException {
                return Arrays.asList(new Object[][]{
                        {"California Chrome|7.00", new HorseNameWin(new Horse("California " +
                                "Chrome"), 7.00)},
                        {"Bayern", new HorseNameWin(new Horse("Bayern"), null)}
                });
            }

            @Test
            public void parseHorseNameAndWinPayoff_ParsesNameAndWinCorrectly() throws Exception {
                assertThat(WinPlaceShowPayoff.parseHorseNameAndWinPayoff(horseNameWin),
                        equalTo(expected));
            }
        }
    }

    public static class ExoticPayoffPoolTest {

        @Test
        public void parse_WithExactaGridRow_CreatesCorrectExoticPayoffPool() throws Exception {
            ExoticPayoffPool expected = new ExoticPayoffPool(new WagerNameUnit(2.0, "Exacta"),
                    new WinningNumbersPayoff("7-8", null, 23.4), 2892, null);

            Map<String, List<ChartCharacter>> exactaGridRow = SAMPLE_CHARTS.getExactaGridRow();

            ExoticPayoffPool exoticPayoffPool = ExoticPayoffPool.parse(exactaGridRow);

            assertThat(exoticPayoffPool, equalTo(expected));
        }

        @RunWith(Parameterized.class)
        public static class WagerNameUnitTest {

            private String wagerType;
            private WagerNameUnit expected;

            public WagerNameUnitTest(String wagerType, WagerNameUnit expected) {
                this.wagerType = wagerType;
                this.expected = expected;
            }

            @Parameterized.Parameters(name = "{0}")
            public static Collection wagerAmountsAndTypes() throws IOException {
                return Arrays.asList(new Object[][]{
                        {"$2.00 Exacta", new WagerNameUnit(2.0, "Exacta")},
                        {"$2.00 Daily Double", new WagerNameUnit(2.0, "Daily Double")},
                        {"$0.10 Superfecta", new WagerNameUnit(0.1, "Superfecta")},
                        {"$2.00 Consolation\nDouble", new WagerNameUnit(2.0, "Consolation " +
                                "Double")}
                });
            }

            @Test
            public void parseWagerType_WithVariousWagerTypes_CreatesCorrectWagerNameAmounts()
                    throws Exception {
                assertThat(ExoticPayoffPool.parseWagerType(wagerType), equalTo(expected));
            }

        }

        @RunWith(Parameterized.class)
        public static class WinningNumbersPayoffTest {

            private String winningNumbersPayoffText;
            private WinningNumbersPayoff expected;

            public WinningNumbersPayoffTest(String winningNumbersPayoffText, WinningNumbersPayoff
                    expected) {
                this.winningNumbersPayoffText = winningNumbersPayoffText;
                this.expected = expected;
            }

            @Parameterized.Parameters(name = "{0}")
            public static Collection wagerAmountsAndTypes() throws IOException {
                return Arrays.asList(new Object[][]{
                        {"7-8|23.40", new WinningNumbersPayoff("7-8", null, 23.4)},
                        {"OAKS/DERBY 13-5|5.70",
                                new WinningNumbersPayoff("OAKS/DERBY 13-5", null, 5.7)},
                        {"8-5-9-1-5/11/21 (5 correct)|694.95",
                                new WinningNumbersPayoff("8-5-9-1-5/11/21", 5, 694.95)},
                        {"2-8-5-9-1-5/11/21 (6|5,574.10\ncorrect)",
                                new WinningNumbersPayoff("2-8-5-9-1-5/11/21", 6, 5574.10)}
                });
            }

            @Test
            public void parseWinningNumbersAndPayoff_WithWinningNumbersAndPayoffs_ParsesCorrectly()
                    throws Exception {
                assertThat(ExoticPayoffPool.parseWinningNumbersAndPayoff(winningNumbersPayoffText),
                        equalTo(expected));
            }

        }

    }
}
