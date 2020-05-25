package com.example.conferencebookingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Downloader extends AsyncTask<String, Void, String> {
    private static final String TAG = "Downloader";
    private CallbackActivity context;
    private String message;

    public Downloader(CallbackActivity context) {
        this(context, "");
    }

    public Downloader(CallbackActivity  context, String message) {
        super();
        this.context = context;
        this.message = message;
    }


    @Override
    protected void onPostExecute(String s) {
        //System.out.println("Download complete. Resulting data is: " + s);

        //JsonParser parser = new JsonParser();
        //List<ConferenceRoom> availableRooms = parser.parseRoom(s);
        //Log.d(TAG, "onPostExecute: number of rooms is: " + availableRooms.size());
        try {
            context.onDownloadComplete(s, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }

            return result.toString();

        } catch(MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IOException reading data: " + e.getMessage());
        } catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. Needs permission? " + e.getMessage());
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Problem closing stream " + e.getMessage());
                }
            }
        }
        return null;
    }

}
