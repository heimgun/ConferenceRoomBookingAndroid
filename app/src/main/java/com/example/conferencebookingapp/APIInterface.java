package com.example.conferencebookingapp;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("/user/add/")
    public void registration(@Field("first_name") String firstName,
                             @Field("last_name") String lastName,
                             @Field("username") String username,
                             @Field("password") String password,
                             @Field("email") String email,
                             @Field("phone_number") int phone,
                             @Field("organization_name") String organization,
                             @Field("organization_nr") int orgNumber,
                             @Field("street") String street,
                             @Field("city_name") String city,
                             @Field("zipCode") int zipCode,
                             Callback<SignUpResponse> callback);


    //GET-request underneath




}
