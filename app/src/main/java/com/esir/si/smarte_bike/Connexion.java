package com.esir.si.smarte_bike;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class Connexion extends ActionBarActivity {

    public boolean connecte = false;

    private TextView etat = null;
    private TextView message = null;


    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice mmDevice = null;
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);


        //Est ce que le téléphone a le bluetooth ?
        bluetoothExiste();
        estConnecte();

    }

    //Fonctions bluetooth

    //Recherche de la techno
    public void bluetoothExiste(){
        if (mBluetoothAdapter == null)
            Toast.makeText(this, "Votre téléphone n'a pas le Bluetooth, vous ne pouvez pas utiliser l'application au maximum de ses fonctionnalités.",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Le téléphone dispose du Bluetooth, veuillez le connecter au vélo.",
                    Toast.LENGTH_SHORT).show();
    }

    public void estConnecte(){

        etat = (TextView) findViewById(R.id.etat);
        connecte = mBluetoothAdapter.isEnabled();

        if (connecte)
            etat.setText("Connecté au vélo");
        else
            etat.setText("Non connecté au vélo");

    }

    public void seConnecter(View view){

        Toast.makeText(this, "En cours de connexion", Toast.LENGTH_SHORT).show();

        //Activation du bluetooth
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);

            Set<BluetoothDevice> setpairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            BluetoothDevice[] pairedDevices = (BluetoothDevice[]) setpairedDevices.toArray(new BluetoothDevice[setpairedDevices.size()]);

            // On parcours la liste pour trouver notre module bluetooth
            for(int i=0;i<pairedDevices.length;i++)
            {
                // On teste si ce périphérique contient le nom du module bluetooth connecté au microcontrôleur
                if(pairedDevices[i].getName().contains("raspberrypi")) {
                    mmDevice = pairedDevices[i];
                }
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0)
            return;
        if (resultCode == RESULT_OK) {
            // L'utilisation a activé le bluetooth
            Toast.makeText(this, "Bluetooth activé", Toast.LENGTH_SHORT).show();

        } else {
            // L'utilisation n'a pas activé le bluetooth
            Toast.makeText(this, "Erreur durant l'activation du bluetooth", Toast.LENGTH_SHORT).show();

        }

        estConnecte();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connexion, menu);
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

    public void RPi(View view){
        Intent intent = new Intent(this, RPi.class);
        startActivity(intent);
    }
}
