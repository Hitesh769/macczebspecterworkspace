package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ebabu on 24/2/18.
 */


public class FilterResponse implements Parcelable {
    private String car_name_id = "0";
    private String version_id = "0";
    private String car_type = "";
    private String colour = "";
    private String model_id = "0";
    private String price_min_range = "0";
    private String price_max_range = "0";
    private String show_price_max_range = "";
    private String year_from = "";
    private String year_to = "";
    private String type = "";
    private String create_at = "0";
    private String list_type = "0";
    private String carName = "";
    private String carModel = "";
    private String carVersion = "";
    private String service_type = "0";
    private String location = "location";
    private String latitude = "lot";
    private String longitude = "lon";

    /*{
        "car_name_id": "0",
            "model_id": "0",
            "version_id": "0",
            "car_type": "",
            "colour": "",
            "price_min_range": "0",
            "price_max_range": "0",
            "year_from": "",
            "year_to": "",
            "type":"1",
            "create_at":"0",
            "list_type":"0"
    }*/

    public FilterResponse() {
    }

    protected FilterResponse(Parcel in) {
        car_name_id = in.readString();
        version_id = in.readString();
        car_type = in.readString();
        colour = in.readString();
        model_id = in.readString();
        price_min_range = in.readString();
        price_max_range = in.readString();
        year_from = in.readString();
        year_to = in.readString();
        type = in.readString();
        create_at = in.readString();
        list_type = in.readString();
        show_price_max_range = in.readString();
        carName = in.readString();
        carModel = in.readString();
        carVersion = in.readString();
        service_type = in.readString();
        location = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<FilterResponse> CREATOR = new Creator<FilterResponse>() {
        @Override
        public FilterResponse createFromParcel(Parcel in) {
            return new FilterResponse(in);
        }

        @Override
        public FilterResponse[] newArray(int size) {
            return new FilterResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(car_name_id);
        dest.writeString(version_id);
        dest.writeString(car_type);
        dest.writeString(colour);
        dest.writeString(model_id);
        dest.writeString(price_min_range);
        dest.writeString(price_max_range);
        dest.writeString(year_from);
        dest.writeString(year_to);
        dest.writeString(type);
        dest.writeString(create_at);
        dest.writeString(list_type);
        dest.writeString(show_price_max_range);
        dest.writeString(carName);
        dest.writeString(carModel);
        dest.writeString(carVersion);
        dest.writeString(service_type);
        dest.writeString(location);
        dest.writeString(longitude);
        dest.writeString(latitude);

    }

    public String getCar_name_id() {
        return car_name_id;
    }

    public void setCar_name_id(String car_name_id) {
        this.car_name_id = car_name_id;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getPrice_min_range() {
        return price_min_range;
    }

    public void setPrice_min_range(String price_min_range) {
        this.price_min_range = price_min_range;
    }

    public String getPrice_max_range() {
        return price_max_range;
    }

    public void setPrice_max_range(String price_max_range) {
        this.price_max_range = price_max_range;
    }

    public String getYear_from() {
        return year_from;
    }

    public void setYear_from(String year_from) {
        this.year_from = year_from;
    }

    public String getYear_to() {
        return year_to;
    }

    public void setYear_to(String year_to) {
        this.year_to = year_to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getList_type() {
        return list_type;
    }

    public void setList_type(String list_type) {
        this.list_type = list_type;
    }

    public static Creator<FilterResponse> getCREATOR() {
        return CREATOR;
    }

    public String getShow_price_max_range() {
        return show_price_max_range;
    }

    public void setShow_price_max_range(String show_price_max_range) {
        this.show_price_max_range = show_price_max_range;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarVersion() {
        return carVersion;
    }

    public void setCarVersion(String carVersion) {
        this.carVersion = carVersion;
    }

    public String getService_type() {
        return service_type == null ? "0" : service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}




