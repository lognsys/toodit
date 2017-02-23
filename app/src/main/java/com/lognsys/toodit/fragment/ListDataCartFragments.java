package com.lognsys.toodit.fragment;

/**
 * Created by admin on 23-02-2017.
 */

public class ListDataCartFragments {
    private String cartItemSelected;
    private String cartItemComment;
    private String cartItemquanatity;
    private String cartItemPrice;
    private int cartItemImage;

    public int getCartItemImage() {
        return cartItemImage;
    }

    public void setCartItemImage(int cartItemImage) {
        this.cartItemImage = cartItemImage;
    }

    public String getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(String cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public String getCartItemquanatity() {
        return cartItemquanatity;
    }

    public void setCartItemquanatity(String cartItemquanatity) {
        this.cartItemquanatity = cartItemquanatity;
    }

    public String getCartItemComment() {
        return cartItemComment;
    }

    public void setCartItemComment(String cartItemComment) {
        this.cartItemComment = cartItemComment;
    }

    public String getCartItemSelected() {
        return cartItemSelected;
    }

    public void setCartItemSelected(String cartItemSelected) {
        this.cartItemSelected = cartItemSelected;
    }
}
