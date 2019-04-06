package com.example.spotick;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Martyna on 2018-01-11.
 */

public class User implements Parcelable {

    public String id;
    public String email;
    public String username;
    public String color;

    public User() {
        // Default constructor required for calls to ColorSnapshot.getValue(User.class)
    }

    public User(String id, String email, String username, String color) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.color = color;
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        username = in.readString();
        color = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public Character getUserFirstLetter(){
        Character lower = username.charAt(0);
        Character upper = Character.toUpperCase(lower);
        return upper;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(color);
    }

}