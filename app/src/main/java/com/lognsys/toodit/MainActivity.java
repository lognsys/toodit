package com.lognsys.toodit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lognsys.toodit.fragment.CartFragment;
import com.lognsys.toodit.fragment.HomeFragment;
import com.lognsys.toodit.fragment.NotificationFragment;
import com.lognsys.toodit.fragment.SettingFragment;
import com.lognsys.toodit.model.CityName;
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.FragmentTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM = "arg_selected_item";
    private BottomNavigationViewEx mBottomNav;
    private int mSelectedItem; //index of bottom nagivation bar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNav = (BottomNavigationViewEx) findViewById(R.id.nav);
        mBottomNav.enableAnimation(false);
        mBottomNav.enableShiftingMode(false);
        mBottomNav.enableItemShiftingMode(false);


        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
    }

    /**
     * This will save the mSelectedItem value of bottom nav bar
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * select fragment gets item from bottom navigation bar
     * and displays fragment
     *
     * @param item
     */
    private void selectFragment(MenuItem item) {
        Fragment fragment = null;
        CallAPI callAPI = new CallAPI();

        String fragmentTag = "";

        switch (item.getItemId()) {
            case R.id.menu_home:
                item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                fragment = HomeFragment.newInstance();
                fragmentTag = FragmentTag.FRAGMENT_HOME.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);
                break;
            case R.id.menu_notifications:
               item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                 /*fragment = NotificationFragment.newInstance(getString(R.string.text_notification),

                        getColorFromRes(R.color.colorAccent));*/
                fragment = new NotificationFragment();

               fragmentTag = FragmentTag.FRAGMENT_NOTIFICATION.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);

                break;
            case R.id.menu_cart:
                item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                fragment = new CartFragment();
               fragmentTag = FragmentTag.FRAGMENT_CART.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);

                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/
                break;
            case R.id.menu_settings:
                item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                fragment = new SettingFragment();
                fragmentTag = FragmentTag.FRAGMENT_SETTING.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);

                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();


        // uncheck the other items.
        for (int i = 0; i < mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);

            if (menuItem.getItemId() != mSelectedItem) {
                menuItem.setChecked(false);
                menuItem.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
        }

        //Update ActionBar Based on Fragment
        //updateToolbarText(item.getTitle());

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, fragmentTag);
            ft.commit();
        }
    }

   /* //Update Action Bar
    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            actionBar.setLogo(R.drawable.toodit_logo);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }



}

