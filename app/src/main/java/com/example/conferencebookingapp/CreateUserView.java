package com.example.conferencebookingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*
TODO:
 1. Move Async Restconnection-Class/implement w APIRequester-class
 2. Create onFocusListener for email with params

 */


public class CreateUserView extends AppCompatActivity {

    EditText firstName, lastName, phone, email, organization, orgNumber, street, city, zipCode;
    Button submit;

    RestConnection asyncTask = null;

    User user = new User();

    int phoneInt, orgNumberInt, zipInt;
    String token;


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

                if (validate(firstName) && validate(lastName) && validate(phone) && validate(email) && validate(organization) && validate(orgNumber) && validate(street) && validate(city) && validate(zipCode)) {

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


    public void createUser() {

        final ProgressDialog progressDialog = new ProgressDialog(CreateUserView.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        AsyncTask<String, Void, String> asyncTask = null;

        try {
            asyncTask = new RestConnection();

            String jsonCreateUser = "{" +
                    "    \"first_name\": \"" + user.getFirstName() + "\"," +
                    "    \"last_name\"" + user.getLastName() + "\"," +
                    "    \"username\"" + user.getEmail() + "\"," +
                    "    \"password\"" + user.getPassword() + "\"," +
                    "    \"email\"" + user.getEmail() + "\"," +
                    "    \"phone_number\"" + user.getPhone() + "\"," +
                    "    \"organization_name\"" + user.getOrganization() + "\"," +
                    "    \"organization_nr\"" + user.getOrgNumber() + "\"," +
                    "    \"street\"" + user.getStreet() + "\"," +
                    "    \"city_name\"" + user.getCity() + "\"," +
                    "    \"zipCode\"" + user.getZipCode() + "\"," +
                    "}";
            asyncTask.execute("https://dev-be.timetomeet.se/service/rest/user/add/", jsonCreateUser);

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

        AsyncTask<String, Void, String> asyncTask = null;
        try {
            asyncTask = new RestConnection();

            String jsonLogin = "{ \"username\": \"" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\" }";
            asyncTask.execute("https://dev-be.timetomeet.se/service/rest/api-token-auth/", jsonLogin);

            System.out.println("Login Success");


        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    public void onDownloadComplete(String s) {

    }


    //TODO: Move this class
    class RestConnection extends AsyncTask<String, Void, String> {
        //private HttpURLConnection Client ;
        private String responseContent;
        private String Error = null;


        protected String doInBackground(String... requestData) {
            BufferedReader reader = null;


            try {
                URL url = new URL(requestData[0]);

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true); // True för POST, PUT. False för GET

                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(requestData[1]); // { "username": ....
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "  \n");
                }
                int code = connection.getResponseCode();

                responseContent = sb.toString();
            } catch (Exception ex) {
                Error = ex.getMessage();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {

                }
            }

            return responseContent;
        }

    }
}

/*

   public void register(){

        //ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(CreateUserView.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        APIRestAdapter.getClient().registration(firstName.getText().toString().trim(),
                lastName.getText().toString().trim(),
                email.getText().toString().trim(),
                generateRandom(),
                email.getText().toString().trim(),
                phoneInt,
                organization.getText().toString().trim(),
                orgNumberInt,
                street.getText().toString().trim(),
                city.getText().toString().trim(),
                zipInt,
                new Callback<User>() {
                    @Override
                    public void success(User userResponse, Response response) {
                        progressDialog.dismiss();
                        user = userResponse;

                        Toast.makeText(CreateUserView.this, "User created: " + userResponse.getUsername(), Toast.LENGTH_SHORT).show();
                        System.out.println("Success");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(CreateUserView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        System.out.println("Failure");

                    }
                });


    } */



