package com.lognsys.toodit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lognsys.toodit.R;

/**
 * Created by admin on 07-03-2017.
 */

public class FragmentB extends Fragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_list_of_item, container, false);


        return v;
    }

}
