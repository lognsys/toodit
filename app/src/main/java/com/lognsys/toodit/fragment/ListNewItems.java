package com.lognsys.toodit.fragment;

/**
 * Created by admin on 06-03-2017.
 */

public class ListNewItems {

    private String itemName;
    private String itemDescription;
    private int image;

    /*public ListNewItems(String itemName, String itemDescription, int image) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.image = image;
    }*/

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
