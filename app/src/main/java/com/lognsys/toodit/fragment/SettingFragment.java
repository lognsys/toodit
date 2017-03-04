package com.lognsys.toodit.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.lognsys.toodit.LoginActivity;
import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.adapter.ProfileListAdapter;


public class SettingFragment extends Fragment {

    private static final String ARG_NAME = "username";
    private static final String ARG_LOC = "location";
    private String name;
    private String loc;
    private Button logout;
    private Button update;
    //Google API variable
    private GoogleApiClient mGoogleApiClient;
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
        logout = (Button) view.findViewById(R.id.logout);
        update = (Button) view.findViewById(R.id.edit_profile);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UpdateFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //firebase variable declaration
                                FirebaseAuth mAuth;
                                mAuth = FirebaseAuth.getInstance();
                                // Firebase sign out
                                mAuth.signOut();

                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
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