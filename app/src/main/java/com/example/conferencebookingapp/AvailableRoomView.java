package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AvailableRoomView extends AppCompatActivity {

    private static final String TAG = "AvailableRoomView";
    private List<ConferenceRoom> availableRooms;

    private TextView searchHeading;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean finishedDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room_view);

        availableRooms = new ArrayList<>();
        String urlAddress = getIntent().getStringExtra(MainActivity.URL_MESSAGE);
        Log.d(TAG, "onCreate: urlAddress is: " + urlAddress);

        Downloader downloader = new Downloader(AvailableRoomView.this);
        downloader.execute(urlAddress);

        searchHeading = findViewById(R.id.resultsTextView);
        searchHeading.setText("Sökresultat");
        recyclerView = findViewById(R.id.resultsRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AvailableRoomListAdapter(availableRooms);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: adapter set");

    }


    public void onDownLoadComplete(List<ConferenceRoom> results) {

        Log.d(TAG, "onDownLoadComplete: download completed");
        availableRooms = results;
        finishedDownloading = true;
        AvailableRoomListAdapter newAdapter = new AvailableRoomListAdapter(availableRooms);
        recyclerView.setAdapter(newAdapter);
    }
}
