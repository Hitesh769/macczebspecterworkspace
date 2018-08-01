package com.spectre;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
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


    }

}
