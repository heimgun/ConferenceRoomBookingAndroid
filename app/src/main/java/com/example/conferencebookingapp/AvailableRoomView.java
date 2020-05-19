package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class AvailableRoomView extends AppCompatActivity {

    private static final String TAG = "AvailableRoomView";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room_view);

        String urlAddress = getIntent().getStringExtra(MainActivity.URL_MESSAGE);
        Log.d(TAG, "onCreate: urlAddress is: " + urlAddress);

       // recyclerView = findViewById(R.id.resultsRecyclerView);
       // recyclerView.setHasFixedSize(true);

        Downloader downloader = new Downloader();

        downloader.execute(urlAddress);
    }


    public void onDownLoadComplete(List<ConferenceRoom> availableRooms) {

    }
}
