package com.example.conferencebookingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Karin
 */
public class ConferenceJsonParser {
    private static final String TAG = "ConferenceJsonParser";

    public List<ConferenceRoom> parseRoom(String jsonString) {

        List<ConferenceRoom> availableRooms = new ArrayList<>();

        try{
            JSONObject jsonResult = new JSONObject(jsonString);
            JSONArray jsonRooms = jsonResult.getJSONArray("result");


            int numberOfRooms = jsonRooms.length();

            Log.d(TAG, "parseRoom: numberOfRooms is: " + numberOfRooms);

            for (int i = 0; i < numberOfRooms; i++) {
                JSONObject jsonRoom = jsonRooms.getJSONObject(i);
                ConferenceRoom newRoom = new ConferenceRoom();
                newRoom.setRoomId(jsonRoom.getString("room_id"));
                Log.d(TAG, "parseRoom: roomId is: " + newRoom.getRoomId());

                String fullDayPrice = jsonRoom.getString("fullDayPrice");
                fullDayPrice = fullDayPrice.replace(".00", "");
                newRoom.setFullDayPrice(fullDayPrice);
                Log.d(TAG, "parseRoom: price is: " + newRoom.getFullDayPrice());

                String preNoonPrice = jsonRoom.getString("preNoonPrice");
                preNoonPrice = preNoonPrice.replace(".00", "");
                newRoom.setPreNoonPrice(preNoonPrice);

                String afternoonPrice = jsonRoom.getString("afterNoonPrice");
                afternoonPrice = afternoonPrice.replace(".00", "");
                newRoom.setAfternoonPrice(afternoonPrice);

                newRoom.setPreNoonBookingCode(jsonRoom.getString("id31"));
                newRoom.setAfternoonBookingCode(jsonRoom.getString("id32"));
                newRoom.setPlantId(jsonRoom.getString("plant_id"));

                availableRooms.add(newRoom);
            }

            return availableRooms;

        } catch(JSONException e) {
            Log.e(TAG, "parseRoom: JSONException " + e.getMessage());
            return null;
        }
    }

    public List<Plant> parsePlants(String jsonString) {

        List<Plant> plants = new ArrayList<>();

        try{
            JSONObject jsonResult = new JSONObject(jsonString);
            JSONArray jsonPlants = jsonResult.getJSONArray("plantsOverview");


            int numberOfPlants = jsonPlants.length();

            Log.d(TAG, "parsePlants: numberOfPlants is: " + numberOfPlants);

            for (int i = 0; i < numberOfPlants; i++) {

                JSONObject jsonPlant = jsonPlants.getJSONObject(i);

                Plant newPlant = new Plant();
                newPlant.setPlantId (jsonPlant.getString("plantId"));
                newPlant.setName(jsonPlant.getString("plantName"));
                newPlant.setFacts(jsonPlant.getString("plantFacts"));

                String priceFrom = jsonPlant.getString("priceFrom");
                priceFrom = priceFrom.replace(".0", "");
                newPlant.setPriceFrom(priceFrom);

                newPlant.setNumberOfAvailableRooms(jsonPlant.getString("roomsAvailable"));

                JSONObject jsonAddress = jsonPlant.getJSONObject("visitingAddress");

                newPlant.setAddress(jsonAddress.getString("street"));
                newPlant.setCity(jsonAddress.getString("city"));

                JSONArray jsonImages = jsonPlant.getJSONArray("plantImages");
                int numberOfImages = jsonImages.length();

                for (int j = 0; j < numberOfImages; j++) {
                    JSONObject jsonImage = jsonImages.getJSONObject(j);
                    newPlant.addImageUrl(jsonImage.getString("image"));
                }

                plants.add(newPlant);

            }

            return plants;

        } catch(JSONException e) {
            Log.e(TAG, "parsePlants: JSONException " + e.getMessage());
            return null;
        }
    }

