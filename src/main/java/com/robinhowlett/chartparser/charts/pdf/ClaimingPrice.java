package com.robinhowlett.chartparser.charts.pdf;

import com.robinhowlett.chartparser.exceptions.ChartParserException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Locale.US;

/**
 * Parses and stores the program number, horse name, and claiming price for each {@link Starter} who
 * was available to be claimed
 */
public class ClaimingPrice {

    private static final Pattern CLAIMING_PRICES = Pattern.compile("Claiming Prices:(.+)");
    private static final Pattern CLAIMING_PRICE =
            Pattern.compile("(\\w+)?\\s?-\\s?(.+?):\\s?\\$([0-9]{1,3}(,[0-9]{3})*)");

    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimingPrice.class);

    private final String program;
    private final Horse horse;
    private final int claimingPrice;

    public ClaimingPrice(String program, Horse horse, int claimingPrice) {
        this.program = program;
        this.horse = horse;
        this.claimingPrice = claimingPrice;
    }

    public static List<ClaimingPrice> parse(List<List<ChartCharacter>> lines)
            throws ChartParserException {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            Matcher matcher = CLAIMING_PRICES.matcher(text);
            if (matcher.find()) {
                return parseClaimingPrices(text);
            }
        }
        return new ArrayList<>();
    }

    static List<ClaimingPrice> parseClaimingPrices(String text) throws ChartParserException {
        List<ClaimingPrice> claimingPrices = new ArrayList<>();

        text = text.substring(text.indexOf('|') + 1).replaceAll(System.lineSeparator(), " ");
        List<String> claimingPricesText = Arrays.asList(text.split(";"));
        for (String claimingPriceText : claimingPricesText) {
            ClaimingPrice claimingPrice = parseClaimingPrice(claimingPriceText);
            if (claimingPrice != null) {
                claimingPrices.add(claimingPrice);
            }
        }

        if (claimingPrices.isEmpty()) {
            LOGGER.warn(String.format("No claiming prices were successfully parsed from text: %s",
                    text));
        }

        return claimingPrices;
    }

    private static ClaimingPrice parseClaimingPrice(String text) throws ChartParserException {
        Matcher matcher = CLAIMING_PRICE.matcher(text);
        if (matcher.find()) {
            String programNumber = matcher.group(1);
            String horseName = matcher.group(2);
            String claimingPriceAmount = matcher.group(3);
            try {
                int claimingPriceInDollars =
                        NumberFormat.getNumberInstance(US).parse(claimingPriceAmount).intValue();

                return new ClaimingPrice(programNumber, new Horse(horseName),
                        claimingPriceInDollars);
            } catch (ParseException e) {
                throw new ChartParserException(String.format("Unable to parse claiming price: " +
                        "%s", text), e);
            }
        }
        return null;
    }

    public String getProgram() {
        return program;
    }

    public Horse getHorse() {
        return horse;
    }

    public int getClaimingPrice() {
        return claimingPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClaimingPrice that = (ClaimingPrice) o;

        if (claimingPrice != that.claimingPrice) return false;
        if (program != null ? !program.equals(that.program) : that
                .program != null)
            return false;
        return horse != null ? horse.equals(that.horse) : that.horse == null;
    }

    @Override
    public int hashCode() {
        int result = program != null ? program.hashCode() : 0;
        result = 31 * result + (horse != null ? horse.hashCode() : 0);
        result = 31 * result + claimingPrice;
        return result;
    }

    @Override
    public String toString() {
        return "ClaimingPrice{" +
                "program='" + program + '\'' +
                ", horse='" + horse + '\'' +
                ", claimingPrice=" + claimingPrice +
                '}';
    }
}
