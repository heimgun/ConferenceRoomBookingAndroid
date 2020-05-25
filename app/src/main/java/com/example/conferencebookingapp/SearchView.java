package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SearchView";
    public static final String SEARCH_PARAMETERS_INFO = "com.example.conferencebookingapp.SEARCH_PARAMETERS";

    private String city;
    private int numberOfPeople;
    private String chosenDate;

    private Spinner citiesSpinner;
    private EditText numberOfPeopleEditText;
    private EditText dateEditText;


    private String requestPlantsJSON = "{" +
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
        setContentView(R.layout.activity_search_view);

        citiesSpinner = findViewById(R.id.citiesSpinner);
        citiesSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citiesSpinner.setAdapter(adapter);

        numberOfPeopleEditText = findViewById(R.id.numberOfPeopleEditText);
        numberOfPeople = 1;

        dateEditText = findViewById(R.id.dateEditText);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        chosenDate = df.format(date);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CharSequence chosenCity = (CharSequence) parent.getItemAtPosition(position);
        city = chosenCity.toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        city = "Stad";
    }

    public void OnSearchButtonClicked(View v) {

        String cityId = "";
        switch(city) {
            case "Göteborg":
                cityId = "1";
                break;
            case "Mölndal":
                cityId = "84";
                break;
            case "Skövde":
                cityId = "108";
                break;
            case "Stockholm":
                cityId = "2";
                break;
            default:
        }

        numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());
        chosenDate = dateEditText.getText().toString();

        Log.d(TAG, "OnSearchButtonClicked: city is: " + city);
        Log.d(TAG, "OnSearchButtonClicked: numberOfPeople is: " + numberOfPeople);
        Log.d(TAG, "OnSearchButtonClicked: chosenDate is: " + chosenDate);

        boolean ready = validateData();
        if(ready) {
            String dateStringFrom = chosenDate + "T09:00:00+02:00";
            String dateStringTo = chosenDate + "T15:00:00+02:00";
            String searchJSON = String.format(requestPlantsJSON, cityId, numberOfPeople, dateStringFrom, dateStringTo);
            Intent intent = new Intent(this, AvailableRoomView.class);
            intent.putExtra(SEARCH_PARAMETERS_INFO, searchJSON);
            startActivity(intent);
        }

    }

    private boolean validateData() {
        // Implement method for checking data input
        return true;
    }
}
