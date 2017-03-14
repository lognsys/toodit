package com.lognsys.toodit.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lognsys.toodit.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 10-03-2017.
 */

public class FragmentMyOrders extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<ListDataMyOrders> myList = new ArrayList<ListDataMyOrders>();
    NotificationFragment.MyBaseAdapter myBaseAdapter;
    //by Akhilesh
    String[] item = {"Tomato Garlic Pizza", "Cheese Veg Burger"};
    String[] qualit = {"With added cheasy cream", "With added cheasy cream"};
    String[] quant = {"Qty :1", "Qty :3"};
    String[] amoun = {" 50", " 250"};
    ListView li;
    int[] image = {R.drawable.tomato12, R.drawable.vegitable12};
    FragmentMyOrders.MyBaseAdapter pwd;
//

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMyOrders.OnFragmentInteractionListener mListener;

    public FragmentMyOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMyOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMyOrders newInstance(String param1, String param2) {
        FragmentMyOrders fragment = new FragmentMyOrders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_order_history, container, false);



        li = (ListView) v.findViewById(R.id.lvMyOrders);
        getDataInList();

        //FragmentMyOrders.PasswordAdapter pwd=new FragmentMyOrders().PasswordAdapter(getActivity(),R.layout.list_raw_cart,item, qualit, quant, amoun);
        FragmentMyOrders.MyBaseAdapter pwd = new FragmentMyOrders.MyBaseAdapter(getActivity(), myList);
        li.setAdapter(pwd);


        return v;
        // return inflater.inflate(R.layout.fragment_cart, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

  /*  @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class MyBaseAdapter extends BaseAdapter {

        private  ArrayList<ListDataMyOrders> myList = new ArrayList<ListDataMyOrders>();
        LayoutInflater inflater;
        Context context=getActivity();


        public MyBaseAdapter(Context context, ArrayList<ListDataMyOrders> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListDataMyOrders getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final FragmentMyOrders.MyBaseAdapter.MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_order_history, parent, false);
                mViewHolder = new FragmentMyOrders.MyBaseAdapter.MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (FragmentMyOrders.MyBaseAdapter.MyViewHolder) convertView.getTag();
            }

            mViewHolder.llRateClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewHolder.llRateAndReview.setVisibility(View.VISIBLE);
                    mViewHolder.llRateClick.setVisibility(View.GONE);
                }
            });
            mViewHolder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewHolder.llRateAndReview.setVisibility(View.GONE);
                    mViewHolder.llRateClick.setVisibility(View.VISIBLE);
                }
            });

            mViewHolder.itemQuantity.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().startsWith("Qty :")) {
                        mViewHolder.itemQuantity.setText("Qty :");
//                                                           Selection.setSelection(itemQuantity.getText(), itemQuantity
//                                                                   .getText().length());

                    }

                }
            });
            ListDataMyOrders currentListDataMyOrders = getItem(position);
            mViewHolder.temName.setText(currentListDataMyOrders.getOrderId());
            mViewHolder.itemQuality.setText(currentListDataMyOrders.getOrderSummery());
            mViewHolder.itemQuantity.setText(currentListDataMyOrders.getOrderItemQuantity());
            mViewHolder.itemAmount.setText(currentListDataMyOrders.getOrderItemPrice());

            String next = "<font color='#CCCCCC'>Rs. </font>";


            //CircleImageView img = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.ivItemInCart);

            //img.setBackgroundResource(image[position]);
            Bitmap bitmap = BitmapFactory.decodeResource(FragmentMyOrders.this.getResources(), currentListDataMyOrders.getOrderItemImage());
            Bitmap circularBitmap = new FragmentMyOrders.ImageConverter().getRoundedCornerBitmap(bitmap, 100);


            mViewHolder.img.setImageBitmap(circularBitmap);

            return convertView;
        }

        private class MyViewHolder {
            TextView subTotal, taxes, Total, temName, itemAmount, cusstomise, itemQuality,itemQuantity;
            Button makePayment, btnDone;
            ImageView cancelSelection, img;
            LinearLayout llAmount, llRateClick,llRateAndReview;
            RatingBar rbRating;

            public MyViewHolder(View item) {
                
                img = (ImageView) item.findViewById(R.id.ivItemInCart);
                temName = (TextView) item.findViewById(R.id.tvItemName);
                itemQuality = (TextView) item.findViewById(R.id.tvItemQuality);
                itemQuantity = (TextView) item.findViewById(R.id.tvItemQuantity);
                itemAmount = (TextView) item.findViewById(R.id.tvAmount);
                cusstomise = (TextView) item.findViewById(R.id.tvConfirm);
                rbRating = (RatingBar)item. findViewById(R.id.rbRating);
                llRateClick=(LinearLayout)item.findViewById(R.id.llRateClick);
                llRateAndReview=(LinearLayout)item.findViewById(R.id.llRateAndReview) ;
                btnDone=(Button)item.findViewById(R.id.btnDone);
            }
        }
    }

    public class ImageConverter {

        public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }



    private void getDataInList() {
        for (int i = 0; i < image.length; i++) {
            // Create a new object for each list item
            ListDataMyOrders ld = new ListDataMyOrders();
            ld.setOrderItemImage(image[i]);
            ld.setOrderId(item[i]);
            ld.setOrderSummery(qualit[i]);
            ld.setOrderItemPrice(amoun[i]);
            ld.setOrderItemQuantity(quant[i]);

            // Add this object into the ArrayList myList
            myList.add(ld);


        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_map);
        item.setVisible(false);
    }
}
