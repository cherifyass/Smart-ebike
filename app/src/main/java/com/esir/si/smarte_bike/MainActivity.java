package com.esir.si.smarte_bike;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.esir.si.smarte_bike.sup.MyPagerAdapter;
import com.esir.si.smarte_bike.navigation.Itineraire;
import com.esir.si.smarte_bike.navigation.Navigation;

import java.util.List;
import java.util.Vector;

/**
 * Activité principale comprenant les fragments :
 *      + Donnees
 *      + Accueil
 *      + Navigation
 */
public class MainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        // initialising the object of the FragmentManager.
        fragmentManager = getSupportFragmentManager();

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(android.support.v4.app.Fragment.instantiate(this, Donnees.class.getName()));
        fragments.add(android.support.v4.app.Fragment.instantiate(this, Accueil.class.getName()));
        fragments.add(android.support.v4.app.Fragment.instantiate(this, Navigation.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);
        //On fixe la page de début (page d'accueil)
        pager.setCurrentItem(1);


    }


    public void seConnecter(View view){
        Intent intent = new Intent(this, Connexion.class);
        startActivity(intent);
    }

    public void goToMesItineraires(View view){
        Intent intent = new Intent(this, Historique.class);
        startActivity(intent);
    }

    // Formulaire itineraire
    public void newItinerary(View view){
        Intent intent = new Intent(this,Itineraire.class);
        startActivity(intent);
    }

}