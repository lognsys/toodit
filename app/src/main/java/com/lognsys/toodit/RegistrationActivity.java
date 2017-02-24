package com.lognsys.toodit;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lognsys.toodit.R;
import com.lognsys.toodit.model.CountryName;
import com.lognsys.toodit.model.StateName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 17-02-2017.
 */

public class RegistrationActivity extends AppCompatActivity {
    private EditText etName, etEmail, etMobile, etPassword, etConfirmPasword;
    private String name, email, mobile, password;
    private Button btnRegister;
    private Spinner spnCountry, spnState, spnCity;
    ArrayList<CountryName> countryList;
    ArrayList<StateName> stateList;
    Map<String, String> hashmap = new HashMap<String, String>();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobileNo);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPasword = (EditText) findViewById(R.id.etconfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        spnCountry = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.spnCountry);
        spnState = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.spnState);
        spnCity = (android.support.v7.widget.AppCompatSpinner) findViewById(R.id.spnCity);
        countryList = new ArrayList<CountryName>();
        stateList = new ArrayList<StateName>();

        hashmap.put("country_id", "IND");
        hashmap.put("name", "India");

                      /* hashmap.put("name", "John");
                        hashmap.put("username", "john");
                        hashmap.put("password", "john123");
                        hashmap.put("city_id", "1");
                        hashmap.put("state_id", "2");
                        hashmap.put("country_id", "3");
                        hashmap.put("username", "John");
                        hashmap.put("mobile", "12345");
                        hashmap.put("email", "john@123");
                        hashmap.put("device_token","0987654321");*/


        //registerUser("http://food.swatinfosystem.com/api/Customer_registration", hashmap);

        String response = registerUser("http://food.swatinfosystem.com/api/Country", hashmap);

        //create ObjectMapper instance
        //ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        JSONArray countryArray = null;
        ArrayAdapter<CountryName> dataAdapter = new ArrayAdapter<CountryName>(this, android.R.layout.simple_spinner_item, countryList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(dataAdapter);

        spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country_id = (((CountryName) spnCountry.getSelectedItem()).getCountry_id()).toString();
                Log.e("country_id", country_id);
                Toast.makeText(RegistrationActivity.this,country_id,Toast.LENGTH_LONG).show();
//food.swatinfosystem.com/api/State
                hashmap = null;
                hashmap.put("country_id", country_id);

                registerCity("http://food.swatinfosystem.com/api/State", hashmap);
                ArrayAdapter<StateName> dataAdapter = new ArrayAdapter<StateName>(RegistrationActivity.this, android.R.layout.simple_spinner_item, stateList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnState.setAdapter(dataAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


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

                        final String email = etEmail.getText().toString().trim();

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
                        });

                    } else if (etMobile.getText().toString().length() == 0) {
                        etMobile.setError("Mobile number is Required");
                        etMobile.requestFocus();
                    } else if (etPassword.getText().toString().length() == 0) {
                        etPassword.setError("Password not entered");
                        etPassword.requestFocus();
                    } else if (etConfirmPasword.getText().toString().length() == 0) {
                        etConfirmPasword.setError("Please confirm password");
                        etConfirmPasword.requestFocus();
                    } else {
                        Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();


                    }
                }
            }
        });

    }

    /*private static class ContactJsonDataHolder {
        @JsonProperty("contacts")
        public List<CountryName> mContactList;
    }

    public List<CountryName> getCountryFromJson(String json) throws JSONException, IOException {

        ContactJsonDataHolder dataHolder = new ObjectMapper().readValue(json, ContactJsonDataHolder.class);

        // ContactPojo contact = dataHolder.mContactList.get(0);
        // String name = contact.getName();
        // String phoneNro = contact.getPhone().getMobileNro();
        try {
            String get=dataHolder.mContactList.get(0).getName();
           // Log.e("Check country ", get);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataHolder.mContactList;

    }
*/
    private String registerUser(String URL, final Map<String, String> params) {
        String response = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        CountryName countryName;
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray countryArray = jsonObject.getJSONObject("data").getJSONArray("country_list");
                                //Log.e("check", countryArray.toString());
                                for (int i = 0; i < countryArray.length(); i++) {
                                    JSONObject obj = countryArray.getJSONObject(i);
                                    countryName = new CountryName(obj.getString("country_id"), obj.getString("name"), obj.getString("sortname"), obj.getString("phonecode"), obj.getString("is_delete"));

                                    countryList.add(countryName);

                                }
                                Log.e("check", countryList.get(4).getName());
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
                        Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        return response;
    }

    private String registerCity(String URL, final Map<String, String> params) {
        String response = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        StateName stateName;
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray countryArray = jsonObject.getJSONObject("data").getJSONArray("state_list");
                                //Log.e("check", countryArray.toString());
                                for (int i = 0; i < countryArray.length(); i++) {
                                    JSONObject obj = countryArray.getJSONObject(i);
                                    stateName = new StateName(obj.getString("state_id"), obj.getString("name"), obj.getString("country_id"));

                                    stateList.add(stateName);

                                }

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
                        Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        return response;
    }




  /*  public class  RegistrationRequest extends Request<String>
    {
        private Map<String, String> mParams;

    }*/
}
