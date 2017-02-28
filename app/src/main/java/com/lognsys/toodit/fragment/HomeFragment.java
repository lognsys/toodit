package com.lognsys.toodit.fragment;

import android.support.v4.app.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.adapter.ImageAdapter;
import com.lognsys.toodit.adapter.OutletsRecylerViewAdapter;
import com.lognsys.toodit.util.FragmentTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String ARG_ = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private GridLayoutManager lLayout;
    private Button findMoreBtn;
    private Button btnWayToOutlets;
    ArrayList<String>  listOfmalls= new ArrayList<>();;

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
                //Akhilesh changes
                if(position==7){

                  // Fragment fragment1 = new Fragment();
                    Fragment fragment = new FragmentCentralGrill();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();







                }
            }
        });
        btnWayToOutlets=(Button)homeFragmentView.findViewById(R.id.btnSelectWayToOutlet) ;
        //get Intent
        String city=getActivity().getIntent().getStringExtra("city_id");
        // Log.e("city", city);
        HashMap<String, String> hashMap= new HashMap<>();
        hashMap.put("city_id", city);
        listOfmalls=mallsInCity("http://food.swatinfosystem.com/api/Mall_list", hashMap);
        btnWayToOutlets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogWayToOutles(listOfmalls);
            }
        });
//        findMoreBtn = (Button) homeFragmentView.findViewById(R.id.findmore_button);
//        findMoreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageAdapter a = new ImageAdapter(getContext());
//                a.startLazyLoadImages();
//                gridview.setAdapter(a);
//            }
//        });

        // Inflate the layout for this fragment
        return homeFragmentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // ((MainActivity)getActivity()).getSupportActionBar().show();
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.chillis_trans, R.drawable.sbarro_trans,
            R.drawable.dunkin, R.drawable.maroosh,
            R.drawable.subway, R.drawable.sunskri,
            R.drawable.cafe_theoborma, R.drawable.mac_trans,
            R.drawable.domino
    };
    private void dialogWayToOutles( ArrayList<String> listOfMall)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_mall_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.lvWayToOutlets);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listOfMall);
        lv.setAdapter(adapter);
        alertDialog.show();
    }


    private ArrayList<String> mallsInCity(String URL, final Map<String, String> params) {
        String response = "";
        final ArrayList<String> listOfMall1= new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;

                        try {
                            try {
                                JSONArray countryArray;
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getJSONObject("data").getJSONArray("mall_list")!=null){
                                    countryArray = jsonObject.getJSONObject("data").getJSONArray("mall_list");
                                    for (int i = 0; i < countryArray.length(); i++) {
                                        String mall = countryArray.getJSONObject(i).getString("mall_name");
                                        Log.e("mallname", mall);
                                        Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();
                                        listOfMall1.add(mall);

                                    }
                                }

                                //Log.e("check", countryArray.toString());


                                //getCountryFromJson(countryArray.toString());
                            } catch (JSONException je) {
                                je.printStackTrace();
                            }

                            //CountryName emp = objectMapper.readValue(response, CountryName.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        //  Log.e("Lisof malls", listOfMall1.get(0));
        return listOfMall1;

    }
}
