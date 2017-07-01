package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static com.robinhowlett.chartparser.charts.pdf.DistanceSurfaceTrackRecord
        .parseDistanceSurface;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DistanceSurfaceTrackRecordTest {

    @Test
    public void parseDistanceSurface_WithDistanceSurfaceTrackRecord_ParsesCorrectly()
            throws Exception {
        DistanceSurfaceTrackRecord.TrackRecord expectedTrackRecord =
                new DistanceSurfaceTrackRecord.TrackRecord("No It Ain't", "1:08.19", 68190L,
                        LocalDate.of(2011, 8, 12));
        DistanceSurfaceTrackRecord expected = new DistanceSurfaceTrackRecord("Six Furlongs",
                "Dirt", null, expectedTrackRecord);

        Optional<DistanceSurfaceTrackRecord> distanceSurface = parseDistanceSurface("Six Furlongs" +
                " On The Dirt|Track Record: (No It Ain't - 1:08.19 - August 12, 2011)");

        assertThat(distanceSurface.get(), equalTo(expected));
    }

    @Test
    public void parseDistanceSurface_WithOffTheTurf_ParsesCorrectly() throws Exception {
        DistanceSurfaceTrackRecord.TrackRecord expectedTrackRecord =
                new DistanceSurfaceTrackRecord.TrackRecord("No It Ain't", "1:08.19", 68190L,
                        LocalDate.of(2011, 8, 12));
        DistanceSurfaceTrackRecord expected = new DistanceSurfaceTrackRecord("Six Furlongs",
                "Dirt", "Turf", expectedTrackRecord);

        Optional<DistanceSurfaceTrackRecord> distanceSurface = parseDistanceSurface("Six Furlongs" +
                " On The Dirt - Originally Scheduled For the Turf|Track Record: (No It Ain't - " +
                "1:08.19 - August 12, 2011)");

        assertThat(distanceSurface.get(), equalTo(expected));
    }

    @Test
    public void parseDistanceSurface_WithFalsePositive_DoesNotParse() throws Exception {
        Optional<DistanceSurfaceTrackRecord> distanceSurface =
                parseDistanceSurface("Or Restricted Over A Mile On The Turf In 1998-99. Weight " +
                        "122 lbs. Non-winners of $35,000 over a mile since June 1 allowed, 3 lbs.");

        assertThat(distanceSurface.isPresent(), equalTo(false));
    }
}
