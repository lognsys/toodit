package com.lognsys.toodit.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.R;
import com.lognsys.toodit.TooditApplication;
import com.lognsys.toodit.model.CountryName;
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.FragmentTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by admin on 04-02-2017.
 */

public class FragmentCentralGrill extends Fragment {
    private ListView lvComment;
    private ImageView ivItemImage, ivAdd, iivMinus, ivAddToCart;
    private TextView tvItemName, tvItemDescript, tvNoOfAddedItems, tvThecentralgrill, tvAddress, tvAmount;
    private RatingBar rbRating;
    private  String food_id, customer_id, outlet_id,quantity;
    Context context = null;
    MyBaseAdapter myBaseAdapter;
    private static int noOfItems = 1;

    private ArrayList<ListData> myList = new ArrayList<ListData>();

    String[] comment = new String[]{
            "Great Test, good service by The Central Hill ", "Yummy !! loving it"

    };
    String[] profileName = new String[]{
            "Rahul Sharma", "Sneha"};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_central_grill, container, false);
        context = this.getActivity();
        ivItemImage = (ImageView) v.findViewById(R.id.ivItemImage);
        ivAddToCart=(ImageView)v.findViewById(R.id.ivAddTo_cart);
        tvItemName = (TextView) v.findViewById(R.id.tvItemName);
        tvItemDescript = (TextView) v.findViewById(R.id.tvItemQuality);
        tvThecentralgrill = (TextView) v.findViewById(R.id.tvTheCentralGrill);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        ivItemImage = (ImageView) v.findViewById(R.id.ivItemImage);
        lvComment = (ListView) v.findViewById(R.id.lvComment);
        rbRating = (RatingBar) v.findViewById(R.id.rbRating);
        ivAdd = (ImageView) v.findViewById(R.id.ivAdd);
        iivMinus = (ImageView) v.findViewById(R.id.ivMinus);
        tvNoOfAddedItems = (TextView) v.findViewById(R.id.tvNoOfAddedItem);
        tvAmount=(TextView)v.findViewById(R.id.tvAmount);
        HashMap<String, String> hashmapReview= new HashMap<>();
        hashmapReview.put("food_id", (String) getArguments().get("food_id"));
        food_id=(String) getArguments().get("food_id");
        hashmapReview.put("outlet_id", (String) getArguments().get("outlet_id"));
        outlet_id=(String) getArguments().get("outlet_id");
       // getFoodItemReview("http://food.swatinfosystem.com/api/Get_all_reviews", hashmapReview);
       /* Drawable drawable = rbRating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFAF0A"), PorterDuff.Mode.SRC_ATOP);*/
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("food_item_id", (String) getArguments().get("food_id"));
        Log.e("food_id", (String) getArguments().get("food_id"));
        getFoodItemDetail("http://food.swatinfosystem.com/api/Food_item_details", hashMap);
        getDataInList();
        myBaseAdapter = new MyBaseAdapter(this.getActivity(), myList);
        new Helper().getListViewSize(lvComment);
