package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.robinhowlett.chartparser.exceptions.ChartParserException;
import com.robinhowlett.chartparser.fractionals.FractionalService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.robinhowlett.chartparser.charts.pdf.Purse.PURSE_PATTERN;

/**
 * Parses the textual description of the race value and converts it into a {@link RaceDistance}
 * instance, including calculating the race value in feet. The scheduled and actual surface race
 * on is additionally stored. It also parses and stores, in a {@link TrackRecord} instance, the
 * details of the track record for this value/surface.
 */
@JsonPropertyOrder({"distance", "surface", "trackCondition", "scheduledSurface", "offTurf",
        "trackRecord"})
public class DistanceSurfaceTrackRecord {

    static final Pattern DIST_SURF_RECORD_PATTERN =
            Pattern.compile("^((About )?(One|Two|Three|Four|Five|Six|Seven|Eight|Nine)[\\w\\s]+) " +
                    "On The ([A-Za-z\\s]+)(\\s?- Originally Scheduled For the " +
                    "([A-Za-z0-9\\-\\s]+))?(\\|Track Record: \\((.+) - ([\\d:\\.]+) - (.+)\\))?");

    private static final List<String> NUMERATORS = Arrays.asList("zero", "one", "two", "three",
            "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen",
            "fourteen", "fifteen");

    private static final List<String> TENS = Arrays.asList("zero", "ten", "twenty", "thirty",
            "forty", "fifty", "sixty", "seventy", "eighty", "ninety");

    private static final Pattern MILES_ONLY_PATTERN =
            Pattern.compile("^(about)? ?([\\w]+)( and ([\\w ]+))? miles?$");

    private static final Pattern FURLONGS_ONLY_PATTERN =
            Pattern.compile("^(about)? ?([\\w]+)( and ([\\w ]+))? furlongs?$");

    private static final Pattern YARDS_ONLY_PATTERN =
            Pattern.compile("^(about)? ?((\\w+) thousand)? ?(([\\w]+) hundred ?( ?and )?([\\w " +
                    "]+)?)? yards?$");

    private static final Pattern MILES_YARDS_PATTERN =
            Pattern.compile("(about)? ?([\\w]+) miles? and ([\\w ]+) yards?");

    private static final Pattern FURLONGS_YARDS_PATTERN =
            Pattern.compile("(about)? ?([\\w]+) furlongs? and ([\\w ]+) yards?");

    private static final Pattern MISSING_YARDS_PATTERN =
            Pattern.compile("^(about)? ?((\\w+) thousand)? ?(([\\w]+) hundred ?( ?and )?([\\w " +
                    "]+)?)?$");

    @JsonProperty("distance")
    private final RaceDistance raceDistance;
    private final String surface;
    private final String scheduledSurface;
    private final TrackRecord trackRecord;
    private String trackCondition;

    public DistanceSurfaceTrackRecord(String distanceDescription, String surface,
            String scheduledSurface, TrackRecord trackRecord) throws ChartParserException {
        this.raceDistance = (distanceDescription != null ?
                parseRaceDistance(distanceDescription) : null);
        this.surface = surface;
        this.scheduledSurface = (scheduledSurface != null ? scheduledSurface : surface);
        this.trackRecord = trackRecord;
    }

    public static DistanceSurfaceTrackRecord parse(final List<List<ChartCharacter>> lines)
            throws ChartParserException {
        boolean found = false;
        StringBuilder distanceSurfaceTrackRecordBuilder = new StringBuilder();
        String prefix = "";
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            if (found) {
                Matcher matcher = PURSE_PATTERN.matcher(text);
                if (matcher.find()) {
                    break;
                } else {
                    distanceSurfaceTrackRecordBuilder.append(prefix).append(text);
                }
            }
            Matcher matcher = DIST_SURF_RECORD_PATTERN.matcher(text);
            if (matcher.find() && isValidDistanceText(text)) {
                found = true;
                // prefix a space at the start of each line (except for the first)
                distanceSurfaceTrackRecordBuilder.append(prefix).append(text);
                prefix = " ";
            }
        }

        String distanceSurfaceTrackRecord = distanceSurfaceTrackRecordBuilder.toString();
        Optional<DistanceSurfaceTrackRecord> distanceSurface =
                parseDistanceSurface(distanceSurfaceTrackRecord);
        if (distanceSurface.isPresent()) {
            return distanceSurface.get();
        }

