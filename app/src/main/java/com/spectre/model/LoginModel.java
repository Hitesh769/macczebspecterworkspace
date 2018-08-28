package com.spectre.model;

import com.google.gson.annotations.SerializedName;

public class LoginModel extends ResponseStateModel {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("user_id")
        public String userId;
        @SerializedName("user_name")
        public String userName;
        @SerializedName("mobile_code")
        public String mobileCode;
        @SerializedName("user_mobile")
        public String userMobile;
        @SerializedName("user_email")
        public String userEmail;
        @SerializedName("user_address")
        public String userAddress;
        @SerializedName("user_type")
        public String userType;
        @SerializedName("service_type")
        public String serviceType;
        @SerializedName("user_image")
        public String userImage;
        @SerializedName("garage_image")
        public String garageImage;
        @SerializedName("user_token")
        public String userToken;
        @SerializedName("expertise")
        public String expertise;
        @SerializedName("car_repaire")
        public String carRepaire;
    }
}
