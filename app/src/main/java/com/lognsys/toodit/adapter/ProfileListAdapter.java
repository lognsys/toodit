package com.lognsys.toodit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lognsys.toodit.R;

/**
 * Created by pdoshi on 31/01/17.
 */

public class ProfileListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public ProfileListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.setting_listview, itemname);

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.setting_listview, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.setting_title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_settings);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    }

}
