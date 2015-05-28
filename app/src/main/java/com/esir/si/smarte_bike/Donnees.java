package com.esir.si.smarte_bike;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esir.si.smarte_bike.meteo.JSONWeatherParser;
import com.esir.si.smarte_bike.meteo.WeatherHttpClient;
import com.esir.si.smarte_bike.meteo.model_donnees.Weather;
import com.esir.si.smarte_bike.sup.Slider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.text.DecimalFormat;

public class Donnees extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = Donnees.class.getSimpleName();

    private Slider slider = null;

    private Button boutonStat = null;
    private Button boutonMeteo = null;
    private Button boutonSante = null;

    private RelativeLayout contenuStat = null;
    private RelativeLayout contenuMeteo = null;
    private RelativeLayout contenuSante = null;

    // Widget Meteo
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView windSpeed;
    private TextView windLab;
    private TextView hum;
    private TextView humLab;
    private ImageView condIcon;
    private Bitmap iconData;

    // Location attributes
    private GoogleApiClient mGoogleApiClient;
    private boolean mWritten = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.donnees, container, false);

        //On récupère le bouton pour cacher/afficher le menu
        boutonStat = (Button) view.findViewById(R.id.boutonStat);
        boutonMeteo = (Button) view.findViewById(R.id.boutonMeteo);
        boutonSante = (Button) view.findViewById(R.id.boutonSante);

        // On récupère le menu
        contenuStat = (RelativeLayout) view.findViewById(R.id.contenuStat);
        contenuMeteo = (RelativeLayout) view.findViewById(R.id.contenuMeteo);
        contenuSante = (RelativeLayout) view.findViewById(R.id.contenuSante);

        // On récupère le layout principal
        slider = (Slider) view.findViewById(R.id.slider);

        //On rajoute un Listener sur le clic du bouton stat
        boutonStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                //On définit le contenu
                slider.setToHide(contenuStat);
                //...pour afficher ou cache le menu
                if (slider.toggle()) {
                    //Si le Slider est ouvert...
                    //... on change le texte en "Cacher"
                    boutonStat.setText("- Mes Statistiques");
                } else {
                    //Sinon on met "Afficher"
                    boutonStat.setText("+ Mes Statistiques");
                }
            }
        });

        boutonMeteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                slider.setToHide(contenuMeteo);
                if (slider.toggle()) {
                    boutonMeteo.setText("- Ma météo");
                } else {
                    boutonMeteo.setText("+ Ma météo");
                }
            }
        });

        boutonSante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                slider.setToHide(contenuSante);

                if (slider.toggle()) {
                    boutonSante.setText("- Ma santé");
                } else {
                    boutonSante.setText("+ Ma santé");
                }
            }
        });

        // add LocationServices API
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (savedInstanceState != null) {
            restoreSavedState(view, savedInstanceState);
            return view;
        }

        /* Widget météo */
        final ConnectivityManager cm = (ConnectivityManager) this.getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // initialisation des views
        initWeatherViews(view);

        // check if device is connected to a network
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            cityText.setText("Impossible de se connecter à internet !\n " +
                    "Veuillez vérifier votre connexion !");
        } else {
            cityText.setText("Chargement de météo ...");
        }

        return view;
    }

    public void initWeatherViews(View view) {
        temp = (TextView) view.findViewById(R.id.temp);
        condIcon = (ImageView) view.findViewById(R.id.condIcon);
        cityText = (TextView) view.findViewById(R.id.cityText);
        condDescr = (TextView) view.findViewById(R.id.condDescr);
        hum = (TextView) view.findViewById(R.id.hum);
        humLab = (TextView) view.findViewById(R.id.humLab);
        windSpeed = (TextView) view.findViewById(R.id.windSpeed);
        windLab = (TextView) view.findViewById(R.id.windLab);
    }

    @Override
    public void onStart() {
        super.onStart();
        // connect the client
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Location services connected");

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        // location param is the new updated location
        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // check if device is connected to a network
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            if (!mWritten) {
                Log.i(TAG, "No internet connection");
                mWritten = true;
            }

            cityText.setText("Impossible de se connecter à internet !\n " +
                    "Veuillez vérifier votre connexion !");
            temp.setText("");
            condDescr.setText("");
            windLab.setText("");
            windSpeed.setText("");
            hum.setText("");
            humLab.setText("");
            condIcon.setImageDrawable(null);
        } else {
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();

            String data;
            data = ((new WeatherHttpClient()).getWeatherData(params[0], params[1]));
            if (data == null)
                return null;

            try {
                weather = JSONWeatherParser.getWeather(data);
                //Log.i(TAG, "Weather [" + data + "]");
                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather == null)
                return;

            if (weather.iconData != null) {
                iconData = weather.iconData;
                condIcon.setImageBitmap(weather.iconData);
                condIcon.setAdjustViewBounds(true);
            }

            cityText.setText(weather.location.getCity() + ", " + weather.location.getCountry());

            double celsius = weather.temperature.getTemp();
            DecimalFormat df = new DecimalFormat("0");
            temp.setText("" + df.format(celsius) + "°");

            condDescr.setText(weather.currentCondition.getDescr());

            humLab.setText("Humidité");
            hum.setText("" + weather.currentCondition.getHumidity() + "%");

            windLab.setText("Vent");
            // conversion mps -> kph
            double kph = weather.wind.getSpeed() * 3.6;
            df = new DecimalFormat("0.#");
            windSpeed.setText("" + df.format(kph) + " km/h");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(boutonMeteo!=null) {
            savedInstanceState.putString("boutonMeteo", boutonMeteo.getText().toString());
            savedInstanceState.putParcelable("iconData", iconData);
            savedInstanceState.putString("temp", temp.getText().toString());
            savedInstanceState.putString("cityText", cityText.getText().toString());
            savedInstanceState.putString("condDescr", condDescr.getText().toString());
            savedInstanceState.putString("hum", hum.getText().toString());
            savedInstanceState.putString("humLab", humLab.getText().toString());
            savedInstanceState.putString("windSpeed", windSpeed.getText().toString());
            savedInstanceState.putString("windLab", windLab.getText().toString());
        }
    }

    public void restoreSavedState(View view, Bundle savedInstanceState) {
        initWeatherViews(view);
        if(savedInstanceState.getString("boutonMeteo")!=null) {
            if (savedInstanceState.getString("boutonMeteo").equals("- Ma météo")) {
                boutonMeteo.performClick();
            }
            condIcon.setImageBitmap((Bitmap) savedInstanceState.getParcelable("iconData"));
            condIcon.setAdjustViewBounds(true);
            temp.setText(savedInstanceState.getString("temp"));
            cityText.setText(savedInstanceState.getString("cityText"));
            condDescr.setText(savedInstanceState.getString("condDescr"));
            hum.setText(savedInstanceState.getString("hum"));
            humLab.setText(savedInstanceState.getString("humLab"));
            windSpeed.setText(savedInstanceState.getString("windSpeed"));
            windLab.setText(savedInstanceState.getString("windLab"));
        }
    }
}
