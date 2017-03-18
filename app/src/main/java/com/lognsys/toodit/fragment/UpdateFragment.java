package com.lognsys.toodit.fragment;

import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.lognsys.toodit.R;
import com.lognsys.toodit.RegistrationActivity;
import com.lognsys.toodit.model.CountryName;
import com.lognsys.toodit.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by admin on 02-03-2017.
 */

public class UpdateFragment extends Fragment {

   private  String name, email, mobile;
    private  EditText etName, etEmail, etMobile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View updateFragment = inflater.inflate(R.layout.fragment_update, container, false);
        etName=(EditText)updateFragment.findViewById(R.id.etNameUpdate);
       etMobile=(EditText)updateFragment.findViewById(R.id.etMobileNoUpdate);
     etEmail=(EditText)updateFragment.findViewById(R.id.etEmailUpdate);
        Button btnUpdate=(Button)updateFragment.findViewById(R.id.btnUpdate);


        HashMap<String, String> hashMapProfile= new HashMap();
        hashMapProfile.put("customer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", ""));
        //hashMapProfile.put("customer_id", "1");

        getProfileData("http://food.swatinfosystem.com/api/Profile",hashMapProfile);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap= new HashMap<String, String>();
                String name=etName.getText().toString().trim();
                String email=etEmail.getText().toString().trim();
                String mobile=etMobile.getText().toString().trim();
                hashMap.put("customer_id",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("customer_id", "") );
               // hashMap.put("customer_id","1" );
                hashMap.put("name",name);
                hashMap.put("mobile", mobile);
                hashMap.put("email",email);
                hashMap.put("device_token",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.Shared.DEVICE_TOKEN_ID.name(), ""));
               // Log.e("device_token",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.Shared.DEVICE_TOKEN_ID.name(), "") );
                hashMap.put("device_type",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("device_type", ""));
               // Log.e("device_type",PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("device_type", "") );
                updateData("http://food.swatinfosystem.com/api/Update_customer_profile",hashMap );
            }
        });
        return updateFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private String updateData(String URL, final Map<String, String> params) {
        String response = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;

                        CountryName countryName;
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!(jsonObject.getString("message").equals("Profile Updated")||jsonObject.getString("message").equals( "Customer registration successfully otp send to your email id and mobile"))) {
                                    DialogFragment dialog = new NetworkStatusDialog();
                                    Bundle args = new Bundle();
                                    args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_registration_failed));
                                    args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                    dialog.setArguments(args);
                                    dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                    dialog.show(getFragmentManager(), "NetworkDialogFragment");
                                    Log.e(TAG, "Username Invalid!");
                                } else {
                                    DialogFragment dialog = new NetworkStatusDialog();
                                    Bundle args = new Bundle();
                                    args.putString(NetworkStatusDialog.ARG_TITLE, "Updation ");
                                    args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                    dialog.setArguments(args);
                                    dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                    dialog.show(getFragmentManager(), "NetworkDialogFragment");
                                    //Log.e("check", countryArray.toString);

                                    //
                                }  // Log.e("check", countryList.get(4).getName());
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
                        // Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), "Internet not available !", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        return response;
    }

    private String getProfileData(String URL, final Map<String, String> params) {
        String response = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        response = response;
                        CountryName countryName;
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                name=jsonObject.getJSONObject("customer_data").getString("name");
                                email=jsonObject.getJSONObject("customer_data").getString("email");
                                mobile=jsonObject.getJSONObject("customer_data").getString("mobile");
                                etName.setText(name);
                                etEmail.setText(email);
                                etMobile.setText(mobile);
                                //Log.e("check", countryArray.toString);

                                //
                                // Log.e("check", countryList.get(4).getName());
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
                        // Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), "Internet not available !", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        return response;
    }

}
