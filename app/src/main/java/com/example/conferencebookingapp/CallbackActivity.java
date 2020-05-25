package com.example.conferencebookingapp;

import org.json.JSONException;

import java.util.List;

public interface CallbackActivity {

    void onDownloadComplete(String results, String message) throws JSONException;
}
