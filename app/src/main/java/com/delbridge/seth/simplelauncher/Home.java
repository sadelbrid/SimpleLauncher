package com.delbridge.seth.simplelauncher;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Home extends Activity {
    static boolean locked = true;
    static String key = "1234";
    public static final String PREFS_NAME = "HomePrefs";

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
        EditText password = (EditText)findViewById(R.id.passwordInput);
        if(password.getText().toString().equals(key)) {
            password.getText().clear();


            //Reset to 0
            SharedPreferences homeprefs = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = homeprefs.edit();
            editor.putInt("errorCount", 0);
            editor.apply();

            locked = false;
            Intent i = new Intent(this, AppsList.class);
            startActivity(i);
        }
        else{
            //Wrong input
            password.getText().clear();
            //Increment error count
            SharedPreferences homeprefs = getSharedPreferences(PREFS_NAME, 0);
            if(homeprefs.contains("errorCount")){
                //Get old value
                int errorCount = homeprefs.getInt("errorCount", 0);
                //Put incremented value
                errorCount++;
                SharedPreferences.Editor editor = homeprefs.edit();
                editor.putInt("errorCount", errorCount);
                editor.apply();
                Log.i("newvalue", "" + homeprefs.getInt("errorCount", 0));
            }
            else{
                SharedPreferences.Editor editor = homeprefs.edit();
                editor.putInt("errorCount", 1);
                editor.apply();
            }


            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            int numErrorsAllowed = Integer.parseInt(settings.getString("tolerance", "1"));

            int error_count = homeprefs.getInt("errorCount", 0);
            if(error_count > numErrorsAllowed){
                //send email
                Log.i("emailalert", "sending email");
                sendEmail();
            }
        }
    }

    @Override
    protected void onDestroy(){
        Log.i("DestroyAlert", "destroying home");
        super.onDestroy();
    }

    /*
    Email should contain a front camera capture, GPS coordinates
     */
    private void sendEmail(){

    }
}
