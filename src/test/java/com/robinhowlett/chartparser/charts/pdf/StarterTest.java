package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.DistanceSurfaceTrackRecord.RaceDistance;
import com.robinhowlett.chartparser.charts.pdf.running_line.HorseJockey;
import com.robinhowlett.chartparser.charts.pdf.running_line.LastRaced;
import com.robinhowlett.chartparser.charts.pdf.running_line.LastRaced.LastRacePerformance;
import com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment;
import com.robinhowlett.chartparser.charts.pdf.running_line.Odds;
import com.robinhowlett.chartparser.charts.pdf.running_line.Weight;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall;
import com.robinhowlett.chartparser.points_of_call.PointsOfCallService;
import com.robinhowlett.chartparser.tracks.Track;
import com.robinhowlett.chartparser.tracks.TrackService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Equipment
        .BLINKERS;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Equipment
        .FLIPPING_HALTER;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Equipment
        .FRONT_BANDAGES;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Medication
        .BUTE;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Medication
        .LASIX;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class StarterTest {

    private TestChartResources sampleCharts = new TestChartResources();

    private int chartPageIndex = 0;
    private int starterIndex = 1;
    private Breed breed;
    private RaceDistance raceDistance;
    private Starter expected;

    private TrackService trackService;
    private PointsOfCallService pointsOfCallService;

    LocalDate raceDate = LocalDate.of(2016, 7, 24);

    public StarterTest(int chartPageIndex, int starterIndex, Breed breed, RaceDistance raceDistance,
            Starter expected) {
        this.chartPageIndex = chartPageIndex;
        this.starterIndex = starterIndex;
        this.breed = breed;
        this.raceDistance = raceDistance;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "Page {0}, Starter {1}, Breed {2}")
    public static Collection runningLines() throws IOException {
        return Arrays.asList(new Object[][]{
                {0, 1, Breed.THOROUGHBRED, new RaceDistance("Six Furlongs", true, 3960),
                        expectedThoroughbredStarter()},
                {5, 1, Breed.QUARTER_HORSE, new RaceDistance("Three Hundred And Fifty Yards",
                        true, 1050), expectedQuarterHorseStarter()}
        });
    }

    @Test
    public void parseRunningLineData_WithSampleRunningLine_UpdatesStarterCorrectly()
            throws Exception {
        Track track = new Track();
        track.setCode("ARP");
        track.setName("ARAPAHOE PARK");
        track.setCountry("USA");

        trackService = mock(TrackService.class);
        when(trackService.getTrack("ARP")).thenReturn(Optional.of(track));

        pointsOfCallService = mock(PointsOfCallService.class);
        when(pointsOfCallService.getPointsOfCallForDistance(eq(breed), eq(raceDistance)))
                .thenReturn(sampleCharts.getPointsOfCall(breed, raceDistance.getValue()));

        Starter starter = Starter.parseRunningLineData(
                sampleCharts.getSampleRunningLineCharsByColumn(chartPageIndex, starterIndex),
                raceDate, breed, raceDistance, trackService, pointsOfCallService);

        assertThat(starter, equalTo(expected));

        verify(trackService).getTrack("ARP");
        verify(pointsOfCallService).getPointsOfCallForDistance(breed, raceDistance);
    }

    public static Starter expectedThoroughbredStarter() {
        Track track = new Track();
        track.setCode("ARP");
        track.setName("ARAPAHOE PARK");
        track.setCountry("USA");
        LocalDate lastRaceDate = LocalDate.of(2016, 6, 19);
        LastRacePerformance lastRacePerformance = new LastRacePerformance(3, track, 6);

        Horse horse = new Horse("Back Stop");
        Jockey jockey = new Jockey("Dennis", "Collins");

        Starter.Builder expectedBuilder = new Starter.Builder();
        expectedBuilder.lastRaced(new LastRaced(lastRaceDate, 35, lastRacePerformance));
        expectedBuilder.program("6");
        expectedBuilder.horseAndJockey(new HorseJockey(horse, jockey));
        expectedBuilder.weight(new Weight(124, 0));
        expectedBuilder.medicationAndEquipment(new MedicationEquipment("BL f",
                Arrays.asList(BUTE, LASIX), Arrays.asList(FRONT_BANDAGES)));
        expectedBuilder.postPosition(6);
        expectedBuilder.odds(new Odds(3.40, false));
        expectedBuilder.comments("speed off rail 3wd tr");
        expectedBuilder.pointsOfCall(new ArrayList<PointsOfCall.PointOfCall>() {{
            PointsOfCall.PointOfCall first = new PointsOfCall.PointOfCall(1, "Start", null);
            first.setRelativePosition(new PointsOfCall.PointOfCall.RelativePosition(1, null));
            add(first);

            PointsOfCall.PointOfCall second = new PointsOfCall.PointOfCall(2, "1/4", 1320);
            second.setRelativePosition(
                    new PointsOfCall.PointOfCall.RelativePosition(1,
                            new PointsOfCall.PointOfCall.RelativePosition.LengthsAhead("2", 2.0)));
            add(second);

            PointsOfCall.PointOfCall third = new PointsOfCall.PointOfCall(3, "1/2", 2640);
            third.setRelativePosition(
                    new PointsOfCall.PointOfCall.RelativePosition(1,
                            new PointsOfCall.PointOfCall.RelativePosition.LengthsAhead("Head",
                                    0.1)));
            add(third);

            PointsOfCall.PointOfCall fourth = new PointsOfCall.PointOfCall(5, "Str", 3300);
            fourth.setRelativePosition(
                    new PointsOfCall.PointOfCall.RelativePosition(1,
                            new PointsOfCall.PointOfCall.RelativePosition.LengthsAhead("1/2",
                                    0.5)));
            add(fourth);

            PointsOfCall.PointOfCall fifth = new PointsOfCall.PointOfCall(6, "Fin", 3960);
            fifth.setRelativePosition(
                    new PointsOfCall.PointOfCall.RelativePosition(1,
                            new PointsOfCall.PointOfCall.RelativePosition.LengthsAhead("1 1/2",
                                    1.5)));
            add(fifth);
        }});

        return expectedBuilder.build();
    }

    public static Starter expectedQuarterHorseStarter() {
        Track track = new Track();
        track.setCode("ARP");
        track.setName("ARAPAHOE PARK");
        track.setCountry("USA");

        LocalDate lastRaceDate = LocalDate.of(2016, 7, 4);
        LastRacePerformance lastRacePerformance = new LastRacePerformance(4, track, 1);

        Horse horse = new Horse("Acme Rocket");
        Jockey jockey = new Jockey("Edwin", "Pena");

        Starter.Builder expectedBuilder = new Starter.Builder();
        expectedBuilder.lastRaced(new LastRaced(lastRaceDate, 20, lastRacePerformance));
        expectedBuilder.program("3");
        expectedBuilder.horseAndJockey(new HorseJockey(horse, jockey));
        expectedBuilder.weight(new Weight(121, 0));
        expectedBuilder.medicationAndEquipment(new MedicationEquipment("BL bk",
                Arrays.asList(BUTE, LASIX), Arrays.asList(BLINKERS, FLIPPING_HALTER)));
        expectedBuilder.postPosition(3);
        expectedBuilder.odds(new Odds(1.10, true));
        expectedBuilder.individualTimeMillis(17529L);
        expectedBuilder.speedIndex(97);
        expectedBuilder.comments("broke in best");
        expectedBuilder.pointsOfCall(new ArrayList<PointsOfCall.PointOfCall>() {{
            PointsOfCall.PointOfCall first = new PointsOfCall.PointOfCall(1, "Start", null);
            first.setRelativePosition(new PointsOfCall.PointOfCall.RelativePosition(2, null));
            add(first);

            PointsOfCall.PointOfCall second = new PointsOfCall.PointOfCall(2, "Str1", null);
            second.setRelativePosition(
                    new PointsOfCall.PointOfCall.RelativePosition(1,
                            new PointsOfCall.PointOfCall.RelativePosition.LengthsAhead("1/2",
                                    0.5)));
            add(second);

            PointsOfCall.PointOfCall third = new PointsOfCall.PointOfCall(5, "Str", null);
            third.setRelativePosition(
                    new PointsOfCall.PointOfCall.RelativePosition(1,
                            new PointsOfCall.PointOfCall.RelativePosition.LengthsAhead("1", 1.0)));
            add(third);

            PointsOfCall.PointOfCall fourth = new PointsOfCall.PointOfCall(6, "Fin", 1050);

            PointsOfCall.PointOfCall.RelativePosition position =
                    new PointsOfCall.PointOfCall.RelativePosition(1,
                            new PointsOfCall.PointOfCall.RelativePosition.LengthsAhead("1 1/4",
                                    1.25));
            fourth.setRelativePosition(position);
            add(fourth);
        }});

        return expectedBuilder.build();
    }
}
