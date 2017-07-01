package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static com.robinhowlett.chartparser.charts.pdf.TrackRaceDateRaceNumber
        .buildTrackRaceDateRaceNumber;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TrackRaceDateRaceNumberTest {

    @Test
    public void buildTrackRaceDateRaceNumber_WithValidText_ParsesCorrectly() throws Exception {
        TrackRaceDateRaceNumber expected =
                new TrackRaceDateRaceNumber("ARAPAHOE PARK", LocalDate.of(2016, 7, 24), 1);

        Optional<TrackRaceDateRaceNumber> trackRaceDateRaceNumber =
                buildTrackRaceDateRaceNumber("ARAPAHOE PARK - July 24, 2016 - Race 1");

        assertThat(trackRaceDateRaceNumber.get(), equalTo(expected));
    }

    @Test
    public void buildTrackRaceDateRaceNumber_WithInvalidText_ReturnsEmptyOptional()
            throws Exception {
        assertThat(buildTrackRaceDateRaceNumber("nonsense").isPresent(), is(false));
    }
}
