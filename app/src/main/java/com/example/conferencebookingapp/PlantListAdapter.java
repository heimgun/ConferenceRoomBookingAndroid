package com.example.conferencebookingapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

public class PlantListAdapter extends RecyclerView.Adapter<PlantListAdapter.PlantViewHolder> {
    private static final String TAG = "PlantListAdapter";

    private ClickHandler clickHandler;


    public interface ClickHandler {
        void onButtonClicked(int position);
    }

    private List<Plant> availablePlants;

    public PlantListAdapter(List<Plant> availablePlants, ClickHandler clickHandler) {
        super();
        this.availablePlants = availablePlants;
        this.clickHandler = clickHandler;
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


        holder.clickHandler = this.clickHandler;
        Plant plant = availablePlants.get(position);

        int imageNumber = 0;
        int numberOfImages = plant.getImageUrls().size();

        String imageUrl = plant.getImageUrls().get(imageNumber);
        String formattedUrl = setProtocol(imageUrl);
        ImageDecoder decoder = new ImageDecoder(holder.plantImageView);
        decoder.execute(formattedUrl);




        holder.plantNameTextView.setText(plant.getName());

        String addressText = String.format("%s, %s", plant.getAddress(), plant.getCity());
        holder.addressTextView.setText(addressText);
        holder.plantInfoTextView.setText(plant.getFacts());

        StringBuilder foodStringBuilder = new StringBuilder("Tillgänglig mat och dryck: \n");
        Map<String, String> foodItems = plant.getFoodAndBeverages();
        for (String foodItem : foodItems.keySet()) {
            foodStringBuilder.append("- ").append(foodItem).append("\n");
        }
        holder.foodTextView.setText(foodStringBuilder.toString());

        holder.priceFromTextView.setText(String.format("%s kr", plant.getPriceFrom()));

        String numberOfRooms = plant.getNumberOfAvailableRooms();
        String numberOfRoomsText = numberOfRooms.equals("1") ? "%s rum tillgängligt" : "%s rum tillgängliga";

        holder.numberOfRoomsTextView.setText(String.format(numberOfRoomsText, numberOfRooms));



    }

    private String setProtocol(String url) {
        Log.d(TAG, "setProtocol: url is: " + url);
        Log.d(TAG, "setProtocol: protocol is: " + url.substring(0,5));
        String formattedUrl = url;
        if(!url.substring(0,4).equals("http")) {
           formattedUrl = "https:" + url;
        }
        return formattedUrl;
    }

    @Override
    public int getItemCount() {
        return availablePlants.size();
    }


    public static class PlantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView plantImageView;
        public TextView plantNameTextView;
        public TextView addressTextView;
        public TextView plantInfoTextView;
        public TextView foodTextView;
        public TextView priceFromTextView;
        public TextView numberOfRoomsTextView;
        public Button showRoomsButton;

        private ClickHandler clickHandler;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImageView = itemView.findViewById(R.id.plantImageView);
            plantNameTextView = itemView.findViewById(R.id.plantNameTextView);
            addressTextView = itemView.findViewById(R.id.plantAddressTextView);
            plantInfoTextView = itemView.findViewById(R.id.plantInfoTextView);
            foodTextView = itemView.findViewById(R.id.plantFoodTextView);
            priceFromTextView = itemView.findViewById(R.id.priceFromTextView);
            numberOfRoomsTextView = itemView.findViewById(R.id.numberOfRoomsTextView);
            showRoomsButton = itemView.findViewById(R.id.showRoomsButton);

            showRoomsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickHandler != null) {
                clickHandler.onButtonClicked(getAdapterPosition());
            }

        }
    }
}
