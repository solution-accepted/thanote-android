package edu.uci.thanote.apis.demo;

import com.google.gson.annotations.SerializedName;

public class Post {
    private int userId;
    private int id;
    private String title;

    // if name in json is different than your object
    // use @SerializedName in gson to convert
    @SerializedName("body")
    private String text;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
