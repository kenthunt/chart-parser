package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and stores the wind speed (in miles per hour e.g. 5) and direction (e.g. "Cross")
 */
public class WindSpeedDirection {
    static final Pattern WIND_SPEED_DIRECTION_PATTERN =
            Pattern.compile("Wind Speed: (\\d+) Wind Direction: (.+)");

    private final Integer speed;
    private final String direction;

    @JsonCreator
    public WindSpeedDirection(Integer speed, String direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public static Optional<WindSpeedDirection> parse(List<List<ChartCharacter>> sections) {
        for (List<ChartCharacter> section : sections) {
            String text = Chart.convertToText(section);
            Matcher matcher = WIND_SPEED_DIRECTION_PATTERN.matcher(text);
            if (matcher.find()) {
                Integer speed = Integer.parseInt(matcher.group(1));
                String direction = matcher.group(2);
                return Optional.of(new WindSpeedDirection(speed, direction));
            }
        }
        return Optional.empty();
    }


    public Integer getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "WindSpeedDirection{" +
                "speed=" + speed +
                ", direction='" + direction + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WindSpeedDirection that = (WindSpeedDirection) o;

        if (speed != null ? !speed.equals(that.speed) : that.speed != null) return false;
        return direction != null ? direction.equals(that.direction) : that.direction == null;
    }

    @Override
    public int hashCode() {
        int result = speed != null ? speed.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }
}
