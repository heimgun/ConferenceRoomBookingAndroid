package com.example.conferencebookingapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AvailableRoomView extends AppCompatActivity implements CallbackActivity {

    private static final String TAG = "AvailableRoomView";
    private List<ConferenceRoom> availableRooms;

    private TextView searchHeading;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String plantId;
    private String plantName;
    private String chosenDate;
    private String city;
    private String cityId;
    private int numberOfPeople;

    public static final String FIND_ROOMS = "com.example.conferencebookingapp.FIND_ROOMS";
    public static final String GET_ROOM_INFO = "com.example.conferencebookingapp.GET_ROOM_INFO";
    public static final String END_OF_GET_ROOM_INFO = "com.example.conferencebookingapp.GET_ROOM_INFO_STOP";

    public static final String ROOM_URL = "https://dev-be.timetomeet.se/service/rest/search/availability/period/v3";
    public static final String EXTRA_INFO_URL = "https://dev-be.timetomeet.se/service/rest/conferenceroom/";

    private String requestRooms = "{" +
            "    \"objectIds\": \"%s\"," +
            "    \"objectType\": \"plant\"," +
            "    \"fromDate\": \"%s\"," +
            "    \"toDate\": \"%s\" "+
            "}";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room_view);

        availableRooms = new ArrayList<>();

        plantId = getIntent().getStringExtra(AvailablePlantView.CHOSEN_PLANT_ID);
        plantName = getIntent().getStringExtra(AvailablePlantView.CHOSEN_PLANT_NAME);
        chosenDate = getIntent().getStringExtra(SearchView.CHOSEN_DATE_INFO);
        cityId = getIntent().getStringExtra(SearchView.CHOSEN_CITY_ID);
        city = getIntent().getStringExtra(SearchView.CHOSEN_CITY_NAME);
        numberOfPeople = getIntent().getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);

        String roomRequest = String.format(requestRooms, plantId, chosenDate, chosenDate);

        APIRequester requester = new APIRequester(AvailableRoomView.this, FIND_ROOMS);
        requester.execute(ROOM_URL, roomRequest);

        searchHeading = findViewById(R.id.resultsTextView);
        searchHeading.setText(String.format("Tillgängliga rum %s på %s", chosenDate, plantName));

        recyclerView = findViewById(R.id.resultsRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AvailableRoomListAdapter(availableRooms);

        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: adapter set");


       // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeButtonEnabled(true);

    }


    public void updateRoomData() {

        Log.d(TAG, "updateRoomData: start");
        Downloader downloader = new Downloader(AvailableRoomView.this, GET_ROOM_INFO);
        downloader.execute(EXTRA_INFO_URL);

    }

    public void onDownloadComplete(String results, String message) {

        Log.d(TAG, "onDownloadComplete: download completed");

        if(message.equals(FIND_ROOMS)) {
            Log.d(TAG, "onDownloadComplete: entering FIND_ROOMS if");
            ConferenceJsonParser parser = new ConferenceJsonParser();
            availableRooms = parser.parseRoom(results);
            updateRoomData();
        } else if (message.equals(GET_ROOM_INFO)) {
            Log.d(TAG, "onDownloadComplete: entering GET_ROOM_INFO if");
            ConferenceJsonParser parser = new ConferenceJsonParser();
            String nextPage = parser.parseExtraRoomInfo(results, availableRooms);
            Log.d(TAG, "onDownloadComplete: nextPage is: " + nextPage);
            if (nextPage == null || nextPage.equals("") || nextPage.equals("null")) {
                Log.d(TAG, "onDownloadComplete: finished downloading");
                AvailableRoomListAdapter newAdapter = new AvailableRoomListAdapter(availableRooms);
                recyclerView.setAdapter(newAdapter);
            } else {
                Log.d(TAG, "onDownloadComplete: Downloading. NextPage is: " + nextPage);
                Downloader downloader = new Downloader(AvailableRoomView.this, GET_ROOM_INFO);
                downloader.execute(nextPage);

            }
        }
    }

    /*

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            onBackClicked();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackClicked(){
        Intent intent = new Intent(this, AvailablePlantView.class);
        intent.putExtra(SearchView.CHOSEN_CITY_ID, cityId);
        intent.putExtra(SearchView.CHOSEN_CITY_NAME, city);
        intent.putExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, numberOfPeople);
        intent.putExtra(SearchView.CHOSEN_DATE_INFO, chosenDate);
        startActivity(intent);
        finish();
    }

     */
}
