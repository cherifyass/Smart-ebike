package com.esir.si.smarte_bike.json;


import java.util.ArrayList;
import java.util.List;

public class JsonModel {

    private String sessionId;
    private List<Trip> trajetsList;


    public JsonModel() {
        this.sessionId = "SessionIdTestYupii";
        this.trajetsList = new ArrayList<Trip>();
    }

    public List<Trip> getTrajetsList() {
        return trajetsList;
    }

    public void setTrajetsList(List<Trip> trajetsList) {
        this.trajetsList = trajetsList;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}

