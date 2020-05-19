package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.telecom.Conference;

import androidx.recyclerview.widget.RecyclerView;

public class ConferenceRoom {

    private String name;
    private String city;
    private String plant;
    private String price;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void findPlant(int plantNumber) {

    }
}
