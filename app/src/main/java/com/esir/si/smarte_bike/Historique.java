package com.esir.si.smarte_bike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class Historique extends Activity {
    public static final String TAG = Historique.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        //Table Layout from activity_historique : tl
        TableLayout tl = (TableLayout) findViewById(R.id.historiqueTable);

        //Row of headings : tr_head
        TableRow tr_head = new TableRow(this);
        tr_head.setId(View.generateViewId());
        tr_head.setLayoutParams(new RadioGroup.LayoutParams(
        RadioGroup.LayoutParams.MATCH_PARENT,
        RadioGroup.LayoutParams.WRAP_CONTENT));

        TextView label_arrivee = new TextView(this);
        label_arrivee.setId(View.generateViewId());
        label_arrivee.setText("Arrivée");
        label_arrivee.setPadding(5, 5, 5, 5);
        label_arrivee.setTextAppearance(this, R.style.titreColonne);
        label_arrivee.setWidth(380);
        tr_head.addView(label_arrivee);

        TextView label_date = new TextView(this);
        label_date.setId(View.generateViewId());
        label_date.setText("Date");
        label_date.setPadding(5, 5, 5, 5);
        label_date.setTextAppearance(this, R.style.titreColonne);
        tr_head.addView(label_date);

        TextView label_plus = new TextView(this);
        label_plus.setId(View.generateViewId());
        label_plus.setText("Plus");
        label_plus.setPadding(5, 5, 5, 5);
        label_plus.setTextAppearance(this, R.style.titreColonne);
        tr_head.addView(label_plus);

        //add row of headings to the table
        tl.addView(tr_head);


        //JSONObject
        JSONObject jsonObject = parseJSONData();
        Log.i(TAG,"jsonObject = "+jsonObject);

        //Fill table
        try {

            JSONArray trips = jsonObject.getJSONArray("Trips");
            //For each trip
            for(int i=0; i<trips.length();i++){
                JSONObject childJSONObject = trips.getJSONObject(i);
                String arrivee = childJSONObject.getString("arrivee");
                String date = childJSONObject.getString("date");

                //Add row to the table
                TableRow tr = new TableRow(this);
                tr.setId(View.generateViewId());
                tr.setLayoutParams(new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT));

                TextView text_arrivee = new TextView(this);
                text_arrivee.setId(View.generateViewId());
                text_arrivee.setText(arrivee);
                text_arrivee.setPadding(5, 5, 5, 5);
                text_arrivee.setTextAppearance(this, R.style.HTexteC1);
                text_arrivee.setWidth(380);
                tr.addView(text_arrivee);

                TextView text_date = new TextView(this);
                text_date.setId(View.generateViewId());
                text_date.setText(date);
                text_date.setPadding(5, 5, 5, 5);
                text_date.setTextAppearance(this, R.style.HTexteC2);
                tr.addView(text_date);

                Button button_plus = new Button(this);
                button_plus.setId(View.generateViewId());
                button_plus.setPadding(5, 5, 5, 5);
                button_plus.setText("->");
                button_plus.setTextAppearance(this, R.style.boutonDetails);
                button_plus.setBackground(getResources().getDrawable(R.drawable.mybutton));
                tr.addView(button_plus);

                button_plus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        goToDetails(v);
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

    public void goToDetails(View view){
        Intent intent = new Intent(this, Details.class);
        startActivity(intent);
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

