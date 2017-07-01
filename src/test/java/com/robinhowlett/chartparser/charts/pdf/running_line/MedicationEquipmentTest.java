package com.robinhowlett.chartparser.charts.pdf.running_line;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Equipment
        .BLINKERS;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Equipment
        .FLIPPING_HALTER;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Equipment
        .FRONT_BANDAGES;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Medication
        .BUTE;
import static com.robinhowlett.chartparser.charts.pdf.running_line.MedicationEquipment.Medication
        .LASIX;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@RunWith(Parameterized.class)
public class MedicationEquipmentTest {

    private String m_e;
    private MedicationEquipment expected;

    public MedicationEquipmentTest(String m_e, MedicationEquipment expected) {
        this.m_e = m_e;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection medEquips() {
        return asList(new Object[][]{
                {"L", new MedicationEquipment("L", asList(LASIX), emptyList())},
                {"BL bf", new MedicationEquipment("BL bf", asList(BUTE, LASIX),
                        asList(BLINKERS, FRONT_BANDAGES))},
                {"k", new MedicationEquipment("k", emptyList(), asList(FLIPPING_HALTER))},
                {"- -", new MedicationEquipment("- -", emptyList(), emptyList())}
        });
    }

    @Test
    public void parse_WithVariousValues_ReturnsCorrectInstance() throws Exception {
        Assert.assertThat(new MedicationEquipment(m_e), Matchers.equalTo(expected));
    }
}
