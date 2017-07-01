package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DisqualificationTest {

    private static final String DQ = "Disqualification(s):|" +
            "#10 Ncc Anchorman from 3 to 7" + System.lineSeparator() +
            "# 5 This Wagons Okay from 4 to 10" + System.lineSeparator() +
            "# 8 Carpe Diem Drew from 9 to 11" + System.lineSeparator() +
            "#12f Ima Rabbit from 1 to 13";

    @Test
    public void parseDisqualifications_WithThreeDQs_ParsesDataCorrectly() throws Exception {
        List<Disqualification> expected = new ArrayList<Disqualification>() {{
            add(new Disqualification("10", new Horse("Ncc Anchorman"), 3, 7));
            add(new Disqualification("5", new Horse("This Wagons Okay"), 4, 10));
            add(new Disqualification("8", new Horse("Carpe Diem Drew"), 9, 11));
            add(new Disqualification("12f", new Horse("Ima Rabbit"), 1, 13));
        }};

        List<Disqualification> disqualifications = Disqualification.parseDisqualifications(DQ);

        assertThat(disqualifications, equalTo(expected));
    }

}
