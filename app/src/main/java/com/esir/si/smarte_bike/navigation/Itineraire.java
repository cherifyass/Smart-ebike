package com.esir.si.smarte_bike.navigation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.esir.si.smarte_bike.R;
import com.esir.si.smarte_bike.navigation.autocomplete.PlacesAutoCompleteAdapter;

public class Itineraire extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private AutoCompleteTextView autoCompDepart;
    private AutoCompleteTextView autoCompArrivee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itineraire_layout);

        autoCompDepart = (AutoCompleteTextView) findViewById(R.id.auto_comp_tv_depart);
        autoCompArrivee = (AutoCompleteTextView) findViewById(R.id.auto_comp_tv_arrivee);

        autoCompDepart.setAdapter(new PlacesAutoCompleteAdapter(this,android.R.layout.simple_dropdown_item_1line));
        autoCompDepart.setOnItemClickListener(this);

        autoCompArrivee.setAdapter(new PlacesAutoCompleteAdapter(this,android.R.layout.simple_dropdown_item_1line));
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
}
