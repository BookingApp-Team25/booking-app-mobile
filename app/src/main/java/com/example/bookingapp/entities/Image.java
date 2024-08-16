package com.example.bookingapp.entities;

public class Image {
    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    private String base64;
    public Image(String base64){
        this.base64 = base64;
    }

}
