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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookingView extends AppCompatActivity implements CallbackActivity{
    private static final String TAG = "BookingView";

    private ConferenceRoom room;
    //private String token;
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

    public static final String CREATE_BOOKING_URL = "https://dev-be.timetomeet.se/service/rest/booking/add/";
    public static final String BOOK_ROOM_URL = "https://dev-be.timetomeet.se/service/rest/conferenceroomavailability/book/%s/";
    public static final String ADD_FOOD_URL = "https://dev-be.timetomeet.se/service/rest/bookingfoodbeverage/add/";
    public static final String ADD_TECHNOLOGY_URL = "https://dev-be.timetomeet.se/service/rest/bookingselectabletechnology/add/";
    public static final String COMPLETE_BOOKING_URL = "https://dev-be.timetomeet.se/service/rest/booking/completed/";

    public static final String CREATE_BOOKING_MESSAGE = "com.example.conferencebookingapp.CREATE_BOOKING";
    public static final String BOOK_ROOM_MESSAGE = "com.example.conferencebookingapp.BOOK_ROOM";
    public static final String ADD_FOOD_MESSAGE = "com.example.conferencebookingapp.ADD_FOOD";
    public static final String FINAL_FOOD_ADDED_MESSAGE = "com.example.conferencebookingapp.FINAL_FOOD";
    public static final String ADD_TECHNOLOGY_MESSAGE = "com.example.conferencebookingapp.ADD_TECHNOLOGY";
    public static final String FINAL_TECHNOLOGY_ADDED_MESSAGE = "com.example.conferencebookingapp.FINAL_TECHNOLOGY";
    public static final String COMPLETE_BOOKING_MESSAGE = "com.example.conferencebookingapp.COMPLETE_BOOKING";

    public String jsonCreateBooking = "{" +
            "    \"paymentAlternative\": \"1\"," +
            "    \"wantHotelRoomInfo\": false," +
            "    \"wantActivityInfo\": false," +
            "    \"specialRequest\": \"\"," +
            "    \"numberOfParticipants\": \"%d\","+
            "    \"bookingSourceSystem\": \"1\","+
            "    \"agreementNumber\": \"\""+
            "}";

    public String jsonBookRoom = "{" +
            "    \"chosenSeating\": \"%s\"," +
            "    \"reservation_guid\": \"\"" +
            "}";

    public String jsonAddFoodToBooking = "{" +
            "    \"conferenceRoomAvailability\": \"%s\"," +
            "    \"foodBeverage\": \"%s\"," +
            "    \"amount\": \"%d\"," +
            "    \"comment\": \"\"," +
            "    \"timeToServe\": \"%s\"" +
            "}";

    public String jsonAddTechnologyToBooking = "{" +
            "    \"conferenceRoomAvailability\": \"%s\"," +
            "    \"technology\": \"%s\"" +
            "}";



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
                Log.d(TAG, "onClick: number of people in booking is: "+ booking.getNumberOfPeople());
                startActivity(intent);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Plant.class.getClassLoader());

        room = intentIn.getParcelableExtra(AvailableRoomView.CHOSEN_ROOM_INFO);
        //token = intentIn.getStringExtra(CreateUserView.TOKEN_INFO);
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

        chosenTechnologies = new ArrayList<>(); // get input from user (available technologies will soon be available in room)
        chosenFoodAndBeverages = new ArrayList<>(); // get input from user (available food will soon be available in room)
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

       // APIRequester apiRequester = new APIRequester(token, this, CREATE_BOOKING_MESSAGE);
       //String formattedJsonCreateBooking = String.format(jsonCreateBooking, numberOfPeople);
       // Log.d(TAG, "onCreate: jsonString is: " + formattedJsonCreateBooking);
        //apiRequester.execute(CREATE_BOOKING_URL, formattedJsonCreateBooking);
    }

    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {

        switch(message ) {
            case CREATE_BOOKING_MESSAGE:
                Log.d(TAG, "onDownloadComplete: results are: " + results);
                //APIRequester roomRequester = new APIRequester(token, this, BOOK_ROOM_MESSAGE);
                String bookRoomUrl = String.format(BOOK_ROOM_URL, bookingCode);
                String formattedJsonBookRoom = String.format(jsonBookRoom, chosenSeatingId);
                Log.d(TAG, "onDownloadComplete: createBookingmessage-if calling book room");
                //roomRequester.execute(bookRoomUrl, formattedJsonBookRoom);
                break;
            case BOOK_ROOM_MESSAGE:
                Log.d(TAG, "onDownloadComplete: results are: " + results);

                int numberOfFoodItemsRequested = chosenFoodAndBeverages.size();
                if(numberOfFoodItemsRequested > 0) {
                    Log.d(TAG, "onDownloadComplete: bookroommessage-if calling add food");
                    addFoodToBooking(numberOfFoodItemsRequested);
                } else {
                    int numberOfTechnologiesRequested = chosenTechnologies.size();
                    if(numberOfTechnologiesRequested > 0) {
                        Log.d(TAG, "onDownloadComplete: bookroommessage-if calling add technology");
                        addTechnologyToBooking(numberOfTechnologiesRequested);
                    } else {
                        Log.d(TAG, "onDownloadComplete: bookroommessage-if calling complete booking");
                        completeBooking();
                    }
                }
                break;
            case ADD_FOOD_MESSAGE:
                Log.d(TAG, "onDownloadComplete: result is: " + results);
                break;
            case FINAL_FOOD_ADDED_MESSAGE:
                Log.d(TAG, "onDownloadComplete: final food added. Result is: " + results);
                int numberOfTechnologiesRequested = chosenTechnologies.size();
                if(numberOfTechnologiesRequested > 0) {
                    Log.d(TAG, "onDownloadComplete: finalfoodaddedmessage-if calling add technology");
                    addTechnologyToBooking(numberOfTechnologiesRequested);
                } else {
                    Log.d(TAG, "onDownloadComplete: finalfoodaddedmessage-if calling complete booking");
                    completeBooking();
                }
                break;
            case ADD_TECHNOLOGY_MESSAGE:
                break;
            case FINAL_TECHNOLOGY_ADDED_MESSAGE:
                Log.d(TAG, "onDownloadComplete: final technology added. Result is: " + results);
                Log.d(TAG, "onDownloadComplete: finaltechnologyadded-if calling complete booking");
                completeBooking();
                break;
            case COMPLETE_BOOKING_MESSAGE:
                Log.d(TAG, "onDownloadComplete: booking completed. Result is: " + results);
                break;
            default:

        }

    }

    private void addFoodToBooking(int numberOfItemsRequested) {

        for (int i = 0; i < numberOfItemsRequested; i++) {
            String servingTime = chosenDate + "T10:30:00";
            Log.d(TAG, "onDownloadComplete: serving time is: " + servingTime);
            String formattedJsonAddFood = String.format(jsonAddFoodToBooking, bookingCode, chosenFoodAndBeverages.get(i),
                    numberOfPeople, servingTime);

            String addFoodMessage = i == numberOfItemsRequested - 1 ? FINAL_FOOD_ADDED_MESSAGE : ADD_FOOD_MESSAGE;
            //APIRequester foodRequester = new APIRequester(token, this, addFoodMessage);
           // foodRequester.execute(ADD_FOOD_URL, formattedJsonAddFood);
        }
    }

    private void addTechnologyToBooking(int numberOfItemsRequested) {
        for (int i = 0; i < numberOfItemsRequested; i++) {
            String formattedJsonAddTechnology = String.format(jsonAddTechnologyToBooking, bookingCode, chosenTechnologies.get(i));

            String addTechnologyMessage = i == numberOfItemsRequested - 1 ? FINAL_TECHNOLOGY_ADDED_MESSAGE : ADD_TECHNOLOGY_MESSAGE;
            //APIRequester technologyRequester = new APIRequester(token, this, addTechnologyMessage);
           // technologyRequester.execute(ADD_TECHNOLOGY_URL, formattedJsonAddTechnology);
        }
    }

    private void completeBooking() {
        Log.d(TAG, "completeBooking: completeBooking called");
        //Downloader downloader = new Downloader(token, this, COMPLETE_BOOKING_MESSAGE);
        //downloader.execute(COMPLETE_BOOKING_URL);
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
