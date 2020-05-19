package com.example.conferencebookingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class AvailableRoomView extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_room_view);

        recyclerView = findViewById(R.id.resultsRecyclerView);
        recyclerView.setHasFixedSize(true);
    }


    public void onDownLoadComplete(List<ConferenceRoom> availableRooms) {

    }
}
