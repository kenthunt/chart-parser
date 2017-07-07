package com.robinhowlett.chartparser.fractionals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Loads the fractional times from a file
 */
public class FractionalPointRepository {

    private static final String FILENAME = "fractional-times.json";
    private ObjectMapper mapper;

    public FractionalPointRepository(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public FractionalTreeSet findAll() throws ChartParserException {
        try {
            try (InputStream fractionalPoints =
                         getClass().getClassLoader().getResourceAsStream(FILENAME)) {
                return mapper.readValue(fractionalPoints,
                        new TypeReference<FractionalTreeSet>() {
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to read %s as JSON", FILENAME),
                    e);
        }
    }

}
