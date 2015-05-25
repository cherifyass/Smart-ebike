package com.esir.si.smarte_bike;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.esir.si.smarte_bike.json.JsonUtil;

/**
 * Created by Loann on 25/05/2015.
 */
public class Parametres extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
    }

    public void removeHistory(View view){
        JsonUtil.removeHistory(this);
        Toast.makeText(this, "Fichier supprimé !", Toast.LENGTH_SHORT).show();

    }

}
