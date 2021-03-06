package com.example.conferencebookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author Heidi (mostly)
 */
public class CreateUserView extends AppCompatActivity implements CallbackActivity {
    private static final String TAG = "CreateUserView";

    EditText firstName, lastName, phone, email, organization, orgNumber, street, city, zipCode;
    Button submit;

    Booking booking;

    User user = new User();

    int phoneInt, orgNumberInt, zipInt;


    public static final String CREATE_USER = "com.example.conferencebookingapp.CREATE_USER";
    public static final String LOGIN =  "com.example.conferencebookingapp.LOGIN";
    public static final String TOKEN_INFO = "com.example.conferencebookingapp.TOKEN";


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_view);

        Intent intentIn = getIntent();
        intentIn.setExtrasClassLoader(Booking.class.getClassLoader());


        booking = intentIn.getParcelableExtra(BookingView.BOOKING_INFO);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        organization = (EditText) findViewById(R.id.organization);
        orgNumber = (EditText) findViewById(R.id.orgNumber);
        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        zipCode = (EditText) findViewById(R.id.zipCode);

        submit = (Button) findViewById(R.id.SubmitBtn);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate(firstName) && validate(lastName) && validate(phone) && validate(email) && validate(organization) && validate(orgNumber) && validate(street) && validate(city) && validate(zipCode) && validateEmail(email)) {

                    System.out.println("Validation complete");

                    String ph = phone.getText().toString();
                    phoneInt = Integer.parseInt(ph);

                    String oNr = orgNumber.getText().toString();
                    orgNumberInt = Integer.parseInt(oNr);

                    String z = zipCode.getText().toString();
                    zipInt = Integer.parseInt(z);

                    user.setFirstName(firstName.getText().toString().trim());
                    user.setLastName(lastName.getText().toString().trim());
                    user.setPhone(phoneInt);
                    user.setEmail(email.getText().toString().trim());
                    user.setUsername(email.getText().toString().trim());
                    user.setPassword(generateRandom());
                    user.setOrganization(organization.getText().toString().trim());
                    user.setOrgNumber(orgNumberInt);
                    user.setStreet(street.getText().toString().trim());
                    user.setCity(city.getText().toString().trim());
                    user.setZipCode(zipInt);

                    createUser();
                    logIn();


                }


            }
        });


    }


    public String generateRandom() {

        return UUID.randomUUID().toString();

    }

    public boolean validate(EditText editText) {

        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }

        editText.setError("Please fill this");
        editText.requestFocus();
        return false;

    }

    public boolean validateEmail(EditText editText) {

        boolean lowerCase = editText.getText().toString().equals(editText.getText().toString().toUpperCase());
        Pattern special = Pattern.compile("[!#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher match = special.matcher(editText.getText().toString().trim());
        boolean specialCharacter = match.find();


        if (editText.getText().toString().contains("@") && !lowerCase && !specialCharacter) {
            return true;
        }

        editText.setError("Please enter a valid email");
        editText.requestFocus();
        return false;
    }


    public void createUser() {

        final ProgressDialog progressDialog = new ProgressDialog(CreateUserView.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        APIRequester requester = null;

        try {
            requester = new APIRequester(CreateUserView.this, CREATE_USER);

            String jsonCreateUser = "{" +
                    "    \"first_name\": \"" + user.getFirstName() + "\"," +
                    "    \"last_name\": \"" + user.getLastName() + "\"," +
                    "    \"username\": \"z_" + user.getEmail() + "\"," +
                    "    \"password\": \"" + user.getPassword() + "\"," +
                    "    \"email\": \"" + user.getEmail() + "\"," +
                    "    \"phone_number\": \"" + user.getPhone() + "\"," +
                    "    \"organization_name\": \"" + user.getOrganization() + "\"," +
                    "    \"organization_nr\": \"" + user.getOrgNumber() + "\"," +
                    "    \"street\": \"" + user.getStreet() + "\"," +
                    "    \"city_name\": \"" + user.getCity() + "\"," +
                    "    \"zipCode\": \"" + user.getZipCode() + "\"" +
                    "}";

            requester.execute("https://dev-be.timetomeet.se/service/rest/user/add/", jsonCreateUser);

            System.out.println("Success");
            progressDialog.dismiss();
            Toast.makeText(CreateUserView.this, "User created: " + user.getUsername(), Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CreateUserView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            System.out.println("Failure");

        }


    }


    public void logIn() {

        APIRequester requester = null;
        try {

            requester = new APIRequester(CreateUserView.this, LOGIN);

            String jsonLogin = "{\"username\": \"z_" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\"}";
            requester.execute("https://dev-be.timetomeet.se/service/rest/api-token-auth/", jsonLogin);

            System.out.println("Login Success");


        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    public void onDownloadComplete(String s, String message) throws JSONException {

        System.out.println("Download complete. Results are: " + s);

        switch (message) {
            case CREATE_USER:
                break;
            case LOGIN:
                final JSONObject json = new JSONObject(s);

                user.setToken(json.getString("token"));

                Intent intent = new Intent(CreateUserView.this, ReceiptView.class);
                Log.d(TAG, "onDownloadComplete: number of people in booking is: " + booking.getNumberOfPeople());
                intent.putExtra(BookingView.BOOKING_INFO, booking);
                intent.putExtra(TOKEN_INFO, user.getToken());
                startActivity(intent);
                break;
            default:
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
        Intent intent = new Intent(this, BookingView.class);
        intent.putExtra(AvailableRoomView.CHOSEN_ROOM_INFO, booking.getRoom());
        intent.putExtra(AvailablePlantView.CHOSEN_PLANT_NAME, booking.getRoom().getPlant());
        intent.putExtra(SearchView.CHOSEN_DATE_INFO, booking.getChosenDate());
        intent.putExtra(SearchView.CHOSEN_NUMBER_OF_PEOPLE_INFO, booking.getNumberOfPeople());
        intent.putExtra(SearchView.CHOSEN_CITY_NAME, booking.getRoom().getCity());
        startActivity(intent);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}















