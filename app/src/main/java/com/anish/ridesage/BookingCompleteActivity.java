package com.anish.ridesage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BookingCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_complete);

        Intent i = getIntent();
        String time = i.getStringExtra("time");
        TextView t = findViewById(R.id.textView11);
        t.setText("Waiting Time: " + time);

    }
}