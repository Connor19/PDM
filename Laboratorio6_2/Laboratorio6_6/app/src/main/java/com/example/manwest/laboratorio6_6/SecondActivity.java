package com.example.manwest.laboratorio6_6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by FRANZ on 25/03/2016.
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
    public void zoomBack(View button){
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.zoom_back_in,
                R.anim.zoom_back_out);
    }
    public void fade(View button){
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    public void right(View button){
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
    public void back(View button){
        super.onBackPressed();
    }
}
