package com.murilopontes.mqttpushapi9;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.d("MyReceiver","intent action="+action);


        Intent myIntent = new Intent(context, MyService.class);
        myIntent.putExtra("key1","value1");
        context.startService(myIntent);
    }
}
