package com.lognsys.toodit.fragment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 04-03-2017.
 */

public class ListFoodItem implements Parcelable {


    private String food_id;
    private String name;
    private String food_type;
    private String category_name;
    private String description;
    private String price;
    private String outlet_id;
    private String image;


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(food_id);
        out.writeString(name);
        out.writeString(food_type);
        out.writeString(category_name);
        out.writeString(description);
        out.writeString(price);
        out.writeString(outlet_id);
        out.writeString(image);
    }

    public static final Parcelable.Creator<ListFoodItem> CREATOR
            = new Parcelable.Creator<ListFoodItem>() {
        public ListFoodItem createFromParcel(Parcel in) {
            return new ListFoodItem(in);
        }

        public ListFoodItem[] newArray(int size) {
            return new ListFoodItem[size];
        }
    };

    private ListFoodItem(Parcel in) {
        food_id = in.readString();
        name = in.readString();
        food_type = in.readString();
        category_name = in.readString();
        description = in.readString();
        price = in.readString();
        outlet_id = in.readString();
        image = in.readString();
    }

    public ListFoodItem(String food_id, String name, String food_type, String category_name, String description, String price, String outlet_id, String image) {
        this.food_id = food_id;
        this.name = name;
        this.food_type = food_type;
        this.category_name = category_name;
        this.description = description;
        this.price = price;
        this.outlet_id = outlet_id;
        this.image = image;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
