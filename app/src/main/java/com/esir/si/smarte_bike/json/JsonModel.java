package com.esir.si.smarte_bike.json;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonModel {

    private List<MyItineraire> myItineraireList;

    public JsonModel() {
        this.myItineraireList = new ArrayList<MyItineraire>();
    }

    public JsonModel(List<MyItineraire> myItineraireList) {
        this.myItineraireList = myItineraireList;
    }

    public List<MyItineraire> getMyItineraireList() {
        return myItineraireList;
    }

    public void setMyItineraireList(List<MyItineraire> myItineraireList) {
        this.myItineraireList = myItineraireList;
    }

    /**
     *
     * @return le contenu du fichier json en chaine de caractère
     */
    public String toString() {
        try {
            JSONObject root = new JSONObject();

            JSONArray itineraireArr = new JSONArray();

            for(MyItineraire currentItineraire : this.getMyItineraireList()) {
                JSONObject itineraireObj = new JSONObject();

                JSONObject dateObj = new JSONObject();
                dateObj.put("jour", String.valueOf(currentItineraire.getDateJour()));
                dateObj.put("mois", String.valueOf(currentItineraire.getDateMois()));
                dateObj.put("annee", String.valueOf(currentItineraire.getDateAnnee()));
                dateObj.put("h", String.valueOf(currentItineraire.getDateH()));
                dateObj.put("m", String.valueOf(currentItineraire.getDateM()));
                itineraireObj.put("date", dateObj);


                JSONObject departObj = new JSONObject();
                departObj.put("lat", String.valueOf(currentItineraire.getDepLat()));
                departObj.put("long", String.valueOf(currentItineraire.getDepLong()));
                departObj.put("text", currentItineraire.getDepText());
                itineraireObj.put("depart", departObj);

                JSONObject arriveeObj = new JSONObject();
                arriveeObj.put("lat", String.valueOf(currentItineraire.getArrLat()));
                arriveeObj.put("long", String.valueOf(currentItineraire.getArrLong()));
                arriveeObj.put("text", currentItineraire.getArrText());
                itineraireObj.put("arrivee", arriveeObj);

                JSONObject durObj = new JSONObject();
                durObj.put("h", String.valueOf(currentItineraire.getDureeH()));
                durObj.put("m", String.valueOf(currentItineraire.getDureeM()));
                durObj.put("s", currentItineraire.getDureeS());
                itineraireObj.put("duree", durObj);

                itineraireObj.put("distance", String.valueOf(currentItineraire.getDistance()));

                itineraireObj.put("vitesse_moy", String.valueOf(currentItineraire.getVitesseMoy()));

                itineraireObj.put("vitesse_max", String.valueOf(currentItineraire.getVitesseMax()));

                itineraireObj.put("altitude_min", String.valueOf(currentItineraire.getAltitudeMin()));

                itineraireObj.put("altitude_max", String.valueOf(currentItineraire.getAltitudeMax()));

                itineraireObj.put("calories", String.valueOf(currentItineraire.getCalories()));


                itineraireArr.put(itineraireObj);
            }
            root.put("itineraire_list", itineraireArr);

            return root.toString();
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}

