package com.example.judeapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.judeapp.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//Serial Port Service ID
    KProgressHUD kProgressHU;
    boolean deviceConnected = false;
    boolean stopThread;
    Thread thread;
    byte[] buffer;
    int bufferPosition;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private OutputStream outputStream;
    private InputStream inputStream;
    private EditText editTextTaille;
    private EditText editTextPoids;
    private TextView textView;
    private LinearLayout linearLayoutValue;
    private Button buttonConnect;
    private Button buttonGetData;
    private Button buttonInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button startBtn;
        //startBtn = findViewById(R.id.btn_start);

        editTextTaille = findViewById(R.id.taille_id);
        editTextPoids = findViewById(R.id.poids_id);

        textView = findViewById(R.id.textView);

        linearLayoutValue = findViewById(R.id.layoutValue);

        buttonConnect = findViewById(R.id.start);
        buttonGetData = findViewById(R.id.getdata);
        buttonInsert = findViewById(R.id.inserer);

        setUiEnabled(false);


        editTextTaille.setEnabled(false);
        editTextPoids.setEnabled(false);
        linearLayoutValue.setVisibility(View.INVISIBLE);


        kProgressHU = new KProgressHUD(this);
        kProgressHU.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }


    public void setUiEnabled(boolean bool) {
        buttonConnect.setEnabled(!bool);
        buttonGetData.setEnabled(bool);
        buttonInsert.setEnabled(bool);

    }

    public boolean BTinit() {
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                String DEVICE_ADDRESS = "98:D3:41:F5:A4:AF";
                if (iterator.getAddress().equals(DEVICE_ADDRESS)) {   /*kProgressHU.setLabel("Connexion")
                        .show();*/

//                    Toast.makeText(this, "Device find", Toast.LENGTH_SHORT).show();
                    device = iterator;
                    found = true;
                    break;
                } else {
                    Toast.makeText(this, "Device not find", Toast.LENGTH_SHORT).show();
                }

                /*kProgressHU.setLabel("Connexion")
                        .dismiss();*/
            }
        }
        return found;
    }

    public boolean BTconnect() {
        boolean connected = true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
        if (connected) {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }


    void beginListenForData() {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopThread) {
                    try {
                        int byteCount = inputStream.available();
                        if (byteCount > 0) {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string = new String(rawBytes, StandardCharsets.UTF_8);
                            handler.post(new Runnable() {
                                public void run() {
                                    textView.append(string);
                                }
                            });

                        }
                    } catch (IOException ex) {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }


    public void onClickConnect(View view) {

        /*kProgressHU.setLabel("Connexion")
                .show();*/

        if (BTinit()) {
            if (BTconnect()) {
               /* kProgressHU.setLabel("Connexion")
                        .dismiss();*/
                Toast.makeText(this, "Connection au module avec succes", Toast.LENGTH_SHORT).show();
                setUiEnabled(true);
                deviceConnected = true;
                /*beginListenForData();*/
                textView.append("\nConnection au module avec succes\n");
            }

        }

    }

    public void onClickGetData(View view) {

        beginListenForData();

    }

    public void onClickInsert(View view) {
    }

    public void nextStep(View view) {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }
}
