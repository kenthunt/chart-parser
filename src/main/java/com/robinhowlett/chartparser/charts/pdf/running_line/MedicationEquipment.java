package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Starter;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.robinhowlett.chartparser.charts.pdf.Chart.convertToText;

/**
 * Stores the text summary of the medication and equipment used in this race by the {@link Starter},
 * and also contains list for each individual {@link Medication} and {@link Equipment} instance
 */
public class MedicationEquipment {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicationEquipment.class);

    private final String text;
    private List<Medication> medications;
    private List<Equipment> equipment;

    public MedicationEquipment(String text) throws ChartParserException {
        this.text = text;
        this.medications = new ArrayList<>();
        this.equipment = new ArrayList<>();

        if (text != null) {
            char[] chars = text.toCharArray();
            for (char aChar : chars) {
                if (Character.isUpperCase(aChar)) {
                    Medication medication = Medication.lookup(aChar);
                    medications.add(medication);
                } else if (Character.isLowerCase(aChar) || Character.isDigit(aChar)) {
                    Equipment equip = Equipment.lookup(aChar);
                    equipment.add(equip);
                }
            }
        }
    }

    public MedicationEquipment(String text, List<Medication> medications,
            List<Equipment> equipment) {
        this.text = text;
        this.medications = medications;
        this.equipment = equipment;
    }

    public static MedicationEquipment parse(final List<ChartCharacter> chartCharacters)
            throws ChartParserException {
        return new MedicationEquipment(convertToText(chartCharacters));
    }

    public String getText() {
        return text;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    /**
     * Medications used on race day by a {@link Starter}
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Medication {
        BUTE('B', "Bute"),
        LASIX('L', "Lasix"),
        ADJUNCT('A', "Adjunct Medication"),
        FIRST_TIME_BUTE('C', "First Time Bute"),
        FIRST_TIME_LASIX('M', "First Time Lasix");

        private static final Medication[] MEDICATIONS = Medication.values();
        private char code;
        private String text;

        Medication(char code, String text) {
            this.code = code;
            this.text = text;
        }

        public char getCode() {
            return code;
        }

        public String getText() {
            return text;
        }

        @JsonCreator
        static Medication lookup(char code) throws ChartParserException {
            for (Medication medication : MEDICATIONS) {
                if (medication.code == code) {
                    return medication;
                }
            }
            throw new ChartParserException(String.format("Unable to find Medication for code: " +
                    "%s", code));
        }

        @Override
        public String toString() {
            return "Medication{" +
                    "code=" + code +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    /**
     * Equipment used on race day by a {@link Starter}
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Equipment {
        ALUMINUM_PADS('a', "Aluminium Pads"),
        BLINKERS('b', "Blinkers"),
        MUD_CAULKS('c', "Mud Caulks"),
        GLUED_SHOES('d', "Glued Shoes"),
        INNER_RIMS('e', "Inner Rims"),
        FRONT_BANDAGES('f', "Front Bandages"),
        GOGGLES('g', "Goggles"),
        OUTER_RIMS('h', "Outer Rims"),
        INSERTS('i', "Inserts"),
        ALUMINUM_PAD('j', "Aluminium Pad"),
        FLIPPING_HALTER('k', "Flipping Halter"),
        BAR_SHOES('l', "Bar Shoes"),
        NO_WHIP('n', "No Whip"),
        BLINKER_OFF('o', "Blinker Off"),
        PADS('p', "Pads"),
        NASAL_STRIP_OFF('q', "Nasal Strip Off"),
        BAR_SHOE('r', "Bar Shoe"),
        NASAL_STRIP('s', "Nasal Strip"),
        TURNDOWNS('t', "Turndowns"),
        SPURS('u', "Spurs"),
        VISOR('v', "Visor"),
        QUEENS_PLATES('w', "Queens Plates"),
        CHEEK_PIECE_OFF('x', "Cheek Piece Off"),
        NO_SHOES('y', "No Shoes"),
        TONGUE_TIE('z', "Tongue Tie"),
        RUNNING_WS('1', "Running W's"),
        SCREENS('2', "Screens"),
        SHIELDS('3', "Shields");

        private static final Equipment[] EQUIPMENT = Equipment.values();
        private char code;
        private String text;

        Equipment(char code, String text) {
            this.code = code;
            this.text = text;
        }

        public char getCode() {
            return code;
        }

        public String getText() {
            return text;
        }

        @JsonCreator
        static Equipment lookup(char code) throws ChartParserException {
            for (Equipment equipment : EQUIPMENT) {
                if (equipment.code == code) {
                    return equipment;
                }
            }
            throw new ChartParserException(String.format("Unable to find Equipment for code: " +
                    "%s", code));
        }

        @Override
        public String toString() {
            return "Equipment{" +
                    "code=" + code +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicationEquipment that = (MedicationEquipment) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (medications != null ? !medications.equals(that.medications) : that.medications != null)
            return false;
        return equipment != null ? equipment.equals(that.equipment) : that.equipment == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (medications != null ? medications.hashCode() : 0);
        result = 31 * result + (equipment != null ? equipment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MedicationEquipment{" +
                "text='" + text + '\'' +
                ", medications=" + medications +
                ", equipment=" + equipment +
                '}';
    }
}
