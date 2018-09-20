package com.example.jonnd.fuelfinder.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Station {
    @PrimaryKey(autoGenerate = true)
    private long Id;
    private String stationName;
    private String address;
    private String thumbnailFilePath;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumbnailFilePath() {
        return thumbnailFilePath;
    }

    public void setThumbnailFilePath(String thumbnailFilePath) {
        this.thumbnailFilePath = thumbnailFilePath;
    }
}
