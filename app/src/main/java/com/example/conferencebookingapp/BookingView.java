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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_view);

        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Plant.class.getClassLoader());

        room = intentIn.getParcelableExtra(AvailableRoomView.CHOSEN_ROOM_INFO);
        token = intentIn.getStringExtra(CreateUserView.TOKEN_INFO);
        Log.d(TAG, "onCreate: token is: " + token);

        chosenSeatingId = "";
        bookingCode = "";
        chosenTechnologies = new ArrayList<>();
        chosenFoodAndBeverages = new ArrayList<>();

        APIRequester apiRequester = new APIRequester(token, this, CREATE_BOOKING_MESSAGE);
        apiRequester.execute(CREATE_BOOKING_URL);
    }

    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {

        switch(message ) {
            case CREATE_BOOKING_MESSAGE:
                Log.d(TAG, "onDownloadComplete: results are: " + results);

        }

    }
}
