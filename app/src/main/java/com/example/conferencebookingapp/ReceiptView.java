package com.example.conferencebookingapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class ReceiptView extends AppCompatActivity implements CallbackActivity {

    Button backToSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_receipt_view);

            backToSearch = (Button) findViewById(R.id.startOverButton);













            backToSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ReceiptView.this, SearchView.class));
                }
            });


    }






    @Override
    public void onDownloadComplete(String results, String message) throws JSONException {

    }
}
