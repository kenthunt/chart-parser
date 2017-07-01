package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and stores the track name, the race date (as a {@link LocalDate} instance) and the race
 * number
 */
public class TrackRaceDateRaceNumber {
    static final Pattern TRACK_DATE_NUMBER_PATTERN =
            Pattern.compile("([A-Z0-9\\s&]+)\\s-\\s(.+)+\\s-\\sRace\\s(\\d+)");

    private static final DateTimeFormatter MONTH_DAY_YEAR_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d, yyyy");

    private final String trackName;
    private final LocalDate raceDate;
    private final int raceNumber;

    protected TrackRaceDateRaceNumber(final String trackName, final LocalDate raceDate,
            final int raceNumber) {
        this.trackName = trackName;
        this.raceDate = raceDate;
        this.raceNumber = raceNumber;
    }

    public static TrackRaceDateRaceNumber parse(final List<List<ChartCharacter>> lines)
            throws NoLinesToParse, InvalidRaceException {
        if (lines == null || lines.isEmpty()) {
            throw new NoLinesToParse();
        }

        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            Optional<TrackRaceDateRaceNumber> trackRaceDateRaceNumber =
                    buildTrackRaceDateRaceNumber(text);
            if (trackRaceDateRaceNumber.isPresent()) {
                return trackRaceDateRaceNumber.get();
            }
        }

        throw new InvalidRaceException("Unable to detect a valid race track, date and number");
    }

    static Optional<TrackRaceDateRaceNumber> buildTrackRaceDateRaceNumber(String text) {
        Matcher matcher = TRACK_DATE_NUMBER_PATTERN.matcher(text);
        if (matcher.find()) {
            String trackName = matcher.group(1);
            LocalDate raceDate = parseRaceDate(matcher.group(2));
            int raceNumber = Integer.parseInt(matcher.group(3));
            return Optional.of(new TrackRaceDateRaceNumber(trackName, raceDate, raceNumber));
        }
        return Optional.empty();
    }

    static LocalDate parseRaceDate(String raceDateText) {
        return LocalDate.parse(raceDateText, MONTH_DAY_YEAR_FORMATTER);
    }

    @Override
    public String toString() {
        return "TrackRaceDateRaceNumber{" +
                "trackName='" + trackName + '\'' +
                ", raceDate=" + raceDate +
                ", raceNumber=" + raceNumber +
                '}';
    }

    public String getTrackName() {
        return trackName;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public int getRaceNumber() {
        return raceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackRaceDateRaceNumber that = (TrackRaceDateRaceNumber) o;

        if (raceNumber != that.raceNumber) return false;
        if (trackName != null ? !trackName.equals(that.trackName) : that.trackName != null)
            return false;
        return raceDate != null ? raceDate.equals(that.raceDate) : that.raceDate == null;

    }

    @Override
    public int hashCode() {
        int result = trackName != null ? trackName.hashCode() : 0;
        result = 31 * result + (raceDate != null ? raceDate.hashCode() : 0);
        result = 31 * result + raceNumber;
        return result;
    }

    public static class InvalidRaceException extends ChartParserException {
        public InvalidRaceException(String message) {
            super(message);
        }
    }

    public static class NoLinesToParse extends ChartParserException {
        public NoLinesToParse() {
            super("No content available to be parsed.");
        }
    }

}
