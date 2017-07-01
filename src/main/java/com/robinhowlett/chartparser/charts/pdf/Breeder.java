package com.robinhowlett.chartparser.charts.pdf;

/**
 * Stores the name of the {@link Starter}'s breeder
 */
public class Breeder {

    private final String name;

    public Breeder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Breeder breeder = (Breeder) o;

        return name != null ? name.equals(breeder.name) : breeder.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Breeder{" +
                "name='" + name + '\'' +
                '}';
    }
}
