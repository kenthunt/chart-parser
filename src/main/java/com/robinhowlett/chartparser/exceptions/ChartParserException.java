package com.robinhowlett.chartparser.exceptions;

/**
 * Superclass of all checked exceptions for this parser
 */
public class ChartParserException extends Exception {

    public ChartParserException() {
    }

    public ChartParserException(String message) {
        super(message);
    }

    public ChartParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
