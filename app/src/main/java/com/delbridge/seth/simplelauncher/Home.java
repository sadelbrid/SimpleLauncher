package com.delbridge.seth.simplelauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.view.WindowManager;
import android.os.CountDownTimer;
import android.widget.TextView;

public class Home extends Activity {
    static boolean locked = true;
    public static SharedPreferences sharedPreferences;
    public static final String KEY = "password";

    public CountDownTimer timer;
    public boolean lockedOut;
    public long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        lockedOut = sharedPreferences.getBoolean("lockedOut", false);
        timeLeft = sharedPreferences.getLong("timeLeft", 0);

        startService(new Intent(Home.this, UpdateService.class));
        setContentView(R.layout.activity_home2);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        final EditText edittext = (EditText) findViewById(R.id.passwordInput);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    showApps();
                    return true;
                }
                return false;
            }
        });

        int tolerance = Integer.parseInt(sharedPreferences.getString("tolerance", "1"));
        final TextView lockOutput = (TextView)findViewById(R.id.lockOutput);
        final TextView passwordInput = (TextView)findViewById(R.id.passwordInput);

        long duration = Long.parseLong(sharedPreferences.getString("lockTime", "1"))*60000;
        Log.i("duration", String.valueOf(duration));
        if(lockedOut){
            duration = timeLeft;
            passwordInput.setEnabled(false);
        }

        timer = new CountDownTimer(duration, 1000){
            public void onTick(long millisUntilFinished){
                String minutes = String.valueOf(millisUntilFinished/60000);
                String seconds = String.valueOf(millisUntilFinished%60000/1000);
                if(Integer.parseInt(seconds)<10){
                    seconds = "0"+seconds;
                }
                lockOutput.setText("Phone locked for\n"+minutes+":"+seconds);
                lockedOut = true;
                timeLeft = millisUntilFinished;
            }

            public void onFinish(){
                lockOutput.setText("");
                passwordInput.setEnabled(true);
                lockedOut=false;
            }
        };

        if(lockedOut){
            timer.start();
        }
    }

    public void showApps(){
        String key = sharedPreferences.getString(KEY, "");
        EditText password = (EditText)findViewById(R.id.passwordInput);
        Log.i("Passwords", password.getText().toString() + ", " + key);
        if(password.getText().toString().equals(key)) {
            password.getText().clear();


            //Reset to 0
            SharedPreferences.Editor editor = sharedPreferences.edit();
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
            if(sharedPreferences.contains("errorCount")){
                //Get old value
                int errorCount = sharedPreferences.getInt("errorCount", 0);
                //Put incremented value
                errorCount++;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("errorCount", errorCount);
                editor.apply();
                Log.i("newvalue", "" + sharedPreferences.getInt("errorCount", 0));
            }
            else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("errorCount", 1);
                editor.apply();
            }

            int numErrorsAllowed = Integer.parseInt(sharedPreferences.getString("tolerance", "1"));

            int error_count = sharedPreferences.getInt("errorCount", 0);
            if(error_count >= numErrorsAllowed){
                lockOut();
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

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed(){

    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("timeLeft", timeLeft);
        editor.putBoolean("lockedOut", lockedOut);
        editor.apply();
    }

    /*
    Email should contain a front camera capture, GPS coordinates
     */
    private void sendEmail(){

    }

    //Lock homescreen for @tolerance minutes
    private void lockOut(){
        EditText passwordInput = (EditText)findViewById(R.id.passwordInput);
        passwordInput.setEnabled(false);
        timer.start();
    }
}
