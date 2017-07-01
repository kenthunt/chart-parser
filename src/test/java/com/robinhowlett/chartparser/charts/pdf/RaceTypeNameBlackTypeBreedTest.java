package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.robinhowlett.chartparser.charts.pdf.Breed.QUARTER_HORSE;
import static com.robinhowlett.chartparser.charts.pdf.Breed.THOROUGHBRED;
import static com.robinhowlett.chartparser.charts.pdf.RaceTypeNameBlackTypeBreed
        .parseRaceTypeNameBlackTypeBreed;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class RaceTypeNameBlackTypeBreedTest {

    private String chartText;
    private RaceTypeNameBlackTypeBreed expected;

    public RaceTypeNameBlackTypeBreedTest(String chartText, RaceTypeNameBlackTypeBreed expected) {
        this.chartText = chartText;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection races() {
        return Arrays.asList(new Object[][]{
                {"MAIDEN - Quarter Horse", new RaceTypeNameBlackTypeBreed("MAIDEN",
                        QUARTER_HORSE)},
                {"MAIDEN SPECIAL WEIGHT - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("MAIDEN SPECIAL WEIGHT", THOROUGHBRED)},
                {"STAKES Mount Elbert S. - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("STAKES", "Mount Elbert S.", THOROUGHBRED)},
                {"STAKES Juvenile Turf Sprint S. Listed - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("STAKES", "Juvenile Turf Sprint S.",
                                "Listed", THOROUGHBRED)},
                {"STAKES Golden State Juvenile S. Black Type - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("STAKES", "Golden State Juvenile S.",
                                "Black Type", THOROUGHBRED)},
                {"STAKES Senator Ken Maddy S. Grade 3 - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("STAKES", "Senator Ken Maddy S.", 3,
                                "Grade 3", THOROUGHBRED)},
                {"STAKES Breeders' Cup Classic Grade 1 - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("STAKES", "Breeders' Cup Classic", 1,
                                "Grade 1", THOROUGHBRED)},
                {"STAKES 14 Hands Winery Breeders' Cup Juvenile Fillies Grade 1 - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("STAKES", "14 Hands Winery Breeders' Cup " +
                                "Juvenile Fillies", 1, "Grade 1", THOROUGHBRED)},
                {"DERBY Prairie Meadows Derby Challenge - Quarter Horse",
                        new RaceTypeNameBlackTypeBreed("DERBY", "Prairie Meadows Derby " +
                                "Challenge", QUARTER_HORSE)},
                {"STAKES CTBA Derby - Thoroughbred",
                        new RaceTypeNameBlackTypeBreed("STAKES", "CTBA Derby", THOROUGHBRED)}
        });
    }

    @Test
    public void parseBreed_WithParameterizedInput_ReturnsCorrectRaceDetails() throws Exception {
        assertThat(parseRaceTypeNameBlackTypeBreed(chartText).get(), equalTo(expected));
    }
}
