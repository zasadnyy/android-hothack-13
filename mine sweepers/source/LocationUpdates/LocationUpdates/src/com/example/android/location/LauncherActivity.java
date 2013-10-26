package com.example.android.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LauncherActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        ((Button) findViewById(R.id.btnBomb)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnPlayer)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);
        return true;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.btnBomb:
            Intent intent = new Intent(this, BombActivity.class);
            startActivity(intent);
            break;
        case R.id.btnPlayer: {
            Intent intent1 = new Intent(this, PlayerActivity.class);
            startActivity(intent1);
            break;
        }
        }
    }
}
