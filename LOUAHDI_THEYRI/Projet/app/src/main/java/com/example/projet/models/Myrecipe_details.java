package com.example.projet.models;
import java.io.Serializable;
import java.util.ArrayList;

public class Myrecipe_details implements Serializable {
    private int id;
    private String title;
    private String time;
    private String category;
    private String discription;
    private ArrayList<String>ingredients;

    public Myrecipe_details(int id,String title,String category, String time, String description, ArrayList<String>ingredients) {
        this.ingredients=ingredients;
        this.id=id;
        this.title = title;
        this.time = time;
        this.category = category;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
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
