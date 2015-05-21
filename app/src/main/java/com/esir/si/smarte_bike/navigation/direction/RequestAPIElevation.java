package com.esir.si.smarte_bike.navigation.direction;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.esir.si.smarte_bike.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Loann on 04/05/2015.
 */
public class RequestAPIElevation extends AsyncTask<List<Step>,Integer,List<Step>> {

    private Context mycontext;
    private View myview;

    public RequestAPIElevation(Context c, View v){
        mycontext = c;
        myview = v;
    }

    private String computeCoordinates(List<Step> steps){
        String data = "";
        LatLng coord = null;
        for(int i = 0; i < steps.size(); i++ ){
         coord = steps.get(i).getStartLocation();
         data += coord.latitude + "," + coord.longitude;
         if((i + 1) < steps.size()) data += "|";
        }
        return data;
    }

    @Override
    protected List<Step> doInBackground(List<Step>... params) {
        List<Step> steps = params[0];

        //traiter coordonées
        String data_coords = computeCoordinates(steps);
        Log.d("RequestAPIElevation", "data_coords : " + data_coords);

        // build api url
        String API_KEY = mycontext.getString(R.string.API_KEY_SERVER);
        String key = API_KEY;
        String url_string = "https://maps.googleapis.com/maps/api/elevation/json?"
                + "path=" + data_coords
                + "&samples=" + steps.size()
                + "&key=" + key;

        Log.d("RequestAPIElevation", "url : " + url_string);

        HttpURLConnection conn = null;
        List<Float> elevation_data = null;
        try {
            URL url = new URL(url_string);
            conn = (HttpURLConnection) url.openConnection();
            String s = convertStreamToString(conn.getInputStream());
            elevation_data = parse(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("RequestAPIELEVATIO","data elevation : " + elevation_data);
        for(int i = 0; i < steps.size(); i++){
            steps.get(i).setElevation(elevation_data.get(i));

        }

        return steps;
    }

    protected void onPostExecute(List<Step> routes) {

    }

    private String convertStreamToString(final InputStream input) throws Exception {
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            final StringBuffer sBuf = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
            return sBuf.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                throw e;
            }
        }
    }

    String RESULTS = "results";
    String ELEVATION = "elevation";

    public List<Float> parse(String routesJSONString) throws Exception {
        try {
            List<Float> elevationList = new LinkedList<>();
            final JSONObject jSONObject = new JSONObject(routesJSONString);

            JSONArray arrayResult = jSONObject.getJSONArray(RESULTS);
            for (int m = 0; m < arrayResult.length(); m++) {
                JSONObject currentResult = arrayResult.getJSONObject(m);
                elevationList.add(Float.parseFloat(currentResult.getString(ELEVATION)));
            }
            return elevationList;
        } catch (Exception e) {
            throw e;
        }
    }
}
