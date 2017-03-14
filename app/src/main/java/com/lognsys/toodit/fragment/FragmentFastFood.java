package com.lognsys.toodit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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
    private ArrayList<ListNewItems> myList = new ArrayList<ListNewItems>();

    String[] item = {"Tomato Garlic Special Pizza", "Cheesy onion Veg Burger"};
    String[] qualit = {"With added cheasy cream", "With extra butter chease"};
    String[] amount={"61.00", "61.00"};
    int[] image = {R.drawable.tomato_cheese, R.drawable.cheese_burger};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_new_list_of_item, container, false);
        context = this.getActivity();
        lvComment= (ListView)v.findViewById(R.id.lvComment);
        getDataInList();
        myBaseAdapter= new FragmentFastFood.MyBaseAdapter(this.getActivity(), myList);

        lvComment.setAdapter(myBaseAdapter);
        lvComment.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
               Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                Fragment fragment = new FragmentCentralGrill();
                Bundle args = new Bundle();
                args.putString("position", String.valueOf(position));
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


            }
        });




        return v;
    }

    private void getDataInList() {
        for (int i = 0;i<item .length; i++) {
            // Create a new object for each list item
            ListNewItems ld = new ListNewItems();
            ld.setItemName(item [i]);
            ld.setItemDescription(qualit[i]);
            ld.setImage(image[i]);
            ld.setAmount(amount[i]);
            // Add this object into the ArrayList myList
            myList.add(ld);


        }
    }


    public class MyBaseAdapter extends BaseAdapter {

        private  ArrayList<ListNewItems> myList = new ArrayList<ListNewItems>();
        LayoutInflater inflater;
        Context context=getActivity();


        public MyBaseAdapter(Context context, ArrayList<ListNewItems> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListNewItems getItem(int position) {
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

            ListNewItems currentListNewItems = getItem(position);
            mViewHolder.llItemImage.setBackgroundResource(currentListNewItems.getImage());
            mViewHolder.tvItemName.setText(currentListNewItems.getItemName());
            // Log.e("check",currentListNewItems.getProfileName());
            mViewHolder.tvItemDescript.setText(currentListNewItems.getItemDescription());
            mViewHolder.tvAmount.setText(currentListNewItems.getAmount());
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
            return convertView;
        }

        private class MyViewHolder {
            private ImageView ivAdd, iivMinus, ivLogbook;
            private TextView tvItemName, tvItemDescript, tvNoOfAddedItems, tvAmount;
            private RatingBar rbRating;
            private LinearLayout llItemImage;


            public MyViewHolder(View item) {
                rbRating=(RatingBar)item.findViewById(R.id.rbRating) ;
                ivAdd=(ImageView) item.findViewById(R.id.ivAdd) ;
                iivMinus=(ImageView) item.findViewById(R.id.ivMinus) ;
                tvNoOfAddedItems=(TextView) item.findViewById(R.id.tvNoOfAddedItem);
                tvItemName=(TextView)item.findViewById(R.id.tvItemName);
                tvItemDescript=(TextView)item.findViewById(R.id.tvItemQuality);
                llItemImage=(LinearLayout)item.findViewById(R.id.llItemImage);
                tvAmount=(TextView)item.findViewById(R.id.tvAmount);
            }
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        myList.clear();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_map);
        item.setVisible(false);
    }
}
