package com.lognsys.toodit;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.lognsys.toodit.fragment.HomeFragment;
import com.lognsys.toodit.fragment.NotificationFragment;

import static android.R.color.holo_red_dark;


public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM = "arg_selected_item";

    private BottomNavigationView mBottomNav;
    private int mSelectedItem; //index of bottom nagivation bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.inflateMenu(R.menu.bottom_nav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
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
     * select fragment gets item from bottom navigation bar and displays fragment
     *
     * @param item
     */
    private void selectFragment(MenuItem item) {
        Fragment fragment = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = HomeFragment.newInstance();
                break;
            case R.id.menu_notifications:
                fragment = NotificationFragment.newInstance(getString(R.string.text_notification),
                        getColorFromRes(R.color.colorAccent));
                break;
            case R.id.menu_cart:
                Toast.makeText(getApplicationContext(), "Cart is Empty!",
                        Toast.LENGTH_SHORT).show();

                break;
            case R.id.menu_settings:
                Toast.makeText(getApplicationContext(), "Settings Work In Progress!",
                        Toast.LENGTH_SHORT).show();
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i < mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);

            if (menuItem.getItemId() == mSelectedItem) {
                menuItem.setChecked(true);
                menuItem.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            } else {
                menuItem.setChecked(false);
                menuItem.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

        }

        updateToolbarText(item.getTitle());

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, fragment, fragment.getTag());
            ft.commit();
        }
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            actionBar.setLogo(R.drawable.toodit_logo);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);


        }
    }

    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }
}
