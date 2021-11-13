package com.anish.ridesage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BookRideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_ride);
    }

    public void onButtonClicked(View v){
        Intent myIntent = new Intent(this, BookingCompleteActivity.class);
        startActivity(myIntent);
    }
}
