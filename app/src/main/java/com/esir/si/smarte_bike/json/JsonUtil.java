package com.esir.si.smarte_bike.json;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

     private static final String FILENAME = "data.json";

    /**
     * Ecrit dans le fichier FILENAME le nouvel itinéraire
     * Si le fichier contient déja des itinéraires, les conserve et ajoute le nouveau
     * @param c : context de l'application
     * @param newItineraire : nouvel itinéraire à ajouter
     */
    public static void write(Context c, MyItineraire newItineraire){
        File f = getOrCreateFile(c);
        List<MyItineraire> list = new ArrayList<MyItineraire>();
        if( f.length() != 0){ // si le fichier contient déja des itinéraires, lit le fichier
             list = JsonUtil.read(c);
        }
        list.add(newItineraire);

        new EcrireAsyncTask().execute(f,list);
    }

    /**
     * Lit le fichier FILENAME et retourne une liste des itinéraires contenus dans le fichier
     * @param c : context de l'application
     * @return list des itinéraires
     */
    public static List<MyItineraire> read(Context c){
        File f = getOrCreateFile(c);
        List<MyItineraire> list = null;
        if( f.length() != 0) {
            try {
                list = new LireAsyncTask().execute(f).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Supprime entierement le fichier
     * @param c
     */
    public static void removeHistory(Context c){
        File root = new File(c.getFilesDir().getAbsolutePath());
        if( root.exists() ){
            File f = new File(c.getFilesDir().getAbsolutePath() + File.separator + FILENAME);
            if( f.exists() ){
                f.delete();
            }
        }
    }


    /**
     * Récupère le fichier FILENAME où sont stocker les itinéraires.
     * Si le fichier n'existe pas, en créer un nouveau
     * @param c : context de l'application
     * @return le fichier correspondant à FILENAME
     */
    private static File getOrCreateFile(Context c){
        File root = new File(c.getFilesDir().getAbsolutePath());
        if( !root.exists()){
            Log.d("JSONUTIL", "Création du répertoire : " + root.getPath() );
            root.mkdirs();
        }
        File f = new File(c.getFilesDir().getAbsolutePath() + File.separator + FILENAME);
        if( !f.exists()){ //fichier n'existe pas, on le crée
            Log.d("JSONUTIL", "Création du fichier : " + f.getPath() );
            f = new File(c.getFilesDir().getAbsolutePath() + File.separator,FILENAME);
            try {
                boolean b = f.createNewFile();
                Log.d("JSONUTIL", "File created: " + b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    /**
     * ASYNCTASK pour écrire dans le fichier via le JSONModel
     */
    private static class EcrireAsyncTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            File path = (File) params[0];
            List<MyItineraire> list_itineraire = ( List<MyItineraire>)params[1];

            JsonModel model = new JsonModel(list_itineraire);

            FileWriter writer = null;
            try {
                writer = new FileWriter(path);
                writer.append(model.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * ASYNCTASK pour lire depuis le fichier via le JSONModel
     * Retourne une liste d'itinéraires
     */
    private static class LireAsyncTask extends AsyncTask<File, Void, List<MyItineraire>> {

        @Override
        protected List<MyItineraire> doInBackground(File... params) {
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

            List<MyItineraire> myItineraireList = JsonModel.fillMyItineraireListFromJsonObject(jsonObject);

            return myItineraireList;
        }
    }
}