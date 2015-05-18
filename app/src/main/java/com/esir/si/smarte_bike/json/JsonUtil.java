package com.esir.si.smarte_bike.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

    public static String toJson(JsonModel im) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("sessionId", im.getSessionId());

            JSONArray trajetListJsonArr = new JSONArray();
            for(Trip ut : im.getTrajetsList()) {
                JSONObject utObj = new JSONObject();
                utObj.put("origin", ut.getOrigin());
                utObj.put("destination", ut.getDestination());
                utObj.put("tripDate", ut.getTripDate());
                trajetListJsonArr.put(utObj);
            }
            jsonObj.put("trips", trajetListJsonArr);

            return jsonObj.toString();

        } catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
