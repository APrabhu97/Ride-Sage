package com.anish.ridesage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class PlaceAutocompleteAdapter
        extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private List<AutocompletePrediction> mResultList;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private OnPlaceClickListener onPlaceClickListener;

    public PlaceAutocompleteAdapter(Context context, PlacesClient placesClient, OnPlaceClickListener onPlaceClickListener) {
        super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
        this.placesClient = placesClient;
        this.onPlaceClickListener = onPlaceClickListener;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public AutocompletePrediction getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        AutocompletePrediction item = getItem(position);
        TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
        textView1.setTextColor(Color.BLACK);
        textView2.setTextColor(Color.BLACK);
        textView1.setText(item.getPrimaryText(STYLE_BOLD));
        textView2.setText(item.getSecondaryText(STYLE_BOLD));
        row.setOnClickListener(v -> {
            if (onPlaceClickListener != null) {
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                final FetchPlaceRequest newRequest =FetchPlaceRequest.builder(item.getPlaceId(), placeFields).build();
                placesClient.fetchPlace(newRequest).addOnSuccessListener((response) -> {
                    Place place = response.getPlace();
                    onPlaceClickListener.onPlaceClicked(place);
                    Log.i(TAG, "Place found: " + place.getName());
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        // Handle error with given status code.
                        Log.e(TAG, "Place not found: " + exception.getMessage());
                    }
                });
            }
        });

        return row;
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<AutocompletePrediction> filterData = new ArrayList<>();
                if (constraint != null) {
                    filterData = getAutocomplete(constraint);
                }
                results.values = filterData;
                if (filterData != null) {
                    results.count = filterData.size();
                } else {
                    results.count = 0;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResultList = (List<AutocompletePrediction>)convertObjectToList(results.values);
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    private List<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setQuery(constraint.toString())
                .setCountries("US")
                .build();
        Task<FindAutocompletePredictionsResponse> results =placesClient.findAutocompletePredictions(newRequest);
        try {
            Tasks.await(results, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        try {
            FindAutocompletePredictionsResponse autocompletePredictions = results.getResult();
            return autocompletePredictions.getAutocompletePredictions();
        } catch (RuntimeExecutionException e) {
            Toast.makeText(getContext(), "Error contacting API: " + e.toString(),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error getting autocomplete prediction API call", e);
            return null;
        }
    }

    interface OnPlaceClickListener {
        void onPlaceClicked(Place place);
    }

}
