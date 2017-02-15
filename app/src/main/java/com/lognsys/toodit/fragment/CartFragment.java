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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lognsys.toodit.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //by Akhilesh
    String[] item={"Tomato Garlic Pizza", "Cheese Veg Burger"};
    String[] qualit={"With added cheasy cream","With added cheasy cream" };
    String[] quant={"Qty :1", "Qty :3"};
    String[] amoun={" 50", " 250"};
    ListView li;
    int [] image={R.drawable.tomato12,R.drawable.vegitable12};
   ItemSelectedListAdapter pwd;
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

        li=(ListView)v.findViewById(R.id.lvCart);
        //CartFragment.PasswordAdapter pwd=new CartFragment().PasswordAdapter(getActivity(),R.layout.list_raw_cart,item, qualit, quant, amoun);
        ItemSelectedListAdapter pwd=new ItemSelectedListAdapter(getActivity(),R.layout.list_raw_cart,item, qualit, quant, amoun);
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
    class ItemSelectedListAdapter extends ArrayAdapter {

        public ItemSelectedListAdapter(Context context, int resource, String[] item, String[] qualit, String[] quant, String[] amoun) {
            super(context, resource, item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v=convertView;
            if (convertView== null) {
                v=((Activity)getContext()).getLayoutInflater().inflate(R.layout.list_raw_cart,null);
            }
            LinearLayout llAmount=(LinearLayout) v.findViewById(R.id.llTotalAmount);

            llAmount.setVisibility(View.GONE);

            TextView subTotal=(TextView) v.findViewById(R.id.tvSubTotal);
            TextView      taxes=(TextView) v.findViewById(R.id.tvTaxes);
            TextView      Total=(TextView) v.findViewById(R.id.tvTotal);
            Button makePayment=(Button)v.findViewById(R.id.btnMakePayment) ;
            ImageView cancelSelection=(ImageView)v.findViewById(R.id.ivCancelSelection);
            TextView temName = (TextView) v.findViewById(R.id.tvItemName);
            TextView itemQuality = (TextView) v.findViewById(R.id.tvItemQuality);
            final EditText itemQuantity = (EditText) v.findViewById(R.id.tvItemQuantity);
            TextView itemAmount = (TextView) v.findViewById(R.id.tvAmount);
            TextView cusstomise = (TextView) v.findViewById(R.id.tvCustomise);

            int itemPos = li.getCount();
            if(position == itemPos-1)
            {
                llAmount.setVisibility(View.VISIBLE);

            }

           itemQuantity.addTextChangedListener(new TextWatcher() {

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
                                                           itemQuantity.setText("Qty :");
//                                                           Selection.setSelection(itemQuantity.getText(), itemQuantity
//                                                                   .getText().length());

                                                       }

                                                   }
                                               });

            temName.setText(item[position]);
            itemQuality.setText(qualit[position]);
            itemQuantity.setText(quant[position]);
            itemAmount.setText(amoun[position]);

            String next = "<font color='#CCCCCC'>Rs. </font>";

            subTotal.setText(Html.fromHtml(next+"261.00"));
            taxes.setText(Html.fromHtml(next+"31.00"));
            Total.setText(Html.fromHtml(next+"291.00"));
            //CircleImageView img = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.ivItemInCart);

            //img.setBackgroundResource(image[position]);
            Bitmap bitmap = BitmapFactory.decodeResource(CartFragment.this.getResources(),image[position]);
            Bitmap circularBitmap = new ImageConverter().getRoundedCornerBitmap(bitmap, 100);

            ImageView img = (ImageView) v.findViewById(R.id.ivItemInCart);
            img.setImageBitmap(circularBitmap);
            cancelSelection.setTag(position);
            final int posison=position;
            cancelSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {





                }
            });

            return v;
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

}

