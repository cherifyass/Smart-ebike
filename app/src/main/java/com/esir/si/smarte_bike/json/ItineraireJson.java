package com.esir.si.smarte_bike.json;

import java.util.Date;

public class ItineraireJson {
    private Date itinDate;
    private Endroit depart;
    private Endroit arrivee;
    private double distance;
    private Duree duree;
    private double vitesseMoy;
    private double vitesseMax;
    private double calories;
    private double altitudeMin;
    private double altitudeMax;

    public ItineraireJson(Date itinDate, Endroit depart, Endroit arrivee, double distance, Duree duree, double vitesseMoy, double vitesseMax, double calories, double altitudeMin, double altitudeMax) {
        this.itinDate = itinDate;
        this.depart = depart;
        this.arrivee = arrivee;
        this.distance = distance;
        this.duree = duree;
        this.vitesseMoy = vitesseMoy;
        this.vitesseMax = vitesseMax;
        this.calories = calories;
        this.altitudeMin = altitudeMin;
        this.altitudeMax = altitudeMax;
    }

    public Date getItinDate() {
        return itinDate;
    }

    public void setItinDate(Date itinDate) {
        this.itinDate = itinDate;
    }

    public Endroit getDepart() {
        return depart;
    }

    public void setDepart(Endroit depart) {
        this.depart = depart;
    }

    public Endroit getArrivee() {
        return arrivee;
    }

    public void setArrivee(Endroit arrivee) {
        this.arrivee = arrivee;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Duree getDuree() {
        return duree;
    }

    public void setDuree(Duree duree) {
        this.duree = duree;
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
}
