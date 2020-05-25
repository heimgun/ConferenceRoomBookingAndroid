package com.example.conferencebookingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AvailableRoomListAdapter extends RecyclerView.Adapter<AvailableRoomListAdapter.MyViewHolder> {
    private static final String TAG = "AvailableRoomListAdapter";

    private List<ConferenceRoom> availableRooms;

    public AvailableRoomListAdapter(List<ConferenceRoom> availableRooms) {
        this.availableRooms = availableRooms;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_room, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView roomNameTextView = holder.roomView.findViewById(R.id.plantNameTextView);
        TextView priceTextView = holder.roomView.findViewById(R.id.priceTextView);


        roomNameTextView.setText(availableRooms.get(position).getName());
        String priceText = String.format("Pris per dag: %s kr", availableRooms.get(position).getFullDayPrice());
        priceTextView.setText(priceText);
    }

    @Override
    public int getItemCount() {
        return availableRooms.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View roomView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomView = itemView;
        }
    }
}
