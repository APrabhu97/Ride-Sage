package com.anish.ridesage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FindRideActivity extends AppCompatActivity {

    EditText source;
    EditText destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_ride);
        source = (EditText) findViewById(R.id.findRideSource);
        destination = (EditText) findViewById(R.id.findRideDestination);
    }

    public void onButtonClicked(View v){
        Intent myIntent = new Intent(this, CabListings.class);
        myIntent.putExtra("source", source.getText().toString());
        myIntent.putExtra("destination", destination.getText().toString());
        startActivity(myIntent);
    }
}