package com.esir.si.smarte_bike.navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esir.si.smarte_bike.R;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Loann on 27/04/2015.
 */
public class Navigation extends Activity implements OnMapReadyCallback {

    private MapFragment mapFragment;
    private GoogleMap map;
    private Route route = null;

    private List<Step> steps_origin = null;
    private LatLngBounds bounds = null;

    // UI
    RelativeLayout nav_bar_top = null;
    RelativeLayout nav_bar_bottom = null;
    TextView value_departure = null;
    TextView value_arrival = null;
    TextView value_distance = null;
    TextView value_duration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_navigation);
        mapFragment.getMapAsync(this);

        value_departure = (TextView)(this.findViewById(R.id.value_departure));
        value_arrival = (TextView)(this.findViewById(R.id.value_arrival));
        value_distance = (TextView)(this.findViewById(R.id.value_distance));
        value_duration = (TextView)(this.findViewById(R.id.value_duration));

        nav_bar_top = (RelativeLayout)(this.findViewById(R.id.nav_top_bar));
        nav_bar_bottom = (RelativeLayout)(this.findViewById(R.id.nav_bottom_bar));

        // Get extras
        Intent extras = this.getIntent();
        if(extras.getExtras() != null){
            value_departure.setText(extras.getExtras().getString("START_ADDRESS"));
            value_arrival.setText(extras.getExtras().getString("END_ADDRESS"));
            value_distance.setText("("+extras.getExtras().getString("DISTANCE")+")");
            value_duration.setText(extras.getExtras().getString("DURATION"));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        // center camera
        Location location = map.getMyLocation();
        if (location != null) {
            LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,17));
        }

        // get route
        route = Itineraire.getRoutes().get(0);

        // draw route
        if( route != null ){
            drawRoute();
        }
    }

    public void drawRoute(){
        Log.d("DrawRoute", "draw route : " + route);
        steps_origin = route.getLegs().get(0).getSteps();

        // Add polyline
        PolylineOptions polylineOptions = new PolylineOptions();
        for(int i=0; i<steps_origin.size(); i++){
            List<LatLng> points = steps_origin.get(i).getPoints();
            polylineOptions.addAll(points);
        }

        // Draw polyline
        polylineOptions.color(Color.parseColor("#ff1f8fe5"));
        polylineOptions.width(10);
        map.addPolyline(polylineOptions);

        // Draw marker
        LatLng start_address = route.getLegs().get(0).getStartLocation();
        LatLng end_address = route.getLegs().get(0).getEndLocation();
        Log.d("DrawRoute","end :" + end_address.toString() + " start: " + start_address.toString());

        Marker start_marker = map.addMarker(new MarkerOptions()
                .position(start_address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        Marker end_marker = map.addMarker(new MarkerOptions()
                .position(end_address)
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Center camera
        LatLngBounds.Builder bounds_builder = new LatLngBounds.Builder();
        bounds_builder.include(new LatLng(route.getBounds().getNorthEast().latitude,route.getBounds().getNorthEast().longitude));
        bounds_builder.include(new LatLng(route.getBounds().getSouthWest().latitude,route.getBounds().getSouthWest().longitude));
        bounds = bounds_builder.build();
        Log.d("DrawRoute","Bounds :" + bounds.toString());
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,mapFragment.getView().getWidth(),mapFragment.getView().getHeight(),40));
    }

    public void stopNavigation(View view){
        Intent intent = new Intent(this,Itineraire.class);
        this.startActivity(intent);
        this.finish();
    }

    public void startNavigation(View view){

    }
}