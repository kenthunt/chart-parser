package com.robinhowlett.chartparser.points_of_call;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robinhowlett.chartparser.charts.pdf.Breed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads the points of call for a particular {@link Breed} as a {@link PointsOfCallTreeSet}. For
 * mixed breed races, the charts represent these in a QH-style format, but the points of call are
 * different based on whether the race distance is more in line with a TB or a QH race.
 */
public class PointsOfCallRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointsOfCallRepository.class);

    private ObjectMapper mapper;

    public PointsOfCallRepository(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public PointsOfCallTreeSet findByBreed(Breed breed, String distance) {
        String fileNameForBreed = getFileNameForBreed(breed, distance);

        try {
            try (InputStream pointsOfCall =
                         getClass().getClassLoader().getResourceAsStream(fileNameForBreed)) {
                return mapper.readValue(pointsOfCall, new TypeReference<PointsOfCallTreeSet>() {
                });
            }
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
