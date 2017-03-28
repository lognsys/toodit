package com.lognsys.toodit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.Dialog.NetworkStatusDialog;
import com.lognsys.toodit.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by admin on 15-03-2017.
 */

public class ActivityOTPValidation extends AppCompatActivity {

    private EditText etOTP;
    private Button btnSubmit;
    private String otp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validation);
        etOTP = (EditText) findViewById(R.id.etOTP);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        Intent i = getIntent();

        final String mobile = i.getStringExtra("mobile");
        final String customer_id = i.getStringExtra("customer_id");
        final String email = i.getStringExtra("email");


        HashMap<String, String> hashMap= new HashMap<>();
        hashMap.put("customer_id",customer_id);

        otpGeneration("http://food.swatinfosystem.com/api/Generate_otp",hashMap );
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (etOTP.getText().toString().trim().length() == 0) {
                    etOTP.setError("Please enter OTP");
                    etOTP.requestFocus();
                }  else {*/
                    HashMap<String, String> hashMapValidateOtp = new HashMap<String, String>();
                    hashMapValidateOtp.put("device_token", PreferenceManager.getDefaultSharedPreferences(ActivityOTPValidation.this).getString(Constants.Shared.DEVICE_TOKEN_ID.name(), null));
                    hashMapValidateOtp.put("device_type", "android tab");
                    hashMapValidateOtp.put("customer_id", customer_id);
                    hashMapValidateOtp.put("mobile", mobile);

                    hashMapValidateOtp.put("otp", otp);
                    otpValidation("http://food.swatinfosystem.com/api/Validate_otp", hashMapValidateOtp);
               // }

            }
        });


    }


    private String otpValidation(String URL, final Map<String, String> params) {
        String response = "";
        final ProgressDialog progressDialog = new ProgressDialog(ActivityOTPValidation.this);
        ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(ActivityOTPValidation.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (!jsonObject.getString("message").equals("Account successfully activated")) {
                                DialogFragment dialog = new NetworkStatusDialog();
                                Bundle args = new Bundle();
                                args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                                args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                                Log.e(TAG, "Username Invalid!");
                            } else {
                                DialogFragment dialog = new NetworkStatusDialog();
                                Bundle args = new Bundle();
                                args.putString(NetworkStatusDialog.ARG_TITLE, "Account activation");
                                args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                                Log.e(TAG, "Username Invalid!");
                                try {
                                    if (jsonObject.getJSONObject("data") != null) {


                                        String customer_id = jsonObject.getJSONObject("data").getString("customer_id");
                                        ;
                                        String mobile = jsonObject.getJSONObject("data").getString("mobile");
                                        String email = jsonObject.getJSONObject("data").getString("email");
                                        String name = jsonObject.getJSONObject("data").getString("name");
                                        Intent i = new Intent(ActivityOTPValidation.this, MainActivity.class);
                                        i.putExtra("customer_id", customer_id);
                                        i.putExtra("mobile", mobile);
                                        i.putExtra("email", email);
                                        i.putExtra("otp", name);

                                        startActivity(i);
                                        // Log.e("mallname", mall);
                                        // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();


                                    }
                                } catch (JSONException je) {
                                    je.printStackTrace();
                                }


                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(ActivityOTPValidation.this, error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        DialogFragment dialog = new NetworkStatusDialog();
                        Bundle args = new Bundle();
                        args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                        args.putString(NetworkStatusDialog.ARG_MSG, "Internet connectivity issue");
                        dialog.setArguments(args);
                        dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                        dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                        Log.e(TAG, "Username Invalid!");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityOTPValidation.this);
        requestQueue.add(stringRequest);
        //initialize the progress dialog and show it

        progressDialog.setMessage("Registering please wait....");
        progressDialog.show();
        return response;
    }
    private String otpGeneration(String URL, final Map<String, String> params) {
        String response = "";
        final ProgressDialog progressDialog = new ProgressDialog(ActivityOTPValidation.this);
        ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(ActivityOTPValidation.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (!jsonObject.getString("message").equals("Otp generate successfully and sent to your email id and mobile")) {
                                DialogFragment dialog = new NetworkStatusDialog();
                                Bundle args = new Bundle();
                                args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                                args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                                Log.e(TAG, "Username Invalid!");
                            } else {
                                DialogFragment dialog = new NetworkStatusDialog();
                                Bundle args = new Bundle();
                                args.putString(NetworkStatusDialog.ARG_TITLE, "OTP generation");
                                args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                                Log.e(TAG, "Username Invalid!");
                                try {
                                    if (jsonObject.getJSONObject("data") != null) {


                                       otp = jsonObject.getJSONObject("data").getString("otp");
                                        etOTP.setText(otp);
                                        // Log.e("mallname", mall);
                                        // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();


                                    }
                                } catch (JSONException je) {
                                    je.printStackTrace();
                                }


                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ActivityOTPValidation.this, error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        DialogFragment dialog = new NetworkStatusDialog();
                        Bundle args = new Bundle();
                        args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                        args.putString(NetworkStatusDialog.ARG_MSG, "Internet connectivity issue");
                        dialog.setArguments(args);
                        dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                        dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                        Log.e(TAG, "Username Invalid!");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityOTPValidation.this);
        requestQueue.add(stringRequest);
        //initialize the progress dialog and show it

        progressDialog.setMessage("Registering please wait....");
        progressDialog.show();
        return response;
    }
}
