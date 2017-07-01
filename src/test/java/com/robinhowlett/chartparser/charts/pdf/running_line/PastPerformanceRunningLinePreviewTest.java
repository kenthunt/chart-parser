package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Horse;
import com.robinhowlett.chartparser.charts.pdf.Starter;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall.PointOfCall;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall.PointOfCall.RelativePosition.TotalLengthsBehind;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.robinhowlett.chartparser.points_of_call.PointsOfCall.PointOfCall.RelativePosition;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class PastPerformanceRunningLinePreviewTest {

    private TestChartResources sampleCharts = new TestChartResources();
    Horse horse = new Horse("Magical Twist");

    @Test
    public void getRunningLinePreview_WithSampleChartLines_IdentifiesPreview() throws Exception {
        List<List<ChartCharacter>> lines = sampleCharts.getSampleChartLines(0);

        List<ChartCharacter> runningLinePreview =
                PastPerformanceRunningLinePreview.getRunningLinePreview(lines);

        assertThat(runningLinePreview, equalTo(lines.get(23)));
    }

    @Test
    public void separateIntoLines_WithSimpleData_SeparatesByXDirAdjCorrectly() throws Exception {
        List<List<ChartCharacter>> expected = new ArrayList<List<ChartCharacter>>() {{
            add(new ArrayList<ChartCharacter>() {{
                ChartCharacter a = new ChartCharacter();
                a.setxDirAdj(1.0);
                a.setUnicode('A');
                add(a);

                ChartCharacter b = new ChartCharacter();
                b.setxDirAdj(2.0);
                b.setUnicode('B');
                add(b);
            }});
            add(new ArrayList<ChartCharacter>() {{
                ChartCharacter c = new ChartCharacter();
                c.setxDirAdj(0.5);
                c.setUnicode('C');
                add(c);
            }});

        }};

        List<ChartCharacter> chartCharacters = new ArrayList<ChartCharacter>() {{
            ChartCharacter a = new ChartCharacter();
            a.setxDirAdj(1.0);
            a.setUnicode('A');
            add(a);

            ChartCharacter b = new ChartCharacter();
            b.setxDirAdj(2.0);
            b.setUnicode('B');
            add(b);

            ChartCharacter c = new ChartCharacter();
            c.setxDirAdj(0.5);
            c.setUnicode('C');
            add(c);
        }};

        List<List<ChartCharacter>> lines =
                PastPerformanceRunningLinePreview.separateIntoLines(chartCharacters);

        assertThat(lines, equalTo(expected));
    }

    @Test
    public void parse_WithSingleStarter_UpdatesTotalBeatenLengthsCorrectly() throws Exception {
        List<Starter> expected = new ArrayList<Starter>() {{
            Starter.Builder starterBuilder = new Starter.Builder();
            starterBuilder.program("2");
            starterBuilder.horseAndJockey(new HorseJockey(horse, null));
            starterBuilder.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start", null);
                first.setRelativePosition(new RelativePosition(4, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "1/4", 1320);
                RelativePosition relativePositionPoint2 = new RelativePosition(4, new
                        RelativePosition.LengthsAhead("1/2", 0.5));
                relativePositionPoint2.setTotalLengthsBehind(new TotalLengthsBehind("2", 2.0));
                second.setRelativePosition(relativePositionPoint2);
                add(second);

                PointOfCall third = new PointOfCall(3, "1/2", 2640);
                RelativePosition relativePositionPoint3 = new RelativePosition(4, new
                        RelativePosition.LengthsAhead("1", 1.0));
                relativePositionPoint3.setTotalLengthsBehind(new RelativePosition.TotalLengthsBehind("3 1/2", 3.5));
                third.setRelativePosition(relativePositionPoint3);
                add(third);

                PointOfCall fourth = new PointOfCall(4, "3/4", 3960);
                RelativePosition relativePositionPoint4 = new RelativePosition(3, new
                        RelativePosition.LengthsAhead("3", 3.0));
                relativePositionPoint4.setTotalLengthsBehind(new RelativePosition.TotalLengthsBehind("1 1/2", 1.5));
                fourth.setRelativePosition(relativePositionPoint4);
                add(fourth);

                PointOfCall fifth = new PointOfCall(5, "Str", 4620);
                RelativePosition relativePositionPoint5 = new RelativePosition(1, new
                        RelativePosition.LengthsAhead("Head", 0.1));
                fifth.setRelativePosition(relativePositionPoint5);
                add(fifth);

                PointOfCall sixth = new PointOfCall(6, "Fin", 5280);
                RelativePosition relativePositionPoint6 = new RelativePosition(1, new
                        RelativePosition.LengthsAhead("3 3/4", 3.75));
                sixth.setRelativePosition(relativePositionPoint6);
                add(sixth);
            }});

            add(starterBuilder.build());
        }};

        List<Starter> starters = new ArrayList<Starter>() {{
            Starter.Builder starterBuilder = new Starter.Builder();
            starterBuilder.program("2");
            starterBuilder.horseAndJockey(new HorseJockey(horse, null));
            starterBuilder.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start", null);
                first.setRelativePosition(new RelativePosition(4, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "1/4", 1320);
                second.setRelativePosition(new RelativePosition(4,
                        new RelativePosition.LengthsAhead("1/2", 0.5)));
                add(second);

                PointOfCall third = new PointOfCall(3, "1/2", 2640);
                third.setRelativePosition(new RelativePosition(4,
                        new RelativePosition.LengthsAhead("1", 1.0)));
                add(third);

                PointOfCall fourth = new PointOfCall(4, "3/4", 3960);
                fourth.setRelativePosition(new RelativePosition(3,
                        new RelativePosition.LengthsAhead("3", 3.0)));
                add(fourth);

                PointOfCall fifth = new PointOfCall(5, "Str", 4620);
                fifth.setRelativePosition(new RelativePosition(1,
                        new RelativePosition.LengthsAhead("Head", 0.1)));
                add(fifth);

                PointOfCall sixth = new PointOfCall(6, "Fin", 5280);
                sixth.setRelativePosition(new RelativePosition(1,
                        new RelativePosition.LengthsAhead("3 3/4", 3.75)));
                add(sixth);
            }});

            add(starterBuilder.build());
        }};

        List<List<ChartCharacter>> lines = sampleCharts.getSampleChartLines(6);

        starters = PastPerformanceRunningLinePreview.parse(lines, starters);

        assertThat(starters, equalTo(expected));
    }

    @Test
    public void parse_WithSingleStarter_UpdatesTotalBeatenLengthsCorrectly2() throws Exception {
        Horse horse = new Horse("The Designer");

        List<Starter> expected = new ArrayList<Starter>() {{
            Starter.Builder starterBuilder = new Starter.Builder();
            starterBuilder.program("2");
            starterBuilder.horseAndJockey(new HorseJockey(horse, null));
            starterBuilder.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start", null);
                first.setRelativePosition(new RelativePosition(5, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "Str1", null);
                RelativePosition relativePositionPoint2 = new RelativePosition(4, new
                        RelativePosition.LengthsAhead("Neck", 0.25));
                relativePositionPoint2.setTotalLengthsBehind(new RelativePosition.TotalLengthsBehind("1 1/4", 1.25));
                second.setRelativePosition(relativePositionPoint2);
                add(second);

                PointOfCall third = new PointOfCall(3, "Str", null);
                RelativePosition relativePositionPoint3 = new RelativePosition(5, new
                        RelativePosition.LengthsAhead("Neck", 0.25));
                relativePositionPoint3.setTotalLengthsBehind(new RelativePosition.TotalLengthsBehind("1 3/4", 1.75));
                third.setRelativePosition(relativePositionPoint3);
                add(third);

                PointOfCall sixth = new PointOfCall(6, "Fin", 1050);
                RelativePosition relativePositionPoint6 = new RelativePosition(5, new
                        RelativePosition.LengthsAhead("Nose", 0.05));
                relativePositionPoint6.setTotalLengthsBehind(new RelativePosition.TotalLengthsBehind("2 1/4", 2.25));
                sixth.setRelativePosition(relativePositionPoint6);
                add(sixth);
            }});

            add(starterBuilder.build());
        }};

        List<Starter> starters = new ArrayList<Starter>() {{
            Starter.Builder starterBuilder = new Starter.Builder();
            starterBuilder.program("2");
            starterBuilder.horseAndJockey(new HorseJockey(horse, null));
            starterBuilder.pointsOfCall(new ArrayList<PointOfCall>() {{
                PointOfCall first = new PointOfCall(1, "Start", null);
                first.setRelativePosition(new RelativePosition(5, null));
                add(first);

                PointOfCall second = new PointOfCall(2, "Str1", null);
                second.setRelativePosition(new RelativePosition(4,
                        new RelativePosition.LengthsAhead("Neck", 0.25)));
                add(second);

                PointOfCall third = new PointOfCall(3, "Str", null);
                third.setRelativePosition(new RelativePosition(5,
                        new RelativePosition.LengthsAhead("Neck", 0.25)));
                add(third);

                PointOfCall sixth = new PointOfCall(6, "Fin", 1050);
                sixth.setRelativePosition(new RelativePosition(5,
                        new RelativePosition.LengthsAhead("Nose", 0.05)));
                add(sixth);
            }});

            add(starterBuilder.build());
        }};

        List<List<ChartCharacter>> lines = sampleCharts.getSampleChartLines(5);

        starters = PastPerformanceRunningLinePreview.parse(lines, starters);

        assertThat(starters, equalTo(expected));
    }
}
