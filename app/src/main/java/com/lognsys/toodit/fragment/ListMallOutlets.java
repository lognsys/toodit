package com.lognsys.toodit.fragment;

/**
 * Created by admin on 03-03-2017.
 */

public class ListMallOutlets {
    private String mall_id;
    private String mall_name;
    private String outlet_name;
    private String outlet_id;
    private String outlet_image;

    public String getOutlet_image() {
        return outlet_image;
    }

    public ListMallOutlets(String mall_id, String mall_name, String outlet_name, String outlet_id, String outlet_image) {
        this.mall_id = mall_id;
        this.mall_name = mall_name;
        this.outlet_name = outlet_name;
        this.outlet_id = outlet_id;
        this.outlet_image = outlet_image;
    }

    public void setOutlet_image(String outlet_image) {
        this.outlet_image = outlet_image;
    }


    public String getMall_id() {
        return mall_id;
    }

    public void setMall_id(String mall_id) {
        this.mall_id = mall_id;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getOutlet_id() {
        return outlet_id;
    }

    public void setOutlet_id(String outlet_id) {
        this.outlet_id = outlet_id;
    }

   /* @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if((obj == null) || (obj.getClass() != this.getClass())){
            return false;
        }

        ListMallOutlets that = (ListMallOutlets) obj;

        // Use the equality == operator to check if the argument is the reference to this object,
        // if yes. return true. This saves time when actual comparison is costly.
        return
                (mall_id == that.mall_id || (mall_id != null && mall_id.equals(that.mall_id))&&
                        mall_name == that.mall_name || (mall_name != null && mall_name.equals(that.mall_name))
                &&outlet_id == that.outlet_id || (outlet_id != null && outlet_id.equals(that.outlet_id))
                &&outlet_name == that.outlet_name || (outlet_name != null && outlet_name.equals(that.outlet_name))
        && outlet_image == that.outlet_image || (outlet_image != null && outlet_image.equals(that.outlet_name)));

    }

    *//**
     * This method returns the hash code value for the object on which this method is invoked.
     * This method returns the hash code value as an integer and is supported for the benefit of
     * hashing based collection classes such as Hashtable, HashMap, HashSet etc. This method must
     * be overridden in every class that overrides the equals method.
     *
     * @return
     *//*
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.valueOf(outlet_id);
        hash = 31 * hash + (null == Integer.valueOf(outlet_id)? 0 : Integer.valueOf(outlet_id).hashCode());
        return hash;
    }*/
}

