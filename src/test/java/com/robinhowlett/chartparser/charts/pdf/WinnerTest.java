package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WinnerTest {

    @Test
    public void parseWinner_WithSampleText_ReturnsValidWinnerObject() throws Exception {
        Winner expected = new Winner("Ocho Ocho Ocho", "Dark Bay or Brown", "Colt", "Street " +
                "Sense", "Winner", "Horse Chestnut (SAF)", LocalDate.of(2012, 4, 2), "Kentucky");

        Winner winner = Winner.parseWinner("Winner:|Ocho Ocho Ocho, Dark Bay or Brown Colt, by " +
                "Street Sense out of Winner, by Horse Chestnut (SAF). Foaled Apr 02, 2012 in " +
                "Kentucky.").get();

        assertThat(winner, equalTo(expected));
    }

    @Test
    public void parseWinner_WithMissingColor_ReturnsValidWinnerObject() throws Exception {
        Winner expected = new Winner("PATRIOT MISSLE", null, "Colt", "WIKING", "TOPPERS " +
                "STARLIGHT", "MISTER TOPPER", LocalDate.of(1991, 1, 27), "Florida");

        Winner winner = Winner.parseWinner("Winner:|PATRIOT MISSLE, Colt, by WIKING out of " +
                "TOPPERS STARLIGHT, by MISTER TOPPER. Foaled Jan 27, 1991 in Florida.").get();

        assertThat(winner, equalTo(expected));
    }
}
