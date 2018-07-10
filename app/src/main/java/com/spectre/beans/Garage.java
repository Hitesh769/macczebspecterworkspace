package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ebabu on 7/3/18.
 */

public class Garage implements Parcelable {
    private String add_id;
    private String user_id;
    private String create_at;
    private ArrayList<String> image;
    private ArrayList<Review> reviews;
    private ArrayList<Work> work;
    private String delete_status;
    private String full_name;
    private String email;
    private String mobile_no;
    private String address;
    private String user_image;
    private String is_interest;
    private String expertise;
    private String car_repaire;
    private String myreviews;
    private String myrating;
    private String revdate;
    private String garage_image;
    private String mobile_code;

    public Garage() {
    }


    protected Garage(Parcel in) {
        add_id = in.readString();
        user_id = in.readString();
        create_at = in.readString();
        image = in.createStringArrayList();
        reviews = in.createTypedArrayList(Review.CREATOR);
        work = in.createTypedArrayList(Work.CREATOR);
        delete_status = in.readString();
        full_name = in.readString();
        email = in.readString();
        mobile_no = in.readString();
        address = in.readString();
        user_image = in.readString();
        is_interest = in.readString();
        expertise = in.readString();
        car_repaire = in.readString();
        myreviews = in.readString();
        myrating = in.readString();
        revdate = in.readString();
        garage_image = in.readString();
        mobile_code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(add_id);
        dest.writeString(user_id);
        dest.writeString(create_at);
        dest.writeStringList(image);
        dest.writeTypedList(reviews);
        dest.writeTypedList(work);
        dest.writeString(delete_status);
        dest.writeString(full_name);
        dest.writeString(email);
        dest.writeString(mobile_no);
        dest.writeString(address);
        dest.writeString(user_image);
        dest.writeString(is_interest);
        dest.writeString(expertise);
        dest.writeString(car_repaire);
        dest.writeString(myreviews);
        dest.writeString(myrating);
        dest.writeString(revdate);
        dest.writeString(garage_image);
        dest.writeString(mobile_code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Garage> CREATOR = new Creator<Garage>() {
        @Override
        public Garage createFromParcel(Parcel in) {
            return new Garage(in);
        }

        @Override
        public Garage[] newArray(int size) {
            return new Garage[size];
        }
    };

    public String getAdd_id() {
        return add_id;
    }

    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Work> getWork() {
        return work;
    }

    public void setWork(ArrayList<Work> work) {
        this.work = work;
    }

    public String getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }

    public String getFull_name() {
        return full_name;
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
        return is_interest;
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

    public static Creator<Garage> getCREATOR() {
        return CREATOR;
    }

    public String getMyreviews() {
        return myreviews;
    }

    public void setMyreviews(String myreviews) {
        this.myreviews = myreviews;
    }

    public String getMyrating() {
        return myrating;
    }

    public void setMyrating(String myrating) {
        this.myrating = myrating;
    }

    public String getRevdate() {
        return revdate;
    }

    public void setRevdate(String revdate) {
        this.revdate = revdate;
    }

    public String getGarage_image() {
        return garage_image;
    }

    public void setGarage_image(String garage_image) {
        this.garage_image = garage_image;
    }

    public String getMobile_code() {
        return mobile_code==null?"":mobile_code;
    }

    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }
}