    public String parseExtraRoomInfo(String jsonString, List<ConferenceRoom> rooms, int requestedNumberOfPeople) {

        String nextPage = "";
        try {
            JSONObject jsonResult = new JSONObject(jsonString);

            nextPage = jsonResult.getString("next");

            Log.d(TAG, "parseExtraRoomInfo: nextPage is: " + nextPage);

            JSONArray jsonRooms = jsonResult.getJSONArray("results");
            int numberOfRooms = jsonRooms.length();

            for (int i = 0; i < numberOfRooms; i++) {

                JSONObject jsonRoom =  jsonRooms.getJSONObject(i);
                String roomId = jsonRoom.getString("id");
                List<Integer> roomsToRemove = new ArrayList<>();
                for (int n = 0; n < rooms.size(); n++) {
                    ConferenceRoom room = rooms.get(n);
                    if (room.getRoomId().equals(roomId)) {
                        room.setDescription(jsonRoom.getString("description"));
                        room.setName(jsonRoom.getString("name"));

                        String preNoonHours = jsonRoom.getString("preNoonAvailabilityHourStart").substring(0, 5) +
                                " - " + jsonRoom.getString("preNoonAvailabilityHourEnd").substring(0, 5);
                        room.setPreNoonHours(preNoonHours);

                        String afternoonHours = jsonRoom.getString("afterNoonAvailabilityHourStart").substring(0, 5) +
                                " - " + jsonRoom.getString("afterNoonAvailabilityHourEnd").substring(0, 5);
                        room.setAfternoonHours(afternoonHours);

                        String fullDayHours = jsonRoom.getString("preNoonAvailabilityHourStart").substring(0, 5) +
                                " - " + jsonRoom.getString("afterNoonAvailabilityHourEnd").substring(0, 5);
                        room.setFullDayHours(fullDayHours);

                        JSONArray jsonTechnologies = jsonRoom.getJSONArray("technologies");
                        int numberOfTechnologies = jsonTechnologies.length();

                        for (int j = 0; j < numberOfTechnologies; j++) {
                            JSONObject jsonTechnology = jsonTechnologies.getJSONObject(j);
                            TechnologyItem technologyItem = new TechnologyItem();
                            technologyItem.setId(jsonTechnology.getString("id"));
                            technologyItem.setDescription(jsonTechnology.getString("name"));

                            room.addTechnology(technologyItem);
                        }

                        JSONArray jsonMedia = jsonRoom.getJSONArray("blobs");
                        for (int j = 0; j < jsonMedia.length(); j++) {
                            JSONObject jsonMediaObject = jsonMedia.getJSONObject(j);
                            String imageUrl = jsonMediaObject.getString("url");

                            room.addImageUrl(imageUrl);
                        }

                        JSONArray jsonDefaultSeatings = jsonRoom.getJSONArray("defaultSeating");
                        JSONArray jsonSeatingDetails = jsonRoom.getJSONArray("seats");

                        int numberOfSeatings = jsonDefaultSeatings.length();
                        Log.d(TAG, "parseExtraRoomInfo: numberOfSeatings is: " + numberOfSeatings);

                        int maxNumber = 0;

                        for (int j = 0; j < numberOfSeatings; j++) {
                            JSONObject defaultSeating = jsonDefaultSeatings.getJSONObject(j);

                            Seating seating = new Seating();


                            String seatId = defaultSeating.getString("standardSeating");
                            seating.setId(seatId);

                            for (int k = 0; k < jsonSeatingDetails.length(); k++) {
                                JSONObject seat = jsonSeatingDetails.getJSONObject(k);

                                String newSeatId = seat.getString("id");
                                if(newSeatId.equals(seatId)) {
                                    seating.setDescription(seat.getString("name"));
                                }
                            }

                            String maxNumberOfPeople = defaultSeating.getString("numberOfSeat");
                            seating.setNumberOfPeople(maxNumberOfPeople);

                            int numberOfPeople = Integer.parseInt(maxNumberOfPeople);
                            maxNumber = Math.max(numberOfPeople, maxNumber);

                            if(numberOfPeople >= requestedNumberOfPeople) {
                                room.addSeating(seating);
                            }
                        }

                        room.setMaxNumberOfPeople(maxNumber);

                        if(maxNumber < requestedNumberOfPeople) {
                            roomsToRemove.add(n);
                        }
                    }
                }
                if (!roomsToRemove.isEmpty()) {
                    for (int m = 0; m < roomsToRemove.size(); m++) {
                        rooms.remove(roomsToRemove.get(m).intValue());
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseExtraRoomInfo: JSONException: " + e.getMessage());
        }
        return nextPage;

    }

    public void parsePlantFood(String jsonString, List<Plant> plants, Map<String, String> foodItems) {
        Log.d(TAG, "parsePlantFood: parsePlantFood called");
        try {
            JSONArray jsonFoods = new JSONArray(jsonString);

            for (int i = 0; i < jsonFoods.length(); i++) {
                JSONObject jsonFood = jsonFoods.getJSONObject(i);
                String jsonPlantId = jsonFood.getString("plant");
                for (Plant plant : plants) {
                    if (plant.getPlantId().equals(jsonPlantId)) {
                        Log.d(TAG, "parsePlantFood: plant is: " + plant.getName());
                        FoodItem foodItem = new FoodItem();

                        String foodItemId = jsonFood.getString("foodBeverage");
                        foodItem.setId(foodItemId);
                        foodItem.setDescription(foodItems.get(foodItemId));
                        foodItem.setPrice(jsonFood.getString("price"));
                        plant.addFoodAndBeverage(foodItem);
                        Log.d(TAG, "parsePlantFood: " + plant.getName() + " added " + foodItem.getDescription());
                    }
                }
            }

        } catch(JSONException e) {
            Log.e(TAG, "parsePlantFood: JSONException: " + e.getMessage());
        }
    }

    public Map<String, String> parseFoodInfo(String jsonString) {
        Map<String, String> foodInfo = new HashMap<>();
        try {
            JSONArray jsonFoodItems = new JSONArray(jsonString);
            for (int i = 0; i < jsonFoodItems.length(); i++) {
                JSONObject jsonFoodItem = jsonFoodItems.getJSONObject(i);
                String foodId = jsonFoodItem.getString("id");
                String foodDescription = jsonFoodItem.getString("name");

                foodInfo.put(foodId, foodDescription);
            }

        } catch (JSONException e) {
            Log.e(TAG, "parseFoodInfo: JSONException: " + e.getMessage());
        }
        return foodInfo;
    }
}
