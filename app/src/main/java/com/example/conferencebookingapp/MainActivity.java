package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createUser, searchForRooms;


        //Not sure if this should be a class or not, but is supposed to connect to the API
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //Network-configurations
                try {
                    //URL
                    URL timeToMeetEndPoint = new URL("http://dev-be.timetomeet.se/service/rest/");

                    //Connection
                    HttpURLConnection con = (HttpURLConnection) timeToMeetEndPoint.openConnection();

                } catch (MalformedURLException e) {
                    System.out.println("URL fail");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("Connection fail");
                    e.printStackTrace();
                }

            }
        });


    }
}
