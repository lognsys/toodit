package com.lognsys.toodit.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.Constants;
import com.lognsys.toodit.util.FragmentTag;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 07-03-2017.
 */

public class FragmentFastFood extends Fragment {

    private ListView lvComment;
    Context context = null;
    FragmentFastFood.MyBaseAdapter myBaseAdapter;
    private static int noOfItems=1;
    private BottomNavigationViewEx mBottomNav;
    private int mSelectedItem; //index of bottom nagivation bar
    private ArrayList<ListFoodItem> myList = new ArrayList<ListFoodItem>();
    private ArrayList<ListFoodItem> value;
    private ArrayList<ListFoodItem> valueFinal;
    private String food_id, quantity, customer_id, outlet_id;
    private TextView tvNoItems;
    /*String[] item = {"Tomato Garlic Special Pizza", "Cheesy onion Veg Burger"};
    String[] qualit = {"With added cheasy cream", "With extra butter chease"};
    String[] amount={"61.00", "61.00"};
    int[] image = {R.drawable.tomato_cheese, R.drawable.cheese_burger};*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        valueFinal= new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_new_list_of_item, container, false);
        value = getArguments().getParcelableArrayList("ListFoodItems");
        //Initialize SharedPreferences
       SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //SharedPreferences Editor
        SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        sharedPrefEditor.putString("outlet_id", value.get(0).getOutlet_id().toString());
        sharedPrefEditor.commit();
        tvNoItems=(TextView)v.findViewById(R.id.tvNoItems);
        for(int i=0;i<value.size();i++)
        {
           if(value.get(i).getCategory_name().equals("Pizza")||value.get(i).getCategory_name().equals("test")||value.get(i).getCategory_name().equals("Dessert"))
           {
               valueFinal.add(value.get(i));
           }
        }
        if(valueFinal.size()==0)
        {
            tvNoItems.setVisibility(View.VISIBLE);
            tvNoItems.setText("No Fast Food");

        }

        context = this.getActivity();
        lvComment= (ListView)v.findViewById(R.id.lvComment);
        //getDataInList();
        myBaseAdapter= new FragmentFastFood.MyBaseAdapter(this.getActivity(), valueFinal);

        lvComment.setAdapter(myBaseAdapter);
        lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {  Fragment fragment = new FragmentCentralGrill();
                Bundle args = new Bundle();
                args.putString("position", String.valueOf(position));
                args.putString("food_id" ,valueFinal.get(position).getFood_id());
                args.putString("outlet_id", valueFinal.get(position).getOutlet_id());
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                //  Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();



            }
        });




        return v;
    }

   /* private void getDataInList() {
        for (int i = 0;i<item .length; i++) {
            // Create a new object for each list item
            ListFoodItem ld = new ListFoodItem();
            ld.setItemName(item [i]);
            ld.setItemDescription(qualit[i]);
            ld.setImage(image[i]);
            ld.setAmount(amount[i]);
            // Add this object into the ArrayList myList
            myList.add(ld);


        }
    }*/


    public class MyBaseAdapter extends BaseAdapter {

        private  ArrayList<ListFoodItem> myList = new ArrayList<ListFoodItem>();
        LayoutInflater inflater;
        Context context=getActivity();