        throw new NoRaceDistanceFound(distanceSurfaceTrackRecord);
    }

    public static boolean isValidDistanceText(String text) {
        return !((text.toLowerCase().contains("claiming price") ||
                text.toLowerCase().contains("allowed") ||
                text.toLowerCase().contains("non winners") ||
                text.toLowerCase().contains("other than"))
                && !text.toLowerCase().contains("track record"));
    }

    static Optional<DistanceSurfaceTrackRecord> parseDistanceSurface(String text)
            throws ChartParserException {
        Matcher matcher = DIST_SURF_RECORD_PATTERN.matcher(text);
        if (matcher.find()) {
            String distanceDescription = matcher.group(1);
            String surface = matcher.group(4).trim();
            String scheduledSurface = null;

            // detect off-turf races
            String scheduledSurfaceFlag = matcher.group(5);
            if (scheduledSurfaceFlag != null) {
                scheduledSurface = matcher.group(6);
            }

            TrackRecord trackRecord = null;
            if (matcher.group(7) != null) {
                String holder = matcher.group(8);
                String time = matcher.group(9);
                Optional<Long> recordTime =
                        FractionalService.calculateMillisecondsForFraction(time);

                String raceDateText = matcher.group(10);
                LocalDate raceDate = TrackRaceDateRaceNumber.parseRaceDate(raceDateText);

                trackRecord = new TrackRecord(holder, time,
                        (recordTime.isPresent() ? recordTime.get() : null), raceDate);
            }

            return Optional.of(new DistanceSurfaceTrackRecord(distanceDescription, surface,
                    scheduledSurface, trackRecord));
        }
        return Optional.empty();
    }

    static RaceDistance parseRaceDistance(String distanceDescription) throws ChartParserException {
        String lcDistanceDescription = distanceDescription.toLowerCase();
        Matcher milesOnlyMatcher = MILES_ONLY_PATTERN.matcher(lcDistanceDescription);
        if (milesOnlyMatcher.find()) {
            return forMiles(distanceDescription, milesOnlyMatcher);
        }
        Matcher furlongsOnlyMatcher = FURLONGS_ONLY_PATTERN.matcher(lcDistanceDescription);
        if (furlongsOnlyMatcher.find()) {
            return forFurlongs(distanceDescription, furlongsOnlyMatcher);
        }
        Matcher yardsOnlyMatcher = YARDS_ONLY_PATTERN.matcher(lcDistanceDescription);
        if (yardsOnlyMatcher.find()) {
            return forYards(distanceDescription, yardsOnlyMatcher);
        }
        Matcher milesAndYardsMatcher = MILES_YARDS_PATTERN.matcher(lcDistanceDescription);
        if (milesAndYardsMatcher.find()) {
            return forMilesAndYards(distanceDescription, milesAndYardsMatcher);
        }
        Matcher furlongsAndYardsMatcher = FURLONGS_YARDS_PATTERN.matcher(lcDistanceDescription);
        if (furlongsAndYardsMatcher.find()) {
            return forFurlongsAndYards(distanceDescription, furlongsAndYardsMatcher);
        }
        // sometimes the "Yards" part is missing
        Matcher missingYardsMatcher = MISSING_YARDS_PATTERN.matcher(lcDistanceDescription);
        if (missingYardsMatcher.find()) {
            return forYards(distanceDescription, missingYardsMatcher);
        }

        throw new ChartParserException(String.format("Unable to parse race distance from text: " +
                "%s", distanceDescription));
    }

    private static RaceDistance forMiles(String distanceDescription, Matcher matcher)
            throws ChartParserException {
        int feet = 0;
        boolean isExact = (matcher.group(1) == null);

        String fractionalMiles = matcher.group(4);
        if (fractionalMiles != null && !fractionalMiles.isEmpty()) {
            String[] fraction = fractionalMiles.split(" ");
            String denominator = fraction[1];
            switch (denominator) {
                case "sixteenth":
                case "sixteenths":
                    feet = 330; // 5280 divided by 16
                    break;
                case "eighth":
                case "eighths":
                    feet = 660;
                    break;
                case "fourth":
                case "fourths":
                    feet = 1320;
                    break;
                case "half":
                    feet = 2640;
                    break;
                default:
                    throw new ChartParserException(String.format("Unable to parse a fractional " +
                            "mile denominator from text: %s", denominator));
            }
            String numerator = fraction[0];
            int num = NUMERATORS.indexOf(numerator);
            feet = num * feet;
        }

        String wholeMiles = matcher.group(2);
        int mileNumerator = NUMERATORS.indexOf(wholeMiles);
        feet += (mileNumerator * 5280);
        return new RaceDistance(distanceDescription, isExact, feet);
    }

    private static RaceDistance forFurlongs(String distanceDescription, Matcher matcher)
            throws ChartParserException {
        int feet = 0;
        boolean isExact = (matcher.group(1) == null);

        String fractionalFurlongs = matcher.group(4);
        if (fractionalFurlongs != null && !fractionalFurlongs.isEmpty()) {
            String[] fraction = fractionalFurlongs.split(" ");
            String denominator = fraction[1];
            switch (denominator) {
                case "fourth":
                case "fourths":
                    feet = 165;
                    break;
                case "half":
                    feet = 330;
                    break;
                default:
                    throw new ChartParserException(String.format("Unable to parse a fractional " +
                            "furlong denominator from text: %s", denominator));
            }
            String numerator = fraction[0];
            int num = NUMERATORS.indexOf(numerator);
            feet = num * feet;
        }

        String wholeFurlongs = matcher.group(2);
        int furlongNumerator = NUMERATORS.indexOf(wholeFurlongs);
        feet += (furlongNumerator * 660);
        return new RaceDistance(distanceDescription, isExact, feet);
    }

    private static RaceDistance forYards(String distanceDescription, Matcher matcher) {
        int feet = 0;
        boolean isExact = (matcher.group(1) == null);

        String yards = matcher.group(7);
        if (yards != null && !yards.isEmpty()) {
            String[] splitYards = yards.split(" ");
            if (splitYards.length == 2) {
                feet = (TENS.indexOf(splitYards[0]) * 10 * 3) +
                        (NUMERATORS.indexOf(splitYards[1]) * 3);
            } else {
                int inTensYards = TENS.indexOf(yards);
                if (inTensYards < 0) {
                    feet = (NUMERATORS.indexOf(yards) * 3);
                } else {
                    feet = inTensYards * 10 * 3;
                }
            }
        }

        String thousandYards = matcher.group(3);
        if (thousandYards != null) {
            int yardsInThousands = NUMERATORS.indexOf(thousandYards);
            feet += (yardsInThousands * 3000);
        }

        String hundredYards = matcher.group(5);
        if (hundredYards != null) {
            int yardsInHundreds = NUMERATORS.indexOf(hundredYards);
            feet += (yardsInHundreds * 300);
        }

        return new RaceDistance(distanceDescription, isExact, feet);
    }

    private static RaceDistance forMilesAndYards(String distanceDescription, Matcher matcher) {
        int feet = 0;
        boolean isExact = (matcher.group(1) == null);

        String yards = matcher.group(3);
        if (yards != null && !yards.isEmpty()) {
            feet = TENS.indexOf(yards) * 10 * 3;
        }

        String wholeMiles = matcher.group(2);
        int mileNumerator = NUMERATORS.indexOf(wholeMiles);
        feet += (mileNumerator * 5280);
        return new RaceDistance(distanceDescription, isExact, feet);
    }

    private static RaceDistance forFurlongsAndYards(String distanceDescription, Matcher matcher) {
        int feet = 0;
        boolean isExact = (matcher.group(1) == null);

        String yards = matcher.group(3);
        if (yards != null && !yards.isEmpty()) {
            feet = TENS.indexOf(yards) * 10 * 3;
        }

        String wholeFurlongs = matcher.group(2);
        int furlongNumerator = NUMERATORS.indexOf(wholeFurlongs);
        feet += (furlongNumerator * 660);
        return new RaceDistance(distanceDescription, isExact, feet);
    }

    /**
     * Stores the textual description of the race value, the value expressed in feet, and
     * whether the value is exact or estimated
     */
    @JsonPropertyOrder({"text", "value", "exact", "runUp"})
    public static class RaceDistance {
        private final String text;
        private final boolean exact;
        private final int value;
        private Integer runUp;

        RaceDistance(String text, boolean exact, int value) {
            this(text, exact, value, null);
        }

        @JsonCreator
        private RaceDistance(String text, boolean exact, int value, Integer runUp) {
            this.text = text;
            this.exact = exact;
            this.value = value;
            this.runUp = runUp;
        }

        public String getText() {
            return text;
        }

        public boolean isExact() {
            return exact;
        }

        public int getValue() {
            return value;
        }

        public Integer getRunUp() {
            return runUp;
        }

        public void setRunUp(Integer runUp) {
            this.runUp = runUp;
        }

        @Override
        public String toString() {
            return "RaceDistance{" +
                    "text='" + text + '\'' +
                    ", exact=" + exact +
                    ", value=" + value +
                    ", runUp=" + runUp +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RaceDistance that = (RaceDistance) o;

            if (exact != that.exact) return false;
            if (value != that.value) return false;
            if (text != null ? !text.equals(that.text) : that.text != null) return false;
            return runUp != null ? runUp.equals(that.runUp) : that.runUp == null;
        }

        @Override
        public int hashCode() {
            int result = text != null ? text.hashCode() : 0;
            result = 31 * result + (exact ? 1 : 0);
            result = 31 * result + value;
            result = 31 * result + (runUp != null ? runUp.hashCode() : 0);
            return result;
        }
    }

    /**
     * Stories the name of the track record holder, the track record time (as both a String and in
     * milliseconds) and the date when the record was set
     */
    public static class TrackRecord {
        private final String holder;
        private final String time;
        private final Long millis;
        private final LocalDate raceDate;

        public TrackRecord(String holder, String time, Long millis, LocalDate raceDate) {
            this.holder = holder;
            this.time = time;
            this.millis = millis;
            this.raceDate = raceDate;
        }

        public String getHolder() {
            return holder;
        }

        public String getTime() {
            return time;
        }

        public Long getMillis() {
            return millis;
        }

        public LocalDate getRaceDate() {
            return raceDate;
        }

        @Override
        public String toString() {
            return "TrackRecord{" +
                    "holder='" + holder + '\'' +
                    ", time='" + time + '\'' +
                    ", millis=" + millis +
                    ", raceDate=" + raceDate +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TrackRecord that = (TrackRecord) o;

            if (holder != null ? !holder.equals(that.holder) : that.holder != null) return false;
            if (time != null ? !time.equals(that.time) : that.time != null) return false;
            if (millis != null ? !millis.equals(that.millis) : that.millis != null) return false;
            return raceDate != null ? raceDate.equals(that.raceDate) : that.raceDate == null;
        }

        @Override
        public int hashCode() {
            int result = holder != null ? holder.hashCode() : 0;
            result = 31 * result + (time != null ? time.hashCode() : 0);
            result = 31 * result + (millis != null ? millis.hashCode() : 0);
            result = 31 * result + (raceDate != null ? raceDate.hashCode() : 0);
            return result;
        }
    }

    public String getSurface() {
        return surface;
    }

    public String getScheduledSurface() {
        return scheduledSurface;
    }

    public boolean isOffTurf() {
        return (!surface.equals(scheduledSurface));
    }

    public TrackRecord getTrackRecord() {
        return trackRecord;
    }

    public RaceDistance getRaceDistance() {
        return raceDistance;
    }

    public String getTrackCondition() {
        return trackCondition;
    }

    public void setTrackCondition(String trackCondition) {
        this.trackCondition = trackCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistanceSurfaceTrackRecord that = (DistanceSurfaceTrackRecord) o;

        if (raceDistance != null ? !raceDistance.equals(that.raceDistance) : that.raceDistance !=
                null)
            return false;
        if (surface != null ? !surface.equals(that.surface) : that.surface != null) return false;
        if (scheduledSurface != null ? !scheduledSurface.equals(that.scheduledSurface) : that
                .scheduledSurface != null)
            return false;
        if (trackRecord != null ? !trackRecord.equals(that.trackRecord) : that.trackRecord != null)
            return false;
        return trackCondition != null ? trackCondition.equals(that.trackCondition) : that
                .trackCondition == null;
    }

    @Override
    public int hashCode() {
        int result = raceDistance != null ? raceDistance.hashCode() : 0;
        result = 31 * result + (surface != null ? surface.hashCode() : 0);
        result = 31 * result + (scheduledSurface != null ? scheduledSurface.hashCode() : 0);
        result = 31 * result + (trackRecord != null ? trackRecord.hashCode() : 0);
        result = 31 * result + (trackCondition != null ? trackCondition.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DistanceSurfaceTrackRecord{" +
                "raceDistance=" + raceDistance +
                ", surface='" + surface + '\'' +
                ", scheduledSurface='" + scheduledSurface + '\'' +
                ", trackRecord=" + trackRecord +
                ", trackCondition='" + trackCondition + '\'' +
                '}';
    }

    public static class NoRaceDistanceFound extends ChartParserException {
        public NoRaceDistanceFound(String distanceSurfaceTrackRecord) {
            super(String.format("Unable to identify a valid race value, surface, and/or track " +
                    "record: %s", distanceSurfaceTrackRecord));
        }
    }
}
