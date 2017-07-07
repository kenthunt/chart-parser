# Chart Parser
Parses horse racing result charts into JSON/CSV/Java...

## TL;DR

When given an Equibase result chart PDF file e.g. 

![sample-chart-img](https://i.imgur.com/jQtP1Dw.png)

`chart-parser` can turn it into machine-readable formats, like JSON, e.g.

![sample-json](https://i.imgur.com/hqtJpqb.png)

or CSV, e.g.

![sample-csv](https://i.imgur.com/ZHIIaJd.png)

or even to be used as code in an SDK:

![sample-code](https://i.imgur.com/yAWpxgG.png)

## How it works

PDFs are parsed using a customized [`PDFTextStripper`](https://pdfbox.apache.org/docs/2.0.3/javadocs/org/apache/pdfbox/text/PDFTextStripper.html) instance from [Apache PDFBox](https://pdfbox.apache.org/).

It works by extracting out the x-y position, scale, font size, and unicode value of each character within the PDF file into CSV, and determining the relevant field that text would belong to.

## Highlights

* The entire PDF is parsed; everything you see in the chart can be used, including race conditions, lengths ahead/behind at each point of call, fractional times, wagering payoffs and pools, footnotes etc.

* Full race card PDFs containing multiple races (including those spread over multiple pages) can be parsed.

* An SDK comes out-of-the-box that supports full serialization to and from a JSON API.

* Textual descriptions of race distances are converted to feet e.g. "Six Furlongs" becomes 3,960.

* Values for lengths ahead/behind are converted to decimal formats.

* The software adds additional features such as attempting to lookup the last-raced track details, displaying the day of the week and of the year that a race took place, outlining each medication and equipment used, providing a normalized "X-to-1" odds determination for all wagering payoffs, and calculating estimated individual fractional and splits at each fraction for each starter in a race.

* Thoroughbred, Quarter Horse, Arabian and Mixed breed races are all supported.

* The software handles edge-case scenarios such as dead-heats, walkovers, non-betting races, disqualifications (including adjusting final winning positions), cancellations, claiming price information etc.

## How to use

[Handycapper](https://github.com/robinhowlett/handycapper) is provided as a sample application to parse and convert PDF charts to/from JSON.

Parsing a PDF file is simple and can be done in one-line e.g.:

```java
List<RaceResult> raceResults = ChartParser.create().parse(Paths.get("ARP_2016-07-24_race-charts.pdf").toFile());
```

## Compiling

***IMPORTANT:*** This project relies on enabling [the Java 8 method parameter reflection feature (`-parameters`)](https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html) in your JVM settings e.g. 

![intellij-settings](https://i.imgur.com/8S89Byp.png)

`chart-parser` is a [Maven-based](https://maven.apache.org/) Java open-source project. Running `mvn clean install` will compile the code, run all tests, and install the built artificat to the local repository.

## Notes

This software is open-source and released under the [MIT License](https://github.com/robinhowlett/chart-parser/blob/master/LICENSE). 

This project contains [a single sample Equibase PDF chart](https://github.com/robinhowlett/chart-parser/blob/master/src/test/resources/ARP_2016-07-24_race-charts.pdf) included for testing, educational and demonstration purposes only.

It is recommended users of this software be aware of the conditions on the PDF charts that may apply.
