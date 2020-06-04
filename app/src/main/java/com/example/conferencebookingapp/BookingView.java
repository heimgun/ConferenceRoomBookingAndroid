package com.example.conferencebookingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookingView extends AppCompatActivity {
    private static final String TAG = "BookingView";

    private ConferenceRoom room;
    private int numberOfPeople;
    private String chosenDate;
    private String chosenPlant, city, chosenRoom;

    private Booking booking;

    private String chosenSeatingId;
    private String bookingCode;
    private List<String> chosenTechnologies;
    private List<String> chosenFoodAndBeverages;

    TextView roomNameTV, plantNameTV, numberOfPeopleTV, cityTV, chosenDateTV;
    private Button confirmBookingButton;

    public static final String BOOKING_INFO = "com.example.conferencebookingapp.BOOKING";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_view);

        roomNameTV = (TextView) findViewById(R.id.roomNameTextView);
        plantNameTV = (TextView) findViewById(R.id.plantNameTextView);
        cityTV = (TextView) findViewById(R.id.plantCityTextView);
        chosenDateTV = (TextView) findViewById(R.id.chosenDateTextView);
        numberOfPeopleTV = (TextView) findViewById(R.id.numberOfPeopleTextView);
        confirmBookingButton = findViewById(R.id.confirmBtn);

        confirmBookingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingView.this, CreateUserView.class);
                intent.putExtra(BOOKING_INFO, booking);
                startActivity(intent);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Plant.class.getClassLoader());

        room = intentIn.getParcelableExtra(AvailableRoomView.CHOSEN_ROOM_INFO);
        numberOfPeople = getIntent().getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);
        chosenDate = getIntent().getStringExtra(SearchView.CHOSEN_DATE_INFO);
        chosenPlant = getIntent().getStringExtra(AvailablePlantView.CHOSEN_PLANT_NAME);
        city = getIntent().getStringExtra(SearchView.CHOSEN_CITY_NAME);

        booking = new Booking();
        booking.setRoom(room);
        booking.setNumberOfPeople(numberOfPeople);
        booking.setChosenDate(chosenDate);

        roomNameTV.setText(room.getName());
        plantNameTV.setText(room.getPlant().getName());
        cityTV.setText(city);
        numberOfPeopleTV.setText("Antal deltagare: " + Integer.toString(numberOfPeople));
        chosenDateTV.setText(chosenDate);


        chosenSeatingId = room.getSeatings().get(0).getId(); // get input from user, preferrably a Seating but can be implemented with seatingId as well
        booking.setChosenSeating(room.getSeatings().get(0));

        bookingCode = room.getPreNoonBookingCode(); // get input if user wants prenoon, afternoon or full day
        booking.setBookingCode(bookingCode);

        chosenTechnologies = new ArrayList<>(); // get input from user (available technologies available in room)
        chosenFoodAndBeverages = new ArrayList<>(); // get input from user (available food available in room.getPlant().getFoodAndBeverages)
        if (chosenFoodAndBeverages.size() > 0) {
            chosenFoodAndBeverages.add(room.getPlant().getFoodAndBeverages().get(0).getId());
            booking.addFoodAndBeverage((room.getPlant().getFoodAndBeverages().get(0)));
        }


        if (room.getPlant().getFoodAndBeverages().size() > 1) {
            chosenFoodAndBeverages.add(room.getPlant().getFoodAndBeverages().get(1).getId());
            booking.addFoodAndBeverage(room.getPlant().getFoodAndBeverages().get(1));
        }

        chosenTechnologies.add(room.getTechnologies().get(0).getId());
        booking.addTechnology(room.getTechnologies().get(0));

        if(room.getTechnologies().size() > 1) {
            chosenTechnologies.add(room.getTechnologies().get(1).getId());
            booking.addTechnology(room.getTechnologies().get(1));
        }

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
        Intent intent = new Intent(this, CreateUserView.class);
        intent.putExtra(AvailableRoomView.CHOSEN_ROOM_INFO, room.getName());
        intent.putExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, numberOfPeople);
        intent.putExtra(SearchView.CHOSEN_DATE_INFO, chosenDate);
        intent.putExtra(SearchView.CHOSEN_CITY_NAME, room.getCity());
        intent.putExtra(AvailablePlantView.CHOSEN_PLANT_NAME, room.getPlant());
        startActivity(intent);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



}
