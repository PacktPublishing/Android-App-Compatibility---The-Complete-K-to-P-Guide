package com.example.jonnd.fuelfinder.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FillUp {
    @PrimaryKey(autoGenerate = true)
    private long Id;
    @ForeignKey(entity = Station.class, childColumns = "stationId", parentColumns = "Id")
    private long stationId;
    @ForeignKey(entity = User.class, childColumns = "userId", parentColumns = "Id")
    private long userId;
    private int numberOfGallons;
    private int pricePerGallon;
    private String fuelType;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getNumberOfGallons() {
        return numberOfGallons;
    }

    public void setNumberOfGallons(int numberOfGallons) {
        this.numberOfGallons = numberOfGallons;
    }

    public int getPricePerGallon() {
        return pricePerGallon;
    }

    public void setPricePerGallon(int pricePerGallon) {
        this.pricePerGallon = pricePerGallon;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}
