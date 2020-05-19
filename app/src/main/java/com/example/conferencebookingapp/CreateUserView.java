package com.example.conferencebookingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

/*
TODO:
 1. Create Alerts
 2. Create onFocusListener for email with params
 3. Communicate with REST API (possibly validate params via API?)
 */


public class CreateUserView extends AppCompatActivity {

    EditText firstName, lastName, phone, email, organization, orgNumber, street, city, zipCode;
    Button submit;

    User user = new User();


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


                if(firstName == null){
                    //fix alert btn
                }

                if(lastName == null){
                    //fix alert btn
                }

                if(phone == null) {

                }

                if(email == null){
                    //fix alert btn
                }

                //Alerts for email aka username
                // TODO: setOnFocusListener with params z_, @, ., and all small letters
                if(!email.getText().toString().contains("@")){
                    //fix alert
                }


                if(organization == null){
                    //fix alert btn
                }

                if(orgNumber == null){
                    //fix alert btn
                }

                if(street == null){
                    //fix alert btn
                }

                if(zipCode == null){
                    //fix alert btn
                }

                if(city == null){
                    //fix alert btn
                }

                else{

                    String ph = phone.getText().toString();
                    int phone = Integer.parseInt(ph);

                    String oNr = orgNumber.getText().toString();
                    int orgNumber = Integer.parseInt(oNr);

                    String z = zipCode.getText().toString();
                    int zip = Integer.parseInt(z);

                    user.setFirstName(firstName.getText().toString());
                    user.setLastName(lastName.getText().toString());
                    user.setUsername(email.getText().toString());
                    user.setPassword(generateRandom());
                    user.setPhone(phone);
                    user.setOrganization(organization.getText().toString());
                    user.setOrgNumber(orgNumber);
                    user.setStreet(street.getText().toString());
                    user.setCity(city.getText().toString());
                    user.setZipCode(zip);


                    //COde for Calling POST on API



                }


            }
        });


    }


    public String generateRandom(){

        return UUID.randomUUID().toString();

    }



}
