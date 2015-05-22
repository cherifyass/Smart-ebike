package com.esir.si.smarte_bike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.esir.si.smarte_bike.json.Trip;
import com.esir.si.smarte_bike.navigation.Itineraire;


public class Details extends ActionBarActivity {
    public static final String TAG = Details.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //New Itinerary
        ImageButton it = (ImageButton) findViewById(R.id.imageButton2);
        it.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Details.this, Itineraire.class);
                startActivity(intent);
            }
        });

        TextView depart = (TextView) findViewById(R.id.depart);
        TextView arrivee = (TextView) findViewById(R.id.arrivee);
        TextView date = (TextView) findViewById(R.id.date);
        TextView distance = (TextView) findViewById(R.id.distance);

        Trip t = (Trip) getIntent().getParcelableExtra("trip");
        depart.setText(t.getOrigin());
        arrivee.setText(t.getDestination());
        date.setText(t.getDate());
        distance.setText(t.getDistance());
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
