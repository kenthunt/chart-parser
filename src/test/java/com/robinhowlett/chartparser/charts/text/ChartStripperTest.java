package com.robinhowlett.chartparser.charts.text;

import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ChartStripperTest {

    @Captor
    private ArgumentCaptor<String> captor;

    @Test
    public void writeString_WithSampleTextPositions_WritesRoundedCsv() throws Exception {
        StringWriter writer = mock(StringWriter.class);
        ChartStripper chartStripper = new ChartStripper(writer);

        List<TextPosition> textPositions = new ArrayList<>();
        textPositions.add(sampleTextPosition());

        chartStripper.writeString("A", textPositions);

        Mockito.verify(writer).write(captor.capture());

        assertThat(captor.getValue(),
                equalTo(System.lineSeparator() +
                        "7.000|-6.000|14.000|5.000|11.000|13.000|2.000|A"));
    }

    private TextPosition sampleTextPosition() {
        return new TextPosition(0, 1f, 2f, new Matrix(3f, 4f, 5f, 6f, 7f, 8f),
                9f, 10f, 11f, 12f, 13f, "A", null, null,
                14f, 15);
    }
}
