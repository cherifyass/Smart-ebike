package com.esir.si.smarte_bike.json;

import java.util.Date;

public class MyItineraire {

    private int dateJour;
    private int dateMois;
    private int dateAnnee;
    private int dateH;
    private int dateM;

    private double depLat;
    private double depLong;
    private String depText;

    private double arrLat;
    private double arrLong;
    private String arrText;

    private int dureeH;
    private int dureeM;
    private int dureeS;

    private double distance;
    private double vitesseMoy;
    private double vitesseMax;
    private double calories;
    private double altitudeMin;
    private double altitudeMax;


    public MyItineraire(int dateJour, int dateMois, int dateAnnee, int dateH,
                        int dateM, double depLat, double depLong, String depText,
                        double arrLat, double arrLong, String arrText, int dureeH,
                        int dureeM, int dureeS, double distance, double vitesseMoy,
                        double vitesseMax, double calories, double altitudeMin,
                        double altitudeMax) {
        this.dateJour = dateJour;
        this.dateMois = dateMois;
        this.dateAnnee = dateAnnee;
        this.dateH = dateH;
        this.dateM = dateM;
        this.depLat = depLat;
        this.depLong = depLong;
        this.depText = depText;
        this.arrLat = arrLat;
        this.arrLong = arrLong;
        this.arrText = arrText;
        this.dureeH = dureeH;
        this.dureeM = dureeM;
        this.dureeS = dureeS;
        this.distance = distance;
        this.vitesseMoy = vitesseMoy;
        this.vitesseMax = vitesseMax;
        this.calories = calories;
        this.altitudeMin = altitudeMin;
        this.altitudeMax = altitudeMax;
    }

    public double getArrLat() {
        return arrLat;
    }

    public void setArrLat(double arrLat) {
        this.arrLat = arrLat;
    }

    public double getArrLong() {
        return arrLong;
    }

    public void setArrLong(double arrLong) {
        this.arrLong = arrLong;
    }

    public String getArrText() {
        return arrText;
    }

    public void setArrText(String arrText) {
        this.arrText = arrText;
    }

    public int getDateJour() {
        return dateJour;
    }

    public void setDateJour(int dateJour) {
        this.dateJour = dateJour;
    }

    public int getDateMois() {
        return dateMois;
    }

    public void setDateMois(int dateMois) {
        this.dateMois = dateMois;
    }

    public int getDateAnnee() {
        return dateAnnee;
    }

    public void setDateAnnee(int dateAnnee) {
        this.dateAnnee = dateAnnee;
    }

    public int getDateH() {
        return dateH;
    }

    public void setDateH(int dateH) {
        this.dateH = dateH;
    }

    public int getDateM() {
        return dateM;
    }

    public void setDateM(int dateM) {
        this.dateM = dateM;
    }

    public double getDepLat() {
        return depLat;
    }

    public void setDepLat(double depLat) {
        this.depLat = depLat;
    }

    public double getDepLong() {
        return depLong;
    }

    public void setDepLong(double depLong) {
        this.depLong = depLong;
    }

    public String getDepText() {
        return depText;
    }

    public void setDepText(String depText) {
        this.depText = depText;
    }

    public int getDureeH() {
        return dureeH;
    }

    public void setDureeH(int dureeH) {
        this.dureeH = dureeH;
    }

    public int getDureeM() {
        return dureeM;
    }

    public void setDureeM(int dureeM) {
        this.dureeM = dureeM;
    }

    public int getDureeS() {
        return dureeS;
    }

    public void setDureeS(int dureeS) {
        this.dureeS = dureeS;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getVitesseMoy() {
        return vitesseMoy;
    }

    public void setVitesseMoy(double vitesseMoy) {
        this.vitesseMoy = vitesseMoy;
    }

    public double getVitesseMax() {
        return vitesseMax;
    }

    public void setVitesseMax(double vitesseMax) {
        this.vitesseMax = vitesseMax;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getAltitudeMin() {
        return altitudeMin;
    }

    public void setAltitudeMin(double altitudeMin) {
        this.altitudeMin = altitudeMin;
    }

    public double getAltitudeMax() {
        return altitudeMax;
    }

    public void setAltitudeMax(double altitudeMax) {
        this.altitudeMax = altitudeMax;
    }

    @Override
    public String toString() {
        return "MyItineraire{" +
                "dateJour=" + dateJour +
                ", dateMois=" + dateMois +
                ", dateAnnee=" + dateAnnee +
                ", dateH=" + dateH +
                ", dateM=" + dateM +
                ", depLat=" + depLat +
                ", depLong=" + depLong +
                ", depText='" + depText + '\'' +
                ", arrLat=" + arrLat +
                ", arrLong=" + arrLong +
                ", arrText='" + arrText + '\'' +
                ", dureeH=" + dureeH +
                ", dureeM=" + dureeM +
                ", dureeS=" + dureeS +
                ", distance=" + distance +
                ", vitesseMoy=" + vitesseMoy +
                ", vitesseMax=" + vitesseMax +
                ", calories=" + calories +
                ", altitudeMin=" + altitudeMin +
                ", altitudeMax=" + altitudeMax +
                '}';
    }
}
