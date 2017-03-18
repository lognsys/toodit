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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.R;
import com.lognsys.toodit.adapter.OutletRecylerViewHolders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    MyBaseAdapter.MyViewHolder mViewHolder;
    private  String cart_id, sub_total, service_tax, vat, add_vat,total_amount;
    //by Akhilesh
   /* String[] item = {"Tomato Garlic Pizza", "Cheese Veg Burger"};
    String[] qualit = {"With added cheasy cream", "With added cheasy cream"};
    String[] quant = {"Qty :1", "Qty :3"};*/
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
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        li = (ListView) v.findViewById(R.id.lvCart);
        //getDataInList();
        HashMap<String, String> hashMap= new HashMap<String, String>();
        hashMap.put("customer_id","28");
        hashMap.put("outlet_id","1");
        getFoodItemList("http://food.swatinfosystem.com/api/Cart_details", hashMap);
        //CartFragment.PasswordAdapter pwd=new CartFragment().PasswordAdapter(getActivity(),R.layout.list_raw_cart,item, qualit, quant, amoun);

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


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_raw_cart, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }


            mViewHolder.llAmount.setVisibility(View.GONE);
            mViewHolder.cusstomise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.linear_customizetext.setVisibility(View.VISIBLE);
                    v.setVisibility(View.GONE);
                }
            });
            mViewHolder.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewHolder.cusstomise.setText("Customise "+mViewHolder.itemCustomizetext.getText().toString());
                    mViewHolder.linear_customizetext.setVisibility(View.GONE);
                    mViewHolder.cusstomise.setVisibility(View.VISIBLE);
                }
            });

            int itemPos = li.getCount();
            if (position == itemPos - 1) {
                mViewHolder.llAmount.setVisibility(View.VISIBLE);

            }
            String next = "<font color='#CCCCCC'> "+getResources().getString(R.string.rs)+"</font>";
            mViewHolder.subTotal.setText(Html.fromHtml(next + sub_total));
            mViewHolder.taxes.setText(Html.fromHtml(next + String.valueOf(Double.valueOf(service_tax)+Double.valueOf(vat)+Double.valueOf(add_vat))));
            mViewHolder.Total.setText(Html.fromHtml(next +total_amount));
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.quantity_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            mViewHolder.spinnerItemQuantity.setAdapter(adapter);

            ListDataCartFragments currentListData = getItem(position);
            mViewHolder.temName.setText(currentListData.getCartItemSelected());
            mViewHolder.itemQuality.setText(currentListData.getCartItemComment());
