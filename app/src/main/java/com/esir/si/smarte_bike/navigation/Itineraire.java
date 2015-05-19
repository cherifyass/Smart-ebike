package com.esir.si.smarte_bike.navigation;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.esir.si.smarte_bike.R;
import com.esir.si.smarte_bike.json.Duree;
import com.esir.si.smarte_bike.json.Endroit;
import com.esir.si.smarte_bike.json.ItineraireJson;
import com.esir.si.smarte_bike.json.JsonModel;
import com.esir.si.smarte_bike.json.JsonUtil;
import com.esir.si.smarte_bike.navigation.autocomplete.PlacesAutoCompleteAdapter;
import com.esir.si.smarte_bike.navigation.direction.RequestAPITask;
import com.esir.si.smarte_bike.navigation.direction.Route;

import java.io.File;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Itineraire extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private AutoCompleteTextView autoCompDepart;
    private AutoCompleteTextView autoCompArrivee;
    private CheckBox cb_maposition;

    private static List<Route> routes;

    public ProgressDialog progressDialog;

    //json report creator
    public static JsonModel globalReport = new JsonModel();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itineraire_layout);

        autoCompDepart = (AutoCompleteTextView) findViewById(R.id.auto_comp_tv_depart);
        autoCompArrivee = (AutoCompleteTextView) findViewById(R.id.auto_comp_tv_arrivee);
        cb_maposition = (CheckBox) findViewById(R.id.cb_myposition);

        autoCompDepart.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
        autoCompDepart.setOnItemClickListener(this);

        autoCompArrivee.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
        autoCompArrivee.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_itineraire, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void computeItinerary(View view){

        String origin = autoCompDepart.getText().toString(); //departure
        String destination = autoCompArrivee.getText().toString(); //arrival

        if("".equals(origin.trim()) && !cb_maposition.isChecked()) {
            Toast.makeText(Itineraire.this, "Merci de saisir un lieu de départ", Toast.LENGTH_SHORT).show();
        }
        else if("".equals(destination.trim())) {
            Toast.makeText(Itineraire.this, "Merci de saisir un lieu d'arrivée", Toast.LENGTH_SHORT).show();
        }
        else {

            if (cb_maposition.isChecked()) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(new Criteria(), true);
                Location location = locationManager.getLastKnownLocation(provider);
                origin = location.getLatitude() + "," + location.getLongitude();
            }
            // TODO: Popup fields empty
            /*if (((origin == null) || (origin == "")) || ((destination == null) || (destination == ""))) {
                Log.d("Error", "Fields empty");
                return;
            }*/

            try {
                String API_KEY = this.getString(R.string.API_KEY_SERVER);
                String mode = "bicycling";
                String region = "fr";
                String language = "fr";
                String key = API_KEY;
                String o = URLEncoder.encode(origin, "utf-8");
                String d = URLEncoder.encode(destination, "utf-8");

                String url = "https://maps.googleapis.com/maps/api/directions/json?"
                        + "origin=" + o
                        + "&destination=" + d
                        + "&region=" + region
                        + "&language=" + language
                        + "&sensor=false&units=metric&mode=" + mode + "&alternatives=true"
                        + "&key=" + key;

                Log.d("URL", url);
                Log.d("Departure", origin);
                Log.d("Arrival", destination);


                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);
                Log.d("TripDate", reportDate);


                //create a json report:

                    //create a new itineraire, then push it to the current list of itineraires:
                    Endroit jDepart = new Endroit(0, 0, origin);
                    Endroit jArrivee = new Endroit(0, 0, destination);
                    double jDistance = 0;
                    Duree jDuree = new Duree(0, 0, 0);
                    double jVitesseMoy = 0;
                    double jVitesseMax = 0;
                    double jCalories = 0;
                    double jAltitudeMin = 0;
                    double jAltitudeMax = 0;
                    ItineraireJson itineraireJson = new ItineraireJson(today, jDepart, jArrivee, jDistance, jDuree, jVitesseMoy, jVitesseMax, jCalories, jAltitudeMin, jAltitudeMax);
                    globalReport.getItineraireList().add(itineraireJson);
                    Log.d("json", JsonUtil.toJsonString(globalReport));
                    //fin

                //fin create a json report

                //write to file in external storage (user's public document directory), in background
                JsonParams jp = new JsonParams(JsonUtil.toJsonString(globalReport), "smartebikejsonreport.txt");
                WriteJsonFileAsyncTask wjfat = new WriteJsonFileAsyncTask();
                wjfat.execute(jp);
                //



                //routes = new RequestAPITask(this,this.findViewById(android.R.id.content)).execute(url).get();
                new RequestAPITask(this, this.findViewById(android.R.id.content)).execute(url);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Route> getRoutes(){
        return routes;
    }

    public static void setRoutes(List<Route> r){
        routes = r;
    }

    private static class JsonParams {
        String report;
        String reportName;

        private JsonParams(String r, String rn) {
            this.report = r;
            this.reportName = rn;
        }
    }

    private class WriteJsonFileAsyncTask extends AsyncTask<JsonParams, Void, Void> {

        @Override
        protected Void doInBackground(JsonParams... params) {
            String globalReport = params[0].report;
            String reportName = params[0].reportName;
            try {
                JsonUtil.generateJsonReportOnSD(globalReport, reportName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
