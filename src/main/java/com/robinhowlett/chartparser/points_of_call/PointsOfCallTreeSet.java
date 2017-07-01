package com.robinhowlett.chartparser.points_of_call;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * {@link TreeSet} of {@link PointsOfCall}s with a custom comparator based on {@link
 * PointsOfCall#floor} values
 */
public class PointsOfCallTreeSet extends TreeSet<PointsOfCall> {

    public PointsOfCallTreeSet() {
        super(new PointsOfCallComparator());
    }

    static class PointsOfCallComparator implements Comparator<PointsOfCall> {
        @Override
        public int compare(PointsOfCall o1, PointsOfCall o2) {
            return Integer.compare(o1.getFloor(), o2.getFloor());
        }
    }
}
