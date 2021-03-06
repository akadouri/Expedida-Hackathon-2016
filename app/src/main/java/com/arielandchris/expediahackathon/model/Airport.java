package com.arielandchris.expediahackathon.model;

/**
 * Created by Chris on 1/30/2016.
 */
public class Airport implements Comparable<Airport>{
    private String code;
    private double lat, lng;
    private double origDistance;
    private String airportName;
    private String cityName;
    public Airport(String key, String lat, String lng) {
        this(key, Double.parseDouble(lat), Double.parseDouble(lng));
    }
    public Airport(String code, double lat, double lng) {
        this.code = code;
        this.lat = lat;
        this.lng = lng;
    }
    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }
    public String getAirportName() {
        return this.airportName;
    }
    public String getCityName() { return this.cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public double dist() {
        return Math.sqrt(Math.pow(this.lat,2) + Math.pow(this.lng,2));
    }

    public int compareTo(Airport other) {
        return (int)(this.origDistance - other.getOrigDist());
    }
    public boolean equals(Airport other) { return (this.code.equals(other.code)); }
    public double getLat() {
        return this.lat;
    }
    public double getLng() {
        return this.lng;
    }
    public String getCode() {
        return this.code;
    }
    public double getOrigDist() {
        return this.origDistance;
    }
    public void setOrigDist(double lat1, double lng1, double lat2, double lng2) {
        lat1 = Math.toRadians(lat1);
        lng1 = Math.toRadians(lng1);
        lat2 = Math.toRadians(lat2);
        lng2 = Math.toRadians(lng2);
        double dlat = lat2 - lat1;
        double dlng = lng2 - lng1;
        double r = 3961.0; // Radius for earth in miles
        double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlng/2),2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double d = r * c;
        this.origDistance = d;
    }

    //public latlngToMiles() {
     //   double a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2
      //  c = 2 * atan2( sqrt(a), sqrt(1-a) )
       // d = R * c (where R is the radius of the Earth)
      //          R = 3961
    //}
}