package com.esir.si.smarte_bike.json;

import android.os.AsyncTask;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private JsonModel jsonModel;
    private File pathToJsonFile;
    private static String reportName = "report_3.json";

    public JsonUtil() {

        File root = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "");
        root = new File(root, reportName);
        File parent = root.getParentFile();
        if(!parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
        this.pathToJsonFile = root;
        this.jsonModel = new JsonModel();//contient liste vide des MyItineraire

        //peupler jsonModel
        JSONObject jsonObject = null;
        try {
            InputStream inputStream = new FileInputStream(this.pathToJsonFile);
            int sizeOfJSONFile = inputStream.available();
            byte[] bytes = new byte[sizeOfJSONFile];
            inputStream.read(bytes);
            inputStream.close();
            jsonObject = new JSONObject(new String(bytes, "UTF-8"));
            JSONArray jsonArr = jsonObject.getJSONArray("itineraire_list");
            if (jsonArr.isNull(0)) {
                //le fichier rapport est vide, ou son format n'est pas compatible
                EcrireAsyncTask task = new EcrireAsyncTask();
                task.execute(this.pathToJsonFile, this.jsonModel);
            } else {
                //l'ancien rapport est trouve, faut importer son contenu dans this.jsonModel
                this.lire();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * ajouter un nouveau itineraire dans le rapport
     * @param nouveauItineraire un nouveau itineraire
     */
    public void ecrire(MyItineraire nouveauItineraire) {
        this.getJsonModel().getMyItineraireList().add(nouveauItineraire);
        EcrireAsyncTask task = new EcrireAsyncTask();
        task.execute(this.getPathToJsonFile(), this.getJsonModel());
    }

    /**
     * charger le contenu du rapport dans this.pathToJsonFile
     * dans this.jsonModel,
     * Puis charger le rapport json dans le menu Historique de l'application
     * depuis le chemin. (A FAIRE)
     */
    public void lire() {
        LireAsyncTask lireAsyncTask = new LireAsyncTask(this);
        lireAsyncTask.execute(this.getPathToJsonFile());

//        Puis charger le rapport json dans le menu Historique
// de l'application depuis le chemin. (A FAIRE)
    }

    public void onBackgroundTaskCompleted(JsonModel jm) {
        this.jsonModel = jm;
    }

    public JsonModel getJsonModel() {
        return jsonModel;
    }

    public void setJsonModel(JsonModel jsonModel) {
        this.jsonModel = jsonModel;
    }

    public File getPathToJsonFile() {
        return pathToJsonFile;
    }

    public void setPathToJsonFile(File pathToJsonFile) {
        this.pathToJsonFile = pathToJsonFile;
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

    private class EcrireAsyncTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            File path = (File) params[0];
            JsonModel jsonModel = (JsonModel) params[1];
            FileWriter writer = null;
            try {
                writer = new FileWriter(path);
                writer.append(jsonModel.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class LireAsyncTask extends AsyncTask<File, Void, JsonModel> {
        JsonUtil caller;

        public LireAsyncTask(JsonUtil caller) {
            this.caller = caller;
        }

        @Override
        protected JsonModel doInBackground(File... params) {
            File path = params[0];
            JSONObject jsonObject = null;
            try {
                InputStream inputStream = new FileInputStream(path);
                int sizeOfJSONFile = inputStream.available();
                byte[] bytes = new byte[sizeOfJSONFile];
                inputStream.read(bytes);
                inputStream.close();
                jsonObject = new JSONObject(new String(bytes, "UTF-8"));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<MyItineraire> myItineraireList = new ArrayList<MyItineraire>();
            myItineraireList = JsonPatron.fillMyItineraireListFromJsonObject(jsonObject);

            return new JsonModel(myItineraireList);
        }

        @Override
        protected void onPostExecute(JsonModel jsonModel) {
            caller.onBackgroundTaskCompleted(jsonModel);
        }
    }

}
