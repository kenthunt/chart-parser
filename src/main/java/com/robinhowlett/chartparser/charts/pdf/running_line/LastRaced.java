package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.robinhowlett.chartparser.charts.pdf.Chart;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Starter;
import com.robinhowlett.chartparser.exceptions.ChartParserException;
import com.robinhowlett.chartparser.tracks.Track;
import com.robinhowlett.chartparser.tracks.TrackService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.robinhowlett.chartparser.charts.pdf.running_line.LastRaced.LastRacePerformance
        .parseFromLastRaced;

import static java.lang.Integer.parseInt;

/**
 * Stores the {@link LocalDate} instance of the last race date, the number of days between this race
 * date and then, and the {@link Track}, race number, and finishing position of the {@link
 * Starter}'s last performance (if applicable and they exist)
 */
public class LastRaced {

    private final LocalDate raceDate;
    private final Integer daysSince;
    @JsonUnwrapped
    private final LastRacePerformance lastRacePerformance;

    public LastRaced(LocalDate raceDate, Integer daysSince, LastRacePerformance
            lastRacePerformance) {
        this.raceDate = raceDate;
        this.daysSince = daysSince;
        this.lastRacePerformance = lastRacePerformance;
    }

    private static LastRaced noLastRace() {
        return null;
    }

    public boolean hasLastRace() {
        return (raceDate != null);
    }

    /**
     * Attempt to parse a last race date, track, race number, and finishing position
     *
     * @param chartCharacters the PDF characters that may contain the last race details
     * @param raceDate        the {@link LocalDate} of the current race result
     * @param trackService    the {@link TrackService} to look up a track by code
     * @return LastRaced instance containing, if they exist, the last race date, track, race number,
     * and finishing position
     * @throws UnknownTrackException when the track could not be identified by its code
     */
    public static LastRaced parse(final List<ChartCharacter> chartCharacters,
            final LocalDate raceDate, final TrackService trackService) throws
            UnknownTrackException {
        if (chartCharacters.size() == 3) {
            return noLastRace();
        }

        LocalDate lastRaceDate = parseLastRaceDate(chartCharacters);
        int daysSince = Math.toIntExact(ChronoUnit.DAYS.between(lastRaceDate, raceDate));

        LastRacePerformance lastRacePerformance = parseFromLastRaced(chartCharacters, trackService);

        return new LastRaced(lastRaceDate, daysSince, lastRacePerformance);
    }

    private static LocalDate parseLastRaceDate(List<ChartCharacter> chartCharacters) {
        ChartCharacter lastChartCharacter = null;
        List<ChartCharacter> lastRaceDateCharacters = new ArrayList<>();
        for (ChartCharacter columnCharacter : chartCharacters) {
            // handle when lastChartCharacter race number is unknown
            if (columnCharacter.getFontSize() == 5 ||
                    spaceDetected(lastChartCharacter, columnCharacter)) {
                break;
            }
            lastRaceDateCharacters.add(columnCharacter);
            lastChartCharacter = columnCharacter;
        }
        String lastRaceDateText = Chart.convertToText(lastRaceDateCharacters);
        // so that 97 becomes 1997 and 03 becomes 2003
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("dMMM")
                .appendValueReduced(ChronoField.YEAR_OF_ERA, 2, 2, LocalDate.now().minusYears(80))
                .toFormatter();
        LocalDate lastRaceDate = LocalDate.parse(lastRaceDateText, dateTimeFormatter);

        chartCharacters.removeAll(lastRaceDateCharacters);

        return lastRaceDate;
    }

    /**
     * Returns true when the race number of the last performance is not present
     */
    private static boolean spaceDetected(ChartCharacter lastChartCharacter, ChartCharacter
            columnCharacter) {
        if (lastChartCharacter != null) {
            return (!Chart.addWhitespaceIfRequired(columnCharacter.getxDirAdj(),
                    lastChartCharacter.getxDirAdj(), lastChartCharacter.getWidthDirAdj())
                    .equals(""));
        }
        return false;
    }

    public LocalDate getRaceDate() {
        return raceDate;
    }

