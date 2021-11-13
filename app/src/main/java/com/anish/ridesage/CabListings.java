package com.anish.ridesage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CabListings extends AppCompatActivity {
    // Cab Listing List
    List<CabItem> cabListings;

    // Dialog and Builder
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    // Main View Elements
    RecyclerView recyclerView;
    private EditText source;
    private EditText destination;

    CabItemClickListener listener = (cabItem) -> {
        Intent myIntent = new Intent(this, BookRideActivity.class);
        myIntent.putExtra("cabItem", cabItem);
        myIntent.putExtra("source", source.getText().toString());
        myIntent.putExtra("destination", destination.getText().toString());
        startActivity(myIntent);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_listings);

        cabListings = getCabListings();

        // Assign view elements
        recyclerView = findViewById(R.id.recycler_view);
        source = findViewById(R.id.cabListSource);
        destination = findViewById(R.id.cabListDestination);

        //handling intent
        Intent i = getIntent();
        source.setText(i.getStringExtra("source"));
        destination.setText(i.getStringExtra("destination"));

        // Recycler View
        CabAdapter adapter = new CabAdapter(cabListings, listener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public static List<CabItem> getCabListings() {
        List<CabItem> list = new ArrayList<>();
        list.add(new CabItem("Uber", 13, "8 min", "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Lyft", 15, "3 min", "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, "6 min", "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 21, "9 min", "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 19, "10 min", "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 13, "8 min", "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Lyft", 15, "3 min", "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, "6 min", "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 21, "9 min", "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 19, "10 min", "Premium", 5, R.drawable.uber));
        return list;
    }

    public void onSort(View v){
        PopupMenu popupMenu = new PopupMenu(CabListings.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cheapestB:
                        CabAdapter adapter = new CabAdapter(cheapestList(), listener);
                        recyclerView.setAdapter(adapter);
                        break;
                    case R.id.fastestB:
                        CabAdapter adapter2 = new CabAdapter(fastestList(), listener);
                        recyclerView.setAdapter(adapter2);
                        break;
                }
                return false;
            }
        });
    }

    private List<CabItem> cheapestList() {
        List<CabItem> list = new ArrayList<>();

        // PROTOTYPE: Placeholder Cab Listings
        list.add(new CabItem("Uber", 13, "8 min", "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 13, "8 min", "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Lyft", 15, "3 min", "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 15, "3 min", "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, "6 min", "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, "6 min", "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 19, "10 min", "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 19, "10 min", "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 21, "9 min", "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 21, "9 min", "Economy", 3, R.drawable.uber));
        return list;
    }


    private List<CabItem> fastestList() {
        List<CabItem> list = new ArrayList<>();

        // PROTOTYPE: Placeholder Cab Listings
        list.add(new CabItem("Lyft", 15, "3 min", "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 15, "3 min", "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, "6 min", "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, "6 min", "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 13, "8 min", "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 13, "8 min", "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 21, "9 min", "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 21, "9 min", "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 19, "10 min", "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 19, "10 min", "Premium", 5, R.drawable.uber));
        return list;
    }

    public void onFilterClicked(View v){
        FilterPopup filterPopup = new FilterPopup(recyclerView, listener, cabListings);
        filterPopup.showPopupWindow(v);
    }

}
