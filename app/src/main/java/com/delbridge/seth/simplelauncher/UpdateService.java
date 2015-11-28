package com.delbridge.seth.simplelauncher;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Seth on 11/27/15.
 */
public class UpdateService extends Service {
    BroadcastReceiver mReceiver;
    Boolean isRunning;
    Context context;
    Thread backgroundThread;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        isRunning = false;
        // register receiver that handles screen on and screen off logic
        Log.i("UpdateService", "Started");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_ANSWER);
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        isRunning = false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            Log.i("screenON", "Called");
        } else {
            Log.i("screenOFF", "Called");
            Home.locked = true;
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
