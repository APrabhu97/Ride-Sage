package com.anish.ridesage;
import android.os.Parcel;
import android.os.Parcelable;

public class CabItem implements Parcelable {
    private String provider;
    private int cost;
    private int pickupTime;
    private String cabTier;
    private int maxSeats;
    private int iconId;

    public CabItem(String provider, Integer cost, int pickupTime, String cabTier, Integer maxSeats, Integer iconId) {
        this.provider = provider;
        this.cost = cost;
        this.pickupTime = pickupTime;
        this.cabTier = cabTier;
        this.maxSeats = maxSeats;
        this.iconId = iconId;
    }

    public CabItem(CabItemBuilder builder) {
        this.provider = builder.provider;
        this.cost = builder.cost;
        this.pickupTime = builder.pickupTime;
        this.cabTier = builder.cabTier;
        this.maxSeats = builder.maxSeats;
        this.iconId = builder.iconId;
    }

    protected CabItem(Parcel in) {
        provider = in.readString();
        cost = in.readInt();
        pickupTime = in.readInt();
        cabTier = in.readString();
        maxSeats = in.readInt();
        iconId = in.readInt();
    }

    public static final Creator<CabItem> CREATOR = new Creator<CabItem>() {
        @Override
        public CabItem createFromParcel(Parcel in) {
            return new CabItem(in);
        }

        @Override
        public CabItem[] newArray(int size) {
            return new CabItem[size];
        }
    };

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public int getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(int pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getCabTier() {
        return cabTier;
    }

    public void setCabTier(String cabTier) {
        this.cabTier = cabTier;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(provider);
        parcel.writeInt(pickupTime);
        parcel.writeInt(cost);
        parcel.writeString(cabTier);
        parcel.writeInt(maxSeats);
        parcel.writeInt(iconId);
    }

    public static class CabItemBuilder{
        private final String provider;
        private final int iconId;

        private int cost;
        private int pickupTime;
        private String cabTier;
        private int maxSeats;

        public CabItemBuilder(String provider, int iconId){
            this.provider = provider;
            this.iconId = iconId;
        }

        public CabItemBuilder cost(int cost){
            this.cost = cost;
            return this;
        }

        public CabItemBuilder pickupTime(int pickupTime){
            this.pickupTime = pickupTime;
            return this;
        }

        public CabItemBuilder cabTier(String cabTier){
            this.cabTier = cabTier;
            return this;
        }

        public CabItemBuilder maxSeats(int maxSeats){
            this.maxSeats = maxSeats;
            return this;
        }

        public CabItem build(){
            CabItem cb = new CabItem(this);
            return cb;
        }
    }
}
