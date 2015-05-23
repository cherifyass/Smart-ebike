package com.esir.si.smarte_bike.json;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private JsonModel jsonModel;
    private File jsonReportFile;

    public void ecrire(MyItineraire myi) {
        this.ajouterItineraire(myi);
    }

    public void lire(File f) {
        this.lireRapportJson(f);
    }

    public JsonUtil() {
        this.jsonModel = new JsonModel();
        this.jsonReportFile = JsonUtil.createDefaultPath();
    }

    public JsonUtil(JsonModel jsonModel) {
        this.jsonModel = jsonModel;
        this.jsonReportFile = JsonUtil.createDefaultPath();
    }

    public static File createDefaultPath() {
        File root = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "smart-ebike_reports");
        if (!root.mkdirs()) {
            Log.e("json err", "Directory not existed. Creating new one.");
            root.mkdirs();
        }
        return new File(root, "default_report.json");
    }

    /**
     * @param fileName nom du fichier (inclu le nom de son extension)
     * @return
     */
    public static File createCustomPath(String fileName) {
        File root = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "smart-ebike_reports");
        if (!root.mkdirs()) {
            Log.e("json err", "Directory not existed. Creating new one.");
            root.mkdirs();
        }
        return new File(root, fileName);
    }

    public JsonModel getJsonModel() {
        return jsonModel;
    }

    public void setJsonModel(JsonModel jsonModel) {
        this.jsonModel = jsonModel;
    }

    public File getJsonReportFile() {
        return jsonReportFile;
    }

    public void setJsonReportFile(File jsonReportFile) {
        this.jsonReportFile = jsonReportFile;
    }

    /**
     * mettre-à-jour un nouveau itinéraire dans le rapport
     * puis créer un nouveau rapport json dans le répertoire suivant :
     * "/sdcard/Documents/smartebike_reports/"
     * Le contenu du rapport est fabriqué à partir de
     * l'attribut jsonModel de l'objet JsonUtil courant.
     * Le contenu de l'ancien fichier default_report.json sera écrasé.
     * @param nouveauItineraire un nouveau itinéraire
     */
    public void ajouterItineraire(MyItineraire nouveauItineraire) {
        this.getJsonModel().getMyItineraireList().add(nouveauItineraire);
        this.genererRapportJsonDansSDCard();
    }

    public void genererRapportJsonDansSDCard() {
        WriteToSDcardAsyncTask task = new WriteToSDcardAsyncTask();
        task.execute(this);
    }

    /**
     * remplacer le contenu du rapport dans this.jsonModel.toString() par
     * le contenu obtenu à partir de chemin.
     * Puis charger le rapport json dans le menu Historique de l'application
     * depuis le chemin.
     * @param chemin
     * @throws JSONException
     */
    public void lireRapportJson(File chemin) {
        CustomParams params = new CustomParams(this, chemin);
        ReadJsonFileAsyncTask task = new ReadJsonFileAsyncTask();
        task.execute(params);
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private class WriteToSDcardAsyncTask extends AsyncTask<JsonUtil, Void, Void> {

        @Override
        protected Void doInBackground(JsonUtil... params) {
            JsonUtil host = params[0];
            FileWriter writer = null;
            try {
                writer = new FileWriter(host.getJsonReportFile());
                writer.append(host.getJsonModel().toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class ReadJsonFileAsyncTask extends AsyncTask<CustomParams, Void, Void> {

        @Override
        protected Void doInBackground(CustomParams... params) {

            JsonUtil jUtil = params[0].jsonUtil;
            File chemin = params[0].chemin;
            String JSONString = null;
            JSONObject JSONObject = null;
            try {
                //open the inputStream to the file
                /*
                File root = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "smart-ebike_reports");
                if (!root.mkdirs()) {
                    Log.e("json err", "Directory not existed. Creating new one.");
                    root.mkdirs();
                }
                File file = new File(root, "default_report.txt");
                */
                InputStream inputStream = new FileInputStream(chemin);
                int sizeOfJSONFile = inputStream.available();

                //array that will store all the data
                byte[] bytes = new byte[sizeOfJSONFile];

                //reading data into the array from the file
                inputStream.read(bytes);

                //close the input stream
                inputStream.close();

                JSONString = new String(bytes, "UTF-8");
                JSONObject = new JSONObject(JSONString);

            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            catch (JSONException x) {
                x.printStackTrace();
                return null;
            }


            List<MyItineraire> myItineraireList = new ArrayList<MyItineraire>();
            try {
                JSONArray jsonArr = JSONObject.getJSONArray("itineraire_list");
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

            //remplacer le contenu jsonModel de l'objet JsonUtil courant par
            // le contenu JsonModel généré par l'opération parser depuis
            // le fichier json trouvé dans le chemin indiqué
            jUtil.setJsonModel(new JsonModel(myItineraireList));
            //JsonModel jsonModel = new JsonModel(myItineraireList);
            Log.d("json lireRapportJson", jUtil.getJsonModel().toString());

            //commit sur SD card
            jUtil.genererRapportJsonDansSDCard();
            return null;
        }
    }

    private class CustomParams {
        public JsonUtil jsonUtil;
        public File chemin;

        public CustomParams(JsonUtil jsonUtil, File chemin) {
            this.jsonUtil = jsonUtil;
            this.chemin = chemin;
        }
    }


}
