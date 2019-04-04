package com.example.spotick;

public class cardItem {
    String profileName;
    int profilePhoto;
    int nbFollowers;

    public cardItem(){

    }

    public cardItem(String profileName, int profilePhoto, int nbFollowers) {
        this.profileName = profileName;
        this.profilePhoto = profilePhoto;
        this.nbFollowers = nbFollowers;
    }

    public String getProfileName() {
        return profileName;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public int getNbFollowers() {
        return nbFollowers;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setNbFollowers(int nbFollowers) {
        this.nbFollowers = nbFollowers;
    }
}
