package com.anish.ridesage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }
    public void onButtonClicked(View v){
        Intent myIntent = new Intent(this, FindRideActivity.class);
        startActivity(myIntent);
    }
}