package com.lognsys.toodit.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.lognsys.toodit.R;

import net.bohush.geometricprogressview.GeometricProgressView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 04-03-2017.
 */

public class FragmentsListOfItems extends Fragment {

    FragmentsListOfItems.MyBaseAdapter.MyViewHolder mViewHolder;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list_of_item, container, false);
        ArrayList<ListFoodItem> value = getArguments().getParcelableArrayList("listFoodItems");
        ListView lvListOfItems = (ListView) v.findViewById(R.id.lvListOfItem);
        MyBaseAdapter myBaseAdapter=new MyBaseAdapter(getActivity(),value);
        lvListOfItems.setAdapter(myBaseAdapter);


        lvListOfItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment1 = new Fragment();
                Fragment fragment = new FragmentCentralGrill();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });
        return v;
    }


    public class MyBaseAdapter extends BaseAdapter {

        private ArrayList<ListFoodItem> myList = new ArrayList<ListFoodItem>();
        LayoutInflater inflater;
        Context context = getActivity();


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
            //FragmentsListOfItems.MyBaseAdapter.MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_raw_outlet_items, parent, false);
                mViewHolder = new FragmentsListOfItems.MyBaseAdapter.MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (FragmentsListOfItems.MyBaseAdapter.MyViewHolder) convertView.getTag();
            }

            ListFoodItem currentListData = getItem(position);

            mViewHolder.tvOutletItemName.setText( currentListData.getName());
            Log.e("check", currentListData.getCategory_name());
            mViewHolder.tvOutletItemCategory.setText(currentListData.getCategory_name());
            mViewHolder.tvOutletItemType.setText(currentListData.getFood_type() );
            mViewHolder.tvOutletItemDescription.setText(currentListData.getDescription());
            new ImageLoadTask(currentListData.getImage(),mViewHolder.ivOutletItemImage ).execute();
           // Log.e("check",currentListData.getImage());
           // mViewHolder.ivOutletItemImage.setText("\"" + currentListData.getDescription() + "\"");

            return convertView;
        }

        private class MyViewHolder {
            TextView  tvOutletItemCategory,  tvOutletItemName, tvOutletItemType, tvOutletItemDescription;
            ImageView ivOutletItemImage;
            GeometricProgressView progressView15;


            public MyViewHolder(View item) {

                ivOutletItemImage =(ImageView)item.findViewById(R.id.ivOutletItemImage);
                //tvOutletItemName==(TextView)item.findViewById(R.id.tvOutletItemName);
                tvOutletItemCategory=(TextView)item.findViewById(R.id.tvOutletItemCategory);
                tvOutletItemName=(TextView)item.findViewById(R.id.tvOutletItemName);
                tvOutletItemType=(TextView)item.findViewById(R.id.tvOutletItemType);
                tvOutletItemDescription=(TextView)item.findViewById(R.id.tvOutletItemDescription);
                progressView15 = (GeometricProgressView) item.findViewById(R.id.progressView15);


            }
        }


    }
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
            mViewHolder.progressView15.setType(GeometricProgressView.TYPE.TRIANGLE);
            mViewHolder.progressView15.setFigurePaddingInDp(1);
            mViewHolder.progressView15.setNumberOfAngles(30);
            mViewHolder.progressView15.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
            mViewHolder.progressView15.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);

        }

    }


}
