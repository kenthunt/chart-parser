package com.robinhowlett.chartparser.fractionals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Stores the {@link Fractional}s for a particular race distance
 */
public class FractionalPoint {

    private final String distance;
    private final int floor;
    private final List<Fractional> fractionals;

    public FractionalPoint(int floor) {
        this("", floor, new ArrayList<>());
    }

    @JsonCreator
    public FractionalPoint(
            @JsonProperty("distance") String distance,
            @JsonProperty("floor") int floor,
            @JsonProperty("fractionals") List<Fractional> fractionals) {
        this.distance = distance;
        this.floor = floor;
        this.fractionals = fractionals;
    }

    /**
     * A specific fractional point for the {@link FractionalPoint} in question
     */
    public static class Fractional {
        protected final int point;
        protected final String text;
        protected final int feet;
        protected String time;
        protected Long millis;

        public Fractional(int point, String text, int feet) {
            this(point, text, feet, null, null);
        }

        @JsonCreator
        public Fractional(int point, String text, int feet, String time, Long millis) {
            this.point = point;
            this.text = text;
            this.feet = feet;
            this.time = time;
            this.millis = millis;
        }

        public boolean hasFractionalValue() {
            return !getMillis().equals(Optional.empty());
        }

        public int getPoint() {
            return point;
        }

        public String getText() {
            return text;
        }

        public int getFeet() {
            return feet;
        }

        public Long getMillis() {
            return millis;
        }

        public void setMillis(Long millis) {
            this.millis = millis;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean hasTimeAndMillis() {
            return (getTime() != null && !getTime().isEmpty() && getMillis() != null);
        }

        @Override
        public String toString() {
            return "Fractional{" +
                    "point=" + point +
                    ", text='" + text + '\'' +
                    ", feet=" + feet +
                    ", time=" + time +
                    ", millis=" + millis +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Fractional that = (Fractional) o;

            if (point != that.point) return false;
            if (feet != that.feet) return false;
            if (text != null ? !text.equals(that.text) : that.text != null) return false;
            if (time != null ? !time.equals(that.time) : that.time != null) return false;
            return millis != null ? millis.equals(that.millis) : that.millis == null;
        }

        @Override
        public int hashCode() {
            int result = point;
            result = 31 * result + (text != null ? text.hashCode() : 0);
            result = 31 * result + feet;
            result = 31 * result + (time != null ? time.hashCode() : 0);
            result = 31 * result + (millis != null ? millis.hashCode() : 0);
            return result;
        }
    }

    public String getDistance() {
        return distance;
    }

    public int getFloor() {
        return floor;
    }

    public List<Fractional> getFractionals() {
        return fractionals;
    }

    public String printFractionalDistances() {
        List<String> distances = new ArrayList<>();
        for (Fractional fractional : getFractionals()) {
            distances.add(fractional.getText());
        }
        return String.join(",", distances);
    }

    @Override
    public String toString() {
        return "FractionalPoint{" +
                "distance='" + distance + '\'' +
                ", floor=" + floor +
                ", fractionals=" + fractionals +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FractionalPoint that = (FractionalPoint) o;

        if (floor != that.floor) return false;
        if (distance != null ? !distance.equals(that.distance) : that.distance != null)
            return false;
        return fractionals != null ? fractionals.equals(that.fractionals) : that.fractionals ==
                null;
    }

    @Override
    public int hashCode() {
        int result = distance != null ? distance.hashCode() : 0;
        result = 31 * result + floor;
        result = 31 * result + (fractionals != null ? fractionals.hashCode() : 0);
        return result;
    }

    /**
     * Exception when a fractional was expected but didn't materialize
     */
    public static class MissingFractionalTimeException extends ChartParserException {
        public MissingFractionalTimeException(String message) {
            super(message);
        }
    }

    /**
     * A Split is the difference between two particular {@link Fractional}s
     */
    public static class Split extends Fractional {

        private final Fractional from;
        private final Fractional to;

        @JsonCreator
        Split(int point, String text, int feet, String time, Long millis, Fractional from,
                Fractional to) {
            super(point, text, feet, time, millis);
            this.from = from;
            this.to = to;
        }

        public static Split calculate(Fractional from, Fractional to) throws ChartParserException {
            if (from == null && to != null) {
                return new Split(to.getPoint(), "Start to " + to.getText(), to.getFeet(),
                        to.getTime(), to.getMillis(), from, to);
            } else {
                if (to == null) {
                    throw new ChartParserException(String.format("Unable to create a split time " +
                            "for %s to %s", from, to));
                }

                int splitFeet = to.getFeet() - from.getFeet();
                String text = from.getText() + " to " + to.getText();

                Long splitMillis = null;
                String time = null;
                if (from.getMillis() != null && to.getMillis() != null) {
                    splitMillis = to.getMillis() - from.getMillis();
                    time = convertToTime(splitMillis);
                }

                return new Split(to.getPoint(), text, splitFeet, time, splitMillis, from, to);
            }
        }

        public Fractional getFrom() {
            return from;
        }

        public Fractional getTo() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Split split = (Split) o;

            if (from != null ? !from.equals(split.from) : split.from != null) return false;
            return to != null ? to.equals(split.to) : split.to == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (from != null ? from.hashCode() : 0);
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Split{" +
                    "point=" + point +
                    ", text='" + text + '\'' +
                    ", feet=" + feet +
                    ", time='" + time + '\'' +
                    ", millis=" + millis +
                    ", from=" + from +
                    ", to=" + to +
                    '}';
        }
    }

    public static String convertToTime(Long millis) {
        if (millis != null) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
            long seconds = (TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(minutes));
            long subSecondMillis = (millis % 1000);

            return String.format("%d:%02d.%03d", minutes, seconds, subSecondMillis);
        } else {
            return null;
        }
    }
}
