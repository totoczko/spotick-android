package com.example.spotick;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martyna on 2018-01-11.
 */

public class Post implements Parcelable {

    public String id;
    public String shortText;
    public String geo;
    public Long data;
    public String imageid;
    public String img;
    public Map<String, Object> user = new HashMap<>();
    public String userName;
    public String userColor;
    public Map<String, Object> likes = new HashMap<>();
    public Long likesCount;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String id, String shortText, String geo, Long data, String imageid, String img, Long likesCount, String userName, String userColor) {
        this.id = id;
        this.shortText = shortText;
        this.geo = geo;
        this.data = data;
        this.imageid = imageid;
        this.img = img;
        this.likesCount = likesCount;
        this.userName = userName;
        this.userColor= userColor;
    }

    protected Post(Parcel in) {
        id = in.readString();
        shortText = in.readString();
        geo = in.readString();
        data = in.readLong();
        imageid = in.readString();
        img = in.readString();
        likesCount = in.readLong();
        userName = in.readString();
        userColor = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getId() {
        return id;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public String getUserName() {
        return userName;
    }
    public String getUserColor() {
        return userColor;
    }

    public Character getUserFirstLetter(){
        Character lower = userName.charAt(0);
        Character upper = Character.toUpperCase(lower);
        return upper;
    }

    public Map<String, Object> getLikes() {
        return likes;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public String getShortText() {
        return shortText;
    }

    public String getGeo() {
        return geo;
    }

    public String getData() {
        String pattern = "dd MMMM HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);
        Date postDate = new Date(data);
        String dateString = df.format(postDate);
        return dateString;
    }

    public String getImageid() {
        return imageid;
    }

    public String getImg() {
        return img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(shortText);
        dest.writeString(geo);
        dest.writeLong(data);
        dest.writeString(imageid);
        dest.writeString(img);
    }

}