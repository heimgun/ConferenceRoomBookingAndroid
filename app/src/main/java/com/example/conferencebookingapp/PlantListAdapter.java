package com.example.conferencebookingapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.PlantViewHolder> {
    private static final String TAG = "PlantListAdapter";

    private List<Plant> availablePlants;

    public PlantListAdapter(List<Plant> availablePlants) {
        this.availablePlants = availablePlants;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_plant, parent, false);

        return new PlantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {


        holder.plantNameTextView.setText(availablePlants.get(position).getName());

        String addressText = String.format("%s, %s", availablePlants.get(position).getAddress(),
                availablePlants.get(position).getCity());
        holder.addressTextView.setText(addressText);
        holder.plantInfoTextView.setText(availablePlants.get(position).getFacts());
        holder.priceFromTextView.setText(availablePlants.get(position).getPriceFrom());

        String numberOfRooms = availablePlants.get(position).getNumberOfAvailableRooms();
        String numberOfRoomsText = numberOfRooms.equals("1") ? "%s rum tillgängligt" : "%s rum tillgängliga";

        holder.numberOfRoomsTextView.setText(String.format(numberOfRoomsText, numberOfRooms));

        /*holder.showRoomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(, CreateUserView.class));
            }
        });

         */



    }

    @Override
    public int getItemCount() {
        return availablePlants.size();
    }


    public static class PlantViewHolder extends RecyclerView.ViewHolder {

        public View plantView;
        public TextView plantNameTextView;
        public TextView addressTextView;
        public TextView plantInfoTextView;
        public TextView priceFromTextView;
        public TextView numberOfRoomsTextView;
        public Button showRoomsButton;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantView = itemView;
            plantNameTextView = plantView.findViewById(R.id.plantNameTextView);
            addressTextView = plantView.findViewById(R.id.plantAddressTextView);
            plantInfoTextView = plantView.findViewById(R.id.plantInfoTextView);
            priceFromTextView = plantView.findViewById(R.id.priceFromTextView);
            numberOfRoomsTextView = plantView.findViewById(R.id.numberOfRoomsTextView);
            showRoomsButton = plantView.findViewById(R.id.showRoomsButton);
        }
    }
}
