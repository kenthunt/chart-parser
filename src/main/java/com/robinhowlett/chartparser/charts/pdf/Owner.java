package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The owner of the {@link Starter}
 */
public class Owner {

    private static final Pattern OWNERS_PATTERN = Pattern.compile("Owners:\\|.+");
    private static final Pattern OWNER_PATTERN = Pattern.compile("(\\w+)?\\s?-\\s?(.+)");

    private static final Logger LOGGER = LoggerFactory.getLogger(Owner.class);

    @JsonIgnore
    private final String program;
    private final String name;

    public Owner(String program, String name) {
        this.program = program;
        this.name = name;
    }

    public static List<Owner> parse(List<List<ChartCharacter>> lines) {
        for (List<ChartCharacter> line : lines) {
            String text = Chart.convertToText(line);
            Matcher matcher = OWNERS_PATTERN.matcher(text);
            if (matcher.find()) {
                return parseOwners(text);
            }
        }
        return new ArrayList<>();
    }

    static List<Owner> parseOwners(String text) {
        List<Owner> owners = new ArrayList<>();

        text = text.substring(text.indexOf('|') + 1).replaceAll(System.lineSeparator(), " ");
        List<String> ownersText = Arrays.asList(text.split(";"));

        for (String ownerText : ownersText) {
            Owner owner = parseOwner(ownerText);
            if (owner != null) {
                owners.add(owner);
            }
        }


        return owners;
    }

    private static Owner parseOwner(String text) {
        Matcher matcher = OWNER_PATTERN.matcher(text);
        if (matcher.find()) {
            String programNumber = matcher.group(1);
            String ownerName = matcher.group(2);
            return new Owner(programNumber, ownerName);
        }
        LOGGER.warn(String.format("Unable to parse valid owner from text: %s", text));
        return null;
    }

    public String getProgram() {
        return program;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Owner owner = (Owner) o;

        if (program != null ? !program.equals(owner.program) : owner
                .program != null)
            return false;
        return name != null ? name.equals(owner.name) : owner.name == null;
    }

    @Override
    public int hashCode() {
        int result = program != null ? program.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "program='" + program + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
