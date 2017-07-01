package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.TestChartResources;
import com.robinhowlett.chartparser.charts.pdf.Purse.PurseEnhancement;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.robinhowlett.chartparser.charts.pdf.Purse.EnhancementType.INCLUDES;
import static com.robinhowlett.chartparser.charts.pdf.Purse.EnhancementType.PLUS;
import static com.robinhowlett.chartparser.charts.pdf.Purse.parsePurseText;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class PurseTest {

    private Purse purse;

    @Before
    public void setUp() throws Exception {
        purse = new Purse();
    }

    @Test
    public void parse_WithVariousPurses_ReturnsValidPurseInteger() throws Exception {
        assertThat(parsePurseText("Purse: $500", purse).getValue(), equalTo(500));
        assertThat(parsePurseText("Purse: $5,000", purse).getValue(), equalTo(5000));
        assertThat(parsePurseText("Purse: $40,000 Added", purse).getValue(), equalTo(40000));
        assertThat(parsePurseText("Purse: $100,000 Guaranteed", purse).getValue(),
                equalTo(100000));
        assertThat(parsePurseText("Purse: $6,000,000 Guaranteed", purse).getValue(),
                equalTo(6000000));
        assertThat(parsePurseText("Purse: $16,000,000 Guaranteed", purse).getValue(),
                equalTo(16000000));
    }

    @Test
    public void parse_WithPurses_ReturnsValidString() throws Exception {
        assertThat(parsePurseText("Purse: $500", purse).getText(), equalTo("$500"));
        assertThat(parsePurseText("Purse: $40,000 Added", purse).getText(),
                equalTo("$40,000 Added"));
    }

    @Test
    public void parse_WithAvailableMoney_ReturnsValidString() throws Exception {
        assertThat(parsePurseText("Available Money: $500", purse).getAvailableMoney(),
                equalTo("$500"));
        assertThat(parsePurseText("Available Money: $40,000 Added", purse).getAvailableMoney(),
                equalTo("$40,000 Added"));
    }

    @Test
    public void parse_WithIncludes_ReturnsValidString() throws Exception {
        assertThat(parsePurseText("Includes: $500", purse).getEnhancements(),
                equalTo(new ArrayList<PurseEnhancement>() {{
                    add(new PurseEnhancement(INCLUDES, "$500"));
                }}));
        assertThat(parsePurseText("Includes: $2,000 Other Sources", purse).getEnhancements(),
                equalTo(new ArrayList<PurseEnhancement>() {{
                    add(new PurseEnhancement(INCLUDES, "$500"));
                    add(new PurseEnhancement(INCLUDES, "$2,000 Other Sources"));
                }}));
    }

    @Test
    public void parse_WithPlus_ReturnsValidString() throws Exception {
        assertThat(parsePurseText("Plus: $500", purse).getEnhancements(PLUS),
                equalTo(new ArrayList<PurseEnhancement>() {{
                    add(new PurseEnhancement(PLUS, "$500"));
                }}));
        assertThat(parsePurseText("Plus: $40,000 Added", purse).getEnhancements(PLUS),
                equalTo(new ArrayList<PurseEnhancement>() {{
                    add(new PurseEnhancement(PLUS, "$500"));
                    add(new PurseEnhancement(PLUS, "$40,000 Added"));
                }}));
    }

    @Test
    public void parseValueOfRace_WithSampleRace_ReturnsValidString() throws Exception {
        TestChartResources sampleCharts = new TestChartResources();
        Purse purse = Purse.parse(sampleCharts.getSampleChartLines(1));
        assertThat(purse.getValueOfRace(), equalTo("$9,700 1st $5,820, 2nd $1,940, 3rd $970, " +
                "4th $485, 5th $97, 6th $97, 7th $97, 8th $97, 9th $97"));
    }
}
