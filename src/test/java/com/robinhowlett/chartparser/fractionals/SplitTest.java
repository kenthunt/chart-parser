package com.robinhowlett.chartparser.fractionals;

import com.robinhowlett.chartparser.fractionals.FractionalPoint.Fractional;
import com.robinhowlett.chartparser.fractionals.FractionalPoint.Split;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class SplitTest {

    private Fractional from;
    private Fractional to;
    private Split expected;

    public SplitTest(Fractional from, Fractional to, Split expected) {
        this.from = from;
        this.to = to;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0} {1}")
    public static Collection splits() {
        Fractional first = new Fractional(1, "1/4", 1320, "22.00", 22000L);
        Fractional second = new Fractional(2, "1/2", 2640, "46.00", 46000L);
        Fractional third = new Fractional(3, "1 1/2", 7920, "2:48.98", 168980L);
        Fractional fourth = new Fractional(4, "Fin", 8085, "2:54.08", 174080L);
        return Arrays.asList(new Object[][]{
                {null, first, new Split(1, "Start to 1/4", 1320, "22.00", 22000L, null, first)},
                {first, second, new Split(2, "1/4 to 1/2", 1320, "24.00", 24000L, first, second)},
                {second, third, new Split(3, "1/2 to 1 1/2", 5280, "2:02.98", 122980L, second, third)},
                {third, fourth, new Split(4, "1 1/2 to Fin", 165, "5.10", 5100L, third, fourth)}
        });
    }

    @Test
    public void calculateSplitsFromFrationals_WithVariousFractionals_CalculatesCorrectly() throws Exception {
        assertThat(Split.calculate(from, to), equalTo(expected));
    }
}
