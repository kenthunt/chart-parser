package com.robinhowlett.chartparser.points_of_call;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robinhowlett.chartparser.charts.pdf.Starter;

import java.util.List;
import java.util.Optional;

/**
 * Stores the {@link PointOfCall} instances for a particular race distance
 */
public class PointsOfCall {

    private final String distance;
    private final int floor;
    private final List<PointOfCall> calls;

    @JsonCreator
    public PointsOfCall(
            @JsonProperty("distance") String distance,
            @JsonProperty("floor") int floor,
            @JsonProperty("calls") List<PointOfCall> calls) {
        this.distance = distance;
        this.floor = floor;
        this.calls = calls;
    }

    /**
     * A specific point of call for the specified {@link PointsOfCall} for the race distance in
     * question
     */
    public static class PointOfCall {
        private final int point;
        private final String text;
        private Integer feet;
        private RelativePosition relativePosition;

        @JsonCreator
        public PointOfCall(
                @JsonProperty("point") int point,
                @JsonProperty("text") String text,
                @JsonProperty("feet") Integer feet) {
            this.point = point;
            this.text = text;
            this.feet = feet;
        }

        public boolean hasKnownDistance() {
            return (feet != null);
        }

        public boolean hasRelativePosition() {
            return getRelativePosition() != null;
        }

        public boolean hasLengths() {
            return getRelativePosition() != null && getRelativePosition().getLengthsAhead() != null;
        }

        public int getPoint() {
            return point;
        }

        public String getText() {
            return text;
        }

        public Integer getFeet() {
            return feet;
        }

        public void setFeet(Integer feet) {
            this.feet = feet;
        }

        public RelativePosition getRelativePosition() {
            return relativePosition;
        }

        public void setRelativePosition(RelativePosition relativePosition) {
            this.relativePosition = relativePosition;
        }

        @Override
        public String toString() {
            return "PointOfCall{" +
                    "point=" + point +
                    ", text='" + text + '\'' +
                    ", feet=" + feet +
                    ", relativePosition=" + relativePosition +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PointOfCall that = (PointOfCall) o;

            if (point != that.point) return false;
            if (text != null ? !text.equals(that.text) : that.text != null) return false;
            if (feet != null ? !feet.equals(that.feet) : that.feet != null) return false;
            return relativePosition != null ? relativePosition.equals(that.relativePosition) : that
                    .relativePosition == null;
        }

        @Override
        public int hashCode() {
            int result = point;
            result = 31 * result + (text != null ? text.hashCode() : 0);
            result = 31 * result + (feet != null ? feet.hashCode() : 0);
            result = 31 * result + (relativePosition != null ? relativePosition.hashCode() : 0);
            return result;
        }

        /**
         * Stores the position of the {@link Starter} at this point of call, and, if applicable, the
         * details about the number of lengths ahead of the next starter, and the total number of
         * lengths behind the leader at this point.
         */
        public static class RelativePosition {
            private final Integer position;
            private final LengthsAhead lengthsAhead;
            private TotalLengthsBehind totalLengthsBehind;

            public RelativePosition(Integer position, LengthsAhead lengthsAhead) {
                this.position = position;
                this.lengthsAhead = lengthsAhead;
            }

            public Integer getPosition() {
                return position;
            }

            public LengthsAhead getLengthsAhead() {
                return lengthsAhead;
            }

            public TotalLengthsBehind getTotalLengthsBehind() {
                return totalLengthsBehind;
            }

            public void setTotalLengthsBehind(TotalLengthsBehind totalLengthsBehind) {
                this.totalLengthsBehind = totalLengthsBehind;
            }

            @Override
            public String toString() {
                return "RelativePosition{" +
                        "position=" + position +
                        ", lengthsAhead=" + lengthsAhead +
                        ", totalLengthsBehind=" + totalLengthsBehind +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                RelativePosition that = (RelativePosition) o;

                if (position != null ? !position.equals(that.position) : that.position != null)
                    return false;
                if (lengthsAhead != null ? !lengthsAhead.equals(that.lengthsAhead) : that
                        .lengthsAhead != null)
                    return false;
                return totalLengthsBehind != null ? totalLengthsBehind.equals(that
                        .totalLengthsBehind) : that.totalLengthsBehind == null;
            }

            @Override
            public int hashCode() {
                int result = position != null ? position.hashCode() : 0;
                result = 31 * result + (lengthsAhead != null ? lengthsAhead.hashCode() : 0);
                result = 31 * result + (totalLengthsBehind != null ? totalLengthsBehind.hashCode
                        () : 0);
                return result;
            }

            /**
             * Tracks lengths as the chart's textual description and as a Double
             */
            abstract static class Lengths {
                protected final String text;
                protected final Double lengths;

                public Lengths(String text, Double lengths) {
                    this.text = text;
                    this.lengths = lengths;
                }

                public String getText() {
                    return text;
                }

                public Double getLengths() {
                    return lengths;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;

                    Lengths lengths1 = (Lengths) o;

                    if (text != null ? !text.equals(lengths1.text) : lengths1.text != null)
                        return false;
                    return lengths != null ? lengths.equals(lengths1.lengths) : lengths1.lengths
                            == null;
                }

                @Override
                public int hashCode() {
                    int result = text != null ? text.hashCode() : 0;
                    result = 31 * result + (lengths != null ? lengths.hashCode() : 0);
                    return result;
                }
            }

            /**
             * The number of lengths ahead of the next starter
             */
            public static class LengthsAhead extends Lengths {

                public LengthsAhead(String chart, Double lengths) {
                    super(chart, lengths);
                }

                @Override
                public String toString() {
                    return "ChartLengthsAhead{" +
                            "text='" + text + '\'' +
                            ", lengthsAhead=" + lengths +
                            '}';
                }
            }

            /**
             * The total number of lengths behind the leader at the particular point of call
             */
            public static class TotalLengthsBehind extends Lengths {

                public TotalLengthsBehind(String chart, Double lengths) {
                    super(chart, lengths);
                }

                @Override
                public String toString() {
                    return "TotalLengthsBehind{" +
                            "text='" + text + '\'' +
                            ", lengthsAhead=" + lengths +
                            '}';
                }
            }
        }
    }

    public String getDistance() {
        return distance;
    }

    public int getFloor() {
        return floor;
    }

    public List<PointOfCall> getCalls() {
        return calls;
    }

    public Optional<PointOfCall> getFinishPointOfCall() {
        for (PointOfCall pointOfCall : getCalls()) {
            if (pointOfCall.getPoint() == 6) {
                return Optional.of(pointOfCall);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "PointsOfCall{" +
                "distance='" + distance + '\'' +
                ", floor=" + floor +
                ", calls=" + calls +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointsOfCall that = (PointsOfCall) o;

        if (floor != that.floor) return false;
        if (distance != null ? !distance.equals(that.distance) : that.distance != null)
            return false;
        return calls != null ? calls.equals(that.calls) : that.calls == null;

    }

    @Override
    public int hashCode() {
        int result = distance != null ? distance.hashCode() : 0;
        result = 31 * result + floor;
        result = 31 * result + (calls != null ? calls.hashCode() : 0);
        return result;
    }
}
