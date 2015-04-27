package com.esir.si.smarte_bike.navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.esir.si.smarte_bike.R;
import com.esir.si.smarte_bike.navigation.direction.Distance;
import com.esir.si.smarte_bike.navigation.direction.Route;
import com.esir.si.smarte_bike.navigation.direction.Step;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Loann on 27/04/2015.
 */
public class Navigation extends Activity implements OnMapReadyCallback {

    private MapFragment mapFragment;
    private GoogleMap map;
    private Route route = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent extras = this.getIntent();

        setContentView(R.layout.activity_navigation);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_navigation);
        mapFragment.getMapAsync(this);
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
        PolylineOptions polylineOptions = new PolylineOptions();

        List<Step> steps = route.getLegs().get(0).getSteps();

        for(int i=0; i<steps.size(); i++){
            polylineOptions.addAll(steps.get(i).getPoints());
        }
        //polylineOptions.addAll(route.getOverviewPolyLine());

        polylineOptions.color(Color.BLUE);
        polylineOptions.width(4);
        map.addPolyline(polylineOptions);
    }
}