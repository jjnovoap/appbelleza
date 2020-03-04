package com.example.appbella.Model;

public class LocationHelper {
    private double Longitude;
    private double Latitude;
    private String complements;

    public LocationHelper(double longitude, double latitude, String complements) {
        Longitude = longitude;
        Latitude = latitude;
        this.complements = complements;
    }

    public String getComplements() {
        return complements;
    }

    public void setComplements(String complements) {
        this.complements = complements;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
