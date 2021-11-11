package com.anish.ridesage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClicked(View v){
        Intent myIntent = new Intent(this, CabListings.class);
        myIntent.putExtra("source", "src");
        myIntent.putExtra("destination", "destination");
        startActivity(myIntent);
    }
}