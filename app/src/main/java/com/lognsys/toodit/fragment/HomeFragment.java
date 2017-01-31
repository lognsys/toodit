package com.lognsys.toodit.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.adapter.ImageAdapter;
import com.lognsys.toodit.adapter.OutletsRecylerViewAdapter;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String ARG_ = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private GridLayoutManager lLayout;
    private Button findMoreBtn;



    public HomeFragment() {
        // Required empty public constructor
        Log.d(TAG, "HOME FRAGMENT CALLED");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);


        //Grid Layout Disable for now
        final GridView gridview = (GridView) homeFragmentView.findViewById(R.id.gridview);
        ImageAdapter a = new ImageAdapter(getContext());
         gridview.setAdapter(a);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        findMoreBtn = (Button)homeFragmentView.findViewById(R.id.findmore_button);
        findMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   ImageAdapter a = new ImageAdapter(getContext());
                    a.startLazyLoadImages();
                    gridview.setAdapter(a);
            }
        });



        // Disabling Recyleview
//        lLayout = new GridLayoutManager(getActivity(), 3);
//
//        RecyclerView rView = (RecyclerView)homeFragmentView.findViewById(R.id.recycler_view);
//        rView.setHasFixedSize(true);
//        rView.setLayoutManager(lLayout);
//
//        OutletsRecylerViewAdapter rcAdapter = new OutletsRecylerViewAdapter(getActivity(),    mThumbIds);
//        rView.setAdapter(rcAdapter);

        // Inflate the layout for this fragment
        return homeFragmentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().show();
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
