package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.Chart;
import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.exceptions.ChartParserException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static com.robinhowlett.chartparser.charts.pdf.Chart.THREE;
import static com.robinhowlett.chartparser.charts.pdf.Chart.round;

public class RunningLineHeader {

    public static TreeSet<RunningLineColumnIndex> createIndexOfRunningLineColumns(
            List<ChartCharacter> runningLineHeader) throws MalformedRaceException {
        // the standard headers that are always present
        List<String> preRaceHeaderColumnNames = Arrays.asList("LastRaced", "Pgm",
                "HorseName(Jockey)", "Wgt", "M/E", "PP");

        // assign the start chart character that belongs to the appropriate leading header column
        Map<String, ChartCharacter> headerColumns =
                populateHeaderColumns(runningLineHeader, preRaceHeaderColumnNames);

        // next, assign the first chart character that belongs to the appropriate trailing header
        // column
        Map<String, ChartCharacter> postRaceHeaderColumns =
                createPostRaceRunningLineHeaderColumns(runningLineHeader);

        // finally, update the header columns with the first chart character that belongs to each
        // point of call header column
        headerColumns = populateHeaderColumnsWithInRaceRunningLine(runningLineHeader,
                headerColumns);

        // combine it all
        headerColumns.putAll(postRaceHeaderColumns);

        // create a TreeSet of RunningLineColumnIndex instance so each row's chart character will
        // be mapped to the appropriate header column
        TreeSet<RunningLineColumnIndex> runningLineColumnIndices =
                createRunningLineColumnIndices(headerColumns);

        return runningLineColumnIndices;
    }

    /**
     * Builds a {@link Map} of the first {@link ChartCharacter} detected for each header column (the
     * key)
     */
    public static Map<String, ChartCharacter> populateHeaderColumns(
            List<ChartCharacter> runningLineHeader, List<String> headerColumnNames) {
        ChartCharacter index = null;
        StringBuffer sb = null;
        List<ChartCharacter> progress = null;
        List<ChartCharacter> covered = new ArrayList<>();

        Map<String, ChartCharacter> headerColumns = new LinkedHashMap<>();

        // for each character in the running line header...
        for (ChartCharacter pdfCharacter : runningLineHeader) {
            //...reset the StringBuffer and character list cache if needed, then...
            if (sb == null) {
                index = pdfCharacter;
                sb = new StringBuffer();
                progress = new ArrayList<>();
            }

            // ...build up the characters as a string until...
            progress.add(pdfCharacter);
            sb.append(pdfCharacter.getUnicode());

            // ...it matches one of the known header column names
            for (String headerColumnName : headerColumnNames) {
                // need to do this as tab is inconsistent between Wgt and M/E
                if (sb.toString().equals(headerColumnName)) {
                    // "Pgm" values are right-aligned but the column is centered, therefore
                    // the first value may be to the left of the "P"; allow up to, but not
                    // including, the width of one character for indexing
                    if (headerColumnName.equals("Pgm")) {
                        index.setxDirAdj(Chart.round(index.getxDirAdj() - 3.891).doubleValue());
                    }
                    headerColumns.put(headerColumnName, index);
                    covered.addAll(progress);
                    sb = null;
                    break;
                }
            }
        }

        // remove the characters already assigned to a header column
        runningLineHeader.removeAll(covered);

        return headerColumns;
    }

    /**
     * The trailing column headers are different for Quarter Horse races vs others, and if betting
     * was active or not, so this method attempts to build a {@link Map} of the first {@link
     * ChartCharacter} detected for each header column that, when combined, matches one of the known
     * header-columns-as-text suffixes
     */
    static Map<String, ChartCharacter> createPostRaceRunningLineHeaderColumns(
            List<ChartCharacter> runningLineHeader) throws MalformedRaceException {
        ChartCharacter index = null;
        StringBuffer sb = null;
        List<ChartCharacter> covered = new ArrayList<>();
        Map<String, ChartCharacter> end = new LinkedHashMap<>();

        // is it QH or TB/Arabian, and with betting or not?
        RunningLineHeaderSuffix runningLineHeaderSuffix =
                identifyHeaderSuffixCharactersForRegistry(runningLineHeader);

        String[] suffixColumns = runningLineHeaderSuffix.getHeaderSuffix()
                .split("(?=\\p{Lu})(?!Time|In\\.)");

        for (ChartCharacter character : runningLineHeaderSuffix.getHeaderSuffixCharacters()) {
            if (sb == null) {
                index = character;
                sb = new StringBuffer();
            }
            sb.append(character.getUnicode());
            for (String column : suffixColumns) {
                if (sb.toString().equals(column)) {
                    // "Odds" is right-aligned so the first character can be before
                    // the x-axis; subtract from the xDirAdj to try to account for this.
                    // 3.892 is the normal value of a character in the "Odds" column, so do less
                    // than that
                    if (column.equals("Odds")) {
                        index.setxDirAdj(Chart.round(index.getxDirAdj() - 3.891).doubleValue());
                    }
                    end.put(column, index);
                    sb = null;
                    break;
                }
            }
            covered.add(character);
        }
        runningLineHeader.removeAll(covered);

        return end;
    }

