package com.lognsys.toodit.util;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pdoshi on 25/02/17.
 */

public class CallAPI {

    private final String TAG = this.getClass().getName();
    private static final String ARG_TITLE = "title";
    private static final String ARG_MSG = "message";
    private static final String ARG_INTENT = "intent";


    public CallAPI() {

    }


    /*
     * @param - input parameters
     *
     */
    public void callCustomerLoginURL(String url, final String username, final String password, final String device_token,
                                     final AppCompatActivity activity) {

        RequestQueue queue = Volley.newRequestQueue(activity);

        final ProgressDialog progressDialog = new ProgressDialog(activity, R.style.TooditTheme);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v(TAG, response);

                        progressDialog.hide();
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString(Constants.API_RESPONSE_ATTRIBUTES.status.name());

                            if (status.equalsIgnoreCase(Constants.API_RESPONSE_ATTRIBUTES.status.responeVal)) {

                                Intent i = new Intent(activity, MainActivity.class);
                                activity.startActivity(i);
                                activity.finish();


                            } else {


//                                String title = activity.getString(R.string.text_authentication_failure_title);
//                                String message = activity.getString(R.string.text_authentication_failure_msg);

//                                DialogFragment dialog = NetworkStatusDialog.newInstance(title, message, i);

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
                            Log.e(TAG, "Error: Parsing#CustomerLoginResult Failed - " + je.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.hide();
//                        String title = activity.getString(R.string.text_network_title);
//                        String message = activity.getString(R.string.text_network_msg);
//                        Intent i = new Intent(activity, LoginActivity.class);
//                        DialogFragment dialog = NetworkStatusDialog.newInstance(title, message, i);

                        Intent i = new Intent(activity, LoginActivity.class);
                        DialogFragment dialog = new NetworkStatusDialog();
                        Bundle args = new Bundle();
                        args.putString(NetworkStatusDialog.ARG_TITLE, activity.getString(R.string.text_network_title));
                        args.putString(NetworkStatusDialog.ARG_MSG, activity.getString(R.string.text_network_msg));
                        args.putParcelable(NetworkStatusDialog.ARG_INTENT, i);
                        dialog.setArguments(args);
                        dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                        dialog.show(activity.getSupportFragmentManager(), "NetworkDialogFragment");
                        Log.e(TAG + "#customerLoginURL", error.getMessage());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constants.API_CUSTOMER_LOGIN_ULR_PARAMS.username.name(), username);
                params.put(Constants.API_CUSTOMER_LOGIN_ULR_PARAMS.password.name(), password);
                params.put(Constants.API_CUSTOMER_LOGIN_ULR_PARAMS.device_token.name(), device_token);

                return params;
            }

        };

        queue.add(postRequest);

    }
}
