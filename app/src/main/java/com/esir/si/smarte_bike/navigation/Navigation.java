package com.esir.si.smarte_bike.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esir.si.smarte_bike.R;
import com.esir.si.smarte_bike.json.JsonUtil;
import com.esir.si.smarte_bike.json.MyItineraire;
import com.esir.si.smarte_bike.navigation.direction.Route;
import com.esir.si.smarte_bike.navigation.direction.Step;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Loann on 27/04/2015.
 */
public class Navigation extends Activity implements OnMapReadyCallback {

    private MapFragment mapFragment;
    private GoogleMap map;
    private Location location;
    private static Route route;
    private Polyline polyline = null;

    private static List<Step> steps_origin;
    private LatLngBounds bounds = null;

    private Marker start_marker = null;
    private Marker end_marker = null;

    // UI
    private RelativeLayout nav_bar_top = null;
    private RelativeLayout nav_bar_bottom = null;
    private RelativeLayout nav_bar_bottom_step = null;
    private TextView value_departure = null;
    private TextView value_arrival = null;
    private TextView value_distance = null;
    private TextView value_duration = null;
    private TextView value_instruction = null;
    private TextView value_speed = null;
    private TextView value_arriving_time = null;

    private ImageView nav_img_battery = null;
    private ImageView nav_img_elevation = null;

    private ImageButton arrowNext = null;
    private ImageButton arrowPrevious = null;

    // step by step
    private static int step = -1;
    private static int maxStep = -1;

