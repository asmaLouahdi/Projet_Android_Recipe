package com.example.projet.models;
public class Recette {
    private String title;
    private String image_url;
    private String source_url;
    private String id;
    private String publisher;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Recette(String title, String image_url, String source_url, String id, String publisher) {
        this.publisher = publisher;
        this.id = id;
        this.image_url = image_url;
        this.title = title;
        this.source_url = source_url;
    }
}
