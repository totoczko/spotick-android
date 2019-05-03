package com.example.spotick;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public String userId;
    public Map<String, Object> likes = new HashMap<>();
    public Long likesCount;
    public ArrayList<String> likesUsers;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String id, String shortText, String geo, Long data, String imageid, String img, Long likesCount, ArrayList<String> likesUsers, String userName, String userColor, String userId) {
        this.id = id;
        this.shortText = shortText;
        this.geo = geo;
        this.data = data;
        this.imageid = imageid;
        this.img = img;
        this.likesCount = likesCount;
        this.likesUsers = likesUsers;
        this.userName = userName;
        this.userColor= userColor;
        this.userId= userId;
    }

    protected Post(Parcel in) {
        id = in.readString();
        shortText = in.readString();
        geo = in.readString();
        data = in.readLong();
        imageid = in.readString();
        img = in.readString();
        likesCount = in.readLong();
        likesUsers = (ArrayList<String>) in.readArrayList(null);
        userName = in.readString();
        userColor = in.readString();
        userId = in.readString();
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
    public String getUserId() {
        return userId;
    }

    public String getUserFirstLetter(){
        Character lower = userName.charAt(0);
        String upper = String.valueOf(Character.toUpperCase(lower));
        return upper;
    }

    public Map<String, Object> getLikes() {
        return likes;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public ArrayList<String> getLikesUsers(){
        return likesUsers;
    }

    public String getShortText() {
        return shortText;
    }

    public String getGeo() {
        return geo;
    }

    public long getData() {
        return data;
    }

    public String getDataString() {
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
        dest.writeLong(likesCount);
        dest.writeList(likesUsers);
        dest.writeString(img);
        dest.writeString(userName);
        dest.writeString(userColor);
        dest.writeString(userId);
    }

}