package com.esir.si.smarte_bike.navigation;

/**
 * Created by Quentin on 03/03/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esir.si.smarte_bike.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Map extends Fragment {

    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private boolean update = false; // Update position

    // Define a listener that responds to location updates
    private LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            onLocationChangedActivity(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    // Getting LocationManager object from System Service LOCATION_SERVICE
    private LocationManager locationManager;

    private FragmentActivity myContext;

    private  MapView mv;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(container == null){
            return null;
        }
        Log.d("onCreateViewMap","");
        super.onCreateView(inflater,container,savedInstanceState);
        View view_map = (RelativeLayout) inflater.inflate(R.layout.map_layout, container, false);

        return view_map;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mv = (MapView) getView().findViewById(R.id.map);
        mv.onCreate(savedInstanceState);

        MapsInitializer.initialize(getActivity().getApplicationContext());
        mMap = mv.getMap();

        setUpMap();
    }

    @Override
    public void onResume() {
        mv.onResume();
        super.onResume();

        startLocalisation();
        setUpMap();
    }

    @Override
    public void onPause(){
        super.onPause();
        mv.onPause();

        stopLocalisation();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mv.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }

    private void setUpMap() {
        // Enabling MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Set Location Manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // ClickListener Location Button
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(!update){
                    startLocalisation();
                }else{
                    stopLocalisation();
                }
                return true;
            }
        });

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            onLocationChangedActivity(location);
        }

    }

    private void onLocationChangedActivity(Location location) {
        TextView tvLocation = (TextView) getView().findViewById(R.id.tvmap);

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // Setting latitude and longitude in the TextView tv_location
        tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude + ", Altitude:" + location.getAltitude() + ", Speed:" + location.getSpeed());
    }


    private void startLocalisation(){
        TextView tvFollow = (TextView) getView().findViewById(R.id.tvfollow);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
        tvFollow.setText("Following");
        update = true;
    }

    private void stopLocalisation(){
        TextView tvFollow = (TextView) getView().findViewById(R.id.tvfollow);
        locationManager.removeUpdates(locationListener);
        tvFollow.setText("Not Following");
        update = false;
    }
}