package com.esir.si.smarte_bike;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.esir.si.smarte_bike.json.MyItineraire;


public class Details extends ActionBarActivity {
    public static final String TAG = Details.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView depart = (TextView) findViewById(R.id.depart);
        TextView arrivee = (TextView) findViewById(R.id.arrivee);
        TextView date = (TextView) findViewById(R.id.date);
        TextView distance = (TextView) findViewById(R.id.distance);
        TextView duree = (TextView) findViewById(R.id.duree);
        TextView calories = (TextView) findViewById(R.id.calories);
        TextView vitesseMax = (TextView) findViewById(R.id.vitesseMax);
        TextView vitesseMoy = (TextView) findViewById(R.id.vitesseMoy);


        MyItineraire MyItineraire = (MyItineraire) getIntent().getParcelableExtra("myItineraire");
        //Log.i(TAG, "MyItineraire = " + MyItineraire);
        depart.setText(MyItineraire.getDepText() + "");
        arrivee.setText(MyItineraire.getArrText() + "");
        date.setText(MyItineraire.getDateJour() + "/" + MyItineraire.getDateMois() + "/" + MyItineraire.getDateAnnee());
        distance.setText(MyItineraire.getDistance() + " km");
        duree.setText(MyItineraire.getDureeH() + " : " + MyItineraire.getDureeM() + " : " + MyItineraire.getDureeS());
        calories.setText(MyItineraire.getCalories() + "");
        vitesseMax.setText(MyItineraire.getVitesseMax() + " km/h");
        vitesseMoy.setText(MyItineraire.getVitesseMoy() + " km/h");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
}
