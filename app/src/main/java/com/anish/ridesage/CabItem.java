package com.anish.ridesage;

import java.util.HashMap;
import java.util.Map;

public class CabItem {
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
}
