package com.lognsys.toodit.model;

/**
 * Created by admin on 24-02-2017.
 */

public class CountryName {

    private String country_id;
    private String name;
    private String shortname;
    private String phonecode;
    private String is_delete;

    public CountryName()
    {

    }

    public CountryName(String country_id, String name, String shortname, String phonecode, String is_delete) {
        this.country_id = country_id;
        this.name = name;
        this.shortname = shortname;
        this.phonecode = phonecode;
        this.is_delete = is_delete;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
   @Override
    public String toString()
   {
        return name;
    }
}
