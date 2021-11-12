package com.anish.ridesage;
import android.os.Parcel;
import android.os.Parcelable;

public class CabItem implements Parcelable {
    private String provider;
    private Integer cost;
    private String pickupTime;
    private String cabType;
    private Integer maxSeats;
    private Integer iconId;

    public CabItem(String provider, Integer cost, String pickupTime, String cabType, Integer maxSeats, Integer iconId) {
        this.provider = provider;
        this.cost = cost;
        this.pickupTime = pickupTime;
        this.cabType = cabType;
        this.maxSeats = maxSeats;
        this.iconId = iconId;
    }

    protected CabItem(Parcel in) {
        provider = in.readString();
        if (in.readByte() == 0) {
            cost = null;
        } else {
            cost = in.readInt();
        }
        pickupTime = in.readString();
        cabType = in.readString();
        if (in.readByte() == 0) {
            maxSeats = null;
        } else {
            maxSeats = in.readInt();
        }
        if (in.readByte() == 0) {
            iconId = null;
        } else {
            iconId = in.readInt();
        }
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

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getCabType() {
        return cabType;
    }

    public void setCabType(String cabType) {
        this.cabType = cabType;
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
        parcel.writeString(pickupTime);
        parcel.writeInt(cost);
        parcel.writeString(cabType);
        parcel.writeInt(maxSeats);
        parcel.writeInt(iconId);


    }
}
