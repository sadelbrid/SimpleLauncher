package com.delbridge.seth.simplelauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class Home extends Activity {
    EditText password;
    static boolean locked = true;
    static String key = "1234";
    public static final String PREFS_NAME = "HomePrefs";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(Home.this, UpdateService.class));
        setContentView(R.layout.activity_home2);

//        if(locked)
//            setContentView(R.layout.activity_home2);
//        else
//            showApps(null);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        password = (EditText) findViewById(R.id.passwordInput);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    showApps(v);
                    Log.i("ClickChecked Button", "checking password");
                    handled = true;
                }
                return handled;
            }
        });

    }

    public void showApps(View v) {


        if (password.getText().toString().equals(key)) {
            password.getText().clear();


            //Reset to 0
            SharedPreferences homeprefs = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = homeprefs.edit();
            editor.putInt("errorCount", 0);
            editor.apply();

            locked = false;
            Intent i = new Intent(this, AppsList.class);
            startActivity(i);
        } else {
            //Wrong input
            password.getText().clear();
            //Toast Notification if password is wrong
            Toast.makeText(getApplicationContext(), "Password Incorrect",
                    Toast.LENGTH_LONG).show();
            //Increment error count
            SharedPreferences homeprefs = getSharedPreferences(PREFS_NAME, 0);
            if (homeprefs.contains("errorCount")) {
                //Get old value
                int errorCount = homeprefs.getInt("errorCount", 0);
                //Put incremented value
                errorCount++;
                SharedPreferences.Editor editor = homeprefs.edit();
                editor.putInt("errorCount", errorCount);
                editor.apply();
                Log.i("newvalue", "" + homeprefs.getInt("errorCount", 0));
            } else {
                SharedPreferences.Editor editor = homeprefs.edit();
                editor.putInt("errorCount", 1);
                editor.apply();
            }


            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            int numErrorsAllowed = Integer.parseInt(settings.getString("tolerance", "1"));

            int error_count = homeprefs.getInt("errorCount", 0);
            if (error_count > numErrorsAllowed) {
                //send email
                Log.i("emailalert", "sending email");
                sendEmail();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("DestroyAlert", "destroying home");
        super.onDestroy();
    }

    /*
    Email should contain a front camera capture, GPS coordinates
     */
    private void sendEmail() {
        //email and password of the email sender
        final GMailSender sender = new GMailSender("androidtestingosu@gmail.com", "apptesting123");
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... arg) {
                try {
                    /**send email to the given recipient
                     */
                    sender.sendMail("LockScreen Alert ",
                            "Someone tried to access your mobile device.",
                            "androidtestingosu@gmail.com",
                            "sachinda.yl@gmail.com");
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);

                }
                return null;
            }
        }.execute();


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.delbridge.seth.simplelauncher/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.delbridge.seth.simplelauncher/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
