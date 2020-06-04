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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private Plant plant;

    private Booking booking;

    private String chosenSeatingId;
    private String bookingCode;
    private List<String> chosenTechnologies;
    private List<String> chosenFoodAndBeverages;

    TextView roomNameTV, plantNameTV, numberOfPeopleTV, cityTV, chosenDateTV;
    RadioGroup radiogroup;
    RadioButton radiotest;
    CheckBox afternoonFika, morningFika, lunch, fruit, coffeeTea, water, breakfast;
    CheckBox internet, projectorScreen, projector, flipBoard, whiteBoard, speakers, videoConf, notes;
    LinearLayout foodLayout, equipmentLayout;
    private Button confirmBookingButton;

    public static final String BOOKING_INFO = "com.example.conferencebookingapp.BOOKING";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_view);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        roomNameTV = (TextView) findViewById(R.id.roomNameTextView);
        plantNameTV = (TextView) findViewById(R.id.plantNameTextView);
        cityTV = (TextView) findViewById(R.id.plantCityTextView);
        chosenDateTV = (TextView) findViewById(R.id.chosenDateTextView);
        numberOfPeopleTV = (TextView) findViewById(R.id.numberOfPeopleTextView);
        confirmBookingButton = findViewById(R.id.confirmBtn);


        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Plant.class.getClassLoader());

        room = intentIn.getParcelableExtra(AvailableRoomView.CHOSEN_ROOM_INFO);
        plant = intentIn.getParcelableExtra(AvailablePlantView.CHOSEN_PLANT);

        numberOfPeople = getIntent().getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);
        chosenDate = getIntent().getStringExtra(SearchView.CHOSEN_DATE_INFO);
        chosenPlant = getIntent().getStringExtra(AvailablePlantView.CHOSEN_PLANT_NAME);
        city = getIntent().getStringExtra(SearchView.CHOSEN_CITY_NAME);

        if(room.getSeatings().size() > 0){

            radiogroup = (RadioGroup) findViewById(R.id.radioSeating);

            for(int i = 0; i < room.getSeatings().size(); i++){
                RadioButton radio = new RadioButton(this);
                radiogroup.addView(radio);
                radio.setId(i);
                radio.setText(room.getSeatings().get(i).getDescription());
                Log.d(TAG, "Button " + i + " added with name " + room.getSeatings().get(i).getDescription());

            }

        }

        if(room.getTechnologies().size() > 0){
             equipmentLayout = (LinearLayout) findViewById(R.id.equipmentLayout);

             for(int i = 0; i < room.getTechnologies().size(); i++){
                 CheckBox checkBox = new CheckBox(this);
                 equipmentLayout.addView(checkBox);
                 checkBox.setId(i);
                 checkBox.setText(room.getTechnologies().get(i).getDescription());
                 Log.d(TAG, "Checkbox " + i + " added with name " + room.getTechnologies().get(i).getDescription());

             }

        }

        if(room.getPlant().getFoodAndBeverages().size() > 0){
            foodLayout = (LinearLayout) findViewById(R.id.foodLayout);

            for(int i = 0; i < room.getPlant().getFoodAndBeverages().size(); i++){
                CheckBox checkBox = new CheckBox(this);
                foodLayout.addView(checkBox);
                checkBox.setId(i);
                checkBox.setText(room.getPlant().getFoodAndBeverages().get(i).getDescription());
                Log.d(TAG, "Checkbox " + i + " added with name " + room.getPlant().getFoodAndBeverages().get(i).getDescription());

            }

        }




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
        Log.d(TAG, "Seatings available " + room.getSeatings());





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


        //ConfirmButtonClicked
        confirmBookingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addSeatingToBooking();


                Intent intent = new Intent(BookingView.this, CreateUserView.class);
                intent.putExtra(BOOKING_INFO, booking);
                startActivity(intent);
            }
        });

    }





    private void addSeatingToBooking(){

        int selectedId = radiogroup.getCheckedRadioButtonId();

        booking.setChosenSeating(room.getSeatings().get(selectedId));

        Log.d(TAG, "onDownloadComplete: chosen seating is id: " + selectedId);


    }

    private void addFoodToBooking(int numberOfItemsRequested) {

        //Registrera input
        if(afternoonFika.isChecked()){
            // chosenFoodAndBeverages.add(afternoonFika.getText().toString());
        }
        if(morningFika.isChecked()){
            // chosenFoodAndBeverages.add(morningFika.getText().toString());
        }
        if(lunch.isChecked()){
            // chosenFoodAndBeverages.add(lunch.getText().toString());
        }
        if(coffeeTea.isChecked()){
            // chosenFoodAndBeverages.add(coffeeTea.getText().toString());
        }
        if(breakfast.isChecked()){
            // chosenFoodAndBeverages.add(breakfast.getText().toString());
        }
        if(fruit.isChecked()){
            // chosenFoodAndBeverages.add(fruit.getText().toString());
        }
        if(water.isChecked()){
           // chosenFoodAndBeverages.add(water.getText().toString());
        }


    }

    private void addTechnologyToBooking(int numberOfItemsRequested) {

        //Registrera input
        if(internet.isChecked()){
            // chosenFoodAndBeverages.add(afternoonFika.getText().toString());
        }
        if(projectorScreen.isChecked()){
            // chosenFoodAndBeverages.add(morningFika.getText().toString());
        }
        if(projector.isChecked()){
            // chosenFoodAndBeverages.add(lunch.getText().toString());
        }
        if(videoConf.isChecked()){
            // chosenFoodAndBeverages.add(coffeeTea.getText().toString());
        }
        if(notes.isChecked()){
            // chosenFoodAndBeverages.add(breakfast.getText().toString());
        }
        if(flipBoard.isChecked()){
            // chosenFoodAndBeverages.add(fruit.getText().toString());
        }
        if(whiteBoard.isChecked()){
            // chosenFoodAndBeverages.add(water.getText().toString());
        }
        if(speakers.isChecked()){
            // chosenFoodAndBeverages.add(water.getText().toString());
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
