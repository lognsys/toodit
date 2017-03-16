package com.lognsys.toodit.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.R;
import com.lognsys.toodit.adapter.ImageAdapter;
import com.lognsys.toodit.adapter.ImageAdapterOutlates;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 03-03-2017.
 */

public class FragmentsForMallOutlets extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final String ARG_ = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridLayoutManager lLayout;
    static  String outlet_id;
    ArrayList<ListFoodItem> listFoodItems;
    GridView gridview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
//Retrieve the value
        String value = getArguments().getString("mall_id");


        //Grid Layout Disable for now
        gridview = (GridView) homeFragmentView.findViewById(R.id.gridview);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("mall_id", value);
       outlet_id= getOutletsInMall("http://food.swatinfosystem.com/api/Outlet_list", hashMap);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
                //Akhilesh changes
                if (position == 0) {

                   /* // Fragment fragment1 = new Fragment();
                    Fragment fragment = new FragmentCentralGrill();
                    getActivity().getSupportFragmentManager().beginTransaction()

                            .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/
                    HashMap<String, String> hashMap= new HashMap<String, String>();
                    getItemCategory("http://food.swatinfosystem.com/api/Category",hashMap);


                }
            }
        });
return  homeFragmentView;
    }


    private ArrayList<ListFoodItemCategory> getItemCategory(String URL, final Map<String, String> params) {
        String response = "";
        final ArrayList<ListFoodItemCategory> listFoodItemCategories= new ArrayList<>();
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
                                        getFoodItemList("http://food.swatinfosystem.com/api/Food_item_list",hashMap);
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
        return listFoodItemCategories;

    }
    private ArrayList<ListFoodItem> getFoodItemList(String URL, final Map<String, String> params) {
        String response = "";

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
                                if(jsonObject.getJSONObject("data").getJSONArray("food_list")!=null){
                                    countryArray = jsonObject.getJSONObject("data").getJSONArray("food_list");
                                   for (int i = 0; i < countryArray.length(); i++) {
                                        String food_id = countryArray.getJSONObject(i).getString("food_id");
                                        String name = countryArray.getJSONObject(i).getString("name");
                                        String food_type= countryArray.getJSONObject(i).getString("food_type");
                                        String image=countryArray.getJSONObject(i).getString("image");
                                       String category_name = countryArray.getJSONObject(i).getString("category_name");
                                       String description= countryArray.getJSONObject(i).getString("description");
                                       String price=countryArray.getJSONObject(i).getString("price");
                                       String outlet_id=countryArray.getJSONObject(i).getString("outlet_id");
                                        ListFoodItem listFoodItemCategory= new ListFoodItem( food_id,  name,  food_type,  category_name,  description,  price,  outlet_id,  image);
                                        // Log.e("mallname", mall);
                                        // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();
                                        listFoodItems.add(listFoodItemCategory);

                                    }

                                    Fragment fragment = new TestFragment();
                                    Bundle args = new Bundle();
                                    args.putParcelableArrayList("ListFoodItems", listFoodItems);
                                    //listFoodItems.size();
                                    fragment .setArguments(args);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


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
        return listFoodItems;

    }

    private String getOutletsInMall(String URL, final Map<String, String> params) {
        String response = "";
        final ArrayList<ListMallOutlets> listOfMallOutlets= new ArrayList<>();
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
                                if(jsonObject.getJSONObject("data").getJSONArray("outlet_list")!=null){
                                    countryArray = jsonObject.getJSONObject("data").getJSONArray("outlet_list");
                                    for (int i = 0; i < countryArray.length(); i++) {
                                        String mall_id = countryArray.getJSONObject(i).getString("mall_id");
                                        String mall_name = countryArray.getJSONObject(i).getString("mall_name");
                                        String outlet_name = countryArray.getJSONObject(i).getString("outlet_name");
                                        outlet_id = countryArray.getJSONObject(i).getString("outlet_id");
                                        String image=countryArray.getJSONObject(i).getString("image");
                                        ListMallOutlets listMall= new ListMallOutlets(mall_id,mall_name,outlet_name,outlet_id,image);
                                        // Log.e("mallname", mall);
                                        // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();
                                        if(!listOfMallOutlets.contains(listMall))
                                            listOfMallOutlets.add(listMall);

                                    }
                                    listOfMallOutlets.size();
                                    ImageAdapterOutlates a = new ImageAdapterOutlates(getContext(),listOfMallOutlets);
                                    gridview.setAdapter(a);
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
        return outlet_id;

    }

}