    /**
     * Iterates over the reserved running line header columns text to match one of the known
     * suffixes. This detects if the running line was for a QH or TB/Arabian race, and with betting
     * or not
     */
    static RunningLineHeaderSuffix identifyHeaderSuffixCharactersForRegistry(
            List<ChartCharacter> runningLineHeader) throws MalformedRaceException {
        boolean found = false;
        String headerSuffix = null;
        StringBuilder sb = new StringBuilder();
        List<ChartCharacter> headerSuffixCharacters = new ArrayList<>();

        // reverse so we can focus on the suffix only until we find a match
        Collections.reverse(runningLineHeader);

        for (ChartCharacter line : runningLineHeader) {
            headerSuffixCharacters.add(line);
            sb.append(line.getUnicode());
            String reversed = new StringBuffer(sb.toString()).reverse().toString();
            for (String ending : RunningLineHeaderSuffix.SUFFIXES) {
                if (reversed.equals(ending)) {
                    headerSuffix = ending;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }

        // undo the reversal
        Collections.reverse(runningLineHeader);
        // these were stored in RTL order so make them LTR again
        Collections.reverse(headerSuffixCharacters);

        if (!found) {
            throw new MalformedRaceException(String.format("Unable to create " +
                    "RunningLineHeaderSuffix instance as the headerSuffix value of %s is " +
                    "invalid.", headerSuffix));

        }

        return new RunningLineHeaderSuffix(headerSuffix, headerSuffixCharacters);
    }

    /**
     * Builds a {@link Map} of the first {@link ChartCharacter} detected for each header column (the
     * key) for the point of call header columns (which differ per race distance) by identifying
     * large spaces between characters
     */
    static Map<String, ChartCharacter> populateHeaderColumnsWithInRaceRunningLine(
            List<ChartCharacter> runningLineHeader, Map<String, ChartCharacter> headerColumns) {
        ChartCharacter index = null;
        ChartCharacter prev = null;
        StringBuffer sb = new StringBuffer();

        int counter = 1;
        for (ChartCharacter curr : runningLineHeader) {
            if (counter == 1) {
                prev = index = curr;
            }

            // calculate the positional difference between the previous character and this one
            BigDecimal spacing =
                    round(curr.getxDirAdj())
                            .subtract(
                                    round(prev.getxDirAdj()).add(round(prev.getWidthDirAdj())));

            // if a big space was detected, it's belongs to a new column header, so store what
            // was found so far and reset
            if (spacing.compareTo(THREE) > 0) {
                headerColumns.put(sb.toString(), index);
                sb = new StringBuffer();
                index = curr;
            } else if (counter == runningLineHeader.size()) {
                // we've hit the end
                sb.append(curr.getUnicode());
                headerColumns.put(sb.toString(), index);
            }

            sb.append(curr.getUnicode());
            prev = curr;
            counter++;
        }

        return headerColumns;
    }

    /**
     * Creates the {@link TreeSet} used to assign each running line's character to the appropriate
     * header column
     */
    static TreeSet<RunningLineColumnIndex> createRunningLineColumnIndices(
            Map<String, ChartCharacter> headerColumns) {
        TreeSet<RunningLineColumnIndex> runningLineColumnIndices =
                new TreeSet<>(new RunningLineHeaderComparator());

        for (String columnName : headerColumns.keySet()) {
            ChartCharacter pdfCharacter = headerColumns.get(columnName);
            runningLineColumnIndices.add(
                    new RunningLineColumnIndex(columnName, pdfCharacter.getxDirAdj()));
        }
        return runningLineColumnIndices;
    }

    /**
     * Compares {@link RunningLineColumnIndex} instances by their {@code xDirAdj} values (to find
     * the header column a {@link ChartCharacter} belongs to)
     */
    public static class RunningLineHeaderComparator implements Comparator<RunningLineColumnIndex> {
        @Override
        public int compare(RunningLineColumnIndex o1, RunningLineColumnIndex o2) {
            if (o1.getxDirAdj() < o2.getxDirAdj()) return -1;
            if (o1.getxDirAdj() > o2.getxDirAdj()) return 1;
            return 0;
        }
    }

    public static class MalformedRaceException extends ChartParserException {
        public MalformedRaceException(String message) {
            super(message);
        }
    }
}
