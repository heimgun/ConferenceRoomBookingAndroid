package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BookingView extends AppCompatActivity implements CallbackActivity{
    private static final String TAG = "BookingView";

    private ConferenceRoom room;
    private String token;
    private int numberOfPeople;

    private String chosenSeatingId;
    private String bookingCode;
    private List<String> chosenTechnologies;
    private List<String> chosenFoodAndBeverages;

    public static final String CREATE_BOOKING_URL = "https://dev-be.timetomeet.se/service/rest/booking/add/";
    public static final String BOOK_ROOM_URL = "https://dev-be.timetomeet.se/service/rest/conferenceroomavailability/book/%s";
    public static final String ADD_FOOD_URL = "https://dev-be.timetomeet.se/service/rest/bookingfoodbeverage/add/";
    public static final String ADD_TECHNOLOGY_URL = "https://dev-be.timetomeet.se/service/rest/bookingselectabletechnology/add/";
    public static final String COMPLETE_BOOKING_URL = "https://dev-be.timetomeet.se/service/rest/booking/completed/";

    public static final String CREATE_BOOKING_MESSAGE = "com.example.conferencebookingapp.CREATE_BOOKING";

    public String jsonCreateBooking = "{" +
            "    \"paymentAlternative\": \"1\"," +
            "    \"wantHotelRoomInfo\": false," +
            "    \"wantActivityInfo\": false," +
            "    \"specialRequest\": \"\"," +
            "    \"numberOfParticipants\": \"%d\","+
            "    \"bookingSourceSystem\": \"1\","+
            "    \"agreementNumber\": \"\""+
            "}";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_view);

        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Plant.class.getClassLoader());

        room = intentIn.getParcelableExtra(AvailableRoomView.CHOSEN_ROOM_INFO);
        Log.d(TAG, "onCreate: foodItem 1 is: " + room.getPlant().getFoodAndBeverages().get(0).getDescription());
        Log.d(TAG, "onCreate: technology 1 is: " + room.getTechnologies().get(0).getDescription());
        token = intentIn.getStringExtra(CreateUserView.TOKEN_INFO);
        Log.d(TAG, "onCreate: token is: " + token);
        numberOfPeople = intentIn.getIntExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, 1);

        chosenSeatingId = ""; // get input from user
        bookingCode = ""; // get input if user wants prenoon, afternoon or full day
        chosenTechnologies = new ArrayList<>(); // get input from user (available technologies will soon be available in room)
        chosenFoodAndBeverages = new ArrayList<>(); // get input from user (available food will soon be available in room)

        APIRequester apiRequester = new APIRequester(token, this, CREATE_BOOKING_MESSAGE);
        String formattedJsonCreateBooking = String.format(jsonCreateBooking, numberOfPeople);
        Log.d(TAG, "onCreate: jsonString is: " + formattedJsonCreateBooking);
        apiRequester.execute(CREATE_BOOKING_URL, formattedJsonCreateBooking);
    }

    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {

        switch(message ) {
            case CREATE_BOOKING_MESSAGE:
                Log.d(TAG, "onDownloadComplete: results are: " + results);

        }

    }
}
