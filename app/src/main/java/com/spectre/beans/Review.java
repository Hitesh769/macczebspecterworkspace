package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ebabu on 5/3/18.
 */

public class Review implements Parcelable{
    String rating;
    String reviews;
    String date;
    String user_name;
    private String create_at;
    private String user_image;
    private String user_id;
    private boolean isOpen;


    protected Review(Parcel in) {
        rating = in.readString();
        reviews = in.readString();
        date = in.readString();
        user_name = in.readString();
        create_at = in.readString();
        user_image = in.readString();
        user_id = in.readString();
        isOpen = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rating);
        dest.writeString(reviews);
        dest.writeString(date);
        dest.writeString(user_name);
        dest.writeString(create_at);
        dest.writeString(user_image);
        dest.writeString(user_id);
        dest.writeByte((byte) (isOpen ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getRating() {
        return rating==null?"0":rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews==null?"":reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getDate() {
        return date==null?"":date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_name() {
        return  user_name==null?"":user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public static Creator<Review> getCREATOR() {
        return CREATOR;
    }
}
