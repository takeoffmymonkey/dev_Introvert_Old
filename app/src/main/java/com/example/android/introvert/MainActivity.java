package com.example.android.introvert;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    String TAG = "INTROVERT_MAIN:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e (TAG, "IN ONCREATE");

        setContentView(R.layout.activity_main);



        Button button = (Button) findViewById(R.id.settings);


        if (savedInstanceState != null) {
            button.setText(savedInstanceState.getString("BUNDLED"));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent();
                intent.setAction("com.example.android.introvert.MYSETTINGS");
                startActivity(intent);
                /*Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, 0);
            */}
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            Log.e (TAG, "I have results");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // we have saved state, but need it after on create
        super.onRestoreInstanceState(savedInstanceState);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.e (TAG, "IN ONSTART");
    }

    @Override
    protected void onResume() {
        super.onResume(); // Always call the superclass method first
        //init components after releasing it in onPause()
        Log.e (TAG, "IN RESUME");
    }


    @Override
    protected void onPause() {
        super.onPause();// Always call the superclass method first
        // free up resources here
        Log.e (TAG, "IN ONPAUSE");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("BUNDLED", "FU");
        // call superclass to save any view hierarchy - always needed
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();// call the superclass method first
//free up almost all resources - for UI, leak memo, etc
        // save the note's current draft, because the activity is stopping
        // and we want to be sure the current note progress isn't lost.
        Log.e (TAG, "IN ONSTOP");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Just called after onStop if returning
        Log.e (TAG, "IN RESTART");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e (TAG, "IN ONDESTROY");
        //calling to free up unfree resources
    }


}
