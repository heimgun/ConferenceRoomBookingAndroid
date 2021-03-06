package com.example.conferencebookingapp;

import android.content.Context;
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

/**
 * @author Karin
 */
public class AvailableRoomListAdapter extends RecyclerView.Adapter<AvailableRoomListAdapter.MyViewHolder> {
    private static final String TAG = "RoomListAdapter";

    private List<ConferenceRoom> availableRooms;

    private ClickHandler clickHandler;

    private Context context;


    public interface ClickHandler {
        void onButtonClicked(int position);
    }

    public AvailableRoomListAdapter(List<ConferenceRoom> availableRooms, ClickHandler clickHandler, Context context) {
        super();
        this.availableRooms = availableRooms;
        this.clickHandler = clickHandler;
        this.context = context;
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

        if(room.getImageUrls().size() > 0) {

            String imageUrl = room.getImageUrls().get(0);
            String formattedUrl = setProtocol(imageUrl);

            ImageDecoder imageDecoder = new ImageDecoder(holder.roomImageView);
            imageDecoder.execute(formattedUrl);
        } else {
            holder.roomImageView.setImageResource(R.drawable.timetomeet_logo);
            holder.roomImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }


        holder.roomNameTextView.setText(room.getName());

        String maxNumberText = String.format("Max antal deltagare: %d personer", room.getMaxNumberOfPeople());
        holder.maxNumberTextView.setText(maxNumberText);

        StringBuilder seatingStringBuilder = new StringBuilder("Tillgängliga möbleringar: \n");
        List<Seating> seatings = room.getSeatings();
        for (Seating seating : seatings) {
            seatingStringBuilder.append("- ").append(seating.getDescription()).append("\n");
        }
        holder.seatingsTextView.setText(seatingStringBuilder.toString());

        StringBuilder technologyStringBuilder = new StringBuilder("Bokningsbar teknologi: \n");
        List<TechnologyItem> technologies = room.getTechnologies();
        for (TechnologyItem technology: technologies) {
            technologyStringBuilder.append("- ").append(technology.getDescription()).append("\n");
        }
        holder.technologyTextView.setText(technologyStringBuilder.toString());
        Log.d(TAG, "onBindViewHolder: technology text set");

        holder.descriptionTextView.setText(room.getDescription());
        Log.d(TAG, "onBindViewHolder: description text set");


        holder.preNoonHoursTextView.setText(room.getPreNoonHours());
        holder.preNoonPriceTextView.setText(String.format("%s kr", room.getPreNoonPrice()));
        boolean isAvailablePreNoon = !(room.getPreNoonBookingCode().trim().isEmpty() || room.getPreNoonBookingCode().equals("null"));
        if(isAvailablePreNoon) {
            holder.preNoonPriceTextView.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            int defaultColor = holder.preNoonPriceTextView.getTextColors().getDefaultColor();
            holder.preNoonPriceTextView.setTextColor(defaultColor);
        }


        holder.afternoonHoursTextView.setText(room.getAfternoonHours());
        holder.afternoonPriceTextView.setText(String.format("%s kr", room.getAfternoonPrice()));
        boolean isAvailableAfternoon = !(room.getAfternoonBookingCode().trim().isEmpty() || room.getAfternoonBookingCode().equals("null"));
        if(isAvailableAfternoon) {
            holder.afternoonPriceTextView.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            int defaultColor = holder.afternoonPriceTextView.getTextColors().getDefaultColor();
            holder.afternoonPriceTextView.setTextColor(defaultColor);
        }

        holder.fullDayHoursTextView.setText(room.getFullDayHours());
        holder.fullDayPriceTextView.setText(String.format("%s kr", room.getFullDayPrice()));
        if(isAvailablePreNoon && isAvailableAfternoon) {
            holder.fullDayPriceTextView.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            int defaultColor = holder.fullDayPriceTextView.getTextColors().getDefaultColor();
            holder.fullDayPriceTextView.setTextColor(defaultColor);
        }
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
