package com.spectre.Retrofit;

import com.spectre.model.Chat.UserList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIInterface {

    /*@GET("/api/unknown")
    Call<MultipleResource> doGetListResources();*/
    @Headers("Content-Type: application/json")
    @POST("chatlist")
    Call<UserList> createUser(@Body String user_id);
/*@FormUrlEncoded
    @POST("spectre_api.php/chatlist")
    Call<UserList> createUser(@Field("user_id") String user_id);*/
}

