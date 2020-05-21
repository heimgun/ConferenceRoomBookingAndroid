package com.example.conferencebookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class CreateUserView extends AppCompatActivity implements CallbackActivity {

    EditText firstName, lastName, phone, email, organization, orgNumber, street, city, zipCode;
    Button submit;


    User user = new User();

    int phoneInt, orgNumberInt, zipInt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_view);
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

                    startActivity(new Intent(CreateUserView.this, AvailableRoomView.class));

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

        editText.setError("Please enter a correct email");
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
            requester = new APIRequester(CreateUserView.this);

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

            requester = new APIRequester(CreateUserView.this);

            String jsonLogin = "{\"username\": \"z_" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\"}";
            requester.execute("https://dev-be.timetomeet.se/service/rest/api-token-auth/", jsonLogin);

            System.out.println("Login Success");


        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    public void onDownloadComplete(String s) throws JSONException {
        System.out.println("Download complete. Results are: " + s);

        final JSONObject json = new JSONObject(s);
        System.out.println(json.getString("token"));

        user.setToken(json.getString("token"));


    }

}















