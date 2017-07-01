package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ScratchTest {

    private final String chartText;
    private final List<Scratch> expected;

    public ScratchTest(String chartText, List<Scratch> expected) {
        this.chartText = chartText;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection scratches() {
        return Arrays.asList(new Object[][]{
                {"Scratched Horse(s):|Bright Eyed Vision (Veterinarian)",
                        Arrays.asList(new Scratch(new Horse("Bright Eyed Vision"),
                                "Veterinarian"))},
                {"Scratched Horse(s):|Bright Eyed Vision (GB) (Veterinarian)",
                        Arrays.asList(new Scratch(new Horse("Bright Eyed Vision (GB)"),
                                "Veterinarian"))},
                {"Scratched Horse(s):|Wicked Valentine (Trainer) (Earned $734.00)",
                        Arrays.asList(new Scratch(new Horse("Wicked Valentine"), "Trainer"))},
                {"Scratched Horse(s):|R Diva (Trainer) (Earned $326.00), Valiant Chickie " +
                        "(Trainer) (Earned $326.00)",
                        Arrays.asList(
                                new Scratch(new Horse("R Diva"), "Trainer"),
                                new Scratch(new Horse("Valiant Chickie"), "Trainer"))},
                {"Scratched Horse(s):|Wicked Valentine (IRE) (Trainer) (Earned $734.00)",
                        Arrays.asList(new Scratch(new Horse("Wicked Valentine (IRE)"), "Trainer"))},
        });
    }

    @Test
    public void parseScratches_WithParameterizedInput_ReturnsCorrectListOfScratches()
            throws Exception {
        assertThat(Scratch.parseScratchedHorses(chartText), equalTo(expected));
    }
}
