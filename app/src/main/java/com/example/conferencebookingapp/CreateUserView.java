package com.example.conferencebookingapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/*
TODO:
 1. Create Alerts
 2. Create onFocusListener for email with params
 3. Communicate with REST API (possibly validate params via API?)
 */


public class CreateUserView extends AppCompatActivity {

    EditText firstName, lastName, phone, email, organization, orgNumber, street, city, zipCode;
    Button submit;

    User user;

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

                    if(validate(firstName) && validate(lastName) && validate(phone) && validate(email) && validate(organization) && validate(orgNumber) && validate(street) && validate(city) && validate(zipCode)){

                        System.out.println("Validation complete");

                        String ph = phone.getText().toString();
                        phoneInt = Integer.parseInt(ph);

                        String oNr = orgNumber.getText().toString();
                        orgNumberInt = Integer.parseInt(oNr);

                        String z = zipCode.getText().toString();
                        zipInt = Integer.parseInt(z);

                        register();



                    }



            }
        });


    }


    public String generateRandom(){

        return UUID.randomUUID().toString();

    }

    public boolean validate(EditText editText){

        if(editText.getText().toString().trim().length() > 0){
            return true;
        }

        editText.setError("Please fill this");
        editText.requestFocus();
        return false;

    }

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


    }


}
