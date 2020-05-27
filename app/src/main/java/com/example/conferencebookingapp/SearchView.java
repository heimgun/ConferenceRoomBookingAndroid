package com.example.conferencebookingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SearchView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "SearchView";
    public static final String CHOSEN_CITY_ID = "com.example.conferencebookingapp.CITY_ID";
    public static final String CHOSEN_CITY_NAME = "com.example.conferencebookingapp.CITY_NAME";
    public static final String CHOSEN_NUMBER_OF_PEOPLE_INFO = "com.example.conferencebookingapp.NUMBER_OF_PEOPLE";
    public static final String CHOSEN_DATE_INFO = "com.example.conferencebookingapp.DATE";

    private String city;
    private int numberOfPeople;
    private String chosenDate;

    private Spinner citiesSpinner;
    private EditText numberOfPeopleEditText;
    private DatePicker datePicker;


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

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);


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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        //Calendar spinner stuff
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        chosenDate = df.format(calendar.getTime());

        numberOfPeople = Integer.parseInt(numberOfPeopleEditText.getText().toString());


        Log.d(TAG, "OnSearchButtonClicked: city is: " + city);
        Log.d(TAG, "OnSearchButtonClicked: numberOfPeople is: " + numberOfPeople);
        Log.d(TAG, "OnSearchButtonClicked: chosenDate is: " + chosenDate);

        boolean ready = validateData();
        if(ready) {
            Intent intent = new Intent(this, AvailablePlantView.class);
            intent.putExtra(CHOSEN_CITY_ID, cityId);
            intent.putExtra(CHOSEN_CITY_NAME, city);
            intent.putExtra(CHOSEN_NUMBER_OF_PEOPLE_INFO, numberOfPeople);
            intent.putExtra(CHOSEN_DATE_INFO, chosenDate);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Error in input, please enter correct information", Toast.LENGTH_LONG).show();
        }

    }

    private boolean validateData() {


        if (!citiesSpinner.getSelectedItem().equals("Stad") && !numberOfPeopleEditText.getText().toString().equals("")){
            return true;
        }


        return false;
    }
}
