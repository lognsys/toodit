package com.lognsys.toodit;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.Dialog.NetworkStatusDialog;
import com.lognsys.toodit.R;
import com.lognsys.toodit.fragment.ListMall;
import com.lognsys.toodit.model.CityName;
import com.lognsys.toodit.model.CountryName;
import com.lognsys.toodit.model.StateName;
import com.lognsys.toodit.util.Constants;
import com.lognsys.toodit.util.PropertyReader;
import com.lognsys.toodit.util.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.content.ContentValues.TAG;
import static com.lognsys.toodit.util.Constants.API_RESPONSE_ATTRIBUTES.status;

/**
 * Created by admin on 17-02-2017.
 */

public class RegistrationActivity extends AppCompatActivity {
    private EditText etName, etEmail, etMobile, etPassword, etConfirmPasword;
    private Button btnRegister;
    Map<String, String> hashmap = new HashMap<String, String>();
    SharedPreferences sharedpreferences;
    String customer_id, mobile, email, otp;

    //Properties
    private PropertyReader propertyReader;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "toodit.properties";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        propertyReader = new PropertyReader(this);
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobileNo);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPasword = (EditText) findViewById(R.id.etconfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

       populateDetails();

        /*hashmap.put("country_id", "IND");
        hashmap.put("name", "India");

        hashmap.put("name", "John");
        hashmap.put("name", "John");
        hashmap.put("username", "john");
        hashmap.put("password", "john123");
        hashmap.put("city_id", "1");
        hashmap.put("state_id", "2");
        hashmap.put("country_id", "3");
        hashmap.put("username", "John");
        hashmap.put("mobile", "12345");
        hashmap.put("email", "john@123");
        hashmap.put("device_token","0987654321");

        registerUser("http://food.swatinfosystem.com/api/Customer_registration", hashmap);*/


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().length() == 0) {
                    etName.setError("Name not entered");
                    etName.requestFocus();
                } else {
                    if (etEmail.getText().toString().length() == 0) {
                        etEmail.setError("Email not entered");
                        etEmail.requestFocus();

                        /*final String email = etEmail.getText().toString().trim();

                        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        etEmail.addTextChangedListener(new TextWatcher() {
                            public void afterTextChanged(Editable s) {

                                if (email.matches(emailPattern))
                                    if (s.length() > 0) {
                                        Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                                        // or
                                        // textView.setText("valid email");
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                                        //or
                                        // textView.setText("invalid email");
                                    }
                                else {
                                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                                    //or
                                    // textView.setText("invalid email");
                                }
                            }

                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // other stuffs
                            }

                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // other stuffs
                            }
                        });*/
                    } else if (!Services.isEmailValid(etEmail.getText().toString())) {

                        Log.v("TAG" + "#Email", Services.isEmailValid(etEmail.getText().toString()) + "");

                        Log.v("TAG" + "#Mobile", Services.isValidMobileNo(etEmail.getText().toString()) + "");
                        DialogFragment dialog = new NetworkStatusDialog();
                        Bundle args = new Bundle();
                        args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                        args.putString(NetworkStatusDialog.ARG_MSG, "Please enter valid email");
                        dialog.setArguments(args);
                        dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                        dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                        Log.e("TAG", "Username Invalid!");


                    } else if (etMobile.getText().toString().length() == 0) {
                        etMobile.setError("Mobile number is Required");
                        etMobile.requestFocus();
                    } else if (!Services.isValidMobileNo(etMobile.getText().toString())) {

                        Log.v("TAG" + "#Email", Services.isValidMobileNo(etMobile.getText().toString()) + "");

                        Log.v("TAG" + "#Mobile", Services.isValidMobileNo(etMobile.getText().toString()) + "");
                        DialogFragment dialog = new NetworkStatusDialog();
                        Bundle args = new Bundle();
                        args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                        args.putString(NetworkStatusDialog.ARG_MSG, "Please enter valid mobile no");
                        dialog.setArguments(args);
                        dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                        dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                        Log.e("TAG", "Username Invalid!");


                    } else if (etPassword.getText().toString().length() == 0) {
                        etPassword.setError("Password not entered");
                        etPassword.requestFocus();
                    } else if (etConfirmPasword.getText().toString().length() == 0) {
                        etConfirmPasword.setError("Please confirm password");
                        etConfirmPasword.requestFocus();
                    } else if (!etPassword.getText().toString().equals(etConfirmPasword.getText().toString())) {
                        etConfirmPasword.setError("Password entered did not match");
                        etConfirmPasword.requestFocus();
                    } else {
                        String manufacturer = Build.MANUFACTURER; //this one will work for you.
                        String product = Build.PRODUCT;
                        String model = Build.MODEL;
                        String s = "Manufacturer:" + manufacturer + ",Product:" + product + " ," + "model: " + model;
                        // Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> hashMapRegister = new HashMap<String, String>();
                        hashMapRegister.put("name", etName.getText().toString().trim());
                        hashMapRegister.put("password", etPassword.getText().toString().trim());
                        hashMapRegister.put("mobile", etMobile.getText().toString().trim());
                        hashMapRegister.put("email", etEmail.getText().toString());
                        hashMapRegister.put("device_token", PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this).getString(Constants.Shared.DEVICE_TOKEN_ID.name(), null));
                        hashMapRegister.put("device_type", "android tab");
                        if(etMobile.getText().toString()!=null){
                            String mobile_num=etMobile.getText().toString();
                            TooditApplication.getInstance().getPrefs().setMobile(mobile_num);
                        }
                        String customer_registration_api= properties.getProperty(Constants.API_URL.customer_registration_url.name());
                        String response = registerUser(customer_registration_api, hashMapRegister);



                    }
                }
            }
        });

    }

    private void populateDetails() {
        if(TooditApplication.getInstance().getPrefs().getName()!=null){
            etName.setText(TooditApplication.getInstance().getPrefs().getName());
        }
        if(TooditApplication.getInstance().getPrefs().getEmail()!=null){
            etEmail.setText(TooditApplication.getInstance().getPrefs().getEmail());
        }


    }

    private String registerUser(String URL, final Map<String, String> params) {
        String response = "";
        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
        ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        try {
                            Log.d(TAG, "Rest registerUser response "+response);
//                            Rest registerUser response {"status":"success","message":"Customer registration successfully","data":{"customer_id":7,"email":"monika_sharma39@ymail.com","mobile":"8097526387","name":"Monika Sharma"}}

                            JSONObject jsonObject = new JSONObject(response);

                            if (!(jsonObject.getString("message").equals("Customer added successfully")
                                    ||jsonObject.getString("message").equals( "Customer registration successfully otp send to your email id and mobile"))) {
                                DialogFragment dialog = new NetworkStatusDialog();
                                Bundle args = new Bundle();
                                args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                                args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(i);
                                Log.e(TAG, "Username Invalid!");
                            } else {
                                DialogFragment dialog = new NetworkStatusDialog();
                                Bundle args = new Bundle();
                                args.putString(NetworkStatusDialog.ARG_TITLE, "Registration successfull");
                                args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                                Log.e(TAG, "Username Invalid!");
                                try {

                                    customer_id = jsonObject.getJSONObject("data").getString("customer_id");
                                    mobile = jsonObject.getJSONObject("data").getString("mobile");
                                    email = jsonObject.getJSONObject("data").getString("email");
                                    otp = jsonObject.getJSONObject("data").getString("otp");
                                    Log.d("otp","Rest otp "+ otp);
                                    Log.e("otp", otp);
                                    Intent i = new Intent(RegistrationActivity.this, ActivityOTPValidation.class);
                                    i.putExtra("customer_id", customer_id);
                                    i.putExtra("mobile", mobile);
                                    i.putExtra("email", email);
                                    i.putExtra("otp", otp);

                                    startActivity(i);
                                    finish();
                                    // Log.e("mallname", mall);
                                    // Toast.makeText(getActivity(), mall, Toast.LENGTH_LONG).show();


                                } catch (JSONException je) {
                                    je.printStackTrace();
                                    Log.e("error", je.getMessage());
                                }


                            }
                        } catch (JSONException je) {
                            je.printStackTrace();
                            Log.e("error", je.getMessage());
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        requestQueue.add(stringRequest);
        //initialize the progress dialog and show it

        progressDialog.setMessage("Registering please wait....");
        progressDialog.show();
        return response;
    }


}
