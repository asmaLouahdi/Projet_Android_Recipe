package com.example.projet.models;

import java.io.Serializable;

public class Myrecipe_view implements Serializable {
    private int id;
    private String title;
    private String time;
    private String category;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Myrecipe_view(int id, String title, String category, String time, String image) {
        this.id=id;
        this.title = title;
        this.time = time;
        this.category = category;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
