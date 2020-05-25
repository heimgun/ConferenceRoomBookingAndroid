package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.telecom.Conference;

import androidx.recyclerview.widget.RecyclerView;

public class ConferenceRoom {

    private String roomId;
    private String name;
    private String city;
    private String plantId;
    private String fullDayPrice;
    private String preNoonPrice;
    private String afternoonPrice;
    private String preNoonBookingCode;
    private String afternoonBookingCode;


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

    public String getFullDayPrice() {
        return fullDayPrice;
    }

    public void setFullDayPrice(String price) {
        this.fullDayPrice = price;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getPreNoonPrice() {
        return preNoonPrice;
    }

    public void setPreNoonPrice(String preNoonPrice) {
        this.preNoonPrice = preNoonPrice;
    }

    public String getAfternoonPrice() {
        return afternoonPrice;
    }

    public void setAfternoonPrice(String afternoonPrice) {
        this.afternoonPrice = afternoonPrice;
    }

    public String getPreNoonBookingCode() {
        return preNoonBookingCode;
    }

    public void setPreNoonBookingCode(String preNoonBookingCode) {
        this.preNoonBookingCode = preNoonBookingCode;
    }

    public String getAfternoonBookingCode() {
        return afternoonBookingCode;
    }

    public void setAfternoonBookingCode(String afternoonBookingCode) {
        this.afternoonBookingCode = afternoonBookingCode;
    }
}
