package com.anish.ridesage;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterPopup {

    RecyclerView rv;
    CabItemClickListener listener;
    List<CabItem> cabListings;
    Map<String, String> filters;

    String filterPlatformValue = "";
    String filterSeatValue = "";
    String filterTierValue = "";

    public FilterPopup(RecyclerView rv, CabItemClickListener listener, List<CabItem> cabListings,
                       Map<String, String> filters) {
        this.rv = rv;
        this.listener = listener;
        this.cabListings = cabListings;
        this.filters = filters;

        this.filterPlatformValue = filters.get("platform");
        this.filterSeatValue = filters.get("seats");
        this.filterTierValue = filters.get("tier");


    }

    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_filter_popup, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        dimBackground(popupWindow);

        setPlatformSpinnerData(popupView);
        setSeatSpinnerData(popupView);
        setTierSpinnerData(popupView);
    }

    private void setMapData() {  // 2nd to last step step after resetting: Call this method (filterPlatformValue = "", etc.)
        this.filters.put("platform", filterPlatformValue);
        this.filters.put("seats", filterTierValue);
        this.filters.put("tier", filterSeatValue);
    }

    private void setPlatformSpinnerData(View view) {
        Spinner dynamicSpinner = (Spinner) view.findViewById(R.id.platformSelect);
        String[] platforms = new String[]{"", "Uber", "Lyft"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                R.layout.item, platforms);
        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setSelection(Arrays.asList(platforms).indexOf(filterPlatformValue));  // 1. For reset, set index to 0 (empty string)
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view,
                                       final int position, long id) {
                filterPlatformValue = (String) parent.getItemAtPosition(position);
                CabAdapter adapter = new CabAdapter(getFilteredData(), listener);
                setMapData();
                rv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private List<CabItem> getFilteredData(){
        List<CabItem> items = cabListings;
        if(filterPlatformValue != ""){  // 2. Make sure all of these are empty
            items = items.stream()
                    .filter(cabItem -> cabItem.getProvider() == filterPlatformValue)
                    .collect(Collectors.toList());
        }
        if(filterSeatValue != ""){
            items = items.stream()
                    .filter(cabItem -> cabItem.getMaxSeats() == parseInt(filterSeatValue))
                    .collect(Collectors.toList());
        }
        if(filterTierValue != ""){
            items = items.stream()
                    .filter(cabItem -> cabItem.getCabTier() == filterTierValue)
                    .collect(Collectors.toList());
        }
        // cabListings = items;
        return items;
    }

    private void setSeatSpinnerData(View view) {
        Spinner dynamicSpinner = (Spinner) view.findViewById(R.id.seatSelect);
        String[] platforms = new String[]{"", "3", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                R.layout.item, platforms);
        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setSelection(Arrays.asList(platforms).indexOf(filterSeatValue));
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                filterSeatValue = (String) parent.getItemAtPosition(position);
                CabAdapter adapter = new CabAdapter(getFilteredData(), listener);  // LAST STEP: Call these 3 lines below
                setMapData();
                rv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setTierSpinnerData(View view) {
        Spinner dynamicSpinner = (Spinner) view.findViewById(R.id.tierSelect);
        String[] platforms = new String[]{"", "Economy", "Premium"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                R.layout.item, platforms);
        dynamicSpinner.setAdapter(adapter);
        dynamicSpinner.setSelection(Arrays.asList(platforms).indexOf(filterTierValue));
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                filterTierValue = (String) parent.getItemAtPosition(position);
                CabAdapter adapter = new CabAdapter(getFilteredData(), listener);
                setMapData();
                rv.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void dimBackground(@NonNull PopupWindow popupWindow) {
        View container = (View) popupWindow.getContentView().getParent();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);
    }

}