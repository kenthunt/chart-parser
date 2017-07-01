package com.robinhowlett.chartparser.charts.pdf;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Stores the horse name and, if available (e.g. from a {@link Winner}'s details), breeding
 * information
 */
@JsonInclude(NON_NULL)
public class Horse {
    private final String name;
    private String color;
    private String sex;
    private Horse sire;
    private Horse dam;
    private Horse damSire;
    private LocalDate foalingDate;
    private String foalingLocation;
    private Breeder breeder;

    public Horse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Horse getSire() {
        return sire;
    }

    public void setSire(Horse sire) {
        this.sire = sire;
    }

    public Horse getDam() {
        return dam;
    }

    public void setDam(Horse dam) {
        this.dam = dam;
    }

    public Horse getDamSire() {
        return damSire;
    }

    public void setDamSire(Horse damSire) {
        this.damSire = damSire;
    }

    public LocalDate getFoalingDate() {
        return foalingDate;
    }

    public void setFoalingDate(LocalDate foalingDate) {
        this.foalingDate = foalingDate;
    }

    public String getFoalingLocation() {
        return foalingLocation;
    }

    public void setFoalingLocation(String foalingLocation) {
        this.foalingLocation = foalingLocation;
    }

    public Breeder getBreeder() {
        return breeder;
    }

    public void setBreeder(Breeder breeder) {
        this.breeder = breeder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Horse horse = (Horse) o;

        if (name != null ? !name.equals(horse.name) : horse.name != null) return false;
        if (color != null ? !color.equals(horse.color) : horse.color != null) return false;
        if (sex != null ? !sex.equals(horse.sex) : horse.sex != null) return false;
        if (sire != null ? !sire.equals(horse.sire) : horse.sire != null) return false;
        if (dam != null ? !dam.equals(horse.dam) : horse.dam != null) return false;
        if (damSire != null ? !damSire.equals(horse.damSire) : horse.damSire != null) return false;
        if (foalingDate != null ? !foalingDate.equals(horse.foalingDate) : horse.foalingDate !=
                null)
            return false;
        if (foalingLocation != null ? !foalingLocation.equals(horse.foalingLocation) : horse
                .foalingLocation != null)
            return false;
        return breeder != null ? breeder.equals(horse.breeder) : horse.breeder == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (sire != null ? sire.hashCode() : 0);
        result = 31 * result + (dam != null ? dam.hashCode() : 0);
        result = 31 * result + (damSire != null ? damSire.hashCode() : 0);
        result = 31 * result + (foalingDate != null ? foalingDate.hashCode() : 0);
        result = 31 * result + (foalingLocation != null ? foalingLocation.hashCode() : 0);
        result = 31 * result + (breeder != null ? breeder.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Horse{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sex='" + sex + '\'' +
                ", sire=" + sire +
                ", dam=" + dam +
                ", damSire=" + damSire +
                ", foalingDate=" + foalingDate +
                ", foalingLocation='" + foalingLocation + '\'' +
                ", breeder=" + breeder +
                '}';
    }
}
