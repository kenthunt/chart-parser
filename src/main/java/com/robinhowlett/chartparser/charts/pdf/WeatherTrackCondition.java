package com.robinhowlett.chartparser.charts.pdf;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses and stores the textual description of the weather (e.g. "Clear") and track conditions
 * (e.g. "Fast")
 */
public class WeatherTrackCondition {
    static final Pattern WEATHER_TRACK_PATTERN = Pattern.compile("Weather: (.+)\\|Track: (.+)");

    private final String weather;
    private final String trackCondition;

    public WeatherTrackCondition(String weather, String trackCondition) {
        this.weather = weather;
        this.trackCondition = trackCondition;
    }

    public static Optional<WeatherTrackCondition> parse(List<List<ChartCharacter>> sections) {
        for (List<ChartCharacter> section : sections) {
            String text = Chart.convertToText(section);
            Matcher matcher = WEATHER_TRACK_PATTERN.matcher(text);
            if (matcher.find()) {
                String weather = matcher.group(1);
                String trackCondition = matcher.group(2);
                return Optional.of(new WeatherTrackCondition(weather, trackCondition));
            }
        }
        return Optional.empty();
    }


    public String getWeather() {
        return weather;
    }

    public String getTrackCondition() {
        return trackCondition;
    }

    @Override
    public String toString() {
        return "WeatherTrackCondition{" +
                "weather='" + weather + '\'' +
                ", trackCondition='" + trackCondition + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherTrackCondition that = (WeatherTrackCondition) o;

        if (weather != null ? !weather.equals(that.weather) : that.weather != null) return false;
        return trackCondition != null ? trackCondition.equals(that.trackCondition) : that
                .trackCondition == null;
    }

    @Override
    public int hashCode() {
        int result = weather != null ? weather.hashCode() : 0;
        result = 31 * result + (trackCondition != null ? trackCondition.hashCode() : 0);
        return result;
    }
}
