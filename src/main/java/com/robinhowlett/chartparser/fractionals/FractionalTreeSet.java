package com.robinhowlett.chartparser.fractionals;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * {@link TreeSet} of {@link FractionalPoint}s with a custom comparator based on {@link
 * FractionalPoint#floor} values
 */
public class FractionalTreeSet extends TreeSet<FractionalPoint> {
    public FractionalTreeSet() {
        super(new FractionalPointComparator());
    }

    static class FractionalPointComparator implements Comparator<FractionalPoint> {
        @Override
        public int compare(FractionalPoint o1, FractionalPoint o2) {
            return Integer.compare(o1.getFloor(), o2.getFloor());
        }
    }
}
