package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Stores the textual representation of the postTime time, the start comments (which note if any
 * {@link Starter} had a poor start), and, where applicable, if the timer used was electronic or
 * not
 */
public class PostTimeStartCommentsTimer {
    static final Pattern POST_START_TIMER_PATTERN =
            Pattern.compile("Off at: (.+)\\|Start: ([^\\|]+)(\\|Timer: (.+))?");

    private final String postTime;
    private final String startComments;
    @JsonInclude(NON_NULL)
    private final String timer;

    @JsonCreator
    public PostTimeStartCommentsTimer(String postTime, String startComments) {
        this(postTime, startComments, null);
    }

    public PostTimeStartCommentsTimer(String postTime, String startComments, String timer) {
        this.postTime = postTime;
        this.startComments = startComments;
        this.timer = timer;
    }

    public static Optional<PostTimeStartCommentsTimer> parse(List<List<ChartCharacter>> sections) {
        for (List<ChartCharacter> section : sections) {
            String text = Chart.convertToText(section);
            Matcher matcher = POST_START_TIMER_PATTERN.matcher(text);
            if (matcher.find()) {
                String weather = matcher.group(1);
                String trackCondition = matcher.group(2);
                String timer = null;
                if (matcher.groupCount() > 2) {
                    timer = matcher.group(4);
                }
                return Optional.of(new PostTimeStartCommentsTimer(weather, trackCondition, timer));
            }
        }
        return Optional.empty();
    }


    public String getPostTime() {
        return postTime;
    }

    public String getStartComments() {
        return startComments;
    }

    public String getTimer() {
        return timer;
    }

    @Override
    public String toString() {
        return "PostTimeStartCommentsTimer{" +
                "postTime='" + postTime + '\'' +
                ", startComments='" + startComments + '\'' +
                ", timer='" + timer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostTimeStartCommentsTimer that = (PostTimeStartCommentsTimer) o;

        if (postTime != null ? !postTime.equals(that.postTime) : that.postTime != null)
            return false;
        if (startComments != null ? !startComments.equals(that.startComments) : that
                .startComments != null)
            return false;
        return timer != null ? timer.equals(that.timer) : that.timer == null;
    }

    @Override
    public int hashCode() {
        int result = postTime != null ? postTime.hashCode() : 0;
        result = 31 * result + (startComments != null ? startComments.hashCode() : 0);
        result = 31 * result + (timer != null ? timer.hashCode() : 0);
        return result;
    }
}
