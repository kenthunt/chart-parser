package com.robinhowlett.chartparser.charts.pdf;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class OwnerTest {

    private static final String OWNERS = "Owners:|3 - D P Racing; 14 - Kretz Racing LLC; 9 - " +
            "Rockingham Ranch; 2 - Arndt, Michael. J., McFetridge, S. M., Preiss, Daniel, and " +
            "Metanovic, Mersad;" + System.lineSeparator() +
            "7 - Gorman, Mark, Queen Bee Racing, LLC and Sterling Stable LLC; 8 - Qatar Racing, " +
            "Ltd.; 6 - Reddam Racing LLC;";

    @Test
    public void parseOwners_WithSevenOwnerDetails_ExtractsOwnerInfoCorrectly() throws Exception {
        List<Owner> expected = new ArrayList<Owner>() {{
            add(new Owner("3", "D P Racing"));
            add(new Owner("14", "Kretz Racing LLC"));
            add(new Owner("9", "Rockingham Ranch"));
            add(new Owner("2", "Arndt, Michael. J., McFetridge, S. M., Preiss, Daniel, and " +
                    "Metanovic, Mersad"));
            add(new Owner("7", "Gorman, Mark, Queen Bee Racing, LLC and Sterling Stable LLC"));
            add(new Owner("8", "Qatar Racing, Ltd."));
            add(new Owner("6", "Reddam Racing LLC"));
        }};

        List<Owner> owners = Owner.parseOwners(OWNERS);

        assertThat(owners, equalTo(expected));
    }
}
