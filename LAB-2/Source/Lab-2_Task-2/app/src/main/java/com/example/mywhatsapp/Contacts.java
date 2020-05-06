package com.example.mywhatsapp;

public class Contacts {

    public String name, phnnum, image;

    public Contacts()
    {

    }


    public Contacts(String name, String phnnum, String image) {
        this.name = name;
        this.phnnum = phnnum;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPhnnum() {
        return phnnum;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhnnum(String phnnum) {
        this.phnnum = phnnum;
    }

    public void setName(String name) {
        this.name = name;
    }

}
