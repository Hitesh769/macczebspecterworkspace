package com.spectre.model;

import com.google.gson.annotations.SerializedName;

public class SignUpModel extends ResponseStateModel {
    @SerializedName("data")
    public String data;
}
