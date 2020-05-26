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

    private String plantId;
    private String plantName;
    private String chosenDate;

    public static final String FIND_ROOMS = "find rooms";

    public static final String ROOM_URL = "https://dev-be.timetomeet.se/service/rest/search/availability/period/v3";

    private String requestRooms = "{" +
            "    \"objectIds\": \"%s\"," +
            "    \"objectType\": \"plant\"," +
            "    \"fromDate\": \"%s\"," +
            "    \"toDate\": \"%s\" "+
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room_view);

        availableRooms = new ArrayList<>();

        plantId = getIntent().getStringExtra(AvailablePlantView.CHOSEN_PLANT_ID);
        plantName = getIntent().getStringExtra(AvailablePlantView.CHOSEN_PLANT_NAME);
        chosenDate = getIntent().getStringExtra(SearchView.CHOSEN_DATE_INFO);

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
        }

    }
}
