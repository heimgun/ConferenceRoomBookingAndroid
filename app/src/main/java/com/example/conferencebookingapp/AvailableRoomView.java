package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AvailableRoomView extends AppCompatActivity implements CallbackActivity {

    private static final String TAG = "AvailableRoomView";
    private List<ConferenceRoom> availableRooms;
    private List<Plant> availablePlants;

    private TextView searchHeading;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static final String FIND_ROOMS = "find rooms";
    public static final String FIND_PLANTS = "find plants";

    public static final String PLANT_URL = "https://dev-be.timetomeet.se/service/rest/conferenceroomavailability/search/";
    public static final String ROOM_URL = "https://dev-be.timetomeet.se/service/rest/search/availability/period/v3";

    private String requestJson = "{" +
            "    \"objectIds\": \"1\"," +
            "    \"objectType\": \"city\"," +
            "    \"fromDate\": \"%s\"," +
            "    \"toDate\": \"%s\" "+
            "}";

    private String requestPlants = "{" +
            "    \"cityId\": \"1\"," +
            "    \"distanceInMeters\": null," +
            "    \"distanceSkipInMeters\": null," +
            "    \"seats\": \"5\"," +
            "    \"priceFrom\": null," +
            "    \"priceTo\": null," +
            "    \"plantId\": null," +
            "    \"organizationId\": null," +
            "    \"dateTimeFrom\": \"%s\"," +
            "    \"dateTimeTo\": \"%s\", "+
            "    \"foodBeverageList\": \"\"," +
            "    \"technologyList\": \"\"," +
            "    \"technologyGroup\": \"\"," +
            "    \"rating\": null," +
            "    \"orderBy\": \"\"," +
            "    \"orderDirection\": null," +
            "    \"page\": \"1\"," +
            "    \"pages\": null," +
            "    \"pageSize\": null" +
            "}";

    private String requestRoomData = "{" +
            "    \"id\": \"%\"," +
            "    \"objectType\": \"city\"," +
            "    \"fromDate\": \"%s\"," +
            "    \"toDate\": \"%s\" "+
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room_view);

        availableRooms = new ArrayList<>();
        availablePlants = new ArrayList<>();

        String requestFromSearchView = getIntent().getStringExtra(SearchView.SEARCH_PARAMETERS_INFO);

       // String urlAddress = getIntent().getStringExtra(MainActivity.URL_MESSAGE);
       // String urlAddress = "https://dev-be.timetomeet.se/service/rest/search/availability/period/v3";
       // Log.d(TAG, "onCreate: urlAddress is: " + urlAddress);

        //Downloader downloader = new Downloader(AvailableRoomView.this);
        //downloader.execute(urlAddress);

        /*Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = df.format(date);
        String dateStringFrom = dateString + "T09:00:00+02:00";
        String dateStringTo = dateString + "T15:00:00+02:00";
        Log.d(TAG, "onCreate: date is: " + dateString);

         */

        APIRequester plantRequester = new APIRequester(AvailableRoomView.this, FIND_PLANTS);
        plantRequester.execute(PLANT_URL, requestFromSearchView);

        //APIRequester requester = new APIRequester(AvailableRoomView.this, FIND_ROOMS);
        //requester.execute(urlAddress, String.format(requestJson, dateString, dateString));

        searchHeading = findViewById(R.id.resultsTextView);
        searchHeading.setText("Sökresultat");
        recyclerView = findViewById(R.id.resultsRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AvailableRoomListAdapter(availableRooms);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: adapter set");

    }


    public void updateRoomData(ConferenceRoom room) {


    }

    public void onDownloadComplete(String results, String message) {

        Log.d(TAG, "onDownloadComplete: download completed");

        if(message.equals(FIND_ROOMS)) {
            ConferenceJsonParser parser = new ConferenceJsonParser();
            availableRooms = parser.parseRoom(results);
            for (ConferenceRoom room : availableRooms) {
                updateRoomData(room);
            }
            AvailableRoomListAdapter newAdapter = new AvailableRoomListAdapter(availableRooms);
            recyclerView.setAdapter(newAdapter);
        } else if (message.equals(FIND_PLANTS)) {
            ConferenceJsonParser parser = new ConferenceJsonParser();
            availablePlants = parser.parsePlants(results);

            PlantListAdapter newAdapter = new PlantListAdapter(availablePlants);
            recyclerView.setAdapter(newAdapter);

        }

    }
}
