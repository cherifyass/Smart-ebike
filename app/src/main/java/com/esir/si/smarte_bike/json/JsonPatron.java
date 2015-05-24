package com.esir.si.smarte_bike.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonPatron {

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
