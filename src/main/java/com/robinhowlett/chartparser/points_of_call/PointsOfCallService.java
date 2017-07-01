package com.robinhowlett.chartparser.points_of_call;

import com.robinhowlett.chartparser.charts.pdf.Breed;
import com.robinhowlett.chartparser.charts.pdf.DistanceSurfaceTrackRecord.RaceDistance;

import java.util.ArrayList;

/**
 * Gets the {@link PointsOfCall}s for a particular race distance from the
 * {@link PointsOfCallRepository}
 */
public class PointsOfCallService {

    private PointsOfCallRepository repository;

    public PointsOfCallService(PointsOfCallRepository repository) {
        this.repository = repository;
    }

    public PointsOfCall getPointsOfCallForDistance(Breed breed, RaceDistance raceDistance) {
        PointsOfCallTreeSet pointsOfCall = repository.findByBreed(breed, raceDistance.getText());
        return pointsOfCall.floor(createForFloor(raceDistance.getValue()));
    }

    private static PointsOfCall createForFloor(int pointOfCallInFeet) {
        return new PointsOfCall("", pointOfCallInFeet, new ArrayList<>());
    }
}
