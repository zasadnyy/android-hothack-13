package com.example.android.location;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.hackaton.kyiv.location.Coordinates;
import com.hackaton.kyiv.location.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerActivity extends Activity implements OnClickListener {

    private String gameId;
    public Coordinates startPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ((Button) findViewById(R.id.button_send_player_location)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        gameId = ((EditText) findViewById(R.id.player_pin)).getText().toString();
        intent.putExtra("gameId", gameId);
        startActivity(intent);
    }

}
