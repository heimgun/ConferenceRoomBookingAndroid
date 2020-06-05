package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Karin
 */
public class Seating implements Parcelable {

    private String id;
    private String description;
    private String numberOfPeople;


    public Seating () {
        id = "";
        description = "";
        numberOfPeople = "";
    }

    protected Seating(Parcel in) {
        id = in.readString();
        description = in.readString();
        numberOfPeople = in.readString();
    }

    public static final Creator<Seating> CREATOR = new Creator<Seating>() {
        @Override
        public Seating createFromParcel(Parcel in) {
            return new Seating(in);
        }

        @Override
        public Seating[] newArray(int size) {
            return new Seating[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(numberOfPeople);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
