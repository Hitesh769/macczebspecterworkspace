package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ebabu on 5/3/18.
 */

public class Work implements Parcelable {
    private String model;
    private String mileage;
    private String add_id;
    private String car_name_id;
    private String car_name;
    private String version_id;
    private String car_condition;
    private String car_type;
    private String version;
    private String price;
    private String color;
    private String model_id;
    private String delete_status;
    private String user_id;
    private String year;
    private String year_from;
    private String year_to;
    private String problem;
    private String car_modified;
    private String expertise;
    private String car_repaire;
    private ArrayList<String> image;
    private String create_at;
    private String mobile_code;

    protected Work(Parcel in) {
        model = in.readString();
        mileage = in.readString();
        add_id = in.readString();
        car_name_id = in.readString();
        car_name = in.readString();
        version_id = in.readString();
        car_condition = in.readString();
        car_type = in.readString();
        version = in.readString();
        price = in.readString();
        color = in.readString();
        model_id = in.readString();
        delete_status = in.readString();
        user_id = in.readString();
        year_from = in.readString();
        year_to = in.readString();
        problem = in.readString();
        car_modified = in.readString();
        expertise = in.readString();
        car_repaire = in.readString();
        image = in.createStringArrayList();
        create_at = in.readString();
        year = in.readString();
        mobile_code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(model);
        dest.writeString(mileage);
        dest.writeString(add_id);
        dest.writeString(car_name_id);
        dest.writeString(car_name);
        dest.writeString(version_id);
        dest.writeString(car_condition);
        dest.writeString(car_type);
        dest.writeString(version);
        dest.writeString(price);
        dest.writeString(color);
        dest.writeString(model_id);
        dest.writeString(delete_status);
        dest.writeString(user_id);
        dest.writeString(year_from);
        dest.writeString(year_to);
        dest.writeString(problem);
        dest.writeString(car_modified);
        dest.writeString(expertise);
        dest.writeString(car_repaire);
        dest.writeStringList(image);
        dest.writeString(create_at);
        dest.writeString(year);
        dest.writeString(mobile_code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Work> CREATOR = new Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel in) {
            return new Work(in);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };



    public void setModel(String model) {
        this.model = model;
    }



    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getAdd_id() {
        return add_id;
    }

    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }

    public String getCar_name_id() {
        return car_name_id;
    }

    public void setCar_name_id(String car_name_id) {
        this.car_name_id = car_name_id;
    }


    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public String getCar_condition() {
        return car_condition;
    }

    public void setCar_condition(String car_condition) {
        this.car_condition = car_condition;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPrice() {
        return price!=null?price:"";
    }
    public String getCar_name() {
        return car_name!=null?car_name:"";
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getCar_modified() {
        return car_modified;
    }

    public void setCar_modified(String car_modified) {
        this.car_modified = car_modified;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getCar_repaire() {
        return car_repaire;
    }

    public void setCar_repaire(String car_repaire) {
        this.car_repaire = car_repaire;
    }

    public String getModel() {
        return model!=null?model:"";
    }


    public String getMileage() {
        return mileage!=null?mileage:"";
    }

    public static Creator<Work> getCREATOR() {
        return CREATOR;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMobile_code() {
        return mobile_code==null?"":mobile_code;
    }

    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }
}
