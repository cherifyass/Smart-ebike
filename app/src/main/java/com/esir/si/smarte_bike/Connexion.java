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
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class Connexion extends ActionBarActivity {

    public boolean connecte = false;
    private TextView etat = null;
    private TextView message = null;

    BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    private Set<BluetoothDevice> appareils;
    public BluetoothDevice bdevice = null;
    public BluetoothSocket bsocket = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        etat = (TextView) findViewById(R.id.etat);
        if (connecte)
            etat.setText("Connecté au vélo");

        else
            etat.setText("Non connecté au vélo");


        message = (TextView) findViewById(R.id.message);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_ENABLE_BLUETOOTH)
            return;
        if (resultCode == RESULT_OK) {
            // L'utilisation a activé le bluetooth
            Toast.makeText(this,"Connexion Bluetooth activée", Toast.LENGTH_SHORT).show();
        } else {
            // L'utilisation n'a pas activé le bluetooth
            Toast.makeText(this,"Impossible d'activer la connexion", Toast.LENGTH_SHORT).show();

        }
    }

    public void seConnecter(View view) {
        if (blueAdapter == null) {
            Toast.makeText(this, "Votre téléphone ne dispose pas de la technologie Bluetooth,hahahaha t'es nul !!!!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Génial, votre téléphone a le Bluetooth, ça va roxer du poulet", Toast.LENGTH_SHORT).show();

        }
        if (!blueAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLUETOOTH);
        }


        appareils = blueAdapter.getBondedDevices();
        for (BluetoothDevice blueDevice : appareils) {
            Toast.makeText(this, "Device = " + blueDevice.getName(), Toast.LENGTH_SHORT).show();
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);

        blueAdapter.startDiscovery();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        blueAdapter.cancelDiscovery();
        unregisterReceiver(bluetoothReceiver);
    }


    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(Connexion.this, "New Device = " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };

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
