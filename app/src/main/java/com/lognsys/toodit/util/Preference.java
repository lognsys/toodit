package com.lognsys.toodit.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 3/22/2017.
 */

public class Preference {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Preference(Context context) {
        // TODO Auto-generated constructor stub
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
    }

    public void setCity(String city)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city", city);
        editor.commit();
    }

    public String getCity()
    {
        return sharedPreferences.getString("city", null);
    }
    public void setIsLogin(Boolean isLogin)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.commit();
    }

    public Boolean getIsLogin()
    {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public void setCustomer_id(String customer_id)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customer_id", customer_id);
        editor.commit();
    }

    public String getCustomer_id()
    {
        return sharedPreferences.getString("customer_id", null);
    }

    public void setName(String name)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public String getName()
    {
        return sharedPreferences.getString("name", null);
    }

    public void setMobile(String mobile)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public String getMobile()
    {
        return sharedPreferences.getString("mobile", null);
    }

    public void setEmail(String email)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail()
    {
        return sharedPreferences.getString("email", null);
    }

    public void setImage(String image)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", image);
        editor.commit();
    }

    public String getImage()
    {
        return sharedPreferences.getString("image", null);
    }

    public void setFirst_Name(String first_name)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("first_name", first_name);
        editor.commit();
    }

    public String getFirst_Name()
    {
        return sharedPreferences.getString("first_name", null);
    }
    public void setLast_Name(String last_name)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("last_name", last_name);
        editor.commit();
    }

    public String getLast_Name()
    {
        return sharedPreferences.getString("last_name", null);
    }
    public void setLink(String link)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("link", link);
        editor.commit();
    }

    public String getLink()
    {
        return sharedPreferences.getString("link", null);
    }
    public void setPicture(String picture)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("picture", picture);
        editor.commit();
    }

    public String getPicture()
    {
        return sharedPreferences.getString("picture", null);
    }

    public void setTimezone(String timezone)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("timezone", timezone);
        editor.commit();
    }

    public String getTimezone()
    {
        return sharedPreferences.getString("timezone", null);
    }
}
