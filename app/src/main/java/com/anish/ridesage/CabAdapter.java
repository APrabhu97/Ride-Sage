package com.anish.ridesage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CabAdapter extends RecyclerView.Adapter<CabAdapter.ViewHolder> {

    private List<CabItem> cabList;
    private CabItemClickListener CabItemClickListener;
    private Context context;

    public CabAdapter(List<CabItem> movieList, CabItemClickListener listener){
        this.cabList = movieList;
        this.CabItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listView = inflater.inflate(R.layout.cab_item, parent, false);
        return new ViewHolder(listView, CabItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting the movie data in the individual view holder
        CabItem m = cabList.get(position);
        holder.cabType.setText(m.getCabTier());
        holder.maxSeats.setText(m.getMaxSeats().toString());
        holder.pickupTime.setText(m.getPickupTime()+" min");
        holder.price.setText("$"+m.getCost());
        holder.cabIcon.setImageResource(m.getIconId());
        holder.cabItem = m;
    }

    @Override
    public int getItemCount() {
        return cabList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView cabType;
        public TextView maxSeats;
        public TextView pickupTime;
        public TextView price;
        public ImageView cabIcon;
        private CabItemClickListener listener;
        private View itemView;
        private CabItem cabItem;

        public ViewHolder(@NonNull View itemView, CabItemClickListener passedListener) {
            super(itemView);
            cabType = (TextView) itemView.findViewById(R.id.cabType);
            maxSeats = (TextView) itemView.findViewById(R.id.maxSeats);
            pickupTime = (TextView) itemView.findViewById(R.id.pickupTime);
            price = (TextView) itemView.findViewById(R.id.cost);
            cabIcon = (ImageView) itemView.findViewById(R.id.providerIcon);
            this.itemView = itemView;
            this.listener = passedListener;
            itemView.setOnClickListener(this); //set short click listener
        }

        @Override
        public void onClick(View v) {
            //opening the trailer of the movie on click of movie item
            v.setBackgroundColor(-7829368);

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            listener.onClick(cabItem);

        }
    }
}

