package com.esir.si.smarte_bike;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class Connexion extends ActionBarActivity {

    public boolean connecte = false;
    private TextView etat = null;
    private TextView message = null;

    public BluetoothDevice bdevice = null;
    public BluetoothSocket bsocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        etat = (TextView) findViewById(R.id.etat);
        if (connecte)
            etat.setText("Connecté");

        else
            etat.setText("Non connecté");


        message = (TextView) findViewById(R.id.message);


    }

    public void seConnecter(View view) {
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null) {
            message.setText("Votre téléphone ne dispose pas de la technologie Bluetooth,hahahaha t'es nul !!!!");
        }

        if (!blueAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        if (blueAdapter.isEnabled()) {
            message.setText("Bluetooth activé ! Youpi ;)");
        }

        Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();

        blueAdapter.startDiscovery();
        blueAdapter.cancelDiscovery();

        // On crée un BroadcastReceiver pour ACTION_FOUND
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                ArrayAdapter mArrayAdapter =null;
                // Quand la recherche trouve un terminal
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // On récupère l'object BluetoothDevice depuis l'Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // On ajoute le nom et l'adresse du périphérique dans un ArrayAdapter (par exemple pour l'afficher dans une ListView)
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
        // Inscrire le BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter); // N'oubliez pas de le désinscrire lors du OnDestroy() !

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
}
