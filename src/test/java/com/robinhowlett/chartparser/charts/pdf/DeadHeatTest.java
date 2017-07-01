package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeadHeatTest {

    private static final String DH =
            "Dead Heats:|1st place - # 6 Tina Celesta" + System.lineSeparator() +
                    "1st place - # 7 Chans Pearl" + System.lineSeparator() +
                    "1st place - # 8 Cool Miss Ann";

    @Test
    public void parseDeadHeat_WithTripleDeadHeat_ReturnsTrue() throws Exception {
        assertTrue(DeadHeat.parseDeadHeat(DH));
    }

    @Test
    public void parseDeadHeat_WithNonsenseText_ReturnsFalse() throws Exception {
        assertFalse(DeadHeat.parseDeadHeat("nonsense"));
    }
}
