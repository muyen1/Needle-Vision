package com.example.needlevision;

public class Post {

    private String userID;
    private String description;
    private String status;
    private String date;
    private double latitude;
    private double longitude;
    private String imageURL;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String userID, String description, String status, String date, double latitude, double longitude, String imageURL){
        this.userID = userID;
        this.description = description;
        this.status = status;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageURL = imageURL;
    }

    public String getUserID() {
        return userID;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
