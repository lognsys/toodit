package com.lognsys.toodit.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.lognsys.toodit.adapter.ImageAdapterOutlates;
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.Constants;
import com.lognsys.toodit.util.FragmentTag;
import com.lognsys.toodit.util.PropertyReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static final String ARG_ = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CallAPI callAPI;
    AlertDialog dialog;
    static  String outlet_id;
    private GridLayoutManager lLayout;
    private Button findMoreBtn;
    private TextView tvListOfMalls;
    ArrayList<String>  listOfmalls= new ArrayList<>();;
    ArrayList<ListMall>  listMalls= new ArrayList<ListMall>();
    ArrayList<ListMallOutlets> listMallOutletses;
    ProgressBar mProgressBar;

    ArrayList<ListFoodItem> listFoodItems;
    String value;

    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "toodit.properties";

    GridView gridview;

    public HomeFragment() {
        // Required empty public constructor
//        Log.d(TAG, "HOME FRAGMENT CALLED");
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
        callAPI=new CallAPI();
        propertyReader = new PropertyReader(getContext());
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
      if(getArguments()!=null){
          if(getArguments().getString("mall_id")!=null){
              value = getArguments().getString("mall_id");
              mProgressBar = (ProgressBar) homeFragmentView.findViewById(R.id.progressBar);
              mProgressBar.setVisibility(View.VISIBLE);
          }
      }

            gridview = (GridView) homeFragmentView.findViewById(R.id.gridview);


        TextView tvListOfMalls=(TextView)homeFragmentView.findViewById(R.id.tvListOfMalls);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                String category_api= properties.getProperty(Constants.API_URL.category_api_url.name());
                getItemCategory(category_api);
           }
        });

        if(value != null ) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("mall_id", value);
            String outlet_list_api= properties.getProperty(Constants.API_URL.outlet_api_url.name());

            outlet_id= getOutletsInMall(outlet_list_api, hashMap);

        }
        else{
            String city= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("city_id", "");
            //Log.e("city", city);
            HashMap<String, String> hashMapcity= new HashMap<>();
            // hashMap.put("city_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("city_id", ""));
            hashMapcity.put("city_name", "mumbai");

            String mall_list_api= properties.getProperty(Constants.API_URL.mall_api_url.name());
            listMalls=mallsInCity(mall_list_api, hashMapcity);
        }

            tvListOfMalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("city_id", "");
                //Log.e("city", city);
                HashMap<String, String> hashMapcity= new HashMap<>();
                // hashMap.put("city_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("city_id", ""));
                hashMapcity.put("city_name", "mumbai");

                String mall_list_api= properties.getProperty(Constants.API_URL.mall_api_url.name());
                listMalls=mallsInCity(mall_list_api, hashMapcity);
                if(listMalls!=null){
                    dialogWayToOutles(listMalls);
                }

            }
        });
        return homeFragmentView;
    }

    private ArrayList<ListFoodItemCategory> getItemCategory(String URL) {
        String response = "";

        callAPI.showProgressbar((AppCompatActivity) getActivity());

        final ArrayList<ListFoodItemCategory> listFoodItemCategories= new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Log.d(TAG,"HOME category reponse"+response);
                        try {
                            try {
                                JSONArray countryArray;
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getJSONObject("data").getJSONArray("category_details")!=null){
                                    countryArray = jsonObject.getJSONObject("data").getJSONArray("category_details");
                                    for (int i = 0; i < countryArray.length(); i++) {
                                        String category_id = countryArray.getJSONObject(i).getString("category_id");
                                        String name = countryArray.getJSONObject(i).getString("name");
                                        String description= countryArray.getJSONObject(i).getString("description");
                                        String image=countryArray.getJSONObject(i).getString("image");
                                        ListFoodItemCategory listFoodItemCategory= new ListFoodItemCategory(category_id,name,description,image);
                                          // Log.e("mallname", mall);
                                        // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();
                                        listFoodItemCategories.add(listFoodItemCategory);

                                    }
                                    boolean flage=true;
                                    listFoodItems= new ArrayList<>();
                                    for(int i=0;i<listFoodItemCategories.size();i++)
                                    {
                                        String category_id= listFoodItemCategories.get(i).getCateggory_id();
                                        HashMap<String, String> hashMap= new HashMap<String ,String>();
                                        hashMap.put("outlet_id", outlet_id);
                                        hashMap.put("category_id",category_id);
                                        //testt
                                      /*  hashMap.put("outlet_id", "1");
                                        hashMap.put("category_id",category_id);*/
                                        Log.e("outlet_id", outlet_id);
//                                        Log.d(TAG,"Home category_id"+category_id);
//                                        Log.d(TAG,"Home outlet_id"+outlet_id);

                                        String food_item_list_api= properties.getProperty(Constants.API_URL.food_item_list_api_url.name());

                                        getFoodItemList(food_item_list_api,hashMap);
                                    }


                                }

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
           /* @Override
            protected Map<String, String> getParams() {

                return params;
            }*/

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        //  Log.e("Lisof malls", listOfMall1.get(0));
        return listFoodItemCategories;

    }
    private ArrayList<ListFoodItem> getFoodItemList(String URL, final Map<String, String> params) {
        String response = "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            try {
                                JSONArray jsonArray;
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getJSONObject("data").getJSONArray("food_list")!=null){
                                    jsonArray = jsonObject.getJSONObject("data").getJSONArray("food_list");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String food_id = jsonArray.getJSONObject(i).getString("food_id");
                                        String name = jsonArray.getJSONObject(i).getString("name");
                                        String food_type= jsonArray.getJSONObject(i).getString("food_type");
                                        String image=jsonArray.getJSONObject(i).getString("image");
                                        String category_name = jsonArray.getJSONObject(i).getString("category_name");
                                        String description= jsonArray.getJSONObject(i).getString("description");
                                        String price=jsonArray.getJSONObject(i).getString("price");
                                        String outlet_id=jsonArray.getJSONObject(i).getString("outlet_id");
                                        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                                        //SharedPreferences Editor
                                        SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

                                        sharedPrefEditor.putString("outlet_id", outlet_id);
                                        sharedPrefEditor.commit();
                                        Log.e("outlet_id", outlet_id);
                                        ListFoodItem listFoodItemCategory= new ListFoodItem( food_id,  name,  food_type,  category_name,  description,  price,  outlet_id,  image);
                                        listFoodItems.add(listFoodItemCategory);

                                    }
                                    callAPI.hideProgressbar((AppCompatActivity) getActivity());

                                    Fragment fragment = new TestFragment();
                                    Bundle args = new Bundle();
                                    args.putParcelableArrayList("ListFoodItems", listFoodItems);
                                    //listFoodItems.size();
                                    fragment .setArguments(args);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                }

                            } catch (JSONException je) {
                                je.printStackTrace();
                                Log.d(TAG,"Home countryArray JSONException "+je);

                            }

                            //CountryName emp = objectMapper.readValue(response, CountryName.class);
                        } catch (Exception e) {
                            Log.d(TAG,"Home countryArray Exception "+e);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"Home countryArray VolleyError "+error);

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
        return listFoodItems;

    }


    private String getOutletsInMall(String URL, final Map<String, String> params) {
        String response = "";

        final ArrayList<ListMallOutlets> listOfMallOutlets= new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;

                        try {
                            try {
                                JSONArray countryArray;
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getJSONObject("data").getJSONArray("outlet_list")!=null){
                                    countryArray = jsonObject.getJSONObject("data").getJSONArray("outlet_list");
                                    for (int i = 0; i < countryArray.length(); i++) {
                                        String mall_id = countryArray.getJSONObject(i).getString("mall_id");
                                        String mall_name = countryArray.getJSONObject(i).getString("mall_name");
                                        String outlet_name = countryArray.getJSONObject(i).getString("outlet_name");
                                        outlet_id = countryArray.getJSONObject(i).getString("outlet_id");
                                        String image=countryArray.getJSONObject(i).getString("image");
                                        Log.d("","Home test image"+image);
                                        ListMallOutlets listMall= new ListMallOutlets(mall_id,mall_name,outlet_name,outlet_id,image);

                                        listMall.setOutlet_image(image);
                                        listMall.setMall_id(mall_id);
                                        listMall.setMall_name(mall_name);
                                        listMall.setOutlet_id(outlet_id);
                                        listMall.setOutlet_name(outlet_name);
                                        Log.d("","Home test listMall"+image);

                                        listOfMallOutlets.add(listMall);

                                    }
                                    Log.d(TAG,"HOME listOfMallOutlets ===="+listOfMallOutlets.size());
                                    if(listOfMallOutlets!=null && listOfMallOutlets.size()>0) {
                                      populateAdapter(listOfMallOutlets);
                                        onStop();
                                    }
                                }

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
          return outlet_id;

    }

    private void populateAdapter(ArrayList<ListMallOutlets> listOfMallOutlets) {

        ImageAdapterOutlates imageAdapterOutlates = new ImageAdapterOutlates(getContext(),R.layout.list_image, listOfMallOutlets);
        gridview.setAdapter(imageAdapterOutlates);
        mProgressBar.setVisibility(View.GONE);
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
   private void dialogWayToOutles(ArrayList<ListMall> listOfMall)
   {
//      changed by monika 18/04/17 for dialog
       if(listOfMall!=null && listOfMall.size()>0){
//           Log.d(TAG, "HOME FRAGMENT CALLED dialogWayToOutles listOfMall size"+listOfMall.size());
           MyBaseAdapter myBaseAdapter = new MyBaseAdapter(getActivity(),listOfMall);
           LayoutInflater inflater = getActivity().getLayoutInflater();
           View convertView = (View) inflater.inflate(R.layout.dialog_mall_list, null);

           ListView listView = (ListView) convertView.findViewById(R.id.lvWayToOutlets);
           listView.setAdapter(myBaseAdapter);

           final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
           builder.setIcon(R.drawable.action_bar_map);
           builder.setTitle("Mumbai");
           builder.setCancelable(true);
           builder.setView(convertView);

          dialog=builder.create();
//           dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
           dialog.getWindow().clearFlags(WindowManager.LayoutParams.DIM_AMOUNT_CHANGED);
           dialog.setCanceledOnTouchOutside(false);
           dialog.show();

           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   TextView tvMallName=(TextView)view.findViewById(R.id.tvMallName);
                   String test=tvMallName.getText().toString();
                   //String malli_id = ((ListMall)  tvMallName.getText()).getMallId();
                   String mall_id=((ListMall)listMalls.get(position)).getMallId();
//                   Fragment fragment = new FragmentsForMallOutlets();
                   Fragment fragment = new HomeFragment();
                   Bundle args = new Bundle();
                   args.putString("mall_id", mall_id);
                   fragment .setArguments(args);
                   getActivity().getSupportFragmentManager().beginTransaction()
                           .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                   dialog.dismiss();
               }
           });
       }


  }

//  monika added  here
  private ArrayList<ListMall> mallsInCity(String URL, final Map<String, String> params) {
        String response = "";

        callAPI.showProgressbar((AppCompatActivity) getActivity());
        final ArrayList<ListMall> listOfMall1= new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                                    if(listOfMall1!=null && listOfMall1.size()>0){
                                        callAPI.hideProgressbar((AppCompatActivity) getActivity());
                                        Log.e("mallname", "HOME mail listOfMall1 size "+listOfMall1.size());
                                        dialogWayToOutles(listOfMall1);
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
                        callAPI.hideProgressbar((AppCompatActivity) getActivity());

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
           mViewHolder.tvMallAdd.setText(currentListMall.getMallAddress());

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
@Override
public void onResume() {
        super.onResume();

        CallAPI callAPI = new CallAPI();
        // Log.d("PaymentFragment","Rest getClass().getName().toString() "+getClass().getName().toString());
        callAPI.updateToolbarText(FragmentTag.FRAGMENT_HOME.getFragmentTag(),(AppCompatActivity)getActivity());
        }

    }
