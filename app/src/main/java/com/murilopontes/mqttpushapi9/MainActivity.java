package com.murilopontes.mqttpushapi9;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;



public class MainActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("onCreate","------------------------------------- onCreate");

        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        intent.setAction("flepnight");
        sendBroadcast(intent);



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("onRestart","------------------------------------- onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy","-------------------------------------  onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop","------------------------------------- onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","------------------------------------- onResume");

    }





}
