package com.esir.si.smarte_bike.json;


import java.util.ArrayList;
import java.util.List;

public class JsonModel {

    private String sessionId;
    private List<EBikeUser> ebikeUserList;
    private List<EBikeBattery> ebikeBatteryList;
    private List<ItineraireJson> itineraireList;


    public JsonModel() {
        this.sessionId = "TestYupii";
        this.ebikeUserList = new ArrayList<EBikeUser>();
        this.ebikeBatteryList = new ArrayList<EBikeBattery>();
        this.itineraireList = new ArrayList<ItineraireJson>();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<EBikeUser> getEbikeUserList() {
        return ebikeUserList;
    }

    public void setEbikeUserList(List<EBikeUser> ebikeUserList) {
        this.ebikeUserList = ebikeUserList;
    }

    public List<EBikeBattery> getEbikeBatteryList() {
        return ebikeBatteryList;
    }

    public void setEbikeBatteryList(List<EBikeBattery> ebikeBatteryList) {
        this.ebikeBatteryList = ebikeBatteryList;
    }

    public List<ItineraireJson> getItineraireList() {
        return itineraireList;
    }

    public void setItineraireList(List<ItineraireJson> itineraireList) {
        this.itineraireList = itineraireList;
    }
}

