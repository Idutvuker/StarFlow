package com.example.nick.starflow.databases.types;

/**
 * Created by Nick on 26.06.2017.
 */

public final class City
{
    public String name;
    public float lon;
    public float lat;
    public String county;

    public City(String name, float lat, float lon, String county) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.county = county;
    }
}