package com.esir.si.smarte_bike.navigation.direction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.esir.si.smarte_bike.navigation.Itineraire;
import com.esir.si.smarte_bike.navigation.Navigation;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loann on 20/03/2015.
 */
public class RequestAPITask extends AsyncTask<String,Integer,List<Route>> {

    private Context mycontext;
    private View myview;
    private ProgressDialog progressDialog;

    public RequestAPITask(Context c, View v){
        mycontext = c;
        myview = v;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // create progressDialog
        progressDialog = new ProgressDialog(mycontext);
        progressDialog.setMessage("Calcul de l'itinéraire en cours...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<Route> doInBackground(String... params) {
        int count = params.length;
        List<Route> routes = null;

        for (int i = 0; i < count; i++) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(params[i]);
                conn = (HttpURLConnection) url.openConnection();
                String s = convertStreamToString(conn.getInputStream());

                routes = parse(s);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return routes;
    }

    protected void onPostExecute(List<Route> routes) {
        // Dismiss progresDialog
        if(progressDialog.isShowing()) progressDialog.dismiss();

        // transfer routes
        ((Itineraire)mycontext).setRoutes(routes);

        List<Step> steps = routes.get(0).getLegs().get(0).getSteps();
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<steps.size(); i++){
            sb.append(steps.get(i).getHtmlInstructions());
            sb.append("</br>");
            Log.d("ITINERAIRE", ">>>>>> " + steps.get(i).getHtmlInstructions());
        }

        // set routes
        ((Itineraire) mycontext).setRoutes(routes);

        // start navigation activity
        Intent intent = new Intent(mycontext,Navigation.class);
        Leg leg = routes.get(0).getLegs().get(0); // first leg
        intent.putExtra("START_ADDRESS",leg.getStartAddress());
        intent.putExtra("END_ADDRESS",leg.getEndAddress());
        intent.putExtra("DISTANCE",leg.getDistance().getText());
        intent.putExtra("DURATION",leg.getDuration().getText());
        mycontext.startActivity(intent);
        ((Itineraire) mycontext).finish();
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

    // PARSE JSON ITINERARY
    String ROUTES = "routes";
    String SUMMARY = "summary";
    String LEGS = "legs";
    String BOUNDS = "bounds";
    String STEPS = "steps";
    String DISTANCE = "distance";
    String TEXT = "text";
    String DURATION = "duration";
    String VALUE = "value";
    String END_LOCATION = "end_location";
    String START_LOCATION = "start_location";
    String END_ADDRESS = "end_address";
    String START_ADDRESS = "start_address";
    String LATITUDE = "lat";
    String LONGITUDE = "lng";
    String POLYLINE = "polyline";
    String POINTS = "points";
    String HTML_INSTRUCTION = "html_instructions";

    public List<Route> parse(String routesJSONString) throws Exception {
        try {
            List<Route> routeList = new ArrayList<Route>();
            final JSONObject jSONObject = new JSONObject(routesJSONString);
            JSONArray routeJSONArray = jSONObject.getJSONArray(ROUTES);
            Route route;
            JSONObject routesJSONObject;
            for (int m = 0; m < routeJSONArray.length(); m++) {
                route = new Route(/*context*/);
                routesJSONObject = routeJSONArray.getJSONObject(m);

                // bounds
                JSONObject boundsJSONOBJECT = routesJSONObject.getJSONObject(BOUNDS);
                JSONObject northeastJSONOBJECT = boundsJSONOBJECT.getJSONObject("northeast");
                JSONObject southwestJSONOBJECT = boundsJSONOBJECT.getJSONObject("southwest");
                Bound bound = new Bound();
                bound.setNorthEast(new LatLng(northeastJSONOBJECT.getDouble(LATITUDE),northeastJSONOBJECT.getDouble(LONGITUDE)));
                bound.setSouthWest(new LatLng(southwestJSONOBJECT.getDouble(LATITUDE), southwestJSONOBJECT.getDouble(LONGITUDE)));
                route.setBounds(bound);

                JSONArray legsJSONArray;
                route.setSummary(routesJSONObject.getString(SUMMARY));
                legsJSONArray = routesJSONObject.getJSONArray(LEGS);
                JSONObject legJSONObject;
                Leg leg;
                JSONArray stepsJSONArray;


                JSONObject legStartLocationJSONObject;
                LatLng legStartLocationLatLng;
                JSONObject legEndLocationJSONObject;
                LatLng legEndLocationLatLng;

                for (int b = 0; b < legsJSONArray.length(); b++) {
                    leg = new Leg();
                    legJSONObject = legsJSONArray.getJSONObject(b);

                    // start/end location
                    legStartLocationJSONObject = legJSONObject.getJSONObject(START_LOCATION);
                    legStartLocationLatLng = new LatLng(legStartLocationJSONObject.getDouble(LATITUDE), legStartLocationJSONObject.getDouble(LONGITUDE));
                    leg.setStartLocation(legStartLocationLatLng);

                    legEndLocationJSONObject = legJSONObject.getJSONObject(END_LOCATION);
                    legEndLocationLatLng = new LatLng(legEndLocationJSONObject.getDouble(LATITUDE), legEndLocationJSONObject.getDouble(LONGITUDE));
                    leg.setEndLocation(legEndLocationLatLng);

                    // start/end address
                    leg.setStartAddress(legJSONObject.getString(START_ADDRESS));
                    leg.setEndAddress(legJSONObject.getString(END_ADDRESS));

                    leg.setDistance(new Distance(legJSONObject.optJSONObject(DISTANCE).optString(TEXT), legJSONObject.optJSONObject(DISTANCE).optLong(VALUE)));
                    leg.setDuration(new Duration(legJSONObject.optJSONObject(DURATION).optString(TEXT), legJSONObject.optJSONObject(DURATION).optLong(VALUE)));
                    stepsJSONArray = legJSONObject.getJSONArray(STEPS);
                    JSONObject stepJSONObject, stepDurationJSONObject, legPolyLineJSONObject, stepStartLocationJSONObject, stepEndLocationJSONObject;
                    Step step;
                    String encodedString;
                    LatLng stepStartLocationLatLng, stepEndLocationLatLng;
                    int lengthSteps = stepsJSONArray.length();

                    for (int i = 0; i < lengthSteps; i++) {
                        stepJSONObject = stepsJSONArray.getJSONObject(i);
                        step = new Step();
                        JSONObject stepDistanceJSONObject = stepJSONObject.getJSONObject(DISTANCE);
                        step.setDistance(new Distance(stepDistanceJSONObject.getString(TEXT), stepDistanceJSONObject.getLong(VALUE)));
                        stepDurationJSONObject = stepJSONObject.getJSONObject(DURATION);
                        step.setDuration(new Duration(stepDurationJSONObject.getString(TEXT), stepDurationJSONObject.getLong(VALUE)));
                        stepEndLocationJSONObject = stepJSONObject.getJSONObject(END_LOCATION);
                        stepEndLocationLatLng = new LatLng(stepEndLocationJSONObject.getDouble(LATITUDE), stepEndLocationJSONObject.getDouble(LONGITUDE));
                        step.setEndLocation(stepEndLocationLatLng);
                        step.setHtmlInstructions(stepJSONObject.getString(HTML_INSTRUCTION));
                        legPolyLineJSONObject = stepJSONObject.getJSONObject(POLYLINE);
                        encodedString = legPolyLineJSONObject.getString(POINTS);
                        step.setPoints(decodePolyLines(encodedString));
                        stepStartLocationJSONObject = stepJSONObject.getJSONObject(START_LOCATION);
                        stepStartLocationLatLng = new LatLng(stepStartLocationJSONObject.getDouble(LATITUDE), stepStartLocationJSONObject.getDouble(LONGITUDE));
                        step.setStartLocation(stepStartLocationLatLng);
                        leg.addStep(step);
                    }
                    route.addLeg(leg);
                }
                routeList.add(route);
            }
            return routeList;
        } catch (Exception e) {
            throw e;
        }
    }

    private ArrayList<LatLng> decodePolyLines(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
