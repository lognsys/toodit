package com.lognsys.toodit.fragment;

import android.preference.PreferenceManager;
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
import com.lognsys.toodit.R;
import com.lognsys.toodit.RegistrationActivity;
import com.lognsys.toodit.model.CountryName;
import com.lognsys.toodit.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 02-03-2017.
 */

public class UpdateFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View updateFragment = inflater.inflate(R.layout.fragment_update, container, false);
        final EditText etName=(EditText)updateFragment.findViewById(R.id.etNameUpdate);
        final EditText etMobile=(EditText)updateFragment.findViewById(R.id.etMobileNoUpdate);
        final EditText etEmail=(EditText)updateFragment.findViewById(R.id.etEmailUpdate);
        Button btnUpdate=(Button)updateFragment.findViewById(R.id.btnUpdate);

        etName.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("name", ""));
        etMobile.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("mobile", ""));
        etEmail.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("email", ""));



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap= new HashMap<String, String>();
                String name=etName.getText().toString().trim();
                String email=etEmail.getText().toString().trim();
                String mobile=etMobile.getText().toString().trim();
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
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_map);
        item.setVisible(false);
    }
}
