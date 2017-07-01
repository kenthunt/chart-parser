package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CancellationTest {

    @Test
    public void checkForCancellation_WithCancelledContent_IdentifiesReason() throws Exception {
        Optional<Cancellation> cancellation =
                Cancellation.checkForCancellation("Cancelled - Track Conditions");
        assertThat(cancellation.get(), equalTo(new Cancellation(true, "Track Conditions")));
    }

    @Test
    public void checkForCancellation_WithRegularContent_ReturnsNotCancelled() throws Exception {
        Optional<Cancellation> cancellation = Cancellation.checkForCancellation("CLAIMING - " +
                "Thoroughbred");
        assertThat(cancellation.isPresent(), equalTo(false));
    }

    @Test
    public void checkForCancellation_WithMixedContent_ReturnsCancelledNoReason() throws Exception {
        Optional<Cancellation> cancellation =
                Cancellation.checkForCancellation("CANCELLED - Thoroughbred");
        assertThat(cancellation.get(), equalTo(new Cancellation(Cancellation.NO_REASON_AVAILABLE)));
    }

    @Test
    public void checkForCancellation_WithCancelledRace_ReturnsCancelledNoReason() throws Exception {
        Optional<Cancellation> cancellation =
                Cancellation.checkForCancellation("CANCELLED RACE - Quarter Horse");
        assertThat(cancellation.get(), equalTo(new Cancellation(Cancellation.NO_REASON_AVAILABLE)));
    }
}
