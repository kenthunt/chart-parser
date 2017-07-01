package com.robinhowlett.chartparser.charts.text;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static com.robinhowlett.chartparser.charts.pdf.Chart.round;

/**
 * This class will take a PDF chart and for each character write a CSV row with its location, scale,
 * and value.
 */
public class ChartStripper extends PDFTextStripper {
    private final StringWriter writer;

    public ChartStripper(StringWriter writer) throws IOException {
        super();
        this.writer = writer;
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
            String row =
                    round(text.getXDirAdj()) + "|" +
                            round(text.getYDirAdj()) + "|" +
                            round(text.getFontSize()) + "|" +
                            round(text.getXScale()) + "|" +
                            round(text.getHeightDir()) + "|" +
                            round(text.getWidthOfSpace()) + "|" +
                            round(text.getWidthDirAdj()) + "|" +
                            text.getUnicode();

            writer.write(System.lineSeparator() + row.trim());
        }
    }

    public StringWriter getWriter() {
        return writer;
    }
}
