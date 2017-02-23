package com.lognsys.toodit.fragment;

import android.app.Activity;
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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lognsys.toodit.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<ListDataCartFragments> myList = new ArrayList<ListDataCartFragments>();
    NotificationFragment.MyBaseAdapter myBaseAdapter;
    //by Akhilesh
    String[] item = {"Tomato Garlic Pizza", "Cheese Veg Burger"};
    String[] qualit = {"With added cheasy cream", "With added cheasy cream"};
    String[] quant = {"Qty :1", "Qty :3"};
    String[] amoun = {" 50", " 250"};
    ListView li;
    int[] image = {R.drawable.tomato12, R.drawable.vegitable12};
    MyBaseAdapter pwd;
//

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        li = (ListView) v.findViewById(R.id.lvCart);
        getDataInList();

        //CartFragment.PasswordAdapter pwd=new CartFragment().PasswordAdapter(getActivity(),R.layout.list_raw_cart,item, qualit, quant, amoun);
        MyBaseAdapter pwd = new MyBaseAdapter(getActivity(), myList);
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

        private  ArrayList<ListDataCartFragments> myList = new ArrayList<ListDataCartFragments>();
        LayoutInflater inflater;
        Context context=getActivity();


        public MyBaseAdapter(Context context, ArrayList<ListDataCartFragments> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListDataCartFragments getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_raw_cart, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }


            mViewHolder.llAmount.setVisibility(View.GONE);



            int itemPos = li.getCount();
            if (position == itemPos - 1) {
                mViewHolder.llAmount.setVisibility(View.VISIBLE);

            }

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
            ListDataCartFragments currentListData = getItem(position);
            mViewHolder.temName.setText(currentListData.getCartItemSelected());
            mViewHolder.itemQuality.setText(currentListData.getCartItemComment());
            mViewHolder.itemQuantity.setText(currentListData.getCartItemquanatity());
            mViewHolder.itemAmount.setText(currentListData.getCartItemPrice());

            String next = "<font color='#CCCCCC'>Rs. </font>";

            mViewHolder.subTotal.setText(Html.fromHtml(next + "261.00"));
            mViewHolder.taxes.setText(Html.fromHtml(next + "31.00"));
            mViewHolder.Total.setText(Html.fromHtml(next + "291.00"));
            //CircleImageView img = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.ivItemInCart);

            //img.setBackgroundResource(image[position]);
            Bitmap bitmap = BitmapFactory.decodeResource(CartFragment.this.getResources(), currentListData.getCartItemImage());
            Bitmap circularBitmap = new ImageConverter().getRoundedCornerBitmap(bitmap, 100);


            mViewHolder.img.setImageBitmap(circularBitmap);
            mViewHolder.cancelSelection.setTag(position);
           // final int posison = position;
            mViewHolder.cancelSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer position= (Integer) v.getTag(); //get inde
                     int deletePosition=(int)position;
                    myList.remove(deletePosition); //remove the item from data source
                    Log.e("myList Size", String.valueOf(myList.size()));
                    notifyDataSetChanged(); //notify to refresh
                   // li.setAdapter( new MyBaseAdapter(getActivity(), myListMain));
                }
            });

            return convertView;
        }

        private class MyViewHolder {
            TextView subTotal, taxes, Total, temName, itemAmount, cusstomise, itemQuality;
            Button makePayment;
            ImageView cancelSelection, img;
            EditText itemQuantity;
            LinearLayout llAmount;

            public MyViewHolder(View item) {

                subTotal = (TextView) item.findViewById(R.id.tvSubTotal);
                taxes = (TextView) item.findViewById(R.id.tvTaxes);
                Total = (TextView) item.findViewById(R.id.tvTotal);
                makePayment = (Button) item.findViewById(R.id.btnMakePayment);
                cancelSelection = (ImageView) item.findViewById(R.id.ivCancelSelection);
                img = (ImageView) item.findViewById(R.id.ivItemInCart);
                temName = (TextView) item.findViewById(R.id.tvItemName);
                itemQuality = (TextView) item.findViewById(R.id.tvItemQuality);
                itemQuantity = (EditText) item.findViewById(R.id.tvItemQuantity);
                itemAmount = (TextView) item.findViewById(R.id.tvAmount);
                cusstomise = (TextView) item.findViewById(R.id.tvCustomise);
                llAmount = (LinearLayout) item.findViewById(R.id.llTotalAmount);

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
            ListDataCartFragments ld = new ListDataCartFragments();
            ld.setCartItemImage(image[i]);
            ld.setCartItemSelected(item[i]);
            ld.setCartItemComment(qualit[i]);
            ld.setCartItemPrice(amoun[i]);
            ld.setCartItemquanatity(quant[i]);

            // Add this object into the ArrayList myList
            myList.add(ld);


        }
    }

}

