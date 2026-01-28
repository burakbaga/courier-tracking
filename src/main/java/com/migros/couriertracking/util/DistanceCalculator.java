package com.migros.couriertracking.util;

public interface DistanceCalculator {

    double calculate(double lat1, double lng1,
                     double lat2, double lng2);
}
