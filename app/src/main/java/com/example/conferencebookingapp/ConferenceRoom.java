package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.telecom.Conference;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConferenceRoom {

    private String roomId;
    private String name;
    private String city;
    private String plantId;
    private String preNoonHours;
    private String afternoonHours;
    private String fullDayHours;
    private String fullDayPrice;
    private String preNoonPrice;
    private String afternoonPrice;
    private String preNoonBookingCode;
    private String afternoonBookingCode;
    private String description;
    private int maxNumberOfPeople;
    private Map<String, String> seatings;
    private Map<String, String> seatingIds;
    private Map<String, String> technologies;
    private List<String> imageUrls;


    public ConferenceRoom() {
        roomId = "";
        name = "";
        city = "";
        plantId = "";
        preNoonHours = "";
        afternoonHours = "";
        fullDayHours = "";
        fullDayPrice = "";
        preNoonPrice = "";
        afternoonPrice = "";
        preNoonBookingCode = "";
        afternoonBookingCode = "";
        description = "";
        maxNumberOfPeople = 0;
        seatings = new HashMap<>();
        seatingIds = new HashMap<>();
        technologies = new HashMap<>();
        imageUrls = new ArrayList<>();
    }


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

    public String getPreNoonHours() {
        return preNoonHours;
    }

    public void setPreNoonHours(String preNoonHours) {
        this.preNoonHours = preNoonHours;
    }

    public String getAfternoonHours() {
        return afternoonHours;
    }

    public void setAfternoonHours(String afternoonHours) {
        this.afternoonHours = afternoonHours;
    }

    public String getFullDayHours() {
        return fullDayHours;
    }

    public void setFullDayHours(String fullDayHours) {
        this.fullDayHours = fullDayHours;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getMaxNumberOfPeople() {
        return maxNumberOfPeople;
    }

    public void setMaxNumberOfPeople(int maxNumberOfPeople) {
        this.maxNumberOfPeople = maxNumberOfPeople;
    }

    public Map<String, String> getSeatings() {
        return seatings;
    }

    public void addSeating(String description, String numberOfPeople) {
        seatings.put(description, numberOfPeople);
    }

    public Map<String, String> getSeatingIds() {
        return seatingIds;
    }

    public void addSeatingId(String description, String id) {
        seatings.put(description, id);
    }

    public Map<String, String> getTechnologies() {
        return technologies;
    }

    public void addTechnology(String description, String id) {
        technologies.put(description, id);
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void addImageUrl(String url) {
        imageUrls.add(url);
    }
}
