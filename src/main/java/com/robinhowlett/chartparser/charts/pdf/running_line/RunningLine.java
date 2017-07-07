package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class RunningLine {

    /**
     * For each character in each running line, add it to a list assigned to the appropriate header
     * column e.g. all the characters between
     */
    public static Map<String, List<ChartCharacter>> groupRunningLineCharactersByColumn(
            TreeSet<RunningLineColumnIndex> runningLineColumnIndices,
            List<ChartCharacter> runningLine) {
        Map<String, List<ChartCharacter>> runningLineCharactersByColumn = new LinkedHashMap<>();

        for (ChartCharacter pdfCharacter : runningLine) {
            // get the location of the character for this particular running line row
            RunningLineColumnIndex columnIndex =
                    new RunningLineColumnIndex(pdfCharacter.getxDirAdj());

            // find the header column it belongs to by locating the column with the next-lowest
            // xDirAdj value to the columnIndex
            RunningLineColumnIndex floor = runningLineColumnIndices.floor(columnIndex);

            // for each header column, store the characters that apply to it
            String columnHeader = floor.getColumnHeader();
            List<ChartCharacter> chartCharacters = runningLineCharactersByColumn.get(columnHeader);
            if (chartCharacters == null) {
                chartCharacters = new ArrayList<>();
            }
            chartCharacters.add(pdfCharacter);
            runningLineCharactersByColumn.put(columnHeader, chartCharacters);
        }

        return runningLineCharactersByColumn;
    }

}
