package com.lognsys.toodit.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
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

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lognsys.toodit.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
   /* private ArrayList<ListFoodItem> myList = new ArrayList<ListFoodItem>();

    String[] item = {"Tomato Garlic Special Pizza", "Cheesy onion Veg Burger"};
    String[] qualit = {"With added cheasy cream", "With extra butter chease"};
    String[] amount={"61.00", "61.00"};
    int[] image = {R.drawable.tomato_cheese, R.drawable.cheese_burger};*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_new_list_of_item, container, false);
        final ArrayList<ListFoodItem> value = getArguments().getParcelableArrayList("ListFoodItems");
        context = this.getActivity();
        lvComment= (ListView)v.findViewById(R.id.lvComment);
        //getDataInList();
        myBaseAdapter= new FragmentFastFood.MyBaseAdapter(this.getActivity(), value);

        lvComment.setAdapter(myBaseAdapter);
        lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
             //  Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                Fragment fragment = new FragmentCentralGrill();
                Bundle args = new Bundle();
                args.putString("position", String.valueOf(position));
                args.putString("food_id" ,value.get(position).getFood_id());
                args.putString("outlet_id", value.get(position).getOutlet_id());
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


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
        public View getView(int position, View convertView, ViewGroup parent) {
            final FragmentFastFood.MyBaseAdapter.MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.fragment_litem_list, parent, false);
                mViewHolder = new FragmentFastFood.MyBaseAdapter.MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (FragmentFastFood.MyBaseAdapter.MyViewHolder) convertView.getTag();
            }

            ListFoodItem currentListFoodItem = getItem(position);
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
            return convertView;
        }

        private class MyViewHolder {
            private ImageView ivAdd, iivMinus, ivLogbook;
           // mViewHolder.tvNoOfAddedItems.setText(String.valueOf(noOfItems));
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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setBackgroundDrawable(new BitmapDrawable(result));
            //mViewHolder.progressView15.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);

        }
    }

    }



