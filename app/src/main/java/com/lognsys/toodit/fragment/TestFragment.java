package com.lognsys.toodit.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lognsys.toodit.ActivityTab;
import com.lognsys.toodit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 08-03-2017.
 */

public class TestFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<ListFoodItem> value;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.activity_tab_list_of_item, container, false);
        if(!(null==getArguments()))
        { value = getArguments().getParcelableArrayList("ListFoodItems");}
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        //updateToolbarText("");
        //  setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();
        tabLayout.setTabTextColors(
                ContextCompat.getColor(getActivity(), R.color.light_black),
                ContextCompat.getColor(getActivity(), R.color.red)
        );

        return  v;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter  adapter = new  ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new FragmentFastFood(), "Fast Food");
        adapter.addFragment(new FragmentStarter(), "Starter");
        adapter.addFragment(new FragmentBeverages(), "Beverages");
        adapter.addFragment(new FragmentAll(), "All");
      /*  adapter.addFragment(new TwoFragment(), "TWO
        adapter.addFragment(new ThreeFragment(), "THREE");*/

        viewPager.setAdapter(adapter);
    }
    /*private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Fast Food");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Starter");

        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Beverages");
        tabLayout.getTabAt(2).setCustomView(tabThree);
        TextView tabFour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setText("All");
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }*/
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            Bundle bundle= new Bundle();
            bundle.putParcelableArrayList("ListFoodItems", value);
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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

}
