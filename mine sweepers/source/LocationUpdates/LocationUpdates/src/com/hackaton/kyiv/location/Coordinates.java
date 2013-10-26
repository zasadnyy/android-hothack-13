package com.hackaton.kyiv.location;

/**
 * Created with IntelliJ IDEA.
 * User: Kostya
 * Date: 26.10.13
 * Time: 12:51
 * To change this template use File | Settings | File Templates.
 */

public class Coordinates {
    private final double latitude;
    private final double longitude;

    public Coordinates(double lat, double longt) {
        latitude = lat;
        longitude = longt;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}

