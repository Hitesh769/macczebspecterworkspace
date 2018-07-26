package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ebabu on 24/2/18.
 */


public class AdPost implements Parcelable {
    private String model;
    private String mileage;
    private String add_id;
    private String create_at;
    private ArrayList<String> image;
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
    private String description;
    private String problem;
    private String car_modified;
    private String full_name;
    private String email;
    private String mobile_no;
    private String address;
    private String user_image;
    private String garage_image;
    private String is_interest;
    private String expertise;
    private String car_repaire;
    private String mobile_code;
    private String create_date;
    private ArrayList<String> after_image;
    private String from_date;
    private String to_date;
    private String location;

    public AdPost() {
    }

    protected AdPost(Parcel in) {
        model = in.readString();
        mileage = in.readString();
        add_id = in.readString();
        create_at = in.readString();
        image = in.createStringArrayList();
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
        year = in.readString();
        year_from = in.readString();
        year_to = in.readString();
        description = in.readString();
        problem = in.readString();
        car_modified = in.readString();
        full_name = in.readString();
        email = in.readString();
        mobile_no = in.readString();
        address = in.readString();
        user_image = in.readString();
        is_interest = in.readString();
        expertise = in.readString();
        car_repaire = in.readString();
        garage_image = in.readString();
        mobile_code = in.readString();
        create_date = in.readString();
        after_image = in.createStringArrayList();
        from_date = in.readString();
        to_date = in.readString();
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(model);
        dest.writeString(mileage);
        dest.writeString(add_id);
        dest.writeString(create_at);
        dest.writeStringList(image);
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
        dest.writeString(year);
        dest.writeString(year_from);
        dest.writeString(year_to);
        dest.writeString(description);
        dest.writeString(problem);
        dest.writeString(car_modified);
        dest.writeString(full_name);
        dest.writeString(email);
        dest.writeString(mobile_no);
        dest.writeString(address);
        dest.writeString(user_image);
        dest.writeString(is_interest);
        dest.writeString(expertise);
        dest.writeString(car_repaire);
        dest.writeString(garage_image);
        dest.writeString(mobile_code);
        dest.writeString(create_date);
        dest.writeStringList(after_image);
        dest.writeString(from_date);
        dest.writeString(to_date);
        dest.writeString(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdPost> CREATOR = new Creator<AdPost>() {
        @Override
        public AdPost createFromParcel(Parcel in) {
            return new AdPost(in);
        }

        @Override
        public AdPost[] newArray(int size) {
            return new AdPost[size];
        }
    };

    public String getModel() {
        return model != null ? model : "";
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getLocation() {
        return location != null ? location : "";
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getMileage() {
        return mileage != null ? mileage : "";
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

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public ArrayList<String> getImage() {
        return image == null ? new ArrayList<String>() : image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getCar_name_id() {
        return car_name_id;
    }

    public void setCar_name_id(String car_name_id) {
        this.car_name_id = car_name_id;
    }

    public String getCar_name() {
        return car_name != null ? car_name : "";
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
        return car_condition == null ? "" : car_condition;
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
        return price != null ? price : "";
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

    public String getYear() {
        return year == null ? "" : year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public static Creator<AdPost> getCREATOR() {
        return CREATOR;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getFull_name() {
        return full_name == null ? "" : full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getIs_interest() {
        return is_interest == null ? "" : is_interest;
    }

    public void setIs_interest(String is_interest) {
        this.is_interest = is_interest;
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

    public String getGarage_image() {
        return garage_image;
    }

    public void setGarage_image(String garage_image) {
        this.garage_image = garage_image;
    }

    public String getMobile_code() {
        return mobile_code == null ? "" : mobile_code;
    }

    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }

    public String getCreate_date() {
        return setEmpty(create_date);
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public ArrayList<String> getAfter_image() {
        return after_image == null ? new ArrayList<String>() : after_image;
    }

    public void setAfter_image(ArrayList<String> after_image) {
        this.after_image = after_image;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String setEmpty(String key) {
        if (key == null) {
            return "";
        }

        return key;
    }
}