    public Integer getDaysSince() {
        return daysSince;
    }

    public LastRacePerformance getLastRacePerformance() {
        return lastRacePerformance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LastRaced lastRaced = (LastRaced) o;

        if (raceDate != null ? !raceDate.equals(lastRaced.raceDate) : lastRaced
                .raceDate != null)
            return false;
        if (daysSince != null ? !daysSince.equals(lastRaced.daysSince) : lastRaced.daysSince !=
                null)
            return false;
        return lastRacePerformance != null ? lastRacePerformance.equals(lastRaced
                .lastRacePerformance) : lastRaced.lastRacePerformance == null;
    }

    @Override
    public int hashCode() {
        int result = raceDate != null ? raceDate.hashCode() : 0;
        result = 31 * result + (daysSince != null ? daysSince.hashCode() : 0);
        result = 31 * result + (lastRacePerformance != null ? lastRacePerformance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LastRaced{" +
                "raceDate=" + raceDate +
                ", daysSince=" + daysSince +
                ", lastRacePerformance=" + lastRacePerformance +
                '}';
    }

    /**
     * The {@link Track}, race number, and finishing position of the {@link Starter}'s last
     * performance (if applicable and they exist)
     */
    public static class LastRacePerformance {
        private final Track track;
        private final Integer raceNumber;
        private final Integer officialPosition;

        public LastRacePerformance(Integer raceNumber, Track track, Integer officialPosition) {
            this.raceNumber = raceNumber;
            this.track = track;
            this.officialPosition = officialPosition;
        }

        static LastRacePerformance parseFromLastRaced(List<ChartCharacter> chartCharacters,
                TrackService trackService) throws UnknownTrackException {
            boolean trackCodeFound = false;
            StringBuilder lastRaceNumberBuilder = new StringBuilder();
            StringBuilder trackCodeBuilder = new StringBuilder();
            StringBuilder finishPositionBuilder = new StringBuilder();
            for (ChartCharacter pdfCharacter : chartCharacters) {
                if (pdfCharacter.getFontSize() == 5) {
                    if (trackCodeFound) {
                        finishPositionBuilder.append(pdfCharacter.getUnicode());
                    } else {
                        lastRaceNumberBuilder.append(pdfCharacter.getUnicode());
                    }
                } else {
                    trackCodeBuilder.append(pdfCharacter.getUnicode());
                    trackCodeFound = true;
                }
            }

            String trackCode = trackCodeBuilder.toString();
            Optional<Track> track = trackService.getTrack(trackCode);
            if (track.isPresent()) {
                String lastRaceNumber = lastRaceNumberBuilder.toString();
                String finishPosition = finishPositionBuilder.toString();
                return new LastRacePerformance(
                        (!lastRaceNumber.isEmpty() ? parseInt(lastRaceNumber) : null),
                        track.get(),
                        (!finishPosition.isEmpty() ? parseInt(finishPosition) : null));
            } else {
                throw new UnknownTrackException(trackCode);
            }
        }

        public Track getTrack() {
            return track;
        }

        public Integer getRaceNumber() {
            return raceNumber;
        }

        public Integer getOfficialPosition() {
            return officialPosition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LastRacePerformance that = (LastRacePerformance) o;

            if (track != null ? !track.equals(that.track) : that.track != null) return false;
            if (raceNumber != null ? !raceNumber.equals(that.raceNumber) : that
                    .raceNumber != null)
                return false;
            return officialPosition != null ? officialPosition.equals(that.officialPosition) : that
                    .officialPosition == null;
        }

        @Override
        public int hashCode() {
            int result = track != null ? track.hashCode() : 0;
            result = 31 * result + (raceNumber != null ? raceNumber.hashCode() : 0);
            result = 31 * result + (officialPosition != null ? officialPosition.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LastRacePerformance{" +
                    "track=" + track +
                    ", raceNumber=" + raceNumber +
                    ", officialPosition=" + officialPosition +
                    '}';
        }
    }

    public static class UnknownTrackException extends ChartParserException {
        public UnknownTrackException(String trackCode) {
            super(String.format("Unable to find Track for code %s", trackCode));
        }
    }
}
