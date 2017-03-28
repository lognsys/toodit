package com.lognsys.toodit.fragment;

/**
 * Created by admin on 02-03-2017.
 */

public class ListMall {
    private String mallName;
    private String mallAddress;
    private String mallId;


    public  ListMall()
    {

    }
    public ListMall(String mallName, String mallAddress, String mallId) {
        this.mallName = mallName;
        this.mallAddress = mallAddress;
        this.mallId=mallId;
    }

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public String getMallAddress() {
        return mallAddress;
    }

    public void setMallAddress(String mallAddress) {
        this.mallAddress = mallAddress;
    }
    public String getMallId() {
        return mallId;
    }

    public void setMallId(String mallId) {
        this.mallId = mallId;
    }

}
