package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailablePlantView extends AppCompatActivity implements CallbackActivity, AdapterView.OnItemSelectedListener {

    private static final String TAG = "AvailablePlantView";

    private List<Plant> availablePlants;

    private TextView searchHeading;
    private Spinner sortBySpinner;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String cityId;
    private String city;
    private int numberOfPeople;
    private String chosenDate;
    private String sortBy;
    private Map<String, String> foodItems;

    public static final String CHOSEN_PLANT_ID = "com.example.conferencebookingapp.PLANT_ID";
    public static final String CHOSEN_PLANT_NAME = "com.example.conferencebookingapp.PLANT_NAME";

    public static final String FIND_PLANTS = "com.example.conferencebookingapp.FIND_PLANTS";
    public static final String FIND_FOOD_INFO = "com.example.conferencebookingapp.FIND_FOOD_INFO";
    public static final String ADD_AVAILABLE_FOOD = "com.example.conferencebookingapp.ADD_FOOD";

    public static final String PLANT_URL = "https://dev-be.timetomeet.se/service/rest/conferenceroomavailability/search/";
    public static final String FOOD_INFO_URL = "https://dev-be.timetomeet.se/service/rest/foodbeverage/";
    public static final String AVAILABLE_FOOD_URL = "https://dev-be.timetomeet.se/service/rest/plantfoodbeverage/";

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
            "    \"orderBy\": \"%s\"," +
            "    \"orderDirection\": null," +
            "    \"page\": \"1\"," +
            "    \"pages\": null," +
            "    \"pageSize\": null" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_plant_view);

        availablePlants = new ArrayList<>();

        cityId = getIntent().getStringExtra(SearchView.CHOSEN_CITY_ID);
        city = getIntent().getStringExtra(SearchView.CHOSEN_CITY_NAME);
        numberOfPeople = getIntent().getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);
        chosenDate = getIntent().getStringExtra(SearchView.CHOSEN_DATE_INFO);
        sortBy = "ratingId";
        foodItems = new HashMap<>();

        searchHeading = findViewById(R.id.plantResultsTextView);
        searchHeading.setText(String.format("Tillgängliga anläggningar %s i %s", chosenDate, city));

        sortBySpinner = findViewById(R.id.sortBySpinner);
        sortBySpinner.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.plantRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PlantListAdapter(availablePlants, new PlantListAdapter.ClickHandler() {
            @Override
            public void onButtonClicked(int position) {
                onShowRoomsButtonClicked(position);
            }
        });

        recyclerView.setAdapter(adapter);

        showPage();

        Log.d(TAG, "onCreate: adapter set");

    }

    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {

        switch (message) {
            case FIND_PLANTS: {
                ConferenceJsonParser parser = new ConferenceJsonParser();
                availablePlants = parser.parsePlants(results);

                if(foodItems.isEmpty()) {
                    Downloader downloader = new Downloader(this, FIND_FOOD_INFO);
                    downloader.execute(FOOD_INFO_URL);
                } else {
                    Downloader downloader = new Downloader(this, ADD_AVAILABLE_FOOD);
                    downloader.execute(AVAILABLE_FOOD_URL);
                }
                break;
            }
            case FIND_FOOD_INFO: {
                ConferenceJsonParser parser = new ConferenceJsonParser();
                foodItems = parser.parseFoodInfo(results);

                Downloader downloader = new Downloader(this, ADD_AVAILABLE_FOOD);
                downloader.execute(AVAILABLE_FOOD_URL);

                break;
            }
            case ADD_AVAILABLE_FOOD: {

                ConferenceJsonParser parser = new ConferenceJsonParser();
                parser.parsePlantFood(results, availablePlants, foodItems);
                showResults();
                break;
            }
        }
    }

    private void showResults() {
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
        intent.putExtra(SearchView.CHOSEN_CITY_NAME, city);
        intent.putExtra(SearchView.CHOSEN_CITY_ID, cityId);
        intent.putExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, numberOfPeople);

        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CharSequence sortByChoice = (CharSequence) parent.getItemAtPosition(position);
        String sortByChoiceString = sortByChoice.toString();

        if(sortByChoice.equals("Avstånd från centrum")) {
            sortBy = "ratingId";
            showPage();
        } else {
            switch(sortByChoiceString) {
                case "Popularitet":
                    sortBy = "ratingId";
                    break;
                case "Lägsta pris":
                    sortBy = "priceFrom";
                    break;
                case "Namn":
                    sortBy= "plantName";
                    break;
                default:
            }
            showPage();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        sortBy = "Popularitet";
    }

    private void showPage() {

        String dateStringFrom = chosenDate + "T09:00:00+02:00";
        String dateStringTo = chosenDate + "T15:00:00+02:00";
        String plantRequest = String.format(requestPlants, cityId, numberOfPeople, dateStringFrom, dateStringTo, sortBy);
        APIRequester plantRequester = new APIRequester(AvailablePlantView.this, FIND_PLANTS);
        plantRequester.execute(PLANT_URL, plantRequest);
    }
}
