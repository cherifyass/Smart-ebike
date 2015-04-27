package com.esir.si.smarte_bike.navigation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.esir.si.smarte_bike.R;
import com.esir.si.smarte_bike.navigation.direction.Route;
import com.esir.si.smarte_bike.navigation.direction.Step;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by Loann on 27/04/2015.
 */
public class Navigation extends Activity implements OnMapReadyCallback {

    private MapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_navigation);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }

    public void drawRoute(List<Route> routes){
        Log.d("DrawRoute", "draw route : " + routes);
        PolylineOptions polylineOptions = new PolylineOptions();

        List<Step> steps = routes.get(0).getLegs().get(0).getSteps();

        for(int i=0; i<steps.size(); i++){
            polylineOptions.addAll(steps.get(i).getPoints());
        }
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(2);
        map.addPolyline(polylineOptions);
    }
}