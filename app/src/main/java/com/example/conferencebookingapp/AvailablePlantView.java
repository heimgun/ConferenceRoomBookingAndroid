package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AvailablePlantView extends AppCompatActivity implements CallbackActivity {

    private static final String TAG = "AvailablePlantView";

    private List<Plant> availablePlants;

    private TextView searchHeading;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String cityId;
    private String city;
    private int numberOfPeople;
    private String chosenDate;

    public static final String CHOSEN_PLANT_ID = "com.example.conferencebookingapp.PLANT_ID";
    public static final String CHOSEN_PLANT_NAME = "com.example.conferencebookingapp.PLANT_NAME";
    public static final String FIND_PLANTS = "find plants";
    public static final String PLANT_URL = "https://dev-be.timetomeet.se/service/rest/conferenceroomavailability/search/";

    private String requestPlants = "{" +
            "    \"cityId\": \"%s\"," +
            "    \"distanceInMeters\": null," +
            "    \"distanceSkipInMeters\": null," +
            "    \"seats\": \"%d\"," +
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room_view);

        availablePlants = new ArrayList<>();

        cityId = getIntent().getStringExtra(SearchView.CHOSEN_CITY_ID);
        city = getIntent().getStringExtra(SearchView.CHOSEN_CITY_NAME);
        numberOfPeople = getIntent().getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);
        chosenDate = getIntent().getStringExtra(SearchView.CHOSEN_DATE_INFO);

        String dateStringFrom = chosenDate + "T09:00:00+02:00";
        String dateStringTo = chosenDate + "T15:00:00+02:00";

        String plantRequest = String.format(requestPlants, cityId, numberOfPeople, dateStringFrom, dateStringTo);

        APIRequester plantRequester = new APIRequester(AvailablePlantView.this, FIND_PLANTS);
        plantRequester.execute(PLANT_URL, plantRequest);

        searchHeading = findViewById(R.id.resultsTextView);
        searchHeading.setText(String.format("Tillgängliga anläggningar %s i %s", chosenDate, city));
        recyclerView = findViewById(R.id.resultsRecyclerView);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlantListAdapter(availablePlants, new PlantListAdapter.ClickHandler() {
            @Override
            public void onButtonClicked(int position) {
                onShowRoomsButtonClicked(position);
            }
        });

        recyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreate: adapter set");

    }

    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {

        ConferenceJsonParser parser = new ConferenceJsonParser();
        availablePlants = parser.parsePlants(results);

        PlantListAdapter newAdapter = new PlantListAdapter(availablePlants, new PlantListAdapter.ClickHandler() {
            @Override
            public void onButtonClicked(int position) {
                onShowRoomsButtonClicked(position);
            }
        });

        recyclerView.setAdapter(newAdapter);
    }

    private void onShowRoomsButtonClicked(int position) {
        Plant chosenPlant = availablePlants.get(position);

        Intent intent = new Intent(AvailablePlantView.this, AvailableRoomView.class);
        intent.putExtra(SearchView.CHOSEN_DATE_INFO, chosenDate);
        intent.putExtra(CHOSEN_PLANT_ID, chosenPlant.getPlantId());
        intent.putExtra(CHOSEN_PLANT_NAME, chosenPlant.getName());

        startActivity(intent);
    }



}
