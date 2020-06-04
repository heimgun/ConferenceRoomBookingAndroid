package com.example.conferencebookingapp;

import org.json.JSONException;


public interface CallbackActivity {

    void onDownloadComplete(String results, String message) throws JSONException;

}
