package com.example.conferencebookingapp;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

    private Plant plant;
    private String chosenDate, chosenPlant;
    private String city;
    private String cityId;
    private int numberOfPeople;

    public static final String FIND_ROOMS = "com.example.conferencebookingapp.FIND_ROOMS";
    public static final String GET_ROOM_INFO = "com.example.conferencebookingapp.GET_ROOM_INFO";
    public static final String END_OF_GET_ROOM_INFO = "com.example.conferencebookingapp.GET_ROOM_INFO_STOP";

    public static final String CHOSEN_ROOM_INFO = "com.example.conferencebookingapp.CHOSEN_ROOM";

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
        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Plant.class.getClassLoader());

        plant = intentIn.getParcelableExtra(AvailablePlantView.CHOSEN_PLANT);
        if (plant != null) {
            Log.d(TAG, "onCreate: plant name from parcel is: " + plant.getName());
        }

        String plantId = plant.getPlantId();
        String plantName = plant.getName();
        chosenDate = intentIn.getStringExtra(SearchView.CHOSEN_DATE_INFO);
        cityId = plant.getCityId();
        city = plant.getCity();
        numberOfPeople = intentIn.getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);
        chosenPlant = intentIn.getStringExtra(AvailablePlantView.CHOSEN_PLANT_NAME);

        String roomRequest = String.format(requestRooms, plantId, chosenDate, chosenDate);

        APIRequester requester = new APIRequester(AvailableRoomView.this, FIND_ROOMS);
        requester.execute(ROOM_URL, roomRequest);

        searchHeading = findViewById(R.id.resultsTextView);
        searchHeading.setText(String.format("Tillgängliga rum %s på %s", chosenDate, plantName));

        recyclerView = findViewById(R.id.resultsRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AvailableRoomListAdapter(availableRooms, new AvailableRoomListAdapter.ClickHandler() {
            @Override
            public void onButtonClicked(int position) {
                onContinueToBookingButtonClicked(position);
            }
        });


        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: adapter set");


       Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }


    public void updateRoomData() {

        Log.d(TAG, "updateRoomData: start");
        for (ConferenceRoom room: availableRooms) {
            if (plant != null) {
                room.setPlant(plant);
            } else {
                Toast.makeText(this, "Ett fel inträffade", Toast.LENGTH_SHORT).show();
                onBackClicked();
            }

        }

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
                AvailableRoomListAdapter newAdapter = new AvailableRoomListAdapter(availableRooms, new AvailableRoomListAdapter.ClickHandler() {
                    @Override
                    public void onButtonClicked(int position) {
                            onContinueToBookingButtonClicked(position);

                    }
                });
                recyclerView.setAdapter(newAdapter);
            } else {
                Log.d(TAG, "onDownloadComplete: Downloading. NextPage is: " + nextPage);
                Downloader downloader = new Downloader(AvailableRoomView.this, GET_ROOM_INFO);
                downloader.execute(nextPage);

            }
        }


    }

    private void onContinueToBookingButtonClicked(int position){
        ConferenceRoom chosenRoom = availableRooms.get(position);

        Intent intent = new Intent(this, BookingView.class);
        intent.putExtra(CHOSEN_ROOM_INFO, chosenRoom);
        intent.putExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, numberOfPeople);
        intent.putExtra(SearchView.CHOSEN_DATE_INFO, chosenDate);
        intent.putExtra(SearchView.CHOSEN_CITY_NAME, city);
        intent.putExtra(AvailablePlantView.CHOSEN_PLANT_NAME, chosenPlant);
        startActivity(intent);



    }


    @Override
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

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
