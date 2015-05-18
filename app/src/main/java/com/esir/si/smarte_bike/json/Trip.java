package com.esir.si.smarte_bike.json;

public class Trip {
    private String origin;
    private String destination;
    private String tripDate;

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String date) {
        this.tripDate = date;
    }

    //pr le moment, on s'arrete ici
    //List<route>, et des sous-informations trouvées dans folder direction (Bound, Distance etc...)

    public Trip(String origin, String destination, String date) {
        this.origin = origin;
        this.destination = destination;
        this.tripDate = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
