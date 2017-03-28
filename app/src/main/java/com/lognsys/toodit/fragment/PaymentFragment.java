package com.lognsys.toodit.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lognsys.toodit.R;
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.FragmentTag;

public class PaymentFragment extends Fragment {

   private static final String ARG_NAME = "username";
   private static final String ARG_LOC = "location";
   private String name;
   private String loc;
    private Button logout;
    private RadioGroup rgContainer;
    private TextView txt_price;
    private RadioButton radioButton;
    private Button btnProceed;
    String totalAmount;
    String fragTag;
   public PaymentFragment() {
       // Required empty public constructor

   }
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         name = getArguments().getString(ARG_NAME);
         loc = getArguments().getString(ARG_LOC);
      }
   }
   @Override
   public View onCreateView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
      //Inflate the layout for this fragment
       setHasOptionsMenu(true);
      View rootview =inflater.inflate(R.layout.fragment_payment, container, false);
       totalAmount    = getArguments().getString("totalAmount");
       CallAPI callAPI = new CallAPI();
       Log.d("PaymentFragment","Rest getClass().getName().toString() "+getClass().getName().toString());
       fragTag=FragmentTag.FRAGMENT_PAYMENT.getFragmentTag();
       callAPI.updateToolbarText(fragTag,(AppCompatActivity)getActivity());
       initView(rootview);
       return rootview;
   }
    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_fragment_title);
    }
    private void initView(final View rootview) {
        rgContainer = (RadioGroup)rootview.findViewById(R.id.rgContainer);
        btnProceed = (Button) rootview.findViewById(R.id.btnProceed);
        txt_price= (TextView) rootview.findViewById(R.id.txt_price);
        txt_price.setText(totalAmount);
//        Log.d("PaymentFragment","Rest totalAmount "+totalAmount);

        btnProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
//                Log.d("PaymentFragment","Rest onClick totalAmount "+totalAmount);

                    totalAmount=totalAmount.substring(1);
//                    Log.d("PaymentFragment","Rest totalAmount onClick after substring "+totalAmount);

               double price=Double.parseDouble(totalAmount.toString().trim());
//                Log.d("PaymentFragment","Rest onClick price "+price);
                if(price>0){
                    // get selected radio button from radioGroup
                    int selectedId = rgContainer.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioButton = (RadioButton)rootview.findViewById(selectedId);

                  //  Toast.makeText(getContext(),radioButton.getText(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                    Log.d("PaymentFragment","Rest onClick Exception "+e);

                }
            }

        });

    }
}