        public MyBaseAdapter(Context context, ArrayList<ListFoodItem> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListFoodItem getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final FragmentFastFood.MyBaseAdapter.MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fragment_litem_list, parent, false);
                mViewHolder = new FragmentFastFood.MyBaseAdapter.MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (FragmentFastFood.MyBaseAdapter.MyViewHolder) convertView.getTag();
            }
            ListFoodItem currentListFoodItem = getItem(position);
           // Picasso.with(getActivity()).load(currentListFoodItem.getImage()).into(mViewHolder.llItemImage.setBackground());
            Picasso.with(getActivity()).load(currentListFoodItem.getImage()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (Build.VERSION.SDK_INT < 16) {

                    } else {
                        mViewHolder.llItemImage.setBackground(new BitmapDrawable(bitmap));
                    }

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }

            });

            new FragmentFastFood.ImageLoadTask(currentListFoodItem.getImage(),mViewHolder.llItemImage ).execute();
            //mViewHolder.llItemImage.setBackground(currentListFoodItem.getImage(),mViewHolder.llItemImage ).execute();
            mViewHolder.tvItemName.setText(currentListFoodItem.getName());
            // Log.e("check",currentListFoodItem.getProfileName());
            mViewHolder.tvItemQuality.setText(currentListFoodItem.getFood_type());

            mViewHolder.tvAmount.setText(currentListFoodItem.getPrice());
            mViewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do something when the corky2 is clicked
                    noOfItems++;
                    mViewHolder.tvNoOfAddedItems.setText(String.valueOf(noOfItems));
                }
            });
            mViewHolder.iivMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do something when the corky2 is clicked
                    if(noOfItems>1)
                        noOfItems--;
                    mViewHolder.tvNoOfAddedItems.setText(String.valueOf(noOfItems));
                }
            });
            mViewHolder.ivAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap<String, String> hashMap= new HashMap<String, String>();
                    hashMap.put("food_id", valueFinal.get(position).getFood_id());
                   // Log.e("food_id", valueFinal.get(position).getFood_id());
                    food_id=valueFinal.get(position).getFood_id();
                    hashMap.put("quantity", mViewHolder.tvNoOfAddedItems.getText().toString());
                    //Log.e("quantity", mViewHolder.tvNoOfAddedItems.getText().toString());
                    quantity=mViewHolder.tvNoOfAddedItems.getText().toString();
                   // SharedPreferences userDetails = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    hashMap.put("customer_id",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", ""));
                    Log.e("cust_id",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", ""));
                    customer_id=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", "");
                    hashMap.put("outlet_id", valueFinal.get(position).getOutlet_id());
                    outlet_id= valueFinal.get(position).getOutlet_id();
                    Log.e("otlet id",valueFinal.get(position).getOutlet_id());

                    addToCart("http://food.swatinfosystem.com/api/Add_to_cart", hashMap);

                }
            });
            return convertView;
        }

        private class MyViewHolder {
            private ImageView ivAdd, iivMinus, ivAddToCart;
           // mViewHolder.tvNoOfAddedItems.setText(String.valueFinalOf(noOfItems));
            private TextView tvItemName, tvItemQuality, tvNoOfAddedItems, tvAmount;
            private RatingBar rbRating;
            private LinearLayout llItemImage;


            public MyViewHolder(View item) {
                rbRating=(RatingBar)item.findViewById(R.id.rbRating) ;
                ivAdd=(ImageView) item.findViewById(R.id.ivAdd) ;
                iivMinus=(ImageView) item.findViewById(R.id.ivMinus) ;
                tvNoOfAddedItems=(TextView) item.findViewById(R.id.tvNoOfAddedItem);
                tvItemName=(TextView)item.findViewById(R.id.tvItemName);
                tvItemQuality=(TextView)item.findViewById(R.id.tvItemQuality);
                llItemImage=(LinearLayout)item.findViewById(R.id.llItemImage);
                tvAmount=(TextView)item.findViewById(R.id.tvAmount);
                ivAddToCart=(ImageView)item.findViewById(R.id.ivAddTo_cart);

            }
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        //myList.clear();
    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private LinearLayout imageView;

        public ImageLoadTask(String url, LinearLayout imageView) {
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
        protected void onProgressUpdate(Void... valueFinals) {
            super.onProgressUpdate(valueFinals);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result!=null) {
                imageView.setBackgroundDrawable(new BitmapDrawable(result));
                imageView.setVisibility(View.VISIBLE);
            }
            else {
                //mViewHolder.progressView15.setVisibility(View.INVISIBLE);
                imageView.setBackgroundResource(R.drawable.tomato_cheese);
                imageView.setVisibility(View.VISIBLE);

            }

        }
    }
    private ArrayList<ListFoodItem> addToCart(String URL, final Map<String, String> params) {
        String response = "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
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
        return myList;

    }




    }



