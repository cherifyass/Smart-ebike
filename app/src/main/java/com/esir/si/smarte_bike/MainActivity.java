package com.esir.si.smarte_bike;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.esir.si.smarte_bike.navigation.Map;
import com.esir.si.smarte_bike.sup.MyPagerAdapter;
import com.esir.si.smarte_bike.navigation.Itineraire;

import java.util.List;
import java.util.Vector;

/**
 * Activité principale comprenant les fragments :
 *      + Donnees
 *      + Accueil
 *      + Map
 */
public class MainActivity extends FragmentActivity {

    private PagerAdapter mPagerAdapter;

    private ViewPager pager;

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
        fragments.add(android.support.v4.app.Fragment.instantiate(this, Map.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);
        pager = (ViewPager) super.findViewById(R.id.viewpager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);
        //On fixe la page de début (page d'accueil)
        pager.setCurrentItem(1);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    public void seConnecter(View view){
        Intent intent = new Intent(this, Connexion.class);
        startActivity(intent);
    }

    public void goToMesItineraires(View view){
        Intent intent = new Intent(this, Historique.class);
        startActivity(intent);
    }

    public void goToApropos(View view){
        Intent intent = new Intent(this, Apropos.class);
        startActivity(intent);
    }
    // Formulaire itineraire
    public void newItinerary(View view){
        Intent intent = new Intent(this,Itineraire.class);
        startActivity(intent);
    }

    public void goToParametres(View view){
        Intent intent = new Intent(this, Parametres.class);
        startActivity(intent);
    }

}