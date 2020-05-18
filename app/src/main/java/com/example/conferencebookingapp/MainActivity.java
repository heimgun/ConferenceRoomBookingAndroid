package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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
    TextView tw;
    String urlAddress = "https://dev-be.timetomeet.se/service/rest/conferenceroom/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tw = (TextView) findViewById(R.id.tw_result);

        /*
        //Retrofit connection to API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("dev-be.timetomeet.se/service/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //More retrofit-code should be implemented under this
        */

        Downloader downloader = new Downloader();

        downloader.execute(urlAddress);




    }
}
