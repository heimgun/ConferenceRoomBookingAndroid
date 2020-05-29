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

import java.util.List;
import java.util.Map;

public class AvailableRoomListAdapter extends RecyclerView.Adapter<AvailableRoomListAdapter.MyViewHolder> {
    private static final String TAG = "RoomListAdapter";

    private List<ConferenceRoom> availableRooms;

    private ClickHandler clickHandler;


    public interface ClickHandler {
        void onButtonClicked(int position);
    }

    public AvailableRoomListAdapter(List<ConferenceRoom> availableRooms, ClickHandler clickHandler) {
        super();
        this.availableRooms = availableRooms;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_conference_room, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.clickHandler = this.clickHandler;

        ConferenceRoom room = availableRooms.get(position);

        String imageUrl = room.getImageUrls().get(0);
        String formattedUrl = setProtocol(imageUrl);

        ImageDecoder imageDecoder = new ImageDecoder(holder.roomImageView);
        imageDecoder.execute(formattedUrl);

        holder.roomNameTextView.setText(room.getName());

        String maxNumberText = String.format("Max antal deltagare: %d personer", room.getMaxNumberOfPeople());
        holder.maxNumberTextView.setText(maxNumberText);

        StringBuilder seatingStringBuilder = new StringBuilder("Tillgängliga möbleringar: \n");
        Map<String, String> seatings = room.getSeatings();
        for (String key: seatings.keySet()) {
            seatingStringBuilder.append("- ").append(key).append("\n");
        }
        holder.seatingsTextView.setText(seatingStringBuilder.toString());

        StringBuilder technologyStringBuilder = new StringBuilder("Bokningsbar teknologi: \n");
        Map<String, String> technologies = room.getTechnologies();
        for (String key: technologies.keySet()) {
            technologyStringBuilder.append("- ").append(key).append("\n");
        }
        holder.technologyTextView.setText(technologyStringBuilder.toString());
        Log.d(TAG, "onBindViewHolder: technology text set");

        holder.descriptionTextView.setText(room.getDescription());
        Log.d(TAG, "onBindViewHolder: description text set");


        holder.preNoonHoursTextView.setText(room.getPreNoonHours());
        holder.preNoonPriceTextView.setText(String.format("%s kr", room.getPreNoonPrice()));
        holder.afternoonHoursTextView.setText(room.getAfternoonHours());
        holder.afternoonPriceTextView.setText(String.format("%s kr", room.getAfternoonPrice()));
        holder.fullDayHoursTextView.setText(room.getFullDayHours());
        holder.fullDayPriceTextView.setText(String.format("%s kr", room.getFullDayPrice()));
    }

    @Override
    public int getItemCount() {
        return availableRooms.size();
    }

    private String setProtocol(String url) {
        Log.d(TAG, "setProtocol: url is: " + url);
        Log.d(TAG, "setProtocol: protocol is: " + url.substring(0,5));
        String formattedUrl = url;
        if(!url.substring(0,5).equals("https")) {
            formattedUrl = "https:" + url;
        }
        return formattedUrl;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView roomImageView;
        public TextView roomNameTextView;
        public TextView maxNumberTextView;
        public TextView seatingsTextView;
        public TextView technologyTextView;
        public TextView descriptionTextView;
        public TextView preNoonHoursTextView;
        public TextView preNoonPriceTextView;
        public TextView afternoonHoursTextView;
        public TextView afternoonPriceTextView;
        public TextView fullDayHoursTextView;
        public TextView fullDayPriceTextView;
        public Button bookRoomButton;


        private ClickHandler clickHandler;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImageView = itemView.findViewById(R.id.roomImageView);
            roomNameTextView = itemView.findViewById(R.id.roomNameTextView);
            maxNumberTextView = itemView.findViewById(R.id.maxNumberTextView);
            seatingsTextView = itemView.findViewById(R.id.seatingsTextView);
            technologyTextView = itemView.findViewById(R.id.technologyTextView);
            descriptionTextView = itemView.findViewById(R.id.roomDescriptionTextView);
            preNoonHoursTextView = itemView.findViewById(R.id.roomPreNoonHoursTextView);
            preNoonPriceTextView = itemView.findViewById(R.id.roomPreNoonPriceTextView);
            afternoonHoursTextView = itemView.findViewById(R.id.roomAfternoonHoursTextView);
            afternoonPriceTextView = itemView.findViewById(R.id.roomAfternoonPriceTextView);
            fullDayHoursTextView = itemView.findViewById(R.id.roomFullDayHoursTextView);
            fullDayPriceTextView = itemView.findViewById(R.id.roomFullDayPriceTextView);
            bookRoomButton = itemView.findViewById(R.id.bookRoomButton);
            bookRoomButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                if(clickHandler != null){
                    clickHandler.onButtonClicked(getAdapterPosition());
                }

        }
    }

}
