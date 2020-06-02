package com.example.conferencebookingapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class APIRequester extends AsyncTask<String, Void, String> {

    private static final String TAG = "APIRequester";
    private String token;
    private CallbackActivity context;
    private String message;


    public APIRequester(CallbackActivity context) {
        this("", context, "");
    }

    public APIRequester(CallbackActivity context, String message) {
        this("", context, message);
    }

    public APIRequester(String token, CallbackActivity context){
       this(token, context, "");
    }

    public APIRequester(String token, CallbackActivity context, String message) {
        super();
        this.token = token;
        this.context = context;
        this.message = message;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: start");
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(strings[0]);
            Log.d(TAG, "doInBackground: url is: " + url);

            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept","application/json");


            if(!token.equals("")) {
                String tokenRequest = "Token %s";

                connection.setRequestProperty("Authorization",String.format(tokenRequest, token));
                Log.d(TAG, "doInBackground: tokenRequest is: " + String.format(tokenRequest, token));
            }

            connection.setDoOutput(true);
            connection.connect();


            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

            if (strings.length > 1) {
                Log.d(TAG, "doInBackground: jsonRequest is: " + strings[1]);
                wr.write(strings[1]);
                wr.flush();
            }

            if(connection.getResponseCode()/100 == 2) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                Log.d(TAG, "doInBackground: reader ready");

            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                Log.e(TAG, "doInBackground: response code is: " + connection.getResponseCode());
            }

            StringBuilder result = new StringBuilder();
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }
            Log.d(TAG, "doInBackground: result is: " + result.toString());




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

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
            try {
                context.onDownloadComplete(s, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }



    }

}
