package com.example.conferencebookingapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

/**
 * @author Heidi
 * @author Karin
 */
public class ReceiptView extends AppCompatActivity implements CallbackActivity {
    private static final String TAG = "ReceiptView";

    Button backToSearch;

    TextView roomTV, plantTV, cityTV, addressTV, numberOfPeopleTV, dateTV, foodAndDrinksTV, equipmentTV, seatingTV, priceTV;

    private Booking booking;
    private String token;

    public static final String CREATE_BOOKING_URL = "https://dev-be.timetomeet.se/service/rest/booking/add/";
    public static final String BOOK_ROOM_URL = "https://dev-be.timetomeet.se/service/rest/conferenceroomavailability/book/%s/";
    public static final String ADD_FOOD_URL = "https://dev-be.timetomeet.se/service/rest/bookingfoodbeverage/add/";
    public static final String ADD_TECHNOLOGY_URL = "https://dev-be.timetomeet.se/service/rest/bookingselectabletechnology/add/";
    public static final String COMPLETE_BOOKING_URL = "https://dev-be.timetomeet.se/service/rest/booking/completed/";

    public static final String CREATE_BOOKING_MESSAGE = "com.example.conferencebookingapp.CREATE_BOOKING";
    public static final String BOOK_ROOM_MESSAGE = "com.example.conferencebookingapp.BOOK_ROOM";
    public static final String FINAL_BOOKING_CODE_MESSAGE = "com.example.conferencebookingapp.FINAL_BOOKING_CODE";
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

        roomTV = (TextView) findViewById(R.id.roomNameTV);
        plantTV = (TextView) findViewById(R.id.plantNameTV);
        cityTV = (TextView) findViewById(R.id.cityTV);
        addressTV = (TextView) findViewById(R.id.addressTV);
        numberOfPeopleTV = (TextView) findViewById(R.id.numberOfPeopleTV);
        dateTV = (TextView) findViewById(R.id.dateTV);
        foodAndDrinksTV = (TextView) findViewById(R.id.foodAndDrinksTV);
        equipmentTV = (TextView) findViewById(R.id.equipmentTV);
        seatingTV = (TextView) findViewById(R.id.sittingTV);
        priceTV = (TextView) findViewById(R.id.priceTV);

