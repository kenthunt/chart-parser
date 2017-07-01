package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and stores the race type (e.g. "STAKES"), name (e.g. "Kentucky Derby"), grade (e.g. 1),
 * black type (e.g. "Grade 1"), and for what {@link Breed}
 */
@JsonPropertyOrder({"breed"})
public class RaceTypeNameBlackTypeBreed {

    static final Pattern RACE_TYPE_NAME_GRADE_BREED =
            Pattern.compile("([A-Z ]+)( (.+) (Grade (\\d))| (.+ S\\.)( (.+))?| (.+))? - " +
                    "(Thoroughbred|Quarter Horse|Arabian|Mixed)");

    private static final Logger LOGGER = LoggerFactory.getLogger(RaceTypeNameBlackTypeBreed.class);

    private final String type;
    private final String name;
    private final Integer grade;
    private final String blackType;
    private final Breed breed;

    public RaceTypeNameBlackTypeBreed(String type, Breed breed) {
        this(type, null, breed);
    }

    public RaceTypeNameBlackTypeBreed(String type, String name, Breed breed) {
        this(type, name, null, breed);
    }

    public RaceTypeNameBlackTypeBreed(String type, String name, String blackType, Breed breed) {
        this(type, name, null, blackType, breed);
    }

    @JsonCreator
    public RaceTypeNameBlackTypeBreed(String type, String name, Integer grade, String blackType,
            Breed breed) {
        this.type = type;
        this.name = name;
        this.grade = grade;
        this.blackType = blackType;
        this.breed = breed;
    }

    public static RaceTypeNameBlackTypeBreed parse(List<List<ChartCharacter>> lines)
            throws Breed.NoMatchingBreedException, RaceTypeNameOrBreedNotIdentifiable {
        for (List<ChartCharacter> line : lines) {
            String rawText = Chart.convertToText(line);
            Optional<RaceTypeNameBlackTypeBreed> raceTypeNameGradeBreed =
                    parseRaceTypeNameBlackTypeBreed(rawText);
            if (raceTypeNameGradeBreed.isPresent()) {
                return raceTypeNameGradeBreed.get();
            }
        }
        throw new RaceTypeNameOrBreedNotIdentifiable("Unable to identify a valid race type, name " +
                "and/or breed");
    }

    static Optional<RaceTypeNameBlackTypeBreed> parseRaceTypeNameBlackTypeBreed(String text)
            throws Breed.NoMatchingBreedException {
        Matcher matcher = RACE_TYPE_NAME_GRADE_BREED.matcher(text);
        if (matcher.find()) {
            String breedText = matcher.group(10);
            Breed breed = Breed.forChartValue(breedText);
            String type = matcher.group(1);
            if (type == null) {
                LOGGER.warn("A race type was not found from text: " + text);
            }

            Integer grade = null;
            String blackType = null;
            String gradeText = matcher.group(5);
            if (gradeText != null) {
                grade = Integer.parseInt(gradeText);
                blackType = matcher.group(4);
            }

            String name = null;
            if (grade != null) {
                name = matcher.group(3);
            } else {
                String raceNameText = matcher.group(6);
                if (raceNameText != null) {
                    name = raceNameText;
                } else {
                    raceNameText = matcher.group(9);
                    if (raceNameText != null) {
                        name = raceNameText;
                    }
                }
                String blackTypeText = matcher.group(8);
                if (blackTypeText != null) {
                    blackType = blackTypeText;
                }
            }

            // correct when race name starts with capital letters
            if (type.toUpperCase().startsWith("STAKES")) {
                String[] split = type.split("\\s", 2); // split on the first space
                type = split[0];
                if (split.length > 1 && split[1] != null) {
                    name = split[1] + " " + name;
                }
            }

            return Optional.of(new RaceTypeNameBlackTypeBreed(type, name, grade, blackType, breed));
        }
        return Optional.empty();
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Integer getGrade() {
        return grade;
    }

    public String getBlackType() {
        return blackType;
    }

    public Breed getBreed() {
        return breed;
    }

    @Override
    public String toString() {
        return "RaceTypeNameBlackTypeBreed{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", blackType='" + blackType + '\'' +
                ", breed=" + breed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceTypeNameBlackTypeBreed that = (RaceTypeNameBlackTypeBreed) o;

        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
        if (blackType != null ? !blackType.equals(that.blackType) : that.blackType != null)
            return false;
        return breed == that.breed;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (blackType != null ? blackType.hashCode() : 0);
        result = 31 * result + (breed != null ? breed.hashCode() : 0);
        return result;
    }

    public static class RaceTypeNameOrBreedNotIdentifiable extends ChartParserException {
        public RaceTypeNameOrBreedNotIdentifiable(String message) {
            super(message);
        }
    }
}
