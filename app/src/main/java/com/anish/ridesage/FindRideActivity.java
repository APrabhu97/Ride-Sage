package com.anish.ridesage;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class FindRideActivity extends AppCompatActivity  implements PointsParser.TaskLoadedCallback {

    private static final String TAG = "FindRideActivity";
    Place sourcePlace, destinationPlace;
    AutoCompleteTextView sourceAutocomplete, destinationAutocomplete;
    private GoogleMap mMap;
    private Polyline currentPolyline;

    private PlacesClient placesClient;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_ride);
        sourceAutocomplete = (AutoCompleteTextView) findViewById(R.id.findRideSource);
        destinationAutocomplete = (AutoCompleteTextView) findViewById(R.id.findRideDestination);

        Places.initialize(this, getString(R.string.com_google_android_geo_API_KEY));
        placesClient = Places.createClient(this);
        initAutocomplete();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((map)-> {
            this.mMap = map;
            getLocationPermission();
            updateLocationUI();
            getDeviceLocation();
        });

    }

    private void initAutocomplete(){
        Log.d(TAG, "init: initializing");
        PlaceAutocompleteAdapter sourceAdapter = new PlaceAutocompleteAdapter(this, placesClient, (place) -> {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
            mMap.addMarker(new MarkerOptions()
                    .position(place.getLatLng()).title("Source"));
            sourcePlace = place;
            sourceAutocomplete.setText(place.getName());
            setRoute();
            sourceAutocomplete.dismissDropDown();
            hideKeyboard();
        });
        sourceAutocomplete.setAdapter(sourceAdapter);

        PlaceAutocompleteAdapter destinationAdapter = new PlaceAutocompleteAdapter(this, placesClient, (place) -> {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
            mMap.addMarker(new MarkerOptions()
                    .position(place.getLatLng()).title("Destination"));
            destinationPlace = place;
            destinationAutocomplete.setText(place.getName());
            setRoute();
            destinationAutocomplete.dismissDropDown();
            hideKeyboard();
        });
        destinationAutocomplete.setAdapter(destinationAdapter);
    }

    public void onButtonClicked(View v){
        Intent myIntent = new Intent(this, CabListings.class);
        myIntent.putExtra("source", sourcePlace);
        myIntent.putExtra("destination", destinationPlace);
        double distance = getDistance();
        myIntent.putExtra("distance", distance);
        startActivity(myIntent);
    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED){
                    List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ID);
                    FindCurrentPlaceRequest request =
                            FindCurrentPlaceRequest.builder(placeFields).build();
                    placesClient.findCurrentPlace(request).addOnSuccessListener((response) -> {
                        List<PlaceLikelihood> placeLikelihoods = response.getPlaceLikelihoods();
                        Place place = placeLikelihoods.get(0).getPlace();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
                    });
                }
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
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

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getRootView().getWindowToken(), 0);
    }

    private double getDistance()
    {
        Location locationA = new Location("source");

        locationA.setLatitude(sourcePlace.getLatLng().latitude);
        locationA.setLongitude(sourcePlace.getLatLng().longitude);

        Location locationB = new Location("des");

        locationB.setLatitude(destinationPlace.getLatLng().latitude);
        locationB.setLongitude(destinationPlace.getLatLng().longitude);

        double meters = locationA.distanceTo(locationB);
        double miles = meters/1609;

        return miles;
    }
}