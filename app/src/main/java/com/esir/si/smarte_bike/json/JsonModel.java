package com.esir.si.smarte_bike.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonModel {

    private List<MyItineraire> myItineraireList;

    public JsonModel(List<MyItineraire> myItineraireList) {
        this.myItineraireList = myItineraireList;
    }

    public List<MyItineraire> getMyItineraireList() {
        return myItineraireList;
    }

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
                durObj.put("s", String.valueOf(currentItineraire.getDureeS()));
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

    public static List<MyItineraire> fillMyItineraireListFromJsonObject(JSONObject jsonObject) {
        List<MyItineraire> myItineraireList = new ArrayList<MyItineraire>();
        try {
            JSONArray jsonArr = jsonObject.getJSONArray("itineraire_list");
            for(int i=0; i<jsonArr.length();i++) {
                JSONObject itineraireObj = jsonArr.getJSONObject(i);
                JSONObject date = itineraireObj.getJSONObject("date");
                JSONObject depart = itineraireObj.getJSONObject("depart");
                JSONObject arrivee = itineraireObj.getJSONObject("arrivee");
                JSONObject duree = itineraireObj.getJSONObject("duree");
                String distance = itineraireObj.getString("distance");
                String vitesse_moy = itineraireObj.getString("vitesse_moy");
                String vitesse_max = itineraireObj.getString("vitesse_max");
                String altitude_min = itineraireObj.getString("altitude_min");
                String altitude_max = itineraireObj.getString("altitude_max");
                String calories = itineraireObj.getString("calories");
                MyItineraire my = new MyItineraire(Integer.parseInt(date.getString("jour")),
                        Integer.parseInt(date.getString("mois")),
                        Integer.parseInt(date.getString("annee")),
                        Integer.parseInt(date.getString("h")),
                        Integer.parseInt(date.getString("m")),
                        Double.parseDouble(depart.getString("lat")),
                        Double.parseDouble(depart.getString("long")),
                        depart.getString("text"),
                        Double.parseDouble(arrivee.getString("lat")),
                        Double.parseDouble(arrivee.getString("long")),
                        arrivee.getString("text"),
                        Integer.parseInt(duree.getString("h")),
                        Integer.parseInt(duree.getString("m")),
                        Integer.parseInt(duree.getString("s")),
                        Double.parseDouble(distance),
                        Double.parseDouble(vitesse_moy),
                        Double.parseDouble(vitesse_max), Double.parseDouble(altitude_min),
                        Double.parseDouble(altitude_max), Double.parseDouble(calories));
                myItineraireList.add(my);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myItineraireList;
    }


}

