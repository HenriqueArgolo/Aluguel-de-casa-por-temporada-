package com.example.casaportemporada.Model;

import java.io.Serializable;

public class FilterModel implements Serializable {
    private int roomFilter;
    private int bathroomFilter;
    private int garageFilter;

    public int getRoomFilter() {
        return roomFilter;
    }

    public void setRoomFilter(int roomFilter) {
        this.roomFilter = roomFilter;
    }

    public int getBathroomFilter() {
        return bathroomFilter;
    }

    public void setBathroomFilter(int bathroomFilter) {
        this.bathroomFilter = bathroomFilter;
    }

    public int getGarageFilter() {
        return garageFilter;
    }

    public void setGarageFilter(int garageFilter) {
        this.garageFilter = garageFilter;
    }
}
