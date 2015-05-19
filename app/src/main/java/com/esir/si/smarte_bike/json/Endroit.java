package com.esir.si.smarte_bike.json;

public class Endroit {
    private double e_lat;
    private double e_long;
    private String e_text;

    public Endroit() {
        //never used
    }

    public Endroit(double e_lat, double e_long, String e_text) {
        this.e_lat = e_lat;
        this.e_long = e_long;
        this.e_text = e_text;
    }

    public double getE_lat() {
        return e_lat;
    }

    public void setE_lat(double e_lat) {
        this.e_lat = e_lat;
    }

    public double getE_long() {
        return e_long;
    }

    public void setE_long(double e_long) {
        this.e_long = e_long;
    }

    public String getE_text() {
        return e_text;
    }

    public void setE_text(String e_text) {
        this.e_text = e_text;
    }
}
