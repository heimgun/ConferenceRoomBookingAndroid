package com.example.conferencebookingapp;

import android.app.Activity;

import com.google.gson.annotations.SerializedName;

public class Post {

    //Should contain JSON-references from a specific place in the API
    private String id;

    @SerializedName("fullDayPrice")
    private String fullDayPrice;

    private String conferenceRoomName;

    public String getId() {
        return id;
    }

    public String getFullDayPrice() {
        return fullDayPrice;
    }

    public String getConferenceRoomName() {
        return conferenceRoomName;
    }
}
