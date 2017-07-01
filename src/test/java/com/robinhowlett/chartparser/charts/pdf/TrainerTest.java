package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrainerTest {

    private static final String TRAINERS = "Trainers:|6 - Randazzo, Jr., Frank; 3 - Matthews, " +
            "Doug; 5 - Davis, Liane; 4 - Dorris, Chris; 2 - Cristel, Mark; 1 - Dorris, Tom; " +
            "7 - unknown trainer,; - ---";

    @Test
    public void parseTrainers_WithSixOwnerDetails_ExtractsTrainerInfoCorrectly() throws Exception {
        List<Trainer> expected = new ArrayList<Trainer>() {{
            add(new Trainer("6", "Frank", "Randazzo, Jr."));
            add(new Trainer("3", "Doug", "Matthews"));
            add(new Trainer("5", "Liane", "Davis"));
            add(new Trainer("4", "Chris", "Dorris"));
            add(new Trainer("2", "Mark", "Cristel"));
            add(new Trainer("1", "Tom", "Dorris"));
            add(new Trainer("7", null, "unknown trainer"));
        }};

        List<Trainer> trainers = Trainer.parseTrainers(TRAINERS);

        assertThat(trainers, equalTo(expected));
    }
}
