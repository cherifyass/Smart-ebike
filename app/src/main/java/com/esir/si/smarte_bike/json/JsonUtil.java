package com.esir.si.smarte_bike.json;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonUtil {

    public static String booleanToString(boolean b) {
        if(b == true) {
            return "true";
        }else {
            return "false";
        }
    }

    public static String dateToString(Date d) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String reportDate = df.format(d);
        return reportDate;
    }

    public static String toJsonString(JsonModel jsonModel) {
        try {
            JSONObject root = new JSONObject();

            //recuperer sessionId
            root.put("session_id", jsonModel.getSessionId());
            //fin recuperer sessionId

            //recuperer ebikeUserList
            JSONArray ebikeUserListArr = new JSONArray();
            for(EBikeUser ebu : jsonModel.getEbikeUserList()) {
                JSONObject ebuo = new JSONObject();
                ebuo.put("login", ebu.getLogin());
                ebuo.put("mdp", ebu.getMdp());
                ebuo.put("nom", ebu.getNom());
                ebuo.put("prenom", ebu.getPrenom());
                ebuo.put("sexe", ebu.getSexe());
                ebuo.put("age", ebu.getAge());
                ebuo.put("poids", ebu.getPoids());
                ebuo.put("adresse", ebu.getAdresse());
                ebuo.put("lieu", ebu.getLieu());

                ebikeUserListArr.put(ebuo);
            }
            root.put("ebike_user_list", ebikeUserListArr);
            //fin recuperer ebikeUserList

            //recuperer ebikeBatteryList
            JSONArray ebikeBatteryListArr = new JSONArray();
            for(EBikeBattery ebb : jsonModel.getEbikeBatteryList()) {
                JSONObject ebbo = new JSONObject();
                ebbo.put("bool", booleanToString(ebb.isBool()));
                ebbo.put("niveau", ebb.getNiveau());
                ebbo.put("derniereRecharge", dateToString(ebb.getDerniereRecharge()));

                ebikeBatteryListArr.put(ebb);
            }
            root.put("ebike_battery_list", ebikeBatteryListArr);
            //fin recuperer ebikeBatteryList

            //recuperer itineraireList
            JSONArray itineraireListArr = new JSONArray();
            for(ItineraireJson ijm : jsonModel.getItineraireList()) {
                JSONObject ijmo = new JSONObject();

                ijmo.put("date", dateToString(ijm.getItinDate()));

                JSONArray departArr = new JSONArray();
                Endroit e = ijm.getDepart();
                JSONObject depo = new JSONObject();
                depo.put("lat", e.getE_lat());
                depo.put("long", e.getE_long());
                depo.put("text", e.getE_text());
                departArr.put(depo);
                ijmo.put("depart", departArr);

                JSONArray arriveeArr = new JSONArray();
                Endroit ea = ijm.getArrivee();
                JSONObject arro = new JSONObject();
                arro.put("lat", ea.getE_lat());
                arro.put("long", ea.getE_long());
                arro.put("text", ea.getE_text());
                arriveeArr.put(arro);
                ijmo.put("arrivee", arriveeArr);

                ijmo.put("distance", String.valueOf(ijm.getDistance()));

                JSONArray dureeArr = new JSONArray();
                Duree d = ijm.getDuree();
                JSONObject d_o = new JSONObject();
                d_o.put("h", d.getH());
                d_o.put("m", d.getM());
                d_o.put("s", d.getS());
                dureeArr.put(d_o);
                ijmo.put("duree", dureeArr);

                ijmo.put("vitesse_moy", String.valueOf(ijm.getVitesseMoy()));

                ijmo.put("vitesse_max", String.valueOf(ijm.getVitesseMax()));

                ijmo.put("calories", String.valueOf(ijm.getCalories()));

                ijmo.put("altitude_min", String.valueOf(ijm.getAltitudeMin()));

                ijmo.put("altitude_max", String.valueOf(ijm.getAltitudeMax()));


                itineraireListArr.put(ijmo);
            }
            root.put("itineraire_list", itineraireListArr);
            //fin recuperer itineraireList


            return root.toString();
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void generateJsonReportOnSD(String reportContent, String reportName) {
        // Get the directory for the user's public documents directory.
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "smart-ebike json reports");
            if (!root.mkdirs()) {
                Log.e("json err", "Directory not existed. Creating new one.");
                root.mkdirs();
            }

            File file = new File(root, reportName);
            FileWriter writer = new FileWriter(file);

            writer.append(reportContent);
            writer.flush();
            writer.close();
            //Toast.makeText(this, "Report saved to SD Card", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
