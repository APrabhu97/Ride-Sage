package com.anish.ridesage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.PopupMenu;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class CabListings extends AppCompatActivity {
    // Cab Listing List
    List<CabItem> cabListings;

    // Main View Elements
    RecyclerView recyclerView;
    private Place sourcePlace, destinationPlace;
    AutoCompleteTextView sourceAutocomplete, destinationAutocomplete;
    private PlacesClient placesClient;

    CabItemClickListener listener = (cabItem) -> {
        Intent myIntent = new Intent(this, BookRideActivity.class);
        myIntent.putExtra("cabItem", cabItem);
        myIntent.putExtra("source", sourcePlace);
        myIntent.putExtra("destination", destinationPlace);
        startActivity(myIntent);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_listings);

        cabListings = getCabListings();

        // Assign view elements
        recyclerView = findViewById(R.id.recycler_view);
        sourceAutocomplete = findViewById(R.id.cabListSource);
        destinationAutocomplete = findViewById(R.id.cabListDestination);

        //handling intent
        Intent i = getIntent();
        sourcePlace = i.getParcelableExtra("source");
        destinationPlace = i.getParcelableExtra("destination");
        sourceAutocomplete.setText(sourcePlace.getName());
        destinationAutocomplete.setText(destinationPlace.getName());

        Places.initialize(this, getString(R.string.com_google_android_geo_API_KEY));
        placesClient = Places.createClient(this);
        initAutocomplete();

        // Recycler View
        CabAdapter adapter = new CabAdapter(cabListings, listener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public static List<CabItem> getCabListings() {
        List<CabItem> list = new ArrayList<>();
        list.add(new CabItem.CabItemBuilder("Uber", R.drawable.uber).cabTier("Premium").cost(13).maxSeats(3).pickupTime(8).build());
        list.add(new CabItem("Lyft", 15, 3, "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, 6, "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 21, 9, "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 19, 10, "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 13, 8, "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Lyft", 15, 3, "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, 6, "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 21, 9, "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 19, 10, "Premium", 5, R.drawable.uber));
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
        list.add(new CabItem("Uber", 13, 8, "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 13, 8, "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Lyft", 15, 3, "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 15, 3, "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, 6, "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, 6, "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 19, 10, "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 19, 10, "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 21, 9, "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 21, 9, "Economy", 3, R.drawable.uber));
        return list;
    }


    private List<CabItem> fastestList() {
        List<CabItem> list = new ArrayList<>();

        // PROTOTYPE: Placeholder Cab Listings
        list.add(new CabItem("Lyft", 15, 3, "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 15, 3, "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, 6, "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, 6, "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 13, 8, "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 13, 8, "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 21, 9, "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 21, 9, "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 19, 10, "Premium", 5, R.drawable.uber));
        list.add(new CabItem("Uber", 19, 10, "Premium", 5, R.drawable.uber));
        return list;
    }

    public void onFilterClicked(View v){
        FilterPopup filterPopup = new FilterPopup(recyclerView, listener, cabListings);
        filterPopup.showPopupWindow(v);
    }

    private void initAutocomplete(){
        PlaceAutocompleteAdapter sourceAdapter = new PlaceAutocompleteAdapter(this, placesClient, (place) -> {
            sourcePlace = place;
            sourceAutocomplete.setText(place.getName());
            sourceAutocomplete.dismissDropDown();
            hideKeyboard();
        });
        sourceAutocomplete.setAdapter(sourceAdapter);

        PlaceAutocompleteAdapter destinationAdapter = new PlaceAutocompleteAdapter(this, placesClient, (place) -> {
            destinationPlace = place;
            destinationAutocomplete.setText(place.getName());
            destinationAutocomplete.dismissDropDown();
            hideKeyboard();
        });
        destinationAutocomplete.setAdapter(destinationAdapter);
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getRootView().getWindowToken(), 0);
    }
}
