package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ClaimingPriceTest {

    private static final String PRICES =
            "Claiming Prices:|9 - Furr the South: $15,000; 7 - Classic James: $15,000; 6 - First " +
                    "Noted: $15,000; 1 - Dannys Great Knight: $15,000; 4 -" +
                    System.lineSeparator() +
                    "Six Tins: $15,000; 3 - Brookstone Boy: $15,000; 2 - Hardspun: $15,000; 8 - " +
                    "Famous Dashn Dully: $15,000; 5 -" + System.lineSeparator() +
                    "Motorspeedway: $15,000;";

    @Test
    public void parseClaimingPrices_WithNineStarters_ExtractsDataCorrectly() throws Exception {
        List<ClaimingPrice> expected = new ArrayList<ClaimingPrice>() {{
            add(new ClaimingPrice("9", new Horse("Furr the South"), 15000));
            add(new ClaimingPrice("7", new Horse("Classic James"), 15000));
            add(new ClaimingPrice("6", new Horse("First Noted"), 15000));
            add(new ClaimingPrice("1", new Horse("Dannys Great Knight"), 15000));
            add(new ClaimingPrice("4", new Horse("Six Tins"), 15000));
            add(new ClaimingPrice("3", new Horse("Brookstone Boy"), 15000));
            add(new ClaimingPrice("2", new Horse("Hardspun"), 15000));
            add(new ClaimingPrice("8", new Horse("Famous Dashn Dully"), 15000));
            add(new ClaimingPrice("5", new Horse("Motorspeedway"), 15000));
        }};

        List<ClaimingPrice> claimingPrices = ClaimingPrice.parseClaimingPrices(PRICES);

        assertThat(expected, equalTo(claimingPrices));
    }

    @Test
    public void parseClaimingPrices_WithNoValidClaimingPrices_ReturnsEmpty() throws Exception {
        List<ClaimingPrice> claimingPrices = ClaimingPrice.parseClaimingPrices("nonsense");
        assertThat(claimingPrices, empty());
    }
}
