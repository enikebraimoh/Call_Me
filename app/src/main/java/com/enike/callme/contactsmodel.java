package com.enike.callme;

public class contactsmodel {
    String Name;
    String bio;
    String Picture;

    public contactsmodel() {
    }

    public contactsmodel(String name, String bio, String picture) {
        Name = name;
        this.bio = bio;
        Picture = picture;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }
}