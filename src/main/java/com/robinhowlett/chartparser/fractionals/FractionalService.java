package com.robinhowlett.chartparser.fractionals;

import com.robinhowlett.chartparser.charts.pdf.Breed;
import com.robinhowlett.chartparser.exceptions.ChartParserException;
import com.robinhowlett.chartparser.fractionals.FractionalPoint.Fractional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gets the {@link Fractional}s for a particular race distance from the
 * {@link FractionalPointRepository}. In cases where fractions appear to be missing, it makes a
 * guess about which fraction it should correspond to. Calculates the number of milliseconds each
 * fraction corresponds to as well
 */
public class FractionalService {

    private FractionalPointRepository repository;

    public FractionalService(FractionalPointRepository repository) {
        this.repository = repository;
    }

    public List<Fractional> getFractionalPointsForDistance(List<String> fractions,
            int distanceInFeet, Breed breed) throws ChartParserException {
        List<Fractional> fractionals = new ArrayList<>();

        if (fractions != null && !fractions.isEmpty()) {
            FractionalTreeSet fractionalSet = repository.findAll();

            // find the fractionals for this race distance
            FractionalPoint fractionalPoint =
                    fractionalSet.floor(createForFloor(distanceInFeet));

            int index = 0;
            List<Fractional> fractionalPoints =
                    fractionalPoint.getFractionals();

            // if the number of fractions detected is fewer than expected, or contains an invalid
            // value
            if (fractions.size() < fractionalPoints.size() || fractions.contains("N/A")) {
                for (int i = 0; i < fractions.size(); i++) {
                    String fraction = fractions.get(i);
                    Optional<Long> millis = calculateMillisecondsForFraction(fraction);
                    if (millis.isPresent()) {
                        // the last fraction is always assumed to be the final time
                        if (isLastFraction(fractions, i)) {
                            Fractional fractional =
                                    fractionalPoints.get(fractionalPoints.size() - 1);
                            fractional.setMillis(millis.get());
                            fractional.setTime(FractionalPoint.convertToTime(millis.get()));
                            fractionals.add(fractional);
                        } else {
                            // attempt to guess the fractional that corresponds to the fraction time
                            // by creating a slow-fast range and determining if a fractional fits
                            double slowFeetPerMillis = 0.045;
                            double fastFeetPerMillis = 0.0647;
                            // adjust for race distance and breed
                            double minRegression =
                                    (breed.equals(Breed.ARABIAN) ? (slowFeetPerMillis * 0.87) :
                                            slowFeetPerMillis);
                            double maxBase = (-0.000001 * distanceInFeet) + fastFeetPerMillis;
                            double maxRegression =
                                    (breed.equals(Breed.ARABIAN) ? (maxBase * 0.87) : maxBase);
                            // the range that the matching fractional need fit within
                            int feetMin = (int) (millis.get() * minRegression);
                            int feetMax = (int) (millis.get() * maxRegression);

                            for (int i1 = 0; i1 < (fractionalPoints.size() - 1); i1++) {
                                Fractional fractional = fractionalPoints.get(i1);
                                if (fractional.getFeet() >= feetMin &&
                                        fractional.getFeet() <= feetMax) {
                                    fractionalPoints.remove(fractional);
                                    fractional.setMillis(millis.get());
                                    fractional.setTime(FractionalPoint.convertToTime(millis.get()));
                                    fractionals.add(fractional);
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                // match each fraction to the expected fractional
                for (Fractional fractional : fractionalPoints) {
                    String time = fractions.get(index).toString();
                    Optional<Long> millis = calculateMillisecondsForFraction(time);
                    if (millis.isPresent()) {
                        fractional.setMillis(millis.get());
                        fractional.setTime(FractionalPoint.convertToTime(millis.get()));
                    }
                    fractionals.add(fractional);
                    index++;
                }
            }
        }

        return fractionals;
    }

    private boolean isLastFraction(List<String> fractions, int i) {
        return (i == (fractions.size() - 1));
    }

    private static FractionalPoint createForFloor(int fractionalPointInFeet) {
        return new FractionalPoint(fractionalPointInFeet);
    }

    public static Optional<Long> calculateMillisecondsForFraction(String time) {
        Pattern ELAPSED_TIME_PATTERN = Pattern.compile("((\\d+):)?(\\d+)+\\.(\\d)(\\d)?(\\d)?");
        Matcher matcher = ELAPSED_TIME_PATTERN.matcher(time);
        if (matcher.find()) {
            long millis = 0;

            String minsMatch = matcher.group(2);
            if (minsMatch != null) {
                millis += (Integer.parseInt(minsMatch) * (60 * 1000));
            }

            String secondsMatch = matcher.group(3);
            if (secondsMatch != null) {
                millis += (Integer.parseInt(secondsMatch) * 1000);
            }

            String tenthsMatch = matcher.group(4);
            if (tenthsMatch != null) {
                millis += (Integer.parseInt(tenthsMatch) * 100);
            }

            String hundrethsMatch = matcher.group(5);
            if (hundrethsMatch != null) {
                millis += (Integer.parseInt(hundrethsMatch) * 10);
            }

            String millisMatch = matcher.group(6);
            if (millisMatch != null) {
                millis += Integer.parseInt(millisMatch);
            }

            return Optional.of(millis);
        }
        return Optional.empty();
    }
}