//Retrieve the value
        /*String value = getArguments().getString("position");
        if(value.equals("1")){
            ivItemImage.setImageResource(R.drawable.cheese_burger);
            tvItemName.setText("Cheesy Onion Veg Burger");
            tvItemDescript.setText("With extra buter cheese");

        }*/
        lvComment.setAdapter(myBaseAdapter);


        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something when the corky2 is clicked
                noOfItems++;
                tvNoOfAddedItems.setText(String.valueOf(noOfItems));
            }
        });
        iivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something when the corky2 is clicked
                if (noOfItems > 1)
                    noOfItems--;
                tvNoOfAddedItems.setText(String.valueOf(noOfItems));
            }
        });
        ivAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap= new HashMap<String, String>();
                hashMap.put("food_id", (String)getArguments().get("food_id"));

                hashMap.put("quantity", tvNoOfAddedItems.getText().toString());

                quantity=tvNoOfAddedItems.getText().toString();
                // SharedPreferences userDetails = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                Monika changed25/03/17  hashMap.put("customer_id",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", ""));

                hashMap.put("customer_id", TooditApplication.getInstance().getPrefs().getCustomer_id());
                // Log.e("cust_id",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", ""));
                customer_id= customer_id= TooditApplication.getInstance().getPrefs().getCustomer_id();

                PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", "");
                hashMap.put("outlet_id", (String)getArguments().get("outlet_id"));


                addToCart("http://food.swatinfosystem.com/api/Add_to_cart", hashMap);
            }
        });

        return v;
    }

    private void getDataInList() {
        for (int i = 0; i < profileName.length; i++) {
            // Create a new object for each list item
            ListData ld = new ListData();
            ld.setComment(comment[i]);
            ld.setProfileName(profileName[i]);

            // Add this object into the ArrayList myList
            myList.add(ld);


        }
    }


    public class MyBaseAdapter extends BaseAdapter {

        private ArrayList<ListData> myList = new ArrayList<ListData>();
        LayoutInflater inflater;
        Context context = getActivity();


        public MyBaseAdapter(Context context, ArrayList<ListData> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListData getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.centrlal_grill_list, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            ListData currentListData = getItem(position);

            mViewHolder.tvProfileName.setText("- " + currentListData.getProfileName());
            Log.e("check", currentListData.getProfileName());
            mViewHolder.tvComment.setText("\"" + currentListData.getComment() + "\"");

            return convertView;
        }

        private class MyViewHolder {
            TextView tvComment, tvProfileName;


            public MyViewHolder(View item) {
                tvComment = (TextView) item.findViewById(R.id.tvComment);
                tvProfileName = (TextView) item.findViewById(R.id.tvProfileName);

            }
        }


    }

    public class Helper {
        public void getListViewSize(ListView myListView) {
            ListAdapter BaseAdapter = myListView.getAdapter();
            if (myBaseAdapter == null) {
                //do nothing return null
                return;
            }
            //set listAdapter in loop for getting final size
            int totalHeight = 0;
            for (int size = 0; size < myBaseAdapter.getCount(); size++) {
                View listItem = myBaseAdapter.getView(size, null, myListView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            //setting listview item in adapter
            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = totalHeight + (myListView.getDividerHeight() * (myBaseAdapter.getCount() - 1));
            myListView.setLayoutParams(params);
            // print height of adapter on log
            Log.i("height of listItem:", String.valueOf(totalHeight));
        }
    }

    private String getFoodItemDetail(String URL, final Map<String, String> params) {
        String response = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject foodDetails = jsonObject.getJSONObject("data").getJSONObject("food_item_details");

                                tvItemName.setText(foodDetails.get("name").toString());
                                Log.e("name", foodDetails.get("outlet_name").toString());
                                tvItemDescript.setText(foodDetails.get("description").toString());

                                //tvNoOfAddedItems.setText(foodDetails.get("name").toString());
                                tvThecentralgrill.setText(foodDetails.get("outlet_name").toString());
                                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                                //SharedPreferences Editor
                                SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

                                sharedPrefEditor.putString("outlet_name", foodDetails.get("outlet_name").toString());
                                sharedPrefEditor.commit();
                                tvAmount.setText(foodDetails.get("price").toString());
                                tvAddress.setText(foodDetails.get("outlet_address").toString());
                                //rbRating.setRating(Float.valueOf(foodDetails.get("avg_rating").toString()));
                                rbRating.setRating(Float.valueOf("3.3"));
                                new FragmentCentralGrill.ImageLoadTask(foodDetails.get("image").toString(), ivItemImage).execute();
                                //Log.e("check", countryArray.toString);
                                //
                                // Log.e("check", countryList.get(4).getName());
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
                        // Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), "Internet not available !", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        return response;
    }

   /* private String getFoodItemReview(String URL, final Map<String, String> params) {
        String response = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonList= jsonObject.getJSONObject("data").getJSONObject("reviews_list");
                                //
                                // Log.e("check", countryList.get(4).getName());
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
                        // Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), "Internet not available !", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        return response;
    }*/

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  mViewHolder.progressView15.setType(GeometricProgressView.TYPE.TRIANGLE);
            mViewHolder.progressView15.setFigurePaddingInDp(1);
            mViewHolder.progressView15.setNumberOfAngles(30);
            mViewHolder.progressView15.setVisibility(View.VISIBLE);*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
            //mViewHolder.progressView15.setVisibility(View.INVISIBLE);
            //  imageView.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onResume() {
        super.onResume();

        CallAPI callAPI = new CallAPI();
        // Log.d("PaymentFragment","Rest getClass().getName().toString() "+getClass().getName().toString());
        callAPI.updateToolbarText(FragmentTag.FRAGMENT_CENTRAL_GRILL.getFragmentTag(),(AppCompatActivity)getActivity());
    }
    private void addToCart(String URL, final Map<String, String> params) {
        String response = "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;

                        try {
                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                Fragment fragment = new CartFragment();
                                Bundle args = new Bundle();
                                args.putString("food_id", food_id );
                                args.putString("customer_id" ,customer_id);
                                args.putString("outlet_id", outlet_id);
                                args.putString("quantity", quantity);
                                fragment.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

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
                       // Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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


    }
}
