package com.robinhowlett.chartparser.charts.pdf.wagering;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * A {@link TreeSet} for {@link WageringColumn} instances, including a custom comparator
 */
public class WageringTreeSet extends TreeSet<WageringColumn> {

    public WageringTreeSet() {
        super(new WageringColumnComparator());
    }

    static class WageringColumnComparator implements Comparator<WageringColumn> {
        @Override
        public int compare(WageringColumn o1, WageringColumn o2) {
            return Double.compare(o1.getFloor(), o2.getFloor());
        }
    }
}
