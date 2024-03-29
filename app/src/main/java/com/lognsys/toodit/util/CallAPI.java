package com.lognsys.toodit.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.service.carrier.CarrierService;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.Dialog.NetworkStatusDialog;
import com.lognsys.toodit.LoginActivity;
import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.RegistrationActivity;
import com.lognsys.toodit.fragment.ListData;
import com.lognsys.toodit.fragment.ListMallOutlets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pdoshi on 25/02/17.
 */

public class CallAPI {
    ArrayList<ListMallOutlets> listOfMallOutlets = new ArrayList<>();

    private final String TAG = this.getClass().getName();
    private static final String ARG_TITLE = "title";
    private static final String ARG_MSG = "message";
    private static final String ARG_INTENT = "intent";
    ImageView map_indicator;
    ProgressDialog progressDialog;
    public static String response = "";

    public CallAPI() {

    }

    public void showProgressbar(AppCompatActivity activity) {
        progressDialog = new ProgressDialog(activity,R.style.Theme_MyDialog);
        progressDialog.setMessage("Please Wait for a while....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

    }

    public void hideProgressbar(AppCompatActivity activity) {

        if (progressDialog !=null && progressDialog.isShowing()) {
          Log.d(TAG,"progress dialog hide  is showing "+progressDialog.isShowing());
            progressDialog.dismiss();
        }

    }

    /*
     * @param - input parameters
     *
     */
    public String callCustomerLoginURL(String url, final String username, final String password, final String device_token,
                                       final AppCompatActivity activity) {

        //RequestQueue queue = Volley.newRequestQueue(activity);
        Log.d(TAG,"Rest loging url" +url+"  username "+username+" password "+password+" device_token "+device_token);

        final ProgressDialog progressDialog = new ProgressDialog(activity, R.style.tooditDialog);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
        progressDialog.setMessage("Authenticating...");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(progressDialog.getWindow().getAttributes());
        lp.width = 500;
        lp.height = 150;
        lp.gravity = Gravity.CENTER;
        progressDialog.getWindow().setAttributes(lp);


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Rest loging response" +response);
                        response = response;
                        progressDialog.hide();
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString(Constants.API_RESPONSE_ATTRIBUTES.status.name());

                            if (status.equalsIgnoreCase(Constants.API_RESPONSE_ATTRIBUTES.status.responeVal)) {

                                Intent i = new Intent(activity, MainActivity.class);
                                SharedPreferences sharedpreferences;
                                //Initialize SharedPreferences
                                sharedpreferences = PreferenceManager.getDefaultSharedPreferences(activity);
                                SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();
                                sharedPrefEditor.putString("city_id", "2707");
                                sharedPrefEditor.putString("customer_id", jsonObj.getJSONObject("data").getString("customer_id"));
                                sharedPrefEditor.putString("name", jsonObj.getJSONObject("data").getString("name"));
                                sharedPrefEditor.putString("mobile", jsonObj.getJSONObject("data").getString("mobile"));
                                sharedPrefEditor.putString("email", jsonObj.getJSONObject("data").getString("email"));
                                sharedPrefEditor.commit();
                                activity.startActivity(i);
                                activity.finish();


                            } else {


                                Intent i = new Intent(activity, LoginActivity.class);
                                DialogFragment dialog = new NetworkStatusDialog();
                                Bundle args = new Bundle();
                                args.putString(NetworkStatusDialog.ARG_TITLE, activity.getString(R.string.text_authentication_failure_title));
                                args.putString(NetworkStatusDialog.ARG_MSG, activity.getString(R.string.text_authentication_failure_msg));
                                args.putParcelable(NetworkStatusDialog.ARG_INTENT, i);
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(activity.getSupportFragmentManager(), "NetworkDialogFragment");
                                Log.e(TAG + "#customerLoginURL", "Invalid Username or Password");

                            }


                        } catch (JSONException je) {
                            Log.e(TAG, "Rest Error: Parsing#CustomerLoginResult Failed - " + je.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.hide();

                        Intent i = new Intent(activity, LoginActivity.class);
                        DialogFragment dialog = new NetworkStatusDialog();
                        Bundle args = new Bundle();
                        args.putString(NetworkStatusDialog.ARG_TITLE, activity.getString(R.string.text_network_title));
                        args.putString(NetworkStatusDialog.ARG_MSG, activity.getString(R.string.text_network_msg));
                        args.putParcelable(NetworkStatusDialog.ARG_INTENT, i);
                        dialog.setArguments(args);
                        dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                        dialog.show(activity.getSupportFragmentManager(), "NetworkDialogFragment");
                        //  Log.e(TAG + "#customerLoginURL", error.getMessage());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.API_CUSTOMER_LOGIN_ULR_PARAMS.email_or_mobile.name(), username);
                params.put(Constants.API_CUSTOMER_LOGIN_ULR_PARAMS.password.name(), password);
                params.put(Constants.API_CUSTOMER_LOGIN_ULR_PARAMS.device_token.name(), device_token);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(postRequest);
        return response;
    }

    public void updateToolbarText(String text, final AppCompatActivity appCompatActivity) {
        {
            final ViewGroup actionBarLayout = (ViewGroup) appCompatActivity.getLayoutInflater().inflate(
                    R.layout.activity_actionbar,
                    null);
//            Log.d("PaymentFragment","Rest updateToolbarText = "+text+" appCompatActivity ="+appCompatActivity);

            // Set up your ActionBar
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarLayout);

            // You customization
            final int actionBarColor = appCompatActivity.getResources().getColor(R.color.black);
            actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
            ImageView action_bar_logo = (ImageView) appCompatActivity.findViewById(R.id.action_bar_logo);

            ImageView action_bar_back_button = (ImageView) appCompatActivity.findViewById(R.id.action_bar_back_button);
            map_indicator = (ImageView) appCompatActivity.findViewById(R.id.action_map);
            TextView action_bar_text = (TextView) appCompatActivity.findViewById(R.id.action_bar_text);
            if (text.equalsIgnoreCase(FragmentTag.FRAGMENT_CART.getFragmentTag().toString())) {
                action_bar_text.setText(text);
                action_bar_logo.setVisibility(View.GONE);
                map_indicator.setVisibility(View.GONE);
            } else if (text.equalsIgnoreCase(FragmentTag.FRAGMENT_HOME.getFragmentTag().toString())) {
                //action_bar_logo.setVisibility(View.GONE);
                map_indicator.setVisibility(View.VISIBLE);
                action_bar_text.setVisibility(View.GONE);
            } else if (text.equalsIgnoreCase(FragmentTag.FRAGMENT_PAYMENT.getFragmentTag().toString())) {
                action_bar_text.setText(text);
                action_bar_logo.setVisibility(View.GONE);
                map_indicator.setVisibility(View.GONE);
            } else if (text.equalsIgnoreCase(FragmentTag.FRAGMENT_NOTIFICATION.getFragmentTag().toString())) {
                action_bar_text.setText(text);
                action_bar_logo.setVisibility(View.GONE);
                map_indicator.setVisibility(View.GONE);
            } else if (text.equalsIgnoreCase(FragmentTag.FRAGMENT_SETTING.getFragmentTag().toString())) {
                action_bar_text.setText(text);
                action_bar_logo.setVisibility(View.GONE);
                map_indicator.setVisibility(View.GONE);
            } else {
                action_bar_logo.setVisibility(View.VISIBLE);
                action_bar_text.setVisibility(View.GONE);
                map_indicator.setVisibility(View.GONE);
            }
            action_bar_back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager=appCompatActivity.getSupportFragmentManager();
                    fragmentManager.popBackStack();

                    //Fragment fragment= fragmentManager.getFragment(null,FragmentTag.FRAGMENT_CART.getFragmentTag());
/*
                    List<Fragment> fragments = fragmentManager.getFragments();

                    for(Fragment fr : fragments) {
                        FragmentTag fr = FragmentTag.values()[fr.getFragmentTag().];
                        switch () {
                            case FragmentTag.FRAGMENT_CART:
                                appCompatActivity.getSupportFragmentManager().beginTransaction().remove(fragment);
                                break;

                        }
                    }*/

                }
            });
        }
    }
}









