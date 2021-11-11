package com.anish.ridesage;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CabListings extends AppCompatActivity {
    List<CabItem> cabListings;
    RecyclerView recyclerView;

    CabItemClickListener listener = (url) -> {
        Log.i("CabListings", "reached");
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_listings);
        cabListings = getCabListings();
        recyclerView = findViewById(R.id.recycler_view);
        CabAdapter adapter = new CabAdapter(cabListings, listener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<CabItem> getCabListings(){
        List<CabItem> list = new ArrayList<>();
        list.add(new CabItem("Uber", 13, "8 min", "Premium", 3, R.drawable.uber));
        list.add(new CabItem("Lyft", 15, "3 min", "Economy", 3, R.drawable.lyft));
        list.add(new CabItem("Lyft", 19, "6 min", "Premium", 3, R.drawable.lyft));
        list.add(new CabItem("Uber", 21, "9 min", "Economy", 3, R.drawable.uber));
        list.add(new CabItem("Uber", 19, "10 min", "Premium", 5, R.drawable.uber));
        return list;
    }
}
