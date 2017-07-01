package com.robinhowlett.chartparser.fractionals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

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
            File fractionalTimesFile = Paths.get(getClass().getClassLoader().getResource(FILENAME)
                    .toURI()).toFile();
            return mapper.readValue(fractionalTimesFile,
                    new TypeReference<FractionalTreeSet>() {
                    });
        } catch (IOException | URISyntaxException e) {
            throw new ChartParserException(String.format("Unable to read %s as JSON", FILENAME), e);
        }
    }

}
