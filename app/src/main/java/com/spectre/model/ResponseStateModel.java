package com.spectre.model;

import com.google.gson.annotations.SerializedName;

public class ResponseStateModel {
    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;
}
