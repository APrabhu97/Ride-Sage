package com.anish.ridesage;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

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

    Button buttonReset;
    String filterPlatformValue = "";
    String filterSeatValue = "";
    String filterTierValue = "";


    // setPlatformSpinnerData
    Spinner dynamicPlatformSpinner;
    String[] platforms = new String[]{"", "Uber", "Lyft"};

    // setSeatSpinnerData
    Spinner dynamicSeatSpinner;
    String[] seats = new String[]{"", "3", "5"};

    // setTierSpinnerData
    Spinner dynamicTierSpinner;
    String[] tiers = new String[]{"", "Economy", "Premium"};

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

        // Configure reset button on popup
        Button buttonReset = popupView.findViewById(R.id.buttonReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickResetButton();
            }
        });

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
        this.filters.put("seats", filterSeatValue);
        this.filters.put("tier", filterTierValue);
    }

    private void setPlatformSpinnerData(View view) {
        dynamicPlatformSpinner = (Spinner) view.findViewById(R.id.platformSelect);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, platforms) {
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                // Set the Text color
                tv.setTextColor(Color.BLACK);

                return view;
            }
        };
        dynamicPlatformSpinner.setAdapter(adapter);
        dynamicPlatformSpinner.setSelection(Arrays.asList(platforms).indexOf(filterPlatformValue));  // 1. For reset, set index to 0 (empty string)
        dynamicPlatformSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        dynamicSeatSpinner = (Spinner) view.findViewById(R.id.seatSelect);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                R.layout.item, seats);
        dynamicSeatSpinner.setAdapter(adapter);
        dynamicSeatSpinner.setSelection(Arrays.asList(seats).indexOf(filterSeatValue));
        dynamicSeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        dynamicTierSpinner = (Spinner) view.findViewById(R.id.tierSelect);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                R.layout.item, tiers);
        dynamicTierSpinner.setAdapter(adapter);
        dynamicTierSpinner.setSelection(Arrays.asList(tiers).indexOf(filterTierValue));
        dynamicTierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void onClickResetButton() {
        System.out.println("Worked");

        dynamicPlatformSpinner.setSelection(Arrays.asList(platforms).indexOf(""));
        dynamicSeatSpinner.setSelection(Arrays.asList(seats).indexOf(""));
        dynamicTierSpinner.setSelection(Arrays.asList(tiers).indexOf(""));

        filterPlatformValue = "";
        filterSeatValue = "";
        filterTierValue = "";

        CabAdapter adapter = new CabAdapter(getFilteredData(), listener);  // LAST STEP: Call these 3 lines below
        setMapData();
        rv.setAdapter(adapter);
    }

}