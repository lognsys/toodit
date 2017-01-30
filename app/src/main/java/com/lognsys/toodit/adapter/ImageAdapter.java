package com.lognsys.toodit.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lognsys.toodit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by pdoshi on 24/01/17.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;


    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {

        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            // if it's not recycled, initialize some attributes

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 4, screenWidth / 4));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        imageView.setImageResource(mThumbIds[position]);

        return imageView;
    }


    public void startLazyLoadImages() {

        int arrayLen = mThumbIds.length;
        int newArrLen = (arrayLen * 2) ;
        Integer[] arr = new Integer[newArrLen];

        for (int i = 0; i < newArrLen; i++) {
            arr[i] = mThumbIds[i % arrayLen];
        }
        mThumbIds = arr;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.chillis_trans, R.drawable.sbarro_trans,
            R.drawable.dunkin, R.drawable.maroosh,
            R.drawable.subway, R.drawable.sunskri,
            R.drawable.cafe_theoborma, R.drawable.mac_trans,
            R.drawable.domino
    };
}