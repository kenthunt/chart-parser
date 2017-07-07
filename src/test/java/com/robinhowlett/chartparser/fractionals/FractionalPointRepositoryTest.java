package com.robinhowlett.chartparser.fractionals;

import org.junit.Test;

import java.util.ArrayList;

import static com.robinhowlett.chartparser.ChartParser.getObjectMapper;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class FractionalPointRepositoryTest {

    @Test
    public void findAll_WithFractionalTimesFiles_ReturnsTreeSetOfFractionals() throws Exception {
        ArrayList<FractionalPoint.Fractional> fractionals = new ArrayList<>();
        fractionals.add(new FractionalPoint.Fractional(6, "Fin", 450));

        FractionalPoint expected = new FractionalPoint("150 yards", 450, fractionals);

        FractionalPointRepository repository = new FractionalPointRepository(getObjectMapper());

        FractionalTreeSet fractionalPoints = repository.findAll();
        assertThat(fractionalPoints.size(), equalTo(40));
        assertThat(fractionalPoints.first(), equalTo(expected));
    }
}
