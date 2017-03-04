package com.lognsys.toodit.fragment;

/**
 * Created by admin on 04-03-2017.
 */

public class ListFoodItemCategory {
    private String categgory_id;
    private String name;
    private String description;
    private String image;

    public ListFoodItemCategory(String categgory_id, String name, String description, String image) {
        this.categgory_id = categgory_id;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getCateggory_id() {
        return categgory_id;
    }

    public void setCateggory_id(String categgory_id) {
        this.categgory_id = categgory_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
