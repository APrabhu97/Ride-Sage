package com.anish.ridesage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CabListings extends AppCompatActivity {
    CabListings deleteLater = this;

    // Cab Listing List
    List<CabItem> cabListings;

    // Dialog and Builder
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    // Main View Elements
    private Button buttonSort;
    RecyclerView recyclerView;

    // Sort Popup View Elements
    private Button buttonCheapest;
    private Button buttonFastest;
    private Button buttonQuitSortPopup;
    private EditText source;
    private  EditText destination;

    CabItemClickListener listener = (cabItem) -> {
        Intent myIntent = new Intent(this, BookRideActivity.class);
        myIntent.putExtra("cabItem", cabItem);
        startActivity(myIntent);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_listings);


        // Assign view elements
        cabListings = getCabListings();  // Placeholder Cab Listings
        buttonSort = findViewById(R.id.buttonSort);
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

        buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortListPopup();
            }
        });
    }

    private List<CabItem> getCabListings(){
        List<CabItem> list = new ArrayList<>();

        // PROTOTYPE: Placeholder Cab Listings
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

    public void sortListPopup() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View sortPopupView = getLayoutInflater().inflate(R.layout.activity_sort_popup, null);

        // Assign sort popup view elements to variables
        buttonCheapest = sortPopupView.findViewById(R.id.buttonCheapest);
        buttonFastest = sortPopupView.findViewById(R.id.buttonFastest);
        buttonQuitSortPopup = sortPopupView.findViewById(R.id.buttonQuitSortPopup);

        // Create dialog instance with builder then display
        dialogBuilder.setView(sortPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        // Define event listeners for buttons in the sort popup ------------------------------------
        buttonCheapest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cabListings.sort()

                // PROTOTYPE: Placeholder Cab Listings
                List<CabItem> list = new ArrayList<>();
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
                cabListings = list;

                CabAdapter adapter = new CabAdapter(cabListings, listener);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(deleteLater));

                dialog.cancel();
            };
        });

        buttonFastest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cabListings.sort()

                List<CabItem> list = new ArrayList<>();
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
                cabListings = list;

                CabAdapter adapter = new CabAdapter(cabListings, listener);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(deleteLater));

                dialog.cancel();
            }
        });

        buttonQuitSortPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}