    // infos
    private static Calendar now;
    private static Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        c = this;
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_navigation);
        mapFragment.getMapAsync(this);

        value_departure = (TextView) (this.findViewById(R.id.value_departure));
        value_arrival = (TextView) (this.findViewById(R.id.value_arrival));
        value_distance = (TextView) (this.findViewById(R.id.value_distance));
        value_duration = (TextView) (this.findViewById(R.id.value_duration));
        value_instruction = (TextView) (this.findViewById(R.id.value_instructions));
        value_speed = (TextView) (this.findViewById(R.id.value_speed));
        value_arriving_time = (TextView) (this.findViewById(R.id.value_arriving_time));

        nav_bar_top = (RelativeLayout) (this.findViewById(R.id.nav_top_bar));
        nav_bar_bottom = (RelativeLayout) (this.findViewById(R.id.nav_bottom_bar));
        nav_bar_bottom_step = (RelativeLayout) (this.findViewById(R.id.nav_bottom_bar_step));

        arrowNext = (ImageButton) (this.findViewById(R.id.nav_btn_next_step));
        arrowPrevious = (ImageButton) (this.findViewById(R.id.nav_btn_previous_step));

        nav_img_battery = (ImageView) (this.findViewById(R.id.nav_img_battery));
        nav_img_elevation = (ImageView) (this.findViewById(R.id.nav_img_elevation));


        // Get extras
        Intent extras = this.getIntent();
        if (extras.getExtras() != null) {
            value_departure.setText(extras.getExtras().getString("START_ADDRESS"));
            value_arrival.setText(extras.getExtras().getString("END_ADDRESS"));
            value_distance.setText("(" + extras.getExtras().getString("DISTANCE") + ")");
            value_duration.setText(extras.getExtras().getString("DURATION"));

            new ImageDownloadAsync(this, this.findViewById(android.R.id.content)).execute(extras.getExtras().getString("URL_ELEV"));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        // center camera
        location = map.getMyLocation();

        // get route
        route = Itineraire.getRoutes().get(0);

        // draw route
        if (route != null) {
            drawRoute();
        }
    }

    /**
     * Draw a preview of the route
     */
    public void drawRoute() {
        steps_origin = route.getLegs().get(0).getSteps();

        // Add polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < steps_origin.size(); i++) {
            List<LatLng> points = steps_origin.get(i).getPoints();
            polylineOptions.addAll(points);
        }

        // Draw polyline
        polylineOptions.color(Color.parseColor("#ff1f8fe5"));
        polylineOptions.width(10);
        polyline = map.addPolyline(polylineOptions);

        // Draw marker
        LatLng start_address = route.getLegs().get(0).getStartLocation();
        LatLng end_address = route.getLegs().get(0).getEndLocation();

        start_marker = map.addMarker(new MarkerOptions()
                .position(start_address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        end_marker = map.addMarker(new MarkerOptions()
                .position(end_address)
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Center camera
        LatLngBounds.Builder bounds_builder = new LatLngBounds.Builder();
        bounds_builder.include(new LatLng(route.getBounds().getNorthEast().latitude, route.getBounds().getNorthEast().longitude));
        bounds_builder.include(new LatLng(route.getBounds().getSouthWest().latitude, route.getBounds().getSouthWest().longitude));
        bounds = bounds_builder.build();

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));
            }
        });
    }

    /**
     * Stop navigation (nav_btn_stop)
     * Stop activity and load Itineraire activity
     *
     * @param view
     */
    public void stopNavigation(View view) {
        Intent intent = new Intent(this, Itineraire.class);
        this.startActivity(intent);
        this.finish();
    }

    /**
     * Start navigation (nav_btn_start)
     *
     * @param view
     */
    public void startNavigation(View view) {
        // remove start marker
        if (start_marker != null) start_marker.remove();

        // hide/show bars
        nav_bar_top.setVisibility(View.INVISIBLE);
        nav_bar_bottom.setVisibility(View.INVISIBLE);
        nav_bar_bottom_step.setVisibility(View.VISIBLE);
        nav_img_elevation.setVisibility(View.INVISIBLE);
        nav_img_elevation.setVisibility(View.INVISIBLE);

        // show ui
        nav_img_battery.setVisibility(View.VISIBLE);
        value_arriving_time.setVisibility(View.VISIBLE);
        value_speed.setVisibility(View.VISIBLE);

        if (location != null) value_speed.setText(location.getSpeed() + ""); // speed

        now = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        now.add(Calendar.SECOND, (int) route.getLegs().get(0).getDuration().getValue());

        String hour_of_day = String.format("%02d",now.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d", now.get(Calendar.MINUTE));
        value_arriving_time.setText(hour_of_day + "h" + minute); // arriving time

        maxStep = steps_origin.size();
        nextStep(view);
    }

    public void nextStep(View view) {
        step++;
        step();
    }

    public void previousStep(View view) {
        step--;
        step();
    }

    /**
     * Set: redraw route, move camera and show instructions for the current step
     */
    private void step() {
        // init arrow button
        arrowNext.setEnabled(true);
        arrowPrevious.setEnabled(true);

        if (step <= 0) {
            arrowPrevious.setEnabled(false);
        } else if (step >= maxStep) {
            arrowNext.setEnabled(false);
        }

        arrowNext.setImageResource((arrowNext.isEnabled()) ? R.drawable.ic_chevron_right_white_36dp : R.drawable.ic_chevron_right_grey600_36dp);
        arrowNext.setAlpha((arrowNext.isEnabled()) ? (float) 1.0 : (float) 0.5);
        arrowPrevious.setImageResource((arrowPrevious.isEnabled()) ? R.drawable.ic_chevron_left_white_36dp : R.drawable.ic_chevron_left_grey600_36dp);
        arrowPrevious.setAlpha((arrowPrevious.isEnabled()) ? (float) 1.0 : (float) 0.5);


        if (step == maxStep) { // end
            polyline.remove();

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(route.getLegs().get(0).getEndLocation(), 17));
            value_instruction.setText(Html.fromHtml("<b>Vous êtes arrivé !</b>"));

            new DialogSaveItineraire().show(getFragmentManager(),"");

        } else {
            Step currentStep = steps_origin.get(step);

            // redraw route
            redrawRoute();
            // set instruction
            value_instruction.setText(Html.fromHtml(currentStep.getHtmlInstructions()));
            // move camera
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentStep.getStartLocation(), 17));
        }
    }

    /**
     * Redraw route with current step
     */
    private void redrawRoute() {
        // remove previous polyline
        polyline.remove();

        // create a new polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = step; i < steps_origin.size(); i++) {
            List<LatLng> points = steps_origin.get(i).getPoints();
            polylineOptions.addAll(points);
        }

        // Draw polyline
        polylineOptions.color(Color.parseColor("#ff1f8fe5"));
        polylineOptions.width(10);
        polyline = map.addPolyline(polylineOptions);
    }

    public static void saveItinerary(){

        int dateJour = now.get(Calendar.DAY_OF_MONTH);
        int dateMois = now.get(Calendar.MONTH);
        int dateAnnee = now.get(Calendar.YEAR);
        int dateH = now.get(Calendar.HOUR_OF_DAY);
        int dateM = now.get(Calendar.MINUTE);
        double depLat = route.getLegs().get(0).getStartLocation().latitude;
        double depLong = route.getLegs().get(0).getStartLocation().longitude;
        String depText = route.getLegs().get(0).getStartAddress();
        double arrLat = route.getLegs().get(0).getEndLocation().latitude;
        double arrLong = route.getLegs().get(0).getEndLocation().longitude;
        String arrText = route.getLegs().get(0).getEndAddress();

        int hours = (int) route.getLegs().get(0).getDuration().getValue() / 3600;
        int remainder = (int) route.getLegs().get(0).getDuration().getValue() - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int dureeH = hours;
        int dureeM = mins;
        int dureeS = secs;

        double _distance = (route.getLegs().get(0).getDistance().getValue() * 0.001);
        double vitesseMoy = getVitesseMoy();
        double vitesseMax = getVitesseMax();
        double calories = getCalories();
        double altitudeMin = getAltitudeMin();
        double altitudeMax = getAltitudeMax();

        MyItineraire itineraire = new MyItineraire(dateJour, dateMois, dateAnnee, dateH, dateM, depLat,
                                                    depLong, depText, arrLat, arrLong, arrText, dureeH,
                                                    dureeM, dureeS, _distance, vitesseMoy, vitesseMax,
                                                    calories, altitudeMin, altitudeMax);
        JsonUtil.write(c,itineraire);
        Log.d("Navigation", "Itinéraire enregistré!");
    }

    private static double getVitesseMoy(){
        return 20.2;
    }
    private static double getVitesseMax(){
        return 26.6;
    }

    private static double getCalories(){
        return 856;
    }

    private static double getAltitudeMin(){
        double res = 0;
        for(Step s : steps_origin){
            if(s.getElevation() > res ) res = s.getElevation();
        }
        return  res;
    }

    private static double getAltitudeMax(){
        double res = steps_origin.get(0).getElevation();
        for(Step s : steps_origin){
            if(s.getElevation() < res ) res = s.getElevation();
        }
        return  res;
    }


}

class ImageDownloadAsync extends AsyncTask<String, Void, Bitmap> {

    private Context mycontext;
    private View myview;

    public ImageDownloadAsync(Context c, View v){
        mycontext = c;
        myview = v;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        if( params.length > 0){
            URL url;
            try {
                url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bmp){
        super.onPostExecute(bmp);
        if(bmp != null){
            ImageView nav_img_elevation = (ImageView) (myview.findViewById(R.id.nav_img_elevation));
            nav_img_elevation.setImageBitmap(bmp);
        }
    }
}

