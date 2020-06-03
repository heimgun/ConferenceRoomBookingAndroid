package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Booking implements Parcelable {
    private ConferenceRoom room;
    private String chosenDate;
    private String bookingCode;
    private Seating chosenSeating;
    private int numberOfPeople;
    private List<FoodItem> chosenFoodAndBeverages;
    private List<TechnologyItem> chosenTechnologies;

    public Booking() {
        room = null;
        chosenDate = "";
        bookingCode = "";
        chosenSeating = null;
        numberOfPeople = 0;
        chosenFoodAndBeverages = new ArrayList<>();
        chosenTechnologies = new ArrayList<>();
    }

    protected Booking(Parcel in) {
        room = in.readParcelable(ConferenceRoom.class.getClassLoader());
        chosenDate = in.readString();
        bookingCode = in.readString();
        chosenSeating = in.readParcelable(Seating.class.getClassLoader());
        numberOfPeople = in.readInt();
        chosenFoodAndBeverages = in.createTypedArrayList(FoodItem.CREATOR);
        chosenTechnologies = in.createTypedArrayList(TechnologyItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(room, flags);
        dest.writeString(chosenDate);
        dest.writeString(bookingCode);
        dest.writeParcelable(chosenSeating, flags);
        dest.writeInt(numberOfPeople);
        dest.writeTypedList(chosenFoodAndBeverages);
        dest.writeTypedList(chosenTechnologies);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public ConferenceRoom getRoom() {
        return room;
    }

    public void setRoom(ConferenceRoom room) {
        this.room = room;
    }

    public String getChosenDate() {
        return chosenDate;
    }

    public void setChosenDate(String chosenDate) {
        this.chosenDate = chosenDate;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Seating getChosenSeating() {
        return chosenSeating;
    }

    public void setChosenSeating(Seating chosenSeating) {
        this.chosenSeating = chosenSeating;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public List<FoodItem> getChosenFoodAndBeverages() {
        return chosenFoodAndBeverages;
    }

    public void addFoodAndBeverage(FoodItem foodItem) {
        chosenFoodAndBeverages.add(foodItem);
    }

    public List<TechnologyItem> getChosenTechnologies() {
        return chosenTechnologies;
    }

    public void addTechnology(TechnologyItem technologyItem) {
        chosenTechnologies.add(technologyItem);
    }
}
