package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Stores if a race was cancelled and, if provided, for what reason
 */
@JsonInclude(NON_NULL)
@JsonPropertyOrder({"cancelled", "reason"})
public class Cancellation {

    private static final Pattern CANCELLATION_PATTERN =
            Pattern.compile("^(Cancelled.*|CANCELLED.*) - (.+)");
    public static final String NO_REASON_AVAILABLE = null;

    private final boolean cancelled;
    private final String reason;

    public Cancellation() {
        this(false, NO_REASON_AVAILABLE);
    }

    public Cancellation(String reason) {
        this(true, reason);
    }

    Cancellation(boolean cancelled, String reason) {
        this.cancelled = cancelled;
        this.reason = reason;
    }

    public static Cancellation parse(List<List<ChartCharacter>> lines) {
        for (List<ChartCharacter> line : lines) {
            String rawText = Chart.convertToText(line);
            Optional<Cancellation> cancellation = checkForCancellation(rawText);
            if (cancellation.isPresent()) {
                return cancellation.get();
            }
        }
        return new Cancellation();
    }

    static Optional<Cancellation> checkForCancellation(String text) {
        Matcher matcher = CANCELLATION_PATTERN.matcher(text);
        if (matcher.find()) {
            String reason = matcher.group(2);
            if (Breed.isBreed(reason)) {
                reason = NO_REASON_AVAILABLE;
            }
            return Optional.of(new Cancellation(reason));
        }
        return Optional.empty();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cancellation that = (Cancellation) o;

        if (cancelled != that.cancelled) return false;
        return reason != null ? reason.equals(that.reason) : that.reason == null;
    }

    @Override
    public int hashCode() {
        int result = (cancelled ? 1 : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cancellation{" +
                "cancelled=" + cancelled +
                ", reason='" + reason + '\'' +
                '}';
    }
}
