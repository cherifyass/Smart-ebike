package com.esir.si.smarte_bike;

import android.content.Context;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import org.json.JSONException;

import java.text.DecimalFormat;

public class Donnees extends Fragment{

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
    private Button refreshBtn;
    private ImageView condIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.donnees, container, false);
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
                if(slider.toggle())
                {
                    //Si le Slider est ouvert...
                    //... on change le texte en "Cacher"
                    boutonStat.setText("- Mes Statistiques");
                }else
                {
                    //Sinon on met "Afficher"
                    boutonStat.setText("+ Mes Statistiques");
                }
            }
        });

        boutonMeteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                slider.setToHide(contenuMeteo);

                if(slider.toggle())
                {
                    boutonMeteo.setText("- Ma météo");
                }else
                {
                    boutonMeteo.setText("+ Ma météo");
                }
            }
        });

        boutonSante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                slider.setToHide(contenuSante);

                if(slider.toggle())
                {
                    boutonSante.setText("- Ma santé");
                }else
                {
                    boutonSante.setText("+ Ma santé");
                }
            }
        });

        /* Widget météo */
        final ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // TODO: Récupérer la ville à partir de l'activité Maps
        final String city = "Rennes,FR";
        final String lang = "fr";

        // initialisation des views
        temp = (TextView) view.findViewById(R.id.temp);
        condIcon = (ImageView) view.findViewById(R.id.condIcon);
        cityText = (TextView) view.findViewById(R.id.cityText);

        // check if device is connected to a network
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
            cityText.setText("Impossible de se connecter à internet !\n " +
                    "Veuillez vérifier votre connexion !");
        else
            cityText.setText("Chargement de météo ...");

        condDescr = (TextView) view.findViewById(R.id.condDescr);
        hum = (TextView) view.findViewById(R.id.hum);
        humLab = (TextView) view.findViewById(R.id.humLab);
        windSpeed = (TextView) view.findViewById(R.id.windSpeed);
        windLab = (TextView) view.findViewById(R.id.windLab);
        refreshBtn = (Button) view.findViewById(R.id.refreshButton);

        // bouton actualiser
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {
                    JSONWeatherTask task = new JSONWeatherTask();
                    task.execute(new String[]{city, "fr"});
                } else {
                    cityText.setText("Impossible de se connecter à internet !\n " +
                            "Veuillez vérifier votre connexion !");
                    temp.setText("");
                    condDescr.setText("");
                    windLab.setText("");
                    windSpeed.setText("");
                    hum.setText("");
                    humLab.setText("");
                    condIcon.setImageDrawable(null);
                }
            }
        });

        if(ni != null) {
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{city, lang});
        }

        return view;
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();

            // TODO: Vérifier s'il y a internet ? THIS IS Just WORDKAROUND TO FIX NPE BUG
            String data = null;
            while(data == null)
                data = ((new WeatherHttpClient()).getWeatherData(params[0], params[1]));

            try {
                weather = JSONWeatherParser.getWeather(data);
                System.out.println("Weather [" + weather + "]");
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

            if (weather.iconData != null ) {
                condIcon.setImageBitmap(weather.iconData);
                condIcon.setAdjustViewBounds(true);
            }

            cityText.setText(weather.location.getCity() + ", " + weather.location.getCountry());

            double celsius = weather.temperature.getTemp();
            DecimalFormat df = new DecimalFormat("0");
            temp.setText("" + df.format(celsius)+"°");

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
}
