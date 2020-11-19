package com.example.needlevision;

public class Post {
    private boolean isDisposed = false;
    private String date;
    private String time;
    private String location;
    private String description = null;

    public Post(boolean isDisposed, String date, String time, String location,  String description) {
        this.isDisposed = isDisposed;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    public void setDisposed(boolean disposed) {
        isDisposed = disposed;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
