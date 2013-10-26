/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.location;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.hackaton.kyiv.location.Coordinates;
import com.hackaton.kyiv.location.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This the app's main Activity. It provides buttons for requesting the various features of the
 * app, displays the current location, the current address, and the status of the location client
 * and updating services.
 * <p/>
 * {@link #getLocation} gets the current location using the Location Services getLastLocation()
 * function.
 * {@link #startUpdates} sends a request to Location Services to send periodic location updates to
 * the Activity.
 * {@link #stopUpdates} cancels previous periodic update requests.
 * <p/>
 * The update interval is hard-coded to be 5 seconds.
 */
public class LocationFinder implements
        LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    private Context context;
    private double mLong = 0;
    private double mLat = 0;
    private String pinCode;
    private Coordinates bombCoordinates;
    private int counter = 0;


    public void setPin(String pin) {

        pinCode = pin;
    }

    public void doExecute() {
        new GetLocationTask().execute();
    }

    LocationFinder(Context cont) {
        context = cont;
        mLocationRequest = LocationRequest.create();
        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = cont.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(cont, this, this);
    }

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Handles to UI widgets
    ArrayAdapter<String> adapter;
    ArrayList<String> coordinatesStringList;
    ArrayList<Coordinates> coordinatesList;
    Coordinates startPoint = null;
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;


    double distance = 0;
    String url = "";
    double previousDistance = 0;
    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = false;

    /*
     * Initialize the Activity
     */


    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
//    @Override
//    public void onStop() {
//
//        // If the client is connected
//        if (mLocationClient.isConnected()) {
//            stopPeriodicUpdates();
//        }
//
//        // After disconnect() is called, the client is considered "dead".
//        mLocationClient.disconnect();
//
//        super.onStop();
//    }

    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
//    @Override
//    public void onPause() {
//
//        // Save the current setting for updates
//        mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
//        mEditor.commit();
//
//        super.onPause();
//    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */

    public void startLocationClient() {
        mLocationClient.connect();
    }

    /*
     * Called when the system detects that this Activity is now visible.
     */
    public void resumeLocationClient() {
        // If the app already has a setting for getting location updates, get it
        if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);

            // Otherwise, turn off location updates until requested
        } else {
            mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }

    }


    /**
     * Invoked by the "Get Location" button.
     * <p/>
     * Calls getLastLocation() to get the current location
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void getLocation(View v) {

        // If Google Play Services is available


        // Get the current location
        Location currentLocation = mLocationClient.getLastLocation();

        // Display the current location in the UI
    }


    public void startUpdates() {
        mUpdatesRequested = true;


        startPeriodicUpdates();

    }

    /**
     * Invoked by the "Stop Updates" button
     * Sends a request to remove location updates
     * request them.
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void stopUpdates(View v) {
        mUpdatesRequested = false;


        stopPeriodicUpdates();

    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */

    public void onConnected(Bundle bundle) {

        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        (Activity) context,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.

        }
    }

    public double getLat() {
        return mLat;
    }

    public double getLong() {
        return mLong;
    }

    /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        mLong = location.getLongitude();
        // Report to the UI that the location was updated
        //  coordinatesStringList.add(String.valueOf(location.getLatitude() + " " + String.valueOf(location.getLongitude())));
        //  coordinatesList.add(new Coordinates(location.getLatitude(), location.getLongitude()));
        if (pinCode != null) {
            if (counter == 0) {
                doExecute();

//            if (counter == 0) {
//                //Start GetEnemyLocation
//                GetEnemyCoordination ob = new GetEnemyCoordination();
//                ob.execute();
//            }


                counter++;
            } else {

                if (previousDistance > LocationUtils.calculateDistance(bombCoordinates, new Coordinates(location.getLatitude(), location.getLongitude()))) {
                    Toast.makeText(context, "+", 1000).show();
                } else {
                    Toast.makeText(context, "-", 1000).show();
                }
                previousDistance = LocationUtils.calculateDistance(bombCoordinates, new Coordinates(location.getLatitude(), location.getLongitude()));
                //   Toast.makeText(context, String.valueOf(location.getLatitude() + " " + String.valueOf(location.getLongitude())), 1000).show();
            }
        }    else
            Toast.makeText(context,"isNull",2000).show();
    }


    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
    }


    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    private class GetLocationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            JSONParser jParser = new JSONParser();
            String url = "http://cl225620.tmweb.ru/get/json?pin="+pinCode;
            Log.d("myLogs", url);
            // getting JSON string from URL

            JSONObject json1 = jParser.getJSONFromUrl(url);


            try {
                JSONArray array=json1.getJSONArray("array");
                JSONObject j=array.getJSONObject(0)  ;
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //  Log.d("myLogs", json.toString());
            try {
                bombCoordinates = new Coordinates(Double.valueOf(json1.getString("longg")), Double.valueOf(json1.getString("latt")));
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //   Toast.makeText(BombActivity.this,"Happy lat"+latitude+" "+longitude,1000).show();//To change body of overridden methods use File | Settings | File Templates.
        }
    }


}
