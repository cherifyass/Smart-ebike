package com.esir.si.smarte_bike;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.esir.si.smarte_bike.sup.Slider;

public class Donnees extends Fragment{

    private Slider slider = null;

    private Button boutonStat = null;
    private Button boutonMeteo = null;
    private Button boutonSante = null;

    private RelativeLayout contenuStat = null;
    private RelativeLayout contenuMeteo = null;
    private RelativeLayout contenuSante = null;

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


        return view;

    }



}
