package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConferenceRoom implements Parcelable{

    private Plant plant;

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
    private List<Seating> seatings;
    private List<TechnologyItem> technologies;
    private List<String> imageUrls;


    public ConferenceRoom() {
        plant = null;
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
        seatings = new ArrayList<>();
        technologies = new ArrayList<>();
        imageUrls = new ArrayList<>();
    }


    protected ConferenceRoom(Parcel in) {
        plant = in.readParcelable(Plant.class.getClassLoader());
        roomId = in.readString();
        name = in.readString();
        city = in.readString();
        plantId = in.readString();
        preNoonHours = in.readString();
        afternoonHours = in.readString();
        fullDayHours = in.readString();
        fullDayPrice = in.readString();
        preNoonPrice = in.readString();
        afternoonPrice = in.readString();
        preNoonBookingCode = in.readString();
        afternoonBookingCode = in.readString();
        description = in.readString();
        maxNumberOfPeople = in.readInt();
        imageUrls = in.createStringArrayList();
        seatings = new ArrayList<>();
        technologies = new ArrayList<>();
        in.readTypedList(seatings, Seating.CREATOR);
        in.readTypedList(technologies, TechnologyItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(plant, flags);
        dest.writeString(roomId);
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(plantId);
        dest.writeString(preNoonHours);
        dest.writeString(afternoonHours);
        dest.writeString(fullDayHours);
        dest.writeString(fullDayPrice);
        dest.writeString(preNoonPrice);
        dest.writeString(afternoonPrice);
        dest.writeString(preNoonBookingCode);
        dest.writeString(afternoonBookingCode);
        dest.writeString(description);
        dest.writeInt(maxNumberOfPeople);
        dest.writeStringList(imageUrls);
        dest.writeTypedList(seatings);
        dest.writeTypedList(technologies);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ConferenceRoom> CREATOR = new Creator<ConferenceRoom>() {
        @Override
        public ConferenceRoom createFromParcel(Parcel in) {
            return new ConferenceRoom(in);
        }

        @Override
        public ConferenceRoom[] newArray(int size) {
            return new ConferenceRoom[size];
        }
    };

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
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

    public List<Seating> getSeatings() {
        return seatings;
    }

    public void addSeating(Seating seating) {
        seatings.add(seating);
    }

    public List<TechnologyItem> getTechnologies() {
        return technologies;
    }

    public void addTechnology(TechnologyItem technologyItem) {
        technologies.add(technologyItem);
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void addImageUrl(String url) {
        imageUrls.add(url);
    }
}
