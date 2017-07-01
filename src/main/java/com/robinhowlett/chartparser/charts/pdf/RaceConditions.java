package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.robinhowlett.chartparser.charts.pdf.DistanceSurfaceTrackRecord
        .DIST_SURF_RECORD_PATTERN;
import static com.robinhowlett.chartparser.charts.pdf.RaceTypeNameBlackTypeBreed
        .RACE_TYPE_NAME_GRADE_BREED;

import static java.util.Locale.US;

/**
 * Parses and stores the textual description of the race conditions and, if applicable, the minimum
 * and maximum claiming prices that can be availed of
 */
@JsonPropertyOrder({"raceTypeNameBlackTypeBreed", "text", "claimingPriceRange"})
public class RaceConditions {

    private final String text;
    @JsonInclude(NON_NULL)
    private final ClaimingPriceRange claimingPriceRange;
    @JsonProperty("raceTypeNameBlackTypeBreed") // required for property order but unwrapped
    @JsonUnwrapped
    private RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed;

    public RaceConditions(String text,
            ClaimingPriceRange claimingPriceRange) {
        this.text = text;
        this.claimingPriceRange = claimingPriceRange;
    }

    @JsonCreator
    private RaceConditions(String text, ClaimingPriceRange claimingPriceRange,
            RaceTypeNameBlackTypeBreed raceTypeNameBlackTypeBreed) {
        this(text, claimingPriceRange);
        this.raceTypeNameBlackTypeBreed = raceTypeNameBlackTypeBreed;
    }

    // handles multi-line
    public static RaceConditions parse(List<List<ChartCharacter>> lines)
            throws ChartParserException {
        boolean found = false;
        StringBuilder raceConditionsBuilder = new StringBuilder();
        String prefix = "";
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            if (found) {
                Matcher matcher = DIST_SURF_RECORD_PATTERN.matcher(text);
                if (matcher.find() && DistanceSurfaceTrackRecord.isValidDistanceText(text)) {
                    break;
                } else {
                    // prefix a space at the start of each line (except for the first)
                    raceConditionsBuilder.append(prefix).append(text);
                    prefix = " ";
                }
            }
            Matcher matcher = RACE_TYPE_NAME_GRADE_BREED.matcher(text);
            if (matcher.find()) {
                found = true;
            }
        }
        String raceConditions = raceConditionsBuilder.toString();
        ClaimingPriceRange claimingPriceRange = ClaimingPriceRange.parse(raceConditions);

        return new RaceConditions(raceConditions, claimingPriceRange);
    }

    public String getText() {
        return text;
    }

    public ClaimingPriceRange getClaimingPriceRange() {
        return claimingPriceRange;
    }

    public RaceTypeNameBlackTypeBreed getRaceTypeNameBlackTypeBreed() {
        return raceTypeNameBlackTypeBreed;
    }

    public void setRaceTypeNameBlackTypeBreed(RaceTypeNameBlackTypeBreed
            raceTypeNameBlackTypeBreed) {
        this.raceTypeNameBlackTypeBreed = raceTypeNameBlackTypeBreed;
    }

    /**
     * Stores the range of the claiming prices that apply to a claiming race. Some claiming races
     * allow setting the claim within a particular range, others give weight allowances for lower
     * claim prices, while others use a single value (meaning the minimum and maximum are the same)
     */
    public static class ClaimingPriceRange {
        private static final Pattern CLAIMING_PRICE_PATTERN =
                Pattern.compile("Claiming Price: " +
                        "\\$([0-9]{1,3}(,[0-9]{3})*)( - \\$([0-9]{1,3}(,[0-9]{3})*))?$");

        private final int min;
        private final int max;

        public ClaimingPriceRange(Integer min, Integer max) {
            this.min = min;
            this.max = max;
        }

        static ClaimingPriceRange parse(String raceConditions) throws ChartParserException {
            Integer maxClaim = null, minClaim = null;
            Matcher matcher = CLAIMING_PRICE_PATTERN.matcher(raceConditions);
            if (matcher.find()) {
                String maxClaimAmount = matcher.group(1);
                if (maxClaimAmount != null) {
                    try {
                        maxClaim =
                                NumberFormat.getNumberInstance(US).parse(maxClaimAmount).intValue();
                    } catch (ParseException e) {
                        throw new ChartParserException(String.format("Unable to parse a max claim" +
                                " price value from text: %s", maxClaimAmount), e);
                    }
                }

                String minClaimAmount = matcher.group(4);
                if (minClaimAmount != null) {
                    try {
                        minClaim =
                                NumberFormat.getNumberInstance(US).parse(minClaimAmount).intValue();
                    } catch (ParseException e) {
                        throw new ChartParserException(String.format("Unable to parse a min claim" +
                                " price value from text: %s", minClaimAmount), e);
                    }
                } else if (maxClaim != null) {
                    minClaim = maxClaim;
                }

                // integrity check
                if (minClaim != null && maxClaim != null) {
                    if (minClaim > maxClaim) {
                        int holder = maxClaim;
                        minClaim = holder;
                        maxClaim = minClaim;
                    }
                }

                return new ClaimingPriceRange(minClaim, maxClaim);
            }

            return null;
        }

        public Integer getMin() {
            return min;
        }

        public Integer getMax() {
            return max;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClaimingPriceRange that = (ClaimingPriceRange) o;

            if (min != that.min) return false;
            return max == that.max;
        }

        @Override
        public int hashCode() {
            int result = min;
            result = 31 * result + max;
            return result;
        }

        @Override
        public String toString() {
            return "ClaimingPriceRange{" +
                    "min=" + min +
                    ", max=" + max +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RaceConditions that = (RaceConditions) o;

        if (text != null ? !text.equals(that.text) : that
                .text != null)
            return false;
        return claimingPriceRange != null ? claimingPriceRange.equals(that.claimingPriceRange) :
                that
                        .claimingPriceRange == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (claimingPriceRange != null ? claimingPriceRange.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RaceConditions{" +
                "text='" + text + '\'' +
                ", claimingPriceRange=" + claimingPriceRange +
                '}';
    }
}
