package com.lognsys.toodit.fragment;

/**
 * Created by admin on 10-03-2017.
 */

public class ListDataMyOrders {

    private String orderId;
    private String orderSummery;
    private String orderItemPrice;
    private String orderItemQuantity;
    private int orderItemImage;

    public String getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(String orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSummery() {
        return orderSummery;
    }

    public void setOrderSummery(String orderSummery) {
        this.orderSummery = orderSummery;
    }

    public String getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(String orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public int getOrderItemImage() {
        return orderItemImage;
    }

    public void setOrderItemImage(int orderItemImage) {
        this.orderItemImage = orderItemImage;
    }
}
