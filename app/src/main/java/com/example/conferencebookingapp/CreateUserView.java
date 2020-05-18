package com.example.conferencebookingapp;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class CreateUserView extends AppCompatActivity {

    EditText firstName, lastName, phone, organization, orgNumber, street, city, zipCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_view);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        phone = (EditText) findViewById(R.id.phone);
        organization = (EditText) findViewById(R.id.organization);
        orgNumber = (EditText) findViewById(R.id.orgNumber);
        street = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        zipCode = (EditText) findViewById(R.id.zipCode);






    }



}
