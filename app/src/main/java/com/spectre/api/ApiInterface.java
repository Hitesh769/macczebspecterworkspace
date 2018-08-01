package com.spectre.api;

import com.spectre.LoginModel;
import com.spectre.other.Api;
import com.spectre.other.Urls;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by cisner7 on 14/09/2017.
 */

public interface ApiInterface {

    @Headers({
            "Content-Type:application/x-www-form-urlencoded",
            "Accept:application/json"
    })
//    @FormUrlEncoded
//    @POST(Api.LOGIN_API)
//
//    Call<LoginModel> loginUser(@FieldMap HashMap<String, String> registrationData);

//    @FormUrlEncoded
    @POST(Api.LOGIN_API)

    Call<LoginModel> loginUser(@Body String body);

    /*@POST(Api.LOGIN_API)
    @FormUrlEncoded
    Call<LoginModel> loginUser(@Field("user_mobile") String email,
                               @Field("user_password") String password,
                               @Field("user_device_type") String deviceType,
                               @Field("user_device_id") String deviceId,
                               @Field("user_device_token") String token,
                               @Field("language") String language);*/


   /* Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password,
                                  @Header("token") String token);*/

}
