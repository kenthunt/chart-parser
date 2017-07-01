package com.robinhowlett.chartparser.tracks;

/**
 * Stores a track's code, country, and name
 */
public class Track {

    private String code;
    private String country;
    private String name;

    public String getCode() {
        return code.trim();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country.trim();
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Track{" +
                "code='" + code + '\'' +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (code != null ? !code.equals(track.code) : track.code != null)
            return false;
        if (country != null ? !country.equals(track.country) : track.country != null) return false;
        return name != null ? name.equals(track.name) : track.name == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
