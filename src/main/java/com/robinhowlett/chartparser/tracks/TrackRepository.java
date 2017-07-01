package com.robinhowlett.chartparser.tracks;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 * Loads the {@link Track} codes, countries, and names from a file
 */
public class TrackRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackRepository.class);
    private static final String FILENAME = "track-codes.csv";

    private CsvMapper csvMapper;

    public TrackRepository(CsvMapper csvMapper) {
        this.csvMapper = csvMapper;
    }

    public Optional<Track> findByCode(String trackCode) {
        for (Track track : findAll()) {
            if (track.getCode().trim().equals(trackCode.trim())) {
                return of(track);
            }
        }
        return empty();
    }

    public Optional<Track> findByName(String trackName) {
        for (Track track : findAll()) {
            if (track.getName().trim().equals(trackName.trim())) {
                return of(track);
            }
        }
        return empty();
    }

    public List<Track> findAll() {
        CsvSchema schema = CsvSchema.emptySchema().withColumnSeparator(';').withHeader();

        try {
            File tracks = Paths.get(getClass().getClassLoader().getResource(FILENAME).toURI())
                    .toFile();
            MappingIterator<Track> mappingIterator = csvMapper.readerFor(Track.class)
                    .with(schema).readValues(tracks);
            return mappingIterator.readAll();
        } catch (IOException | URISyntaxException e) {
            LOGGER.error(String.format("Unable to convert %s into a list of Tracks", FILENAME), e);
        }
        return Collections.emptyList();
    }
}
