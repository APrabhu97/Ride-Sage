package com.anish.ridesage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class BookRideActivity extends AppCompatActivity implements PointsParser.TaskLoadedCallback{

    private PlacesClient placesClient;
    private GoogleMap mMap;
    private TextView source, destination;
    private Place sourcePlace, destinationPlace;
    private Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_ride);
        source = findViewById(R.id.bookSource);
        destination = findViewById(R.id.bookDestination);
        TextView price = findViewById(R.id.price);

        Intent i = getIntent();
        double cost = i.getDoubleExtra("cost", 0.0);
        sourcePlace = i.getParcelableExtra("source");
        destinationPlace = i.getParcelableExtra("destination");

        DecimalFormat df = new DecimalFormat("#0.00");
        price.setText(df.format(cost));
        source.setText(sourcePlace.getName());
        destination.setText(destinationPlace.getName());

        Places.initialize(this, getString(R.string.com_google_android_geo_API_KEY));
        placesClient = Places.createClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((map)-> {
            this.mMap = map;
            mMap.addMarker(new MarkerOptions()
                    .position(sourcePlace.getLatLng()).title("Source"));
            mMap.addMarker(new MarkerOptions()
                    .position(destinationPlace.getLatLng()).title("Destination"));
            updateLocationUI();
            setRoute();
        });
    }

    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        try {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void onButtonClicked(View v){
        Intent myIntent = new Intent(this, BookingCompleteActivity.class);
        startActivity(myIntent);
    }

    private void setRoute(){
        if(sourcePlace != null && destinationPlace != null){
            String url = getUrl(sourcePlace.getLatLng(), destinationPlace.getLatLng(), "driving");
            new FetchURL(this).execute(url, "driving");
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(sourcePlace.getLatLng());
            builder.include(destinationPlace.getLatLng());
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10);
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, padding);
            mMap.animateCamera(cu);
        }
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=" + getString(R.string.com_google_android_geo_API_KEY);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline != null){
            currentPolyline.remove();
        }
        currentPolyline = mMap.addPolyline((PolylineOptions)values[0]);
    }

}
