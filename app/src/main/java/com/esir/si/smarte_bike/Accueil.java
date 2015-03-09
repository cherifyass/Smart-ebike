package com.esir.si.smarte_bike;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Accueil extends Fragment {

    Animation animationconnect;
    Button monBouton;
    TextView monTextView;
    FragmentActivity context = null;

    @Override
    public void onAttach(Activity activity) {
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.accueil, container, false);

        monBouton = (Button) view.findViewById(R.id.button2);

        monBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button2) {
                    monBouton.startAnimation(animationconnect);

                }
            }
        });

        animationconnect = AnimationUtils.loadAnimation(context, R.anim.connectanim);

        return view;
    }


}