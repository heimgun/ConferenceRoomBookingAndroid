package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/*import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

 */

public class MainActivity extends AppCompatActivity {

    Button createUser, searchForRooms;
    String urlAddress = "https://dev-be.timetomeet.se/service/rest/conferenceroom/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUser = (Button) findViewById(R.id.CreateUserBtn);
        searchForRooms = (Button) findViewById(R.id.SearchRoomsBtn);


        createUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateUserView.class));
            }
        });

        searchForRooms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AvailableRoomView.class));
            }
        });



        Downloader downloader = new Downloader();

        downloader.execute(urlAddress);




    }
}
