package com.robinhowlett.chartparser.charts.pdf.wagering;

import com.robinhowlett.chartparser.charts.pdf.Chart;
import com.robinhowlett.chartparser.charts.pdf.wagering.WagerPayoffPools.ColumnRange;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Stores the column name and the positional location of the wagering column
 */
public class WageringColumn {

    private final String columnName;
    private final double floor;

    public WageringColumn(double floor) {
        this(null, floor);
    }

    public WageringColumn(String columnName, double floor) {
        this.columnName = columnName;
        this.floor = floor;
    }

    /**
     * For each designated column of the wagering grid, creates a {@link WageringColumn} instance
     * and adds it to a {@link WageringTreeSet}
     */
    static WageringTreeSet calculateColumnFloors(Map<String, ColumnRange> wageringHeaderColumns) {
        WageringTreeSet wageringTreeSet = new WageringTreeSet();

        List<String> wageringColumnNames = Arrays.asList("Pgm", "HorseWin", "Place",
                "Show", "WagerType", "WinningNumbersPayoff", "Pool", "Carryover");

        for (String wageringColumnName : wageringColumnNames) {
            switch (wageringColumnName) {
                case "Pgm":
                    wageringTreeSet.add(new WageringColumn(wageringColumnName, 0));
                    break;
                case "HorseWin":
                    // horse and win are combined as since WinningNumbers also starts with Win
                    wageringTreeSet.add(new WageringColumn(wageringColumnName,
                            wageringHeaderColumns.get("Horse").getLeft()));
                    break;
                case "Place":
                    wageringTreeSet.add(new WageringColumn(wageringColumnName,
                            Chart.round(wageringHeaderColumns.get("Win").getRight())
                                    .doubleValue()));
                    break;
                case "Show":
                    wageringTreeSet.add(new WageringColumn(wageringColumnName,
                            Chart.round(wageringHeaderColumns.get("Place").getRight())
                                    .doubleValue()));
                    break;
                case "WagerType":
                    wageringTreeSet.add(new WageringColumn(wageringColumnName,
                            wageringHeaderColumns.get("WagerType").getLeft()));
                    break;
                case "WinningNumbersPayoff":
                    wageringTreeSet.add(new WageringColumn(wageringColumnName,
                            wageringHeaderColumns.get("WinningNumbers").getLeft()));
                    break;
                case "Pool":
                    wageringTreeSet.add(new WageringColumn(wageringColumnName,
                            Chart.round(wageringHeaderColumns.get("Payoff").getRight())
                                    .doubleValue()));
                    break;
                case "Carryover":
                    wageringTreeSet.add(new WageringColumn(wageringColumnName,
                            Chart.round(wageringHeaderColumns.get("Pool").getRight())
                                    .doubleValue()));
                    break;
            }
        }

        return wageringTreeSet;
    }


    public String getColumnName() {
        return columnName;
    }

    public double getFloor() {
        return floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WageringColumn that = (WageringColumn) o;

        if (Double.compare(that.floor, floor) != 0) return false;
        return columnName != null ? columnName.equals(that.columnName) : that.columnName == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = columnName != null ? columnName.hashCode() : 0;
        temp = Double.doubleToLongBits(floor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "WageringColumn{" +
                "columnName='" + columnName + '\'' +
                ", floor=" + floor +
                '}';
    }
}
