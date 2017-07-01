package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The owner of the {@link Starter}
 */
public class Trainer {

    private static final Pattern TRAINERS_PATTERN = Pattern.compile("Trainers:\\|.+");
    private static final Pattern TRAINER_PATTERN = Pattern.compile("(\\w+)?\\s?-\\s?(.+),( (.+))?");

    private static final Logger LOGGER = LoggerFactory.getLogger(Trainer.class);

    @JsonIgnore
    private final String program;
    private final String name;
    private final String firstName;
    private final String lastName;

    public Trainer(String program, String firstName, String lastName) {
        this.program = program;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = (firstName != null && !firstName.isEmpty()) ?
                (firstName + " " + lastName) : lastName;
    }

    public static List<Trainer> parse(List<List<ChartCharacter>> lines) {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            Matcher matcher = TRAINERS_PATTERN.matcher(text);
            if (matcher.find()) {
                return parseTrainers(text);
            }
        }
        return new ArrayList<>();
    }

    static List<Trainer> parseTrainers(String text) {
        List<Trainer> trainers = new ArrayList<>();

        text = text.substring(text.indexOf('|') + 1).replaceAll(System.lineSeparator(), " ");
        List<String> trainersText = Arrays.asList(text.split(";"));

        for (String trainerText : trainersText) {
            Trainer trainer = parseTrainer(trainerText);
            if (trainer != null) {
                trainers.add(trainer);
            }
        }

        return trainers;
    }

    private static Trainer parseTrainer(String text) {
        Matcher matcher = TRAINER_PATTERN.matcher(text);
        if (matcher.find()) {
            String programNumber = matcher.group(1);
            String trainerLastName = matcher.group(2);
            String trainerFirstName = null;
            if (matcher.group(3) != null) {
                trainerFirstName = matcher.group(4);
            }
            return new Trainer(programNumber, trainerFirstName, trainerLastName);
        }
        // e.g. java.lang.RuntimeException:  3 - unknown trainer,
        // java.lang.RuntimeException:  - ---
        LOGGER.warn(String.format("Unable to parse trainer from text: %s", text));
        return null;
    }

    public String getProgram() {
        return program;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trainer trainer = (Trainer) o;

        if (program != null ? !program.equals(trainer.program) : trainer.program != null)
            return false;
        if (name != null ? !name.equals(trainer.name) : trainer.name != null) return false;
        if (firstName != null ? !firstName.equals(trainer.firstName) : trainer.firstName != null)
            return false;
        return lastName != null ? lastName.equals(trainer.lastName) : trainer.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = program != null ? program.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "program='" + program + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
