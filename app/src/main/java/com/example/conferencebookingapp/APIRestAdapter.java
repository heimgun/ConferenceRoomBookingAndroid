package com.example.conferencebookingapp;

import retrofit.RestAdapter;
import retrofit2.Retrofit;

public class APIRestAdapter {


    public static APIInterface getClient(){


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://dev-be.timetomeet.se/service/rest")
                .build();

        APIInterface api = restAdapter.create(APIInterface.class);
        return api;

    }

}
