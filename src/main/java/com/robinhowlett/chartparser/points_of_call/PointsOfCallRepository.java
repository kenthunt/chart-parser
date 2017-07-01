package com.robinhowlett.chartparser.points_of_call;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robinhowlett.chartparser.charts.pdf.Breed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Loads the points of call for a particular {@link Breed} as a {@link PointsOfCallTreeSet}. For
 * mixed breed races, the charts represent these in a QH-style format, but the points of call are
 * different based on whether the race distance is more in line with a TB or a QH race.
 */
public class PointsOfCallRepository {

    private ObjectMapper mapper;

    public PointsOfCallRepository(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public PointsOfCallTreeSet findByBreed(Breed breed, String distance) {
        String fileNameForBreed = getFileNameForBreed(breed, distance);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ClassLoader.getSystemResourceAsStream(fileNameForBreed)));
            return mapper.readValue(reader,
                    new TypeReference<PointsOfCallTreeSet>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to read %s as JSON", fileNameForBreed),
                    e);
        }
    }

    private String getFileNameForBreed(Breed breed, String distance) {
        StringBuilder filename = new StringBuilder("points_of_call/points-of-call");
        if (breed.equals(Breed.QUARTER_HORSE) || breed.equals(Breed.MIXED)) {
            if (distance.toLowerCase().matches("^.* furlongs?|miles? .*$")) {
                filename.append("_long-mixed"); // TB-type race distances
            } else {
                filename.append("_short-mixed"); // QH-type race distances
            }
        }
        filename.append(".json");

        return filename.toString();
    }

}
