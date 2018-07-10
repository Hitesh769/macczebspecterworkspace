package com.spectre.beans;

import java.util.ArrayList;

/**
 * Created by ebabu on 9/3/18.
 */

public class UpdateReview {
    String myreviews,myrating;
    private ArrayList<Review> reviews;

    public UpdateReview() {
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

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
