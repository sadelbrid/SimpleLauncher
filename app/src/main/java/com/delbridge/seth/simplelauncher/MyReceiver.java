package com.delbridge.seth.simplelauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Seth on 11/27/15.
 */
public class MyReceiver extends BroadcastReceiver {
    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
            Home.locked = true;
            //Run home

            Log.i("screenstate", "off");
        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("screenstate", "on");

        }else if(intent.getAction().equals(Intent.ACTION_ANSWER)) {

        }
        Intent i = new Intent(context, UpdateService.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
        if(screenOff){
            Intent homeintent = new Intent(context, Home.class);
            context.startActivity(i);
        }
    }
}
