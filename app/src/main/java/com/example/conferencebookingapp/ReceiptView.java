package com.example.conferencebookingapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class ReceiptView extends AppCompatActivity implements CallbackActivity {
    private static final String TAG = "ReceiptView";

    Button backToSearch;

    private Booking booking;
    private String token;

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


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_receipt_view);

            backToSearch = (Button) findViewById(R.id.startOverButton);

            Intent intentIn = new Intent();
            intentIn.setExtrasClassLoader(Booking.class.getClassLoader());

            booking = intentIn.getParcelableExtra(BookingView.BOOKING_INFO);
            //Log.d(TAG, "onCreate: number of people in booking is: " + booking.getNumberOfPeople());
            token = intentIn.getStringExtra(CreateUserView.TOKEN_INFO);

           // APIRequester apiRequester = new APIRequester(token, this, CREATE_BOOKING_MESSAGE);
            //String formattedJsonCreateBooking = String.format(jsonCreateBooking, booking.getNumberOfPeople());
            //Log.d(TAG, "onCreate: jsonString is: " + formattedJsonCreateBooking);
            //apiRequester.execute(CREATE_BOOKING_URL, formattedJsonCreateBooking);













            backToSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ReceiptView.this, SearchView.class));
                }
            });


    }






    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {
        switch(message ) {
            case CREATE_BOOKING_MESSAGE:
                Log.d(TAG, "onDownloadComplete: results are: " + results);
                APIRequester roomRequester = new APIRequester(token, this, BOOK_ROOM_MESSAGE);
                String bookRoomUrl = String.format(BOOK_ROOM_URL, booking.getBookingCode());
                String formattedJsonBookRoom = String.format(jsonBookRoom, booking.getChosenSeating().getId());
                Log.d(TAG, "onDownloadComplete: createBookingmessage-if calling book room");
                roomRequester.execute(bookRoomUrl, formattedJsonBookRoom);
                break;
            case BOOK_ROOM_MESSAGE:
                Log.d(TAG, "onDownloadComplete: results are: " + results);

                int numberOfFoodItemsRequested = booking.getChosenFoodAndBeverages().size();
                if(numberOfFoodItemsRequested > 0) {
                    Log.d(TAG, "onDownloadComplete: bookroommessage-if calling add food");
                    addFoodToBooking(numberOfFoodItemsRequested);
                } else {
                    int numberOfTechnologiesRequested = booking.getChosenTechnologies().size();
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
                int numberOfTechnologiesRequested = booking.getChosenTechnologies().size();
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
            String servingTime = booking.getChosenDate() + "T10:30:00";
            Log.d(TAG, "onDownloadComplete: serving time is: " + servingTime);
            String formattedJsonAddFood = String.format(jsonAddFoodToBooking, booking.getBookingCode(), booking.getChosenFoodAndBeverages().get(i).getId(),
                    booking.getNumberOfPeople(), servingTime);

            String addFoodMessage = i == numberOfItemsRequested - 1 ? FINAL_FOOD_ADDED_MESSAGE : ADD_FOOD_MESSAGE;
            APIRequester foodRequester = new APIRequester(token, this, addFoodMessage);
            foodRequester.execute(ADD_FOOD_URL, formattedJsonAddFood);
        }
    }

    private void addTechnologyToBooking(int numberOfItemsRequested) {
        for (int i = 0; i < numberOfItemsRequested; i++) {
            String formattedJsonAddTechnology = String.format(jsonAddTechnologyToBooking, booking.getBookingCode(),
                    booking.getChosenTechnologies().get(i).getId());

            String addTechnologyMessage = i == numberOfItemsRequested - 1 ? FINAL_TECHNOLOGY_ADDED_MESSAGE : ADD_TECHNOLOGY_MESSAGE;
            APIRequester technologyRequester = new APIRequester(token, this, addTechnologyMessage);
            technologyRequester.execute(ADD_TECHNOLOGY_URL, formattedJsonAddTechnology);
        }
    }

    private void completeBooking() {
        Log.d(TAG, "completeBooking: completeBooking called");
        Downloader downloader = new Downloader(token, this, COMPLETE_BOOKING_MESSAGE);
        downloader.execute(COMPLETE_BOOKING_URL);
    }
}
