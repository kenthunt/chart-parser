package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.exceptions.ChartParserException;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ClaimedHorseTest {

    private static final String CLAIMS =
            "3 Claimed Horse(s):|" +
                    "Bella Giornatta|New Trainer: Linda Mikus|New Owner: Carlo Fisco" +
                    System.lineSeparator() +
                    "Magic Mama|New Trainer: Gary Sherlock|New Owner: Bernhardt, Charles and " +
                    "Warren, Craig" + System.lineSeparator() +
                    "Mavicsa|New Trainer: Jeff Bonde|New Owner: Mersad Metanovic";

    @Test
    public void parseClaimedHorses_WithThreeClaims_ExtractsDataCorrectly() throws Exception {
        List<ClaimedHorse> expected = new ArrayList<ClaimedHorse>() {{
            add(new ClaimedHorse(new Horse("Bella Giornatta"), "Linda Mikus", "Carlo Fisco"));
            add(new ClaimedHorse(new Horse("Magic Mama"), "Gary Sherlock",
                    "Bernhardt, Charles and Warren, Craig"));
            add(new ClaimedHorse(new Horse("Mavicsa"), "Jeff Bonde", "Mersad Metanovic"));
        }};

        List<ClaimedHorse> claimedHorses = ClaimedHorse.parseClaimedHorses(CLAIMS);

        assertThat(claimedHorses, equalTo(expected));
    }

    @Test
    public void parseClaimedHorses_WithNoValidClaims_ReturnsEmpty() throws Exception {
        List<ClaimedHorse> claimedHorses = ClaimedHorse.parseClaimedHorses("nonsense");
        assertThat(claimedHorses, empty());
    }

    @Test
    public void parseClaimedHorses_WithClaimsNoTrainerOwner_ParsesCorrectly() throws Exception {
        List<ClaimedHorse> expected = new ArrayList<ClaimedHorse>() {{
            add(new ClaimedHorse(new Horse("Black Velvet Seven"), null, null));
        }};

        List<ClaimedHorse> claimedHorses = ClaimedHorse.parseClaimedHorses("1 Claimed Horse(s)" +
                ":|Black Velvet Seven|New Trainer:|New Owner:");

        assertThat(claimedHorses, equalTo(expected));
    }

    @Test
    public void parseClaimedHorses_WithMultiLineTrainerName_ParsesCorrectly() throws Exception {
        List<ClaimedHorse> expected = new ArrayList<ClaimedHorse>() {{
            add(new ClaimedHorse(new Horse("Matters Not"), "Jimmie C. Richardson,Jr.",
                    "Bill Dean Stivers"));
            add(new ClaimedHorse(new Horse("Pull the Handle"), "Jimmie C. Richardson,Jr.",
                    "Mason A. King"));
        }};

        List<ClaimedHorse> claimedHorses = ClaimedHorse.parseClaimedHorses("2 Claimed Horse(s)" +
                ":|Matters Not|New Trainer: Jimmie C. Richardson," + System.lineSeparator() +
                "Jr.|" + System.lineSeparator() + "New Owner: Bill Dean Stivers" +
                System.lineSeparator() + "Pull the Handle|New Trainer: Jimmie C. Richardson," +
                System.lineSeparator() + "Jr.|" + System.lineSeparator() +
                "New Owner: Mason A. King");

        assertThat(claimedHorses, equalTo(expected));
    }

    @Test
    public void parseClaimedHorses_WithMissingTabs_ExtractsDataCorrectly() throws Exception {
        String claims = "2 Claimed Horse(s):|" +
                "Bella Giornatta|New Trainer: Linda Mikus New Owner: Carlo Fisco" +
                System.lineSeparator() +
                "Magic Mama New Trainer: Gary Sherlock New Owner: Bernhardt, Charles and " +
                "Warren, Craig";

        List<ClaimedHorse> expected = new ArrayList<ClaimedHorse>() {{
            add(new ClaimedHorse(new Horse("Bella Giornatta"), "Linda Mikus", "Carlo Fisco"));
            add(new ClaimedHorse(new Horse("Magic Mama"), "Gary Sherlock",
                    "Bernhardt, Charles and Warren, Craig"));
        }};

        List<ClaimedHorse> claimedHorses = ClaimedHorse.parseClaimedHorses(claims);

        assertThat(claimedHorses, equalTo(expected));
    }
}
