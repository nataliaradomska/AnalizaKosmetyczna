package com.example.analizakosmetyczna;

import android.widget.ImageView;

import java.io.Serializable;

public class Product implements Serializable {

    private String name, desc, status, type;
    private float rate;
    private int image, id;

    public Product(String name, String desc, String type, int image, String status, float rate, int id) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.status = status;
        this.type = type;
        this.rate = rate;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public float getRate() {return rate; }

    public void setRate(float rate) {this.rate = rate; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
