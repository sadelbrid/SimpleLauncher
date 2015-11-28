package com.delbridge.seth.simplelauncher;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Home extends Activity {
    static boolean locked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(Home.this, UpdateService.class));
        setContentView(R.layout.activity_home2);

//        if(locked)
//            setContentView(R.layout.activity_home2);
//        else
//            showApps(null);
    }

    public void showApps(View v){
        locked = false;
        Intent i = new Intent(this, AppsList.class);
        startActivity(i);
    }
}
