package com.lognsys.toodit.fragment;

/**
 * Created by admin on 16-02-2017.
 */

public class ListDataNotification {
    String Notification;
    String setNotificationdate;
    int image;
    boolean isReaded;
    public boolean isReaded() {
        return isReaded;
    }

    public void setIsReaded(boolean isReaded) {
        this.isReaded = isReaded;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNotificationdate() {
        return setNotificationdate;
    }

    public void setNotificationdate(String setNotificationdate) {
        this.setNotificationdate = setNotificationdate;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }
}
