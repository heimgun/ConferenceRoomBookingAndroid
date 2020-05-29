package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plant implements Parcelable {

    private String plantId;
    private String name;
    private List<ConferenceRoom> rooms;
    private String city;
    private String address;
    private List<String> imageUrls;
    private String facts;
    private String priceFrom;
    private String numberOfAvailableRooms;
    private Map<String, String> foodAndBeverages;

    public Plant() {
        plantId = "";
        name = "";
        rooms = new ArrayList<>();
        city = "";
        address = "";
        imageUrls = new ArrayList<>();
        facts = "";
        priceFrom = "";
        numberOfAvailableRooms = "";
        foodAndBeverages = new HashMap<>();

    }

    protected Plant(Parcel in) {
        plantId = in.readString();
        name = in.readString();
        city = in.readString();
        address = in.readString();
        imageUrls = in.createStringArrayList();
        facts = in.readString();
        priceFrom = in.readString();
        numberOfAvailableRooms = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(plantId);
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeStringList(imageUrls);
        dest.writeString(facts);
        dest.writeString(priceFrom);
        dest.writeString(numberOfAvailableRooms);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Plant> CREATOR = new Creator<Plant>() {
        @Override
        public Plant createFromParcel(Parcel in) {
            return new Plant(in);
        }

        @Override
        public Plant[] newArray(int size) {
            return new Plant[size];
        }
    };

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConferenceRoom> getRooms() {
        return rooms;
    }

    public void addRoom(ConferenceRoom room) {
        rooms.add(room);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
    public void addImageUrl(String imageUrl) {
        imageUrls.add(imageUrl);
    }

    public String getFacts() {
        return facts;
    }

    public void setFacts(String facts) {
        this.facts = facts;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getNumberOfAvailableRooms() {
        return numberOfAvailableRooms;
    }

    public void setNumberOfAvailableRooms(String numberOfAvailableRooms) {
        this.numberOfAvailableRooms = numberOfAvailableRooms;
    }

    public Map<String, String> getFoodAndBeverages() {
        return foodAndBeverages;
    }

    public void addFoodAndBeverage(String description, String price) {
        foodAndBeverages.put(description, price);
    }
}
