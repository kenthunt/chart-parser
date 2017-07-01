package com.robinhowlett.chartparser.charts.pdf;

/**
 * AQHA track Quarter Horse related information, such as the individual finshing time for the {@link
 * Starter} and the AQHA Speed Index figure recorded. This object will be null for Starters in
 * non-QH races
 */
public class Aqha {

    private final Long individualTimeMillis;
    private final Integer speedIndex;

    public Aqha(Long individualTimeMillis, Integer speedIndex) {
        this.individualTimeMillis = individualTimeMillis;
        this.speedIndex = speedIndex;
    }

    public Long getIndividualTimeMillis() {
        return individualTimeMillis;
    }

    public Integer getSpeedIndex() {
        return speedIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aqha aqha = (Aqha) o;

        if (individualTimeMillis != null ? !individualTimeMillis.equals(aqha
                .individualTimeMillis) : aqha.individualTimeMillis != null)
            return false;
        return speedIndex != null ? speedIndex.equals(aqha.speedIndex) : aqha.speedIndex == null;
    }

    @Override
    public int hashCode() {
        int result = individualTimeMillis != null ? individualTimeMillis.hashCode() : 0;
        result = 31 * result + (speedIndex != null ? speedIndex.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Aqha{" +
                "individualTimeMillis=" + individualTimeMillis +
                ", speedIndex=" + speedIndex +
                '}';
    }
}
