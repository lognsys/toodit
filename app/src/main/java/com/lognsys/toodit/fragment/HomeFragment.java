package com.lognsys.toodit.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.ActivityTab;
import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.adapter.ImageAdapter;
import com.lognsys.toodit.util.CallAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private TextView tvListOfMalls;
    ArrayList<String>  listOfmalls= new ArrayList<>();;
    ArrayList<ListMall>  listMalls= new ArrayList<ListMall>();
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
        tvListOfMalls=(TextView)homeFragmentView.findViewById(R.id.tvListOfMalls);
        ImageAdapter a = new ImageAdapter(getContext());
        gridview.setAdapter(a);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
              /*  Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();*/
              /*  if(position==0)
                {

                   *//* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*//*
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }*/
                if(position==1)
                {

                   /* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*/
                    Fragment fragment1 = new FragmentMyOrders();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }
                if(position==2)
                {

                   /* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*/
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }
              /*  if(position==3)
                {

                   *//* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*//*
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }
                if(position==4)
                {

                   *//* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*//*
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }
                if(position==5)
                {

                   *//* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*//*
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }  if(position==6)
                {

                   *//* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*//*
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }
                if(position==8)
                {

                   *//* Intent i= new Intent(getActivity(), ActivityTab.class);
                    startActivity(i);*//*
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();
                }

                //Akhilesh changes
                if(position==7){

                  // Fragment fragment1 = new Fragment();
                    Fragment fragment1 = new TestFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment1, fragment1.getClass().getSimpleName()).addToBackStack(null).commit();





                }*/
            }
        });
        //tvListOfMalls=(Button)homeFragmentView.findViewById(R.id.btnSelectWayToOutlet) ;
        //get Intent
        //String city=getActivity().getIntent().getStringExtra("city_id");
        //SharedPreferences sharedpreferences;
        //Initialize SharedPreferences
        //sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
       String city= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("city_id", "");
        //Log.e("city", city);
        HashMap<String, String> hashMap= new HashMap<>();
       // hashMap.put("city_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("city_id", ""));
        hashMap.put("city_name", "mumbai");
        listMalls=mallsInCity("http://food.swatinfosystem.com/api/Mall_list", hashMap);

        tvListOfMalls.setOnClickListener(new View.OnClickListener() {
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
       MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getActivity(),listMalls);
       final Dialog alertDialog = new Dialog(getActivity());
       LayoutInflater inflater = getActivity().getLayoutInflater();
       final View convertView = (View) inflater.inflate(R.layout.dialog_mall_list, null);
       alertDialog.setContentView(convertView);
       alertDialog.setCancelable(true);
       alertDialog.setTitle("List of Malls");
       ListView lv = (ListView) convertView.findViewById(R.id.lvWayToOutlets);
      // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listOfMall);
       lv.setAdapter(myBaseAdapter);
       alertDialog.show();
       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               TextView tvMallName=(TextView)view.findViewById(R.id.tvMallName);
               String test=tvMallName.getText().toString();
                  //String malli_id = ((ListMall)  tvMallName.getText()).getMallId();
               String mall_id=((ListMall)listMalls.get(position)).getMallId();
               Fragment fragment = new FragmentsForMallOutlets();
               Bundle args = new Bundle();
               args.putString("mall_id", mall_id);
               fragment .setArguments(args);
               getActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            alertDialog.dismiss();
              // Log.e("mall_id",String.valueOf(listMalls.get(position).getMallId())+""+mall_id);
               //alertDialog.dismiss();

           }
       });
  }

    private ArrayList<ListMall> mallsInCity(String URL, final Map<String, String> params) {
        String response = "";
        final ArrayList<ListMall> listOfMall1= new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;

                        try {
                            try {
                                JSONArray countryArray;
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getJSONObject("data").getJSONArray("mall_list")!=null){
                                    countryArray = jsonObject.getJSONObject("data").getJSONArray("mall_list");
                                    for (int i = 0; i < countryArray.length(); i++) {
                                        String mall_name = countryArray.getJSONObject(i).getString("mall_name");
                                        String mall_address = countryArray.getJSONObject(i).getString("mall_address");
                                        String mall_id = countryArray.getJSONObject(i).getString("mall_id");
                                        ListMall listMall= new ListMall(mall_name,mall_address,mall_id);
                                       // Log.e("mallname", mall);
                                       // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();
                                        listOfMall1.add(listMall);

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
    private ArrayList<ListMall> getDataInList() {


            return listMalls;
        }



    public class MyBaseAdapter extends BaseAdapter {

        private  ArrayList<ListMall> myList = new ArrayList<ListMall>();
        LayoutInflater inflater;
        Context context=getActivity();


        public MyBaseAdapter(Context context, ArrayList<ListMall> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListMall getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HomeFragment.MyBaseAdapter.MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_raw_way_to_outlets, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (HomeFragment.MyBaseAdapter.MyViewHolder) convertView.getTag();
            }

            ListMall currentListMall = getItem(position);

           mViewHolder.tvMallName.setText(currentListMall.getMallName());
            //Log.e("check",currentListMall.getProfileName());
           //mViewHolder.tvMallAdd.setText("\""+currentListMall.getMallAddress()+"\"");

            return convertView;
        }

        private class MyViewHolder {
            TextView tvMallName, tvMallAdd;


            public MyViewHolder(View item) {
                tvMallName = (TextView) item.findViewById(R.id.tvMallName);
                tvMallAdd= (TextView) item.findViewById(R.id.tvMallAdd);

            }
        }

    }

    }
