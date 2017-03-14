package com.lognsys.toodit.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.app.Fragment;
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
import com.lognsys.toodit.R;

import java.util.ArrayList;

/**
 * Created by admin on 04-02-2017.
 */

public class FragmentCentralGrill extends Fragment {
   private ListView lvComment;
    private ImageView ivItemImage, ivAdd, iivMinus, ivLogbook;
    private  TextView tvItemName, tvItemDescript, tvNoOfAddedItems, tvThecentralgrill, tvAddress;
    private RatingBar rbRating;
     Context context = null;
    MyBaseAdapter myBaseAdapter;
    private static int noOfItems=1;

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
        ivItemImage=(ImageView)v.findViewById(R.id.ivItemImage);
        tvItemName=(TextView)v.findViewById(R.id.tvItemName);
        tvItemDescript=(TextView)v.findViewById(R.id.tvItemQuality) ;
        lvComment=(ListView)v.findViewById(R.id.lvComment);
        rbRating=(RatingBar)v.findViewById(R.id.rbRating) ;
        ivAdd=(ImageView) v.findViewById(R.id.ivAdd) ;
        iivMinus=(ImageView) v.findViewById(R.id.ivMinus) ;
        tvNoOfAddedItems=(TextView) v.findViewById(R.id.tvNoOfAddedItem);
       /* Drawable drawable = rbRating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFAF0A"), PorterDuff.Mode.SRC_ATOP);*/
        getDataInList();
        myBaseAdapter= new MyBaseAdapter(this.getActivity(), myList);
        new Helper().getListViewSize(lvComment);
//Retrieve the value
        String value = getArguments().getString("position");
        if(value.equals("1")){
            ivItemImage.setImageResource(R.drawable.cheese_burger);
            tvItemName.setText("Cheesy Onion Veg Burger");
            tvItemDescript.setText("With extra buter cheese");

        }
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
                if(noOfItems>1)
                noOfItems--;
                tvNoOfAddedItems.setText(String.valueOf(noOfItems));
            }
        });

        return v;
    }

    private void getDataInList() {
        for (int i = 0;i<profileName.length; i++) {
            // Create a new object for each list item
            ListData ld = new ListData();
            ld.setComment(comment[i]);
            ld.setProfileName(profileName[i]);

            // Add this object into the ArrayList myList
            myList.add(ld);


        }
    }


    public class MyBaseAdapter extends BaseAdapter {

      private  ArrayList<ListData> myList = new ArrayList<ListData>();
        LayoutInflater inflater;
        Context context=getActivity();


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

            mViewHolder.tvProfileName.setText("- "+currentListData.getProfileName());
            Log.e("check",currentListData.getProfileName());
            mViewHolder.tvComment.setText("\""+currentListData.getComment()+"\"");

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
        public  void getListViewSize(ListView myListView) {
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
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_map);
        item.setVisible(false);
    }
}
