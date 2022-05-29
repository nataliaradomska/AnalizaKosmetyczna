package com.example.analizakosmetyczna;

import android.widget.ImageView;

public class Product {

    private String name, desc, status;
    private int image;

    public Product(String name, String desc, int image, String status) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.status = status;
    }

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
