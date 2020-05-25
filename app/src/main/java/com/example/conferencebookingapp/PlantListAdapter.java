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

        holder.plantNameTextView.setText(availablePlants.get(position).getName());

        String addressText = String.format("%s, %s", availablePlants.get(position).getAddress(),
                availablePlants.get(position).getCity());
        holder.addressTextView.setText(addressText);
        holder.plantInfoTextView.setText(availablePlants.get(position).getFacts());
        String priceText = availablePlants.get(position).getPriceFrom() + "0 kr";
        holder.priceFromTextView.setText(priceText);

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




    public static class PlantViewHolder extends RecyclerView.ViewHolder {

        public View plantView;
        public ImageView plantImageView;
        public TextView plantNameTextView;
        public TextView addressTextView;
        public TextView plantInfoTextView;
        public TextView priceFromTextView;
        public TextView numberOfRoomsTextView;
        public Button showRoomsButton;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantView = itemView;
            plantImageView = plantView.findViewById(R.id.plantImageView);
            plantNameTextView = plantView.findViewById(R.id.plantNameTextView);
            addressTextView = plantView.findViewById(R.id.plantAddressTextView);
            plantInfoTextView = plantView.findViewById(R.id.plantInfoTextView);
            priceFromTextView = plantView.findViewById(R.id.priceFromTextView);
            numberOfRoomsTextView = plantView.findViewById(R.id.numberOfRoomsTextView);
            showRoomsButton = plantView.findViewById(R.id.showRoomsButton);
        }
    }
}
