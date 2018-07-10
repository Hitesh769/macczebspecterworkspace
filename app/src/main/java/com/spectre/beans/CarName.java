package com.spectre.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 19/11/2016.
 */
public class CarName implements Parcelable {
   String id,car_name;

    protected CarName(Parcel in) {
        id = in.readString();
        car_name = in.readString();
    }


    public CarName(String id, String car_name) {
        this.id = id;
        this.car_name = car_name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(car_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CarName> CREATOR = new Creator<CarName>() {
        @Override
        public CarName createFromParcel(Parcel in) {
            return new CarName(in);
        }

        @Override
        public CarName[] newArray(int size) {
            return new CarName[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String toString() {
        return car_name;
    }

    @Override
    public boolean equals(Object obj) {
        CarName carName = (CarName) obj;
        if (carName.getId().equals(this.getId())) {
            return true;
        }
        return false;
    }
}
