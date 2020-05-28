package com.example.conferencebookingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
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

        //String imageUrl = availablePlants.get(position).getImageUrls().get(0);

       //BitmapTask bitmapTask = new BitmapTask(holder.plantImageView);
        //bitmapTask.execute(imageUrl);


        /*if(!imageUrl.equals("")) {
            Log.d(TAG, "onBindViewHolder: imageUrl is: " + imageUrl);
            Uri imageUri = Uri.parse(imageUrl);
            holder.plantImageView.setImageURI(imageUri);
            Log.d(TAG, "onBindViewHolder: imageUri set");
        } else {
            Log.d(TAG, "onBindViewHolder: no imageUrl received");
        }

         */

        Plant plant = availablePlants.get(position);

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

    @Override
    public int getItemCount() {
        return availablePlants.size();
    }

    /*private class BitmapTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public BitmapTask(ImageView imageView) {
            super();
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {  // the code in this method is taken from https://stackoverflow.com/questions/3870638/how-to-use-setimageuri-on-android
            Bitmap bm = null;
            try {
                URL aURL = new URL(strings[0]);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Error getting bitmap", e);
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

     */




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