        backToSearch = (Button) findViewById(R.id.startOverButton);
        backToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceiptView.this, SearchView.class));
            }
        });

        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Booking.class.getClassLoader());

        booking = intentIn.getParcelableExtra(BookingView.BOOKING_INFO);
        token = intentIn.getStringExtra(CreateUserView.TOKEN_INFO);

        if (isValidBooking() && !token.trim().isEmpty()) {
            Log.d(TAG, "onCreate: booking is valid");
            setUpWindow();

        } else {
            Log.e(TAG, "onCreate: booking is not valid");
            onBookingFailed();
        }
    }


    public boolean isValidBooking() {
        return booking != null && booking.getRoom() != null && booking.getNumberOfPeople() > 0
                && booking.getChosenSeating() != null;
    }

    public void setUpWindow() {
        APIRequester apiRequester = new APIRequester(token, this, CREATE_BOOKING_MESSAGE);
        String formattedJsonCreateBooking = String.format(jsonCreateBooking, booking.getNumberOfPeople());
        Log.d(TAG, "onCreate: jsonString is: " + formattedJsonCreateBooking);
        apiRequester.execute(CREATE_BOOKING_URL, formattedJsonCreateBooking);

        roomTV.setText(booking.getRoom().getName());
        plantTV.setText(booking.getRoom().getPlant().getName());
        cityTV.setText(booking.getRoom().getPlant().getCity());
        addressTV.setText(booking.getRoom().getPlant().getAddress());

        String numberOfPeopleString = booking.getNumberOfPeople() + " personer";
        numberOfPeopleTV.setText(numberOfPeopleString);

        boolean preNoonRequested = false;
        boolean afternoonRequested = false;
        if (!booking.getBookingCodes().isEmpty()) {
            for (int i = 0; i < booking.getBookingCodes().size(); i++) {
                if (booking.getBookingCodes().get(i).equals(booking.getRoom().getPreNoonBookingCode())) {
                    preNoonRequested = true;
                } else if (booking.getBookingCodes().get(i).equals(booking.getRoom().getAfternoonBookingCode())) {
                    afternoonRequested = true;
                }
            }
        }

        Log.d(TAG, "setUpWindow: preNoonRequested = " + preNoonRequested);
        Log.d(TAG, "setUpWindow: afternoonRequested = " + afternoonRequested);

        String timeText = "";
        double priceForRoom = 0;
        if(preNoonRequested && afternoonRequested) {
            priceForRoom += Double.parseDouble(booking.getRoom().getFullDayPrice());
            timeText = booking.getRoom().getPreNoonHours() + " + " + booking.getRoom().getAfternoonHours();
        } else if(preNoonRequested) {
            priceForRoom += Double.parseDouble(booking.getRoom().getPreNoonPrice());
            timeText = booking.getRoom().getPreNoonHours();
        } else {
            priceForRoom += Double.parseDouble(booking.getRoom().getAfternoonPrice());
            timeText = booking.getRoom().getAfternoonHours();
        }

        String dateText = booking.getChosenDate() + ", " + timeText;
        dateTV.setText(dateText);

        String seatingText = "Vald möblering: " + booking.getChosenSeating().getDescription()
                + " (Max antal personer: " + booking.getChosenSeating().getNumberOfPeople() + ")";
        seatingTV.setText(seatingText);

        int priceForFood = 0;
        int numberOfFoodItems = booking.getChosenFoodAndBeverages().size();
        if (numberOfFoodItems == 0) {
            foodAndDrinksTV.setText("Ingen mat eller dryck vald");
        } else {
            StringBuilder foodAndBeveragesSb = new StringBuilder("Vald mat och dryck:");
            for (int i = 0; i < numberOfFoodItems; i++) {
                foodAndBeveragesSb.append("\n- ");
                foodAndBeveragesSb.append(booking.getChosenFoodAndBeverages().get(i).getDescription());
                int numberOfPeople = booking.getNumberOfPeople();
                double pricePerFoodItem = Double.parseDouble(booking.getChosenFoodAndBeverages().get(i).getPrice());
                double totalPriceForFoodItem = numberOfPeople * pricePerFoodItem;
                priceForFood += totalPriceForFoodItem;

                String foodPriceText = String.format(" (%d x %.2f kr = %.2f kr)", numberOfPeople,
                        pricePerFoodItem, totalPriceForFoodItem);
                foodAndBeveragesSb.append(foodPriceText);
            }
            foodAndDrinksTV.setText(foodAndBeveragesSb.toString());
        }

        int numberOfTechnologyItems = booking.getChosenTechnologies().size();
        if (numberOfTechnologyItems == 0) {
            equipmentTV.setText("Ingen extra utrustning vald");
        } else {
            StringBuilder technologySb = new StringBuilder("Vald utrustning:");
            for (int i = 0; i < numberOfTechnologyItems; i++) {
                technologySb.append("\n- ");
                technologySb.append(booking.getChosenTechnologies().get(i).getDescription());
            }
            equipmentTV.setText(technologySb.toString());
        }

        String totalPriceText =  String.format("%.2f kr", (priceForRoom + priceForFood));
        priceTV.setText(totalPriceText);

    }

    public void onBookingFailed() {
        Toast.makeText(this, "Ett fel inträffade.\nBokningen kunde inte genomföras", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SearchView.class);
        startActivity(intent);
    }

    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {
        if(results == null) {
            onBookingFailed();
        } else {
            switch(message) {
                case CREATE_BOOKING_MESSAGE:
                    Log.d(TAG, "onDownloadComplete: results are: " + results);
                    int numberOfBookingCodes = booking.getBookingCodes().size();
                    if (numberOfBookingCodes > 0) {
                        for (int i = 0; i < numberOfBookingCodes; i++) {
                            if (booking.getBookingCodes().get(i).equals("null")) {
                                Log.e(TAG, "onDownloadComplete: invalid booking code");
                                onBookingFailed();
                            } else {
                                String requestMessage = i == numberOfBookingCodes - 1 ? FINAL_BOOKING_CODE_MESSAGE : BOOK_ROOM_MESSAGE;
                                APIRequester roomRequester = new APIRequester(token, this, requestMessage);

                                String bookRoomUrl = String.format(BOOK_ROOM_URL, booking.getBookingCodes().get(i));
                                String formattedJsonBookRoom = String.format(jsonBookRoom, booking.getChosenSeating().getId());
                                Log.d(TAG, "onDownloadComplete: createBookingmessage-if calling book room with message " + requestMessage);
                                roomRequester.execute(bookRoomUrl, formattedJsonBookRoom);
                            }
                        }
                    } else {
                        Log.e(TAG, "onDownloadComplete: no booking code found");
                        onBookingFailed();
                    }

                    break;
                case BOOK_ROOM_MESSAGE:
                    Log.d(TAG, "onDownloadComplete: first booking code added. Result is: " + results);
                    break;
                case FINAL_BOOKING_CODE_MESSAGE:
                    Log.d(TAG, "onDownloadComplete: final booking code added. Result is: " + results);

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

    }

    private void addFoodToBooking(int numberOfItemsRequested) {

        for (int i = 0; i < numberOfItemsRequested; i++) {

            // this could be improved in a later sprint where input from user could be used to set serving time
            String servingTime = booking.getChosenDate() + "T10:30:00";
            String formattedJsonAddFood = String.format(jsonAddFoodToBooking, booking.getBookingCodes().get(0), booking.getChosenFoodAndBeverages().get(i).getId(),
                    booking.getNumberOfPeople(), servingTime);

            String addFoodMessage = i == numberOfItemsRequested - 1 ? FINAL_FOOD_ADDED_MESSAGE : ADD_FOOD_MESSAGE;
            APIRequester foodRequester = new APIRequester(token, this, addFoodMessage);
            foodRequester.execute(ADD_FOOD_URL, formattedJsonAddFood);
        }
    }

    private void addTechnologyToBooking(int numberOfItemsRequested) {
        for (int i = 0; i < numberOfItemsRequested; i++) {
            String formattedJsonAddTechnology = String.format(jsonAddTechnologyToBooking, booking.getBookingCodes().get(0),
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