//          mViewHolder.itemQuantity.setText(currentListData.getCartItemquanatity());
            mViewHolder.spinnerItemQuantity.setTag(currentListData.getCartItemquanatity());
            mViewHolder.itemAmount.setText(currentListData.getCartItemPrice());


            //CircleImageView img = (de.hdodenhof.circleimageview.CircleImageView)v.findViewById(R.id.ivItemInCart);

            //img.setBackgroundResource(image[position]);
           /* Bitmap bitmap = BitmapFactory.decodeResource(CartFragment.this.getResources(), currentListData.getCartItemImage());
            Bitmap circularBitmap = new ImageConverter().getRoundedCornerBitmap(bitmap, 100);


            mViewHolder.img.setImageBitmap(circularBitmap);*/
            mViewHolder.cancelSelection.setTag(position);
           // final int posison = position;
            mViewHolder.cancelSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer position= (Integer) v.getTag(); //get inde
                     int deletePosition=(int)position;
                    myList.remove(deletePosition); //remove the item from data source
                    Log.e("myList Size", String.valueOf(myList.size()));

                    HashMap<String, String> hashMap= new HashMap<String, String>();
                    hashMap.put("transaction_id","16");
                    hashMap.put("remove_quanity","1");
                    deleteCartItems("http://food.swatinfosystem.com/api/Delete_cart_item", hashMap);
                    notifyDataSetChanged(); //notify to refresh
                   // li.setAdapter( new MyBaseAdapter(getActivity(), myListMain));
                }
            });

            mViewHolder.makePayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String totalAmount =mViewHolder.Total.getText().toString();
                    if(totalAmount!=null && totalAmount.length()>0){

                            Fragment fragment = new PaymentFragment();
                        Bundle args = new Bundle();
                        args.putString("totalAmount", totalAmount);
                        fragment .setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container,
                                        fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


                    }
                    else{
                        Toast.makeText(getContext(),"Please Add Products to Cart",Toast.LENGTH_LONG).show();
                    }

                }
            });
            return convertView;
        }

        private class MyViewHolder {
            TextView subTotal, taxes, Total, temName, itemAmount, cusstomise, itemQuality;
            LinearLayout linear_customizetext;
            Button makePayment,btnDone;
            ImageView cancelSelection, img;
            EditText itemCustomizetext;
            Spinner spinnerItemQuantity;
            LinearLayout llAmount;

            public MyViewHolder(View item) {

                subTotal = (TextView) item.findViewById(R.id.tvSubTotal);
                taxes = (TextView) item.findViewById(R.id.tvTaxes);
                Total = (TextView) item.findViewById(R.id.tvTotal);
                makePayment = (Button) item.findViewById(R.id.btnMakePayment);
                btnDone = (Button) item.findViewById(R.id.btnDone);
                cancelSelection = (ImageView) item.findViewById(R.id.ivCancelSelection);
                img = (ImageView) item.findViewById(R.id.ivItemInCart);
                temName = (TextView) item.findViewById(R.id.tvItemName);
                itemQuality = (TextView) item.findViewById(R.id.tvItemQuality);
                spinnerItemQuantity = (Spinner) item.findViewById(R.id.spinnerItemQuantity);
                itemCustomizetext = (EditText) item.findViewById(R.id.itemCustomizetext);
                itemAmount = (TextView) item.findViewById(R.id.tvAmount);
                cusstomise = (TextView) item.findViewById(R.id.tvCustomise);
                llAmount = (LinearLayout) item.findViewById(R.id.llTotalAmount);
                linear_customizetext = (LinearLayout) item.findViewById(R.id.linear_customizetext);

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

    private ArrayList<ListDataCartFragments> getFoodItemList(String URL, final Map<String, String> params) {
        String response = "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;

                        try {
                            try {
                                JSONArray cartItemArray;
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObject1=jsonObject.getJSONObject("cart_data");
                                cart_id=jsonObject1.getString("cart_id");
                                sub_total=jsonObject1.getString("sub_total");
                                service_tax=jsonObject1.getString("service_tax");
                                vat=jsonObject1.getString("vat");
                                add_vat=jsonObject1.getString("add_vat");
                                total_amount=jsonObject1.getString("total_amount");




                                if(jsonObject.getJSONObject("cart_data").getJSONArray("item_details")!=null){
                                    cartItemArray = jsonObject.getJSONObject("cart_data").getJSONArray("item_details");
                                    for (int i = 0; i < cartItemArray.length(); i++) {
                                        String food_name = cartItemArray.getJSONObject(i).getString("food_name");
                                        String food_type= cartItemArray.getJSONObject(i).getString("food_type");
                                        String price=cartItemArray.getJSONObject(i).getString("price");
                                        String quantity=cartItemArray.getJSONObject(i).getString("price");
                                        String transaction_id=cartItemArray.getJSONObject(i).getString("transaction_id");
                                        ListDataCartFragments listDataCartFragments= new ListDataCartFragments();
                                        listDataCartFragments.setCartItemSelected(food_name);
                                        listDataCartFragments.setCartItemComment(food_type);
                                        listDataCartFragments.setCartItemquanatity(quantity);
                                        listDataCartFragments.setCartItemPrice(price);
                                       // listDataCartFragments.setCartItemImage(image[i]);



                                        // Log.e("mallname", mall);
                                        // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();
                                        myList.add(listDataCartFragments);

                                    }

                                    MyBaseAdapter pwd = new MyBaseAdapter(getActivity(), myList);
                                    li.setAdapter(pwd);


                                }

                                //Log.e("check", countryArray.toString());


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
                        //Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

   /* private void getDataInList() {
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
    }*/
   private ArrayList<ListDataCartFragments> deleteCartItems(String URL, final Map<String, String> params) {
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

                               HashMap<String, String> hashMap= new HashMap<String, String>();
                               hashMap.put("customer_id","1");
                               hashMap.put("outlet_id","1");
                               getFoodItemList("http://food.swatinfosystem.com/api/Cart_details", hashMap);
                               //Log.e("check", countryArray.toString());


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
                       //Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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


