package com.example.conferencebookingapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Karin
 */
public class Downloader extends AsyncTask<String, Void, String> {
    private static final String TAG = "Downloader";
    private String token;
    private WeakReference<CallbackActivity> context;
    private String message;

    public Downloader(CallbackActivity context) {
        this("", context, "");
    }

    public Downloader(String token, CallbackActivity context) {
        this(token, context, "");
    }

    public Downloader(CallbackActivity context, String message) {
        this("", context, message);
    }

    public Downloader(String token, CallbackActivity context, String message) {
        super();
        this.token = token;
        this.context = new WeakReference<>(context);
        this.message = message;
    }


    @Override
    protected void onPostExecute(String s) {
        if (context.get() != null) {
            try {
                context.get().onDownloadComplete(s, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

            if(!token.equals("")) {
                String tokenRequest = "Token %s";

                connection.setRequestProperty("Authorization",String.format(tokenRequest, token));
                Log.d(TAG, "doInBackground: tokenRequest is: " + String.format(tokenRequest, token));
            }
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
