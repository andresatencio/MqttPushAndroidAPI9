package com.murilopontes.mqttpushapi9;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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

public class MyService extends Service {


    //----
    MqttAndroidClient mqttAndroidClient;
    final String serverUri = "tcp://iot.eclipse.org:1883";
    final String subscriptionTopic = "mqttpush";

    public MyService() {
        Log.d("MyService","start");
    }
    public void subscribeToTopic(){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("mqtt","Failed to subscribe");
                }
            });
        } catch (MqttException ex){
            Log.d("mqtt",ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service","onStartCommand");

        String clientId = MqttClient.generateClientId();
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended(){


            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if(reconnect){
                    Log.d("mqtt reconnectet to",serverURI);
                    subscribeToTopic();
                }
                else {
                    Log.d("mqtt connectet to",serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d("mqtt connecting lost","");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(topic,new String(message.getPayload()));


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle(topic);
                mBuilder.setContentText(new String(message.getPayload()));
                mBuilder.setAutoCancel(true);
                int notify_id=1;
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(notify_id, mBuilder.build());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d("mqtt","deliveryComplete");
            }
        });
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName("app");
        mqttConnectOptions.setPassword("app".toCharArray());
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);

        try {
            if(!mqttAndroidClient.isConnected()) {
                Log.d("mqtt","not connected -> try connect");
                mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                        disconnectedBufferOptions.setBufferEnabled(true);
                        disconnectedBufferOptions.setBufferSize(100);
                        disconnectedBufferOptions.setPersistBuffer(false);
                        disconnectedBufferOptions.setDeleteOldestMessages(false);
                        mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                        subscribeToTopic();
                    }
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d("mqtt fail to connect", serverUri);
                    }
                });
            } else {
                Log.d("mqtt","already connected");
            }
        } catch (MqttException ex){
            Log.d("mqtt",ex.getMessage());
            ex.printStackTrace();
        }

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("service","onboind dasdsadsadasdsa");
        return null;
    }
}
