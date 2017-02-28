package com.lognsys.toodit.model;

/**
 * Created by admin on 24-02-2017.
 */

public class CityName {
   private String cityId;
   private String cityname;
   private String stateId;

    public CityName(){

    }
    public CityName(String cityId, String cityname, String stateId) {
        this.stateId = stateId;
        this.cityname = cityname;
        this.cityId = cityId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }


    public String toString()
    {
        return cityname;
    }
}
