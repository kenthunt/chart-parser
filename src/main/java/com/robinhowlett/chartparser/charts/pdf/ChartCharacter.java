package com.robinhowlett.chartparser.charts.pdf;

/**
 * Corresponds to a character within the PDF chart
 */
public class ChartCharacter {

    private double xDirAdj;
    private double yDirAdj;
    private double fontSize;
    private double xScale;
    private double height;
    private double widthOfSpace;
    private double widthDirAdj;
    private char unicode;

    public double getxDirAdj() {
        return xDirAdj;
    }

    public void setxDirAdj(double xDirAdj) {
        this.xDirAdj = xDirAdj;
    }

    public double getyDirAdj() {
        return yDirAdj;
    }

    public void setyDirAdj(double yDirAdj) {
        this.yDirAdj = yDirAdj;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public double getxScale() {
        return xScale;
    }

    public void setxScale(double xScale) {
        this.xScale = xScale;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidthOfSpace() {
        return widthOfSpace;
    }

    public void setWidthOfSpace(double widthOfSpace) {
        this.widthOfSpace = widthOfSpace;
    }

    public double getWidthDirAdj() {
        return widthDirAdj;
    }

    public void setWidthDirAdj(double widthDirAdj) {
        this.widthDirAdj = widthDirAdj;
    }

    public char getUnicode() {
        return unicode;
    }

    public void setUnicode(char unicode) {
        this.unicode = unicode;
    }

    @Override
    public String toString() {
        return "ChartCharacter{" +
                "xDirAdj=" + xDirAdj +
                ", yDirAdj=" + yDirAdj +
                ", fontSize=" + fontSize +
                ", xScale=" + xScale +
                ", height=" + height +
                ", widthOfSpace=" + widthOfSpace +
                ", widthDirAdj=" + widthDirAdj +
                ", unicode=" + unicode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChartCharacter that = (ChartCharacter) o;

        if (Double.compare(that.xDirAdj, xDirAdj) != 0) return false;
        if (Double.compare(that.yDirAdj, yDirAdj) != 0) return false;
        if (Double.compare(that.fontSize, fontSize) != 0) return false;
        if (Double.compare(that.xScale, xScale) != 0) return false;
        if (Double.compare(that.height, height) != 0) return false;
        if (Double.compare(that.widthOfSpace, widthOfSpace) != 0) return false;
        if (Double.compare(that.widthDirAdj, widthDirAdj) != 0) return false;
        return unicode == that.unicode;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(xDirAdj);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yDirAdj);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(fontSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(xScale);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(widthOfSpace);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(widthDirAdj);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) unicode;
        return result;
    }
}
