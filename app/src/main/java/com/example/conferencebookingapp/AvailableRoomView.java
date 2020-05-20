package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AvailableRoomView extends AppCompatActivity implements CallbackActivity {

    private static final String TAG = "AvailableRoomView";
    private List<ConferenceRoom> availableRooms;

    private TextView searchHeading;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean finishedDownloading = false;

    private String requestJson = "{" +
            "    \"first_name\": \"Jon\"," +
            "    \"last_name\": \"Mjo\"," +
            "    \"username\": \"z_jon@intrusec.se\"," +
            "    \"password\": \"123\"," +
            "    \"email\": \"jon@intrusec.se\"," +
            "    \"phone_number\": \"12345\"," +
            "    \"organization_name\": \"Intrusec\"," +
            "    \"organization_nr\": \"6789\"," +
            "    \"street\": \"KG4\"," +
            "    \"city_name\": \"GBG\"," +
            "    \"zipCode\": \"42242\"" +
            "}";

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


    public void onDownloadComplete(String results) {

        Log.d(TAG, "onDownloadComplete: download completed");
        //availableRooms = results;
        //finishedDownloading = true;
        //AvailableRoomListAdapter newAdapter = new AvailableRoomListAdapter(availableRooms);
        //recyclerView.setAdapter(newAdapter);
    }
}
