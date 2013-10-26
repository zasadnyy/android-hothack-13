package com.example.android.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Kostya
 * Date: 26.10.13
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends Activity {
    LocationFinder locationFinder;
    String pin;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        pin = intent.getStringExtra("gameId");
        locationFinder = new LocationFinder(this);
        locationFinder.setPin(pin);


    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        locationFinder.startLocationClient();
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        locationFinder.resumeLocationClient();
    }
}