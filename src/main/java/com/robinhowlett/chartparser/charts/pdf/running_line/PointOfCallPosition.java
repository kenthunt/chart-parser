package com.robinhowlett.chartparser.charts.pdf.running_line;

import com.robinhowlett.chartparser.charts.pdf.ChartCharacter;
import com.robinhowlett.chartparser.charts.pdf.Starter;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall.PointOfCall.RelativePosition;
import com.robinhowlett.chartparser.points_of_call.PointsOfCall.PointOfCall.RelativePosition
        .LengthsAhead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.robinhowlett.chartparser.charts.pdf.Chart.convertToText;

/**
 * The position of the {@link Starter} and the lengths ahead of the next participant (if applicable)
 * at a particular point of call
 */
public class PointOfCallPosition {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointOfCallPosition.class);

    private static final Double SIX = new Double(6);
    private static final Double SEVEN = new Double(7);

    /**
     * Groups the chart characters by font size, then extracts and parses those designated for the
     * position at the particular point of call and the lengths ahead (if applicable) of the next
     * participant
     *
     * @return {@link RelativePosition} instance containing this {@link Starter}'s position and
     * lengths ahead
     */
    public static RelativePosition parse(List<ChartCharacter> characters) {
        Map<Double, List<ChartCharacter>> charactersByFontSize = characters.stream().collect(
                Collectors.groupingBy(character -> character.getFontSize()));

        Integer position = getPosition(charactersByFontSize.get(SEVEN));
        LengthsAhead lengthsAhead = ChartLengthsAhead.parse(charactersByFontSize.get(SIX));

        return new RelativePosition(position, lengthsAhead);
    }

    protected static Integer getPosition(List<ChartCharacter> positionCharacters) {
        if (positionCharacters != null) {
            String pos = convertToText(positionCharacters);
            if (!pos.equals("---") && !pos.equals("*")) {
                try {
                    return Integer.parseInt(pos);
                } catch (NumberFormatException e) {
                    LOGGER.warn(String.format("Unable to parse position from following text: " +
                            "%s", pos), e);
                }
            }
        }
        return null;
    }
}
