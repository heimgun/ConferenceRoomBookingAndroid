package com.example.conferencebookingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookingView extends AppCompatActivity {
    private static final String TAG = "BookingView";

    private ConferenceRoom room;
    private int numberOfPeople;
    private String chosenDate;
    private String city;

    private Booking booking;


    TextView roomNameTV, plantNameTV, numberOfPeopleTV, cityTV, chosenDateTV, price;
    RadioGroup radiogroup;
    LinearLayout foodLayout, equipmentLayout;
    Spinner spinner;

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
        Button confirmBookingButton = findViewById(R.id.confirmBtn);
        price = (TextView) findViewById(R.id.priceTextView);


        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Plant.class.getClassLoader());

        room = intentIn.getParcelableExtra(AvailableRoomView.CHOSEN_ROOM_INFO);
        numberOfPeople = getIntent().getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);
        chosenDate = getIntent().getStringExtra(SearchView.CHOSEN_DATE_INFO);
        city = getIntent().getStringExtra(SearchView.CHOSEN_CITY_NAME);

        price.setText("");

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Välj tidspunkt");

        //Spinner info
        if(!room.getPreNoonBookingCode().equals("null")){

            spinnerArray.add(room.getPreNoonHours());
        }

        if(!room.getAfternoonBookingCode().equals("null")){

            spinnerArray.add(room.getAfternoonHours());
        }

        if(!room.getPreNoonBookingCode().equals("null") && !room.getAfternoonBookingCode().equals("null")){

            spinnerArray.add(room.getFullDayHours());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);

        spinner = (Spinner) findViewById(R.id.timeSpinner);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        //RadioButtons on Seating
        if(room.getSeatings().size() > 0){

            radiogroup = (RadioGroup) findViewById(R.id.radioSeating);

            for(int i = 0; i < room.getSeatings().size(); i++){
                RadioButton radio = new RadioButton(this);
                radiogroup.addView(radio);
                radio.setId(i);
                radio.setText(room.getSeatings().get(i).getDescription());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    radio.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                }
                Log.d(TAG, "Button " + i + " added with name " + room.getSeatings().get(i).getDescription());

            }

        }

        //Checkboxes for Food and Drinks
        if(room.getPlant().getFoodAndBeverages().size() > 0){
            foodLayout = (LinearLayout) findViewById(R.id.foodLayout);

            for(int i = 0; i < room.getPlant().getFoodAndBeverages().size(); i++){
                CheckBox checkBox = new CheckBox(this);
                foodLayout.addView(checkBox);
                checkBox.setId(i);
                checkBox.setText(room.getPlant().getFoodAndBeverages().get(i).getDescription());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                }
                Log.d(TAG, "Checkbox " + i + " added with name " + room.getPlant().getFoodAndBeverages().get(i).getDescription());

            }

        }


        //Checkboxes for Equipment
        if(room.getTechnologies().size() > 0){
             equipmentLayout = (LinearLayout) findViewById(R.id.equipmentLayout);

             for(int i = 0; i < room.getTechnologies().size(); i++){
                 CheckBox checkBox = new CheckBox(this);
                 equipmentLayout.addView(checkBox);
                 checkBox.setId(i);
                 checkBox.setText(room.getTechnologies().get(i).getDescription());
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                     checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                 }
                 Log.d(TAG, "Checkbox " + i + " added with name " + room.getTechnologies().get(i).getDescription());

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


        //ConfirmButtonClicked
        confirmBookingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addHoursToBooking();
                addSeatingToBooking();
                addFoodToBooking();
                addTechnologyToBooking();


                Intent intent = new Intent(BookingView.this, CreateUserView.class);
                intent.putExtra(BOOKING_INFO, booking);
                startActivity(intent);
            }
        });

    }


    private void addHoursToBooking() {

        if(!spinner.getSelectedItem().equals("Välj tidspunkt")) {

            if (spinner.getSelectedItem().equals(room.getPreNoonHours())) {
                booking.addBookingCode(room.getPreNoonBookingCode());
                Log.d(TAG, "Bookingcode added : " + room.getPreNoonBookingCode());
            }

            if (spinner.getSelectedItem().equals(room.getAfternoonHours())) {
                booking.addBookingCode(room.getAfternoonBookingCode());
                Log.d(TAG, "Bookingcode added : " + room.getAfternoonBookingCode());
            }

            if (spinner.getSelectedItem().equals(room.getFullDayHours())) {
                booking.addBookingCode(room.getPreNoonBookingCode());
                booking.addBookingCode(room.getAfternoonBookingCode());
                Log.d(TAG, "Bookingcodes added : " + room.getPreNoonBookingCode() + ", " + room.getAfternoonBookingCode());
            }

        }


    }

    private void addSeatingToBooking(){


        int selectedId = radiogroup.getCheckedRadioButtonId();

        if(selectedId >= 0) {

            booking.setChosenSeating(room.getSeatings().get(selectedId));
            Log.d(TAG, "onDownloadComplete: chosen seating is id: " + selectedId);

        }

    }

    private void addFoodToBooking() {

        if (foodLayout != null) {

            for (int i = 0; i < foodLayout.getChildCount(); i++) {

                CheckBox checkbox = foodLayout.findViewById(i);

                if (checkbox.isChecked()) {
                    booking.addFoodAndBeverage(room.getPlant().getFoodAndBeverages().get(i));
                    Log.d(TAG, "Food and Drink added to booking: " + room.getPlant().getFoodAndBeverages().get(i));
                }

            }
        }



    }

    private void addTechnologyToBooking() {

        if(equipmentLayout != null){

        for(int i = 0; i < equipmentLayout.getChildCount(); i++){

            CheckBox checkbox = equipmentLayout.findViewById(i);

            if(checkbox.isChecked()){
                booking.addTechnology(room.getTechnologies().get(i));
                Log.d(TAG, "Equipment added to booking: " + room.getTechnologies().get(i));
            }

        }
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
        Intent intent = new Intent(this, AvailableRoomView.class);
        intent.putExtra(AvailablePlantView.CHOSEN_PLANT, room.getPlant());
        intent.putExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, numberOfPeople);
        intent.putExtra(SearchView.CHOSEN_DATE_INFO, chosenDate);
        startActivity(intent);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



}
