package com.lognsys.toodit.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import  com.lognsys.toodit.fragment.ListDataNotification;
import com.lognsys.toodit.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TEXT = "arg_text";
    private static final String ARG_COLOR = "arg_color";
    private ListView lvNotification;
    private ArrayList<ListDataNotification> myList = new ArrayList<ListDataNotification>();
    MyBaseAdapter myBaseAdapter;
    String orderno="#TDT45675";
    String[] notification = new String[]{
            "Your order <font color='#EE0000'>"+orderno+"</font> has been placed ", "You have order <font color='#EE0000'>Cheesy Garlic Pizza</font> from <font color='#EE0000'>Dominos Pizza</font> ",
            "Your order <font color='#EE0000'>TDT213145</font> has been delivered via FoodCart ", "You have ordered <font color='#EE0000'>Maharaja Mac Burger</font> from <font color='#EE0000'>McDonald's</font> ",
            "Your order <font color='#EE0000'>TDT213145</font> has been delivered via FoodCart ", "You have ordered <font color='#EE0000'>Maharaja Mac Burger</font> from <font color='#EE0000'>McDonald's</font> "


    };
    String[] notificationDate = new String[]{
            "11:20 am, 09 Feb 2017", "11:20 am, 09 Feb 2017","10:20 am, 08 Feb 2017","10:20 am, 08 Feb 2017","10:20 am, 08 Feb 2017","10:20 am, 08 Feb 2017"};

    int [] image={R.drawable.tomato12,R.drawable.vegitable12,R.drawable.tomato12,R.drawable.vegitable12,R.drawable.tomato12,R.drawable.vegitable12};

    private String mText;
    private int mColor;

    private View mContent;
    private TextView mTextView;

    // private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param text  Parameter 1.
     * @param color Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String text, int color) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mText = getArguments().getString(ARG_TEXT);
            mColor = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        getDataInList();
          lvNotification=(ListView)v.findViewById(R.id.lvNotification);
        myBaseAdapter= new MyBaseAdapter(this.getActivity(), myList);
        lvNotification.setAdapter(myBaseAdapter);
        // Inflate the layout for this fragment
        return v;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve text and color from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
           // mText = args.getString(ARG_TEXT);
           // mColor = args.getInt(ARG_COLOR);
        } else {
           // mText = savedInstanceState.getString(ARG_TEXT);
           // mColor = savedInstanceState.getInt(ARG_COLOR);
        }

        // initialize views
        mContent = view.findViewById(R.id.fragment_notify);
        mTextView = (TextView) view.findViewById(R.id.text);

        // set text and background color
        mTextView.setText(mText);
        mContent.setBackgroundColor(mColor);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARG_TEXT, mText);
        outState.putInt(ARG_COLOR, mColor);
        super.onSaveInstanceState(outState);
    }
    public class MyBaseAdapter extends BaseAdapter {

        private  ArrayList<ListDataNotification> myList = new ArrayList<ListDataNotification>();
        LayoutInflater inflater;
        Context context=getActivity();


        public MyBaseAdapter(Context context, ArrayList<ListDataNotification> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListDataNotification getItem(int position) {
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
                convertView = inflater.inflate(R.layout.list_raw_notification, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            ListDataNotification currentListData = getItem(position);

           mViewHolder.tvNotification.setText(Html.fromHtml(currentListData.getNotification()));
            Log.e("check",currentListData.getNotification());
            mViewHolder.tvNotificationDate.setText(Html.fromHtml(currentListData.getNotificationdate()));
            mViewHolder.ivNotifyImage.setImageResource(currentListData.getImage());

            return convertView;
        }

        private class MyViewHolder {
            TextView tvNotification, tvNotificationDate;
            ImageView ivNotifyImage;


            public MyViewHolder(View item) {
                tvNotification = (TextView) item.findViewById(R.id.tvNotification);
                tvNotificationDate = (TextView) item.findViewById(R.id.tvNotificationDate);
                ivNotifyImage=(ImageView)item.findViewById(R.id.ivNotifyImage);

            }
        }


    }
    private void getDataInList() {
        for (int i = 0;i<notification.length; i++) {
            // Create a new object for each list item
            ListDataNotification ld = new ListDataNotification();
            ld.setNotification(notification[i]);
            ld.setNotificationdate(notificationDate[i]);
            ld.setImage(image[i]);

            // Add this object into the ArrayList myList
            myList.add(ld);


        }
    }

}
