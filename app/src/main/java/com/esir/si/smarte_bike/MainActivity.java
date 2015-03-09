package com.esir.si.smarte_bike;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;


import java.util.List;
import java.util.Vector;


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


    public void goTo(View view){
        Intent intent = new Intent(this, Connexion.class);
        startActivity(intent);
    }

}