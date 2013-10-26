package com.example.android.location;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.hackaton.kyiv.location.Coordinates;
import com.hackaton.kyiv.location.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;

public class BombActivity extends Activity implements View.OnClickListener {
    LocationFinder lf;
    double latitude;
    double longitude;
    String gameId;
    Button btnSentCoordinates;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bomb_activity);
        lf = new LocationFinder(this);
        btnSentCoordinates=(Button)findViewById(R.id.button_send_bomb_location)    ;
        btnSentCoordinates.setOnClickListener(this);
       // lf.startUpdates();
        

    }
    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        lf.startLocationClient();
    }
    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        lf.resumeLocationClient();


        //  Toast.makeText(this,String.valueOf(latitude)+" "+longitude,2000).show();

    }

    @Override
    public void onClick(View v) {
        gameId=((EditText) findViewById(R.id.bomb_pin)).getText().toString();
        lf.setPin(gameId);
        GetLocationTask glt=new GetLocationTask();
        glt.execute();
    }


    private class GetLocationTask extends AsyncTask <Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {

            while (lf.getLat() == 0 && lf.getLong() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            latitude = lf.getLat();
            longitude = lf.getLong();
            JSONParser jParser = new JSONParser();
            String url = "http://cl225620.tmweb.ru/set/json?pin="+gameId+"&longg="+longitude+"&latt="+latitude;

            Log.d("myLogs", url);
            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl(url);





            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
             //   Toast.makeText(BombActivity.this,"Happy lat"+latitude+" "+longitude,1000).show();//To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
