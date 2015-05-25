package com.esir.si.smarte_bike;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.esir.si.smarte_bike.json.JsonUtil;
import com.esir.si.smarte_bike.json.MyItineraire;

import org.w3c.dom.Text;

import java.util.List;


public class Historique extends ActionBarActivity {
    public static final String TAG = Historique.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);


        //Table Layout from activity_historique : tl
        TableLayout tl = (TableLayout) findViewById(R.id.historiqueTable);

        //list of itineraries
        List<MyItineraire> list = JsonUtil.read(this);

        // Text view no itinerary found
        TextView aucun_itineraire = (TextView) findViewById(R.id.no_itinerary);

        if( list == null ){
            // show text view
            aucun_itineraire.setVisibility(View.VISIBLE);
        }else {
            // hide text view
            aucun_itineraire.setVisibility(View.INVISIBLE);

            //table row of headings
            TableRow tr_head = new TableRow(this);
            tr_head.setLayoutParams(new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.MATCH_PARENT));

            TextView heading_arrivee = new TextView(this);
            heading_arrivee.setText("Arrivée");
            heading_arrivee.setWidth(380);
            heading_arrivee.setTextAppearance(this, R.style.titreColonne);
            TextView heading_date = new TextView(this);
            heading_date.setText("Date");
            heading_date.setWidth(200);
            heading_date.setTextAppearance(this, R.style.titreColonne);
            TextView heading_plus = new TextView(this);
            heading_plus.setText("Plus");
            heading_plus.setWidth(100);
            heading_plus.setTextAppearance(this, R.style.titreColonne);

            tr_head.addView(heading_arrivee);
            tr_head.addView(heading_date);
            tr_head.addView(heading_plus);
            tr_head.setPadding(5, 5, 5, 20);

            tl.addView(tr_head);

            //Fill table

            //For each itinerary
            for (int i = 0; i < list.size(); i++) {

                int dateJour = list.get(i).getDateJour();
                int dateMois = list.get(i).getDateMois();
                int dateAnnee = list.get(i).getDateAnnee();
                String date = dateJour + "/" + dateMois + "/" + dateAnnee;

                String arrText = list.get(i).getArrText();

                final MyItineraire myItineraire = list.get(i);

                //Add row to the table
                TableRow tr = new TableRow(this);
                //tr.setPadding(5, 5, 0, 0);
                tr.setLayoutParams(new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.MATCH_PARENT));

                TextView text_arrivee = new TextView(this);
                text_arrivee.setText(arrText);
                text_arrivee.setPadding(5, 0, 0, 0);
                text_arrivee.setTextAppearance(this, R.style.HTexteC1);
                text_arrivee.setWidth(380);
                text_arrivee.setMaxWidth(380);

                tr.addView(text_arrivee);

                TextView text_date = new TextView(this);
                text_date.setText(date);
                text_date.setPadding(5, 0, 0, 0);
                text_date.setTextAppearance(this, R.style.HTexteC2);
                text_date.setWidth(200);
                text_date.setMaxWidth(200);
                tr.addView(text_date);

                Button button_plus = new Button(this);
                button_plus.setPadding(5, 5, 5, 5);
                button_plus.setText("->");
                button_plus.setTextAppearance(this, R.style.boutonDetails);
                button_plus.setBackground(getResources().getDrawable(R.drawable.mybutton));
                button_plus.setHeight(50);
                button_plus.setWidth(100);
                tr.addView(button_plus);


                button_plus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(Historique.this, Details.class);
                        intent.putExtra("myItineraire", (Parcelable) myItineraire);
                        startActivity(intent);
                    }
                });

                //add row to the table
                tl.addView(tr);
            }
        }// end else

    }


}

