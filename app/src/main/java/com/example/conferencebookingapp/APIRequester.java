package com.example.conferencebookingapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class APIRequester extends AsyncTask<String, Void, String> {

    private static final String TAG = "APIRequester";

    private String token;
    private WeakReference<CallbackActivity> context;
    private String message;


    public APIRequester(CallbackActivity context, String message) {
        this("", context, message);
    }

    public APIRequester(String token, CallbackActivity context, String message) {
        super();
        this.token = token;
        this.context = new WeakReference<>(context);
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
            if (message.equals(ReceiptView.BOOK_ROOM_MESSAGE) || message.equals(ReceiptView.FINAL_BOOKING_CODE_MESSAGE)){
                connection.setRequestMethod("PUT");
            } else {
                connection.setRequestMethod("POST");
            }
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept","application/json");


            if(!token.equals("")) {
                String tokenRequest = "Token %s";

                connection.setRequestProperty("Authorization",String.format(tokenRequest, token));
            }

            connection.setDoOutput(true);
            connection.connect();


            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

            if (strings.length > 1) {
                wr.write(strings[1]);
                wr.flush();
            }

            int responseCode = connection.getResponseCode();
            InputStream inputStream = responseCode/100 == 2 ? connection.getInputStream() : connection.getErrorStream();
            reader = new BufferedReader((new InputStreamReader(inputStream)));

            StringBuilder resultSb = new StringBuilder();
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                resultSb.append(line).append("\n");
            }

            if (responseCode/100 == 2) {
                return resultSb.toString();
            } else {
                throw new IOException((resultSb.toString()));
            }


        } catch(MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IOException reading data: " + e.getMessage());
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
        if(context.get() != null) {
            try {
                context.get().onDownloadComplete(s, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
