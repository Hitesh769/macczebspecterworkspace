package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 19/11/2016.
 */
public class ModelName implements Parcelable {
    String id, model_name;

    protected ModelName(Parcel in) {
        id = in.readString();
        model_name = in.readString();
    }

    public ModelName(String id, String model_name) {
        this.id = id;
        this.model_name = model_name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(model_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelName> CREATOR = new Creator<ModelName>() {
        @Override
        public ModelName createFromParcel(Parcel in) {
            return new ModelName(in);
        }

        @Override
        public ModelName[] newArray(int size) {
            return new ModelName[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }


    public String toString() {
        return model_name;
    }

    @Override
    public boolean equals(Object obj) {
        ModelName carName = (ModelName) obj;
        if (carName.getId().equals(this.getId())) {
            return true;
        }
        return false;
    }
}
