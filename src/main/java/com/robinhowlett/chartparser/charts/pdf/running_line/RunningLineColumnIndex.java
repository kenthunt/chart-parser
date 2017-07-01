package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;

import java.util.TreeSet;

/**
 * Tracks the location of the first character of a running line column header so that it can be used
 * with {@link TreeSet#floor(Object)} to associate {@link ChartCharacter}s with particular columns
 * in a grid
 */
public class RunningLineColumnIndex {
    private final String columnHeader;
    private final double xDirAdj;

    public RunningLineColumnIndex(String columnHeader, double xDirAdj) {
        this.columnHeader = columnHeader;
        this.xDirAdj = xDirAdj;
    }

    RunningLineColumnIndex(double xDirAdj) {
        this(null, xDirAdj);
    }

    public String getColumnHeader() {
        return columnHeader;
    }

    public double getxDirAdj() {
        return xDirAdj;
    }

    @Override
    public String toString() {
        return "RunningLineColumnIndex{" +
                "columnHeader='" + columnHeader + '\'' +
                ", xDirAdj=" + xDirAdj +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunningLineColumnIndex that = (RunningLineColumnIndex) o;

        if (Double.compare(that.xDirAdj, xDirAdj) != 0) return false;
        return columnHeader != null ? columnHeader.equals(that.columnHeader) : that.columnHeader
                == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = columnHeader != null ? columnHeader.hashCode() : 0;
        temp = Double.doubleToLongBits(xDirAdj);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
