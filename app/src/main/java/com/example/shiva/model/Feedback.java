package com.example.shiva.model;

import com.google.firebase.Timestamp;

public class Feedback {
    private String userName;
    private String message;
    private String type;
    private Timestamp timestamp;
    private float rating;
    public Feedback() {
        // Firestore साठी आवश्यक default constructor
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

        public float getRating() { return rating; }
        public void setRating(float rating) { this.rating = rating; }

    }
