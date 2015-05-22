package com.esir.si.smarte_bike;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.esir.si.smarte_bike.json.Trip;
import com.esir.si.smarte_bike.navigation.Itineraire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class Historique extends ActionBarActivity {
    public static final String TAG = Historique.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        //New Itinerary
        ImageButton it = (ImageButton) findViewById(R.id.imageButton);
        it.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Historique.this, Itineraire.class);
                startActivity(intent);
            }
        });

        //Table Layout from activity_historique : tl
        TableLayout tl = (TableLayout) findViewById(R.id.historiqueTable);

        //JSONObject
        JSONObject jsonObject = parseJSONData();
        Log.i(TAG,"jsonObject = "+jsonObject);


        //Fill table
        try {

            JSONArray trips = jsonObject.getJSONArray("Trips");
            //For each trip
            for(int i=0; i<trips.length();i++){
                JSONObject childJSONObject = trips.getJSONObject(i);
                String depart = childJSONObject.getString("depart");
                final String arrivee = childJSONObject.getString("arrivee");
                String date = childJSONObject.getString("date");
                String distance = childJSONObject.getString("distance");
                final Trip trip = new Trip(depart, arrivee, date, distance);

                //Add row to the table
                TableRow tr = new TableRow(this);
                tr.setGravity(Gravity.CENTER);
                tr.setPadding(5, 5, 5, 5);
                tr.setLayoutParams(new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT));

                TextView text_arrivee = new TextView(this);
                text_arrivee.setText(arrivee);
                text_arrivee.setPadding(5, 5, 5, 5);
                text_arrivee.setTextAppearance(this, R.style.HTexteC1);
                text_arrivee.setWidth(380);
                tr.addView(text_arrivee);

                TextView text_date = new TextView(this);
                text_date.setText(date);
                text_date.setPadding(5, 5, 5, 5);
                text_date.setTextAppearance(this, R.style.HTexteC2);
                tr.addView(text_date);

                Button button_plus = new Button(this);
                button_plus.setPadding(5, 5, 5, 5);
                button_plus.setText("->");
                button_plus.setTextAppearance(this, R.style.boutonDetails);
                button_plus.setBackground(getResources().getDrawable(R.drawable.mybutton));
                tr.addView(button_plus);


                button_plus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Historique.this, Details.class);
                        intent.putExtra("trip", (Parcelable) trip);
                        startActivity(intent);
                    }
                });

                //add row to the table
                tl.addView(tr);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }


    //Method that will parse the JSON file and will return a JSONObject
    public JSONObject parseJSONData() {
        String JSONString = null;
        JSONObject JSONObject = null;
        try {

            //open the inputStream to the file
            InputStream inputStream = getAssets().open("historique.json");

            int sizeOfJSONFile = inputStream.available();

            //array that will store all the data
            byte[] bytes = new byte[sizeOfJSONFile];

            //reading data into the array from the file
            inputStream.read(bytes);

            //close the input stream
            inputStream.close();

            JSONString = new String(bytes, "UTF-8");
            JSONObject = new JSONObject(JSONString);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        catch (JSONException x) {
            x.printStackTrace();
            return null;
        }
        return JSONObject;
    }

}

