package com.lognsys.toodit.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.adapter.ProfileListAdapter;


public class SettingFragment extends Fragment {

    private static final String ARG_NAME = "username";
    private static final String ARG_LOC = "location";
    private String name;
    private String loc;

    ListView profileListView;
    String[] titles;

    //Profile listview images in order
    Integer[] imgid = {
            R.drawable.bag,
            R.drawable.message,
            R.drawable.privacy_policy,
            R.drawable.terms_cond,
            R.drawable.transperant_image,
            R.drawable.transperant_image,
            R.drawable.transperant_image
    };

    public SettingFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            loc = getArguments().getString(ARG_LOC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ((MainActivity) getActivity()).getSupportActionBar().hide();
        ProfileListAdapter adapter = new ProfileListAdapter(getActivity(), titles, imgid);
        profileListView = (ListView) getView().findViewById(R.id.list);
        profileListView.setAdapter(adapter);
        profileListView.setDivider(null);
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedTitle = titles[+position];
                Toast.makeText(getActivity(), selectedTitle, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        titles = new String[4];
        titles[0] = getContext().getResources().getString(R.string.text_profile_title_order);
        titles[1] = getContext().getResources().getString(R.string.text_profile_title_notification);
        titles[2] = getContext().getResources().getString(R.string.text_profile_title_privacy);
        titles[3] = getContext().getResources().getString(R.string.text_profile_title_tc);

    }
}