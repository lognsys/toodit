package com.lognsys.toodit;

import android.app.Activity;
import android.app.DownloadManager;
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
import com.lognsys.toodit.model.CityName;
import com.lognsys.toodit.model.CountryName;
import com.lognsys.toodit.model.StateName;
import com.lognsys.toodit.util.Constants;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.ganfra.materialspinner.MaterialSpinner;

import static com.lognsys.toodit.util.Constants.API_RESPONSE_ATTRIBUTES.status;

/**
 * Created by admin on 17-02-2017.
 */

public class RegistrationActivity extends AppCompatActivity {
    private EditText etName, etEmail, etMobile;
    private String name, email, mobile, password;
    private Button btnNext;
    private MaterialSpinner spnCountry, spnState, spnCity;
    ArrayList<CountryName> countryList;
    ArrayList<StateName> stateList;
    ArrayList<CityName> cityList;
    Map<String, String> hashmap = new HashMap<String, String>();
    String state_id, country_id, city_id;
    SharedPreferences sharedpreferences;
    ArrayAdapter<CountryName> dataAdapter;

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
       // etUsername=(EditText)findViewById(R.id.etUsername);
        //etPassword = (EditText) findViewById(R.id.etPassword);
        //etConfirmPasword = (EditText) findViewById(R.id.etconfirmPassword);
        btnNext = (Button) findViewById(R.id.btnNext);
        spnCountry = (MaterialSpinner) findViewById(R.id.spnCountry);
        spnState = (MaterialSpinner) findViewById(R.id.spnState);
        spnCity = (MaterialSpinner) findViewById(R.id.spnCity);

        countryList = new ArrayList<CountryName>();

        //countryList.add(new CountryName("","Select country...","","",""));
        CountryName countryName= new CountryName();


        /*hashmap.put("country_id", "IND");
        hashmap.put("name", "India");

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

        //create ObjectMapper instance
        //ObjectMapper objectMapper = new ObjectMapper();
        String response = getCountry("http://food.swatinfosystem.com/api/Country", hashmap);

        //convert json string to object
        JSONArray countryArray = null;
        dataAdapter = new ArrayAdapter<CountryName>(this,R.layout.spinner_item, countryList);
        dataAdapter.notifyDataSetChanged();
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(dataAdapter);

        /*try {
            if (spnCountry.getAdapter().getCount() < 2) {
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // Hit WebService


                    }
                }, 0, 3, TimeUnit.SECONDS);

            }
        }
        catch (IllegalStateException ise)
        {
            ise.printStackTrace();
        }*/
        //dataAdapter.notifyDataSetChanged();
        spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
                    country_id = (((CountryName) spnCountry.getItemAtPosition(position))).getCountry_id();
                }
                catch (ClassCastException cce)
                {
                    cce.printStackTrace();
                }


               // Log.e("country_id", country_id);
               // Toast.makeText(RegistrationActivity.this,country_id,Toast.LENGTH_LONG).show();
//food.swatinfosystem.com/api/State
                Map<String, String> hashMap1= new HashMap<String, String>();
                hashMap1.put("country_id", country_id);

                registerState("http://food.swatinfosystem.com/api/State", hashMap1);
                ArrayAdapter<StateName> dataAdapter = new ArrayAdapter<StateName>(RegistrationActivity.this, R.layout.spinner_item, stateList);
                dataAdapter.notifyDataSetChanged();
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnState.setAdapter(dataAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


      spnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    try {
                   state_id = (((StateName) spnState.getItemAtPosition(position)).getStateId());
                    } catch (ClassCastException cce) {
                        cce.printStackTrace();
                    }


                    // Log.e("state_id", state_id);
                    //  Toast.makeText(RegistrationActivity.this,state_id,Toast.LENGTH_LONG).show();
//food.swatinfosystem.com/api/State
                    Map<String, String> hashMapCity = new HashMap<String, String>();
                    hashMapCity.put("state_id", state_id);

                    registerCity("http://food.swatinfosystem.com/api/City", hashMapCity);
                    ArrayAdapter<CityName> dataAdapter = new ArrayAdapter<CityName>(RegistrationActivity.this, R.layout.spinner_item, cityList);
                    dataAdapter.notifyDataSetChanged();
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCity.setAdapter(dataAdapter);
                }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    try {
                        city_id = (((CityName) spnCity.getSelectedItem()).getCityId());
                    } catch (ClassCastException cce) {
                        cce.printStackTrace();
                    }


                    // Toast.makeText(RegistrationActivity.this,city_id,Toast.LENGTH_LONG).show();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }

        });




        btnNext.setOnClickListener(new View.OnClickListener() {
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
                    }
                       else if (!Services.isEmailValid(etEmail.getText().toString())) {

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


                        }


                    else if (etMobile.getText().toString().length() == 0) {
                        etMobile.setError("Mobile number is Required");
                        etMobile.requestFocus();
                    }
                       else if(!Services.isValidMobileNo(etMobile.getText().toString())) {

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


                        }

                    else if (spnCountry.getSelectedItemPosition() == 0) {
                        Toast.makeText(RegistrationActivity.this,"Selection of Country is mandatory", Toast.LENGTH_LONG).show();
                    }
                    else if (spnState.getSelectedItemPosition() == 0) {
                        Toast.makeText(RegistrationActivity.this,"Selection of state is mandatory", Toast.LENGTH_LONG).show();
                    }
                    else if (spnCity.getSelectedItemPosition() == 0) {
                        Toast.makeText(RegistrationActivity.this,"Selection of City is mandatory", Toast.LENGTH_LONG).show();
                    }
                  /*  else if (etUsername.getText().toString().length() == 0) {
                        etUsername.setError("Please entere username");
                        etUsername.requestFocus();
                    }
                    else if (etPassword.getText().toString().length() == 0) {
                        etPassword.setError("Password not entered");
                        etPassword.requestFocus();
                    } else if (etConfirmPasword.getText().toString().length() == 0) {
                        etConfirmPasword.setError("Please confirm password");
                        etConfirmPasword.requestFocus();
                    }
                    else if (!etPassword.getText().toString().equals(etConfirmPasword.getText().toString()) ){
                        etConfirmPasword.setError("Password entered did not match");
                        etConfirmPasword.requestFocus();
                    }*/

                    else {
                         String manufacturer = Build.MANUFACTURER; //this one will work for you.
                        String product = Build.PRODUCT;
                        String model = Build.MODEL;
                        String s="Manufacturer:"+manufacturer+",Product:"+product+" ,"+"model: "+model;
                       // Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                        HashMap<String , String> hashMapRegister=new HashMap<String, String>();
                        hashMapRegister.put("name", etName.getText().toString().trim());
                        hashMapRegister.put("username", etEmail.getText().toString().trim());
                        //hashMapRegister.put("password", etPassword.getText().toString().trim());
                        hashMapRegister.put("city_id", city_id);
                        hashMapRegister.put("state_id", state_id);
                        hashMapRegister.put("country_id",country_id);
                        hashMapRegister.put("username", etEmail.getText().toString().trim());
                        hashMapRegister.put("mobile", etMobile.getText().toString().trim());
                        hashMapRegister.put("email", etEmail.getText().toString());
                        hashMapRegister.put("device_token",Constants.Shared.DEVICE_TOKEN_ID.name());
                        hashMapRegister.put("device_type","android tab");
                        int i2=hashMapRegister.size();
                        Intent i= new Intent(RegistrationActivity.this, RegistrationActivityPartTwo.class);
                        i.putExtra("hashMapRegister", hashMapRegister);
                        SharedPreferences sharedpreferences;
                        //Initialize SharedPreferences
                        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
                        //SharedPreferences Editor
                        SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();


                        sharedPrefEditor.putString("device_type", s);
                        sharedPrefEditor.putString("city_id",city_id);
                        sharedPrefEditor.commit();
                        startActivity(i);



                       //String response=registerUser("http://food.swatinfosystem.com/api/Customer_registration", hashMapRegister);




                        /*try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject.get("status");
                            jsonObject.get("message");
                            if(jsonObject.get("status").equals("failed"))
                            {
                                Toast.makeText(RegistrationActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException je) {
                            je.printStackTrace();
                        }*/
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
    private String getCountry(String URL, final Map<String, String> params) {
        String response = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        CountryName countryName;
                        try {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray countryArray = jsonObject.getJSONObject("data").getJSONArray("country_list");
                                //Log.e("check", countryArray.toString);

                                for (int i = 0; i < countryArray.length(); i++) {
                                    JSONObject obj = countryArray.getJSONObject(i);
                                    countryName = new CountryName(obj.getString("country_id"), obj.getString("name"), obj.getString("sortname"), obj.getString("phonecode"), obj.getString("is_delete"));

                                    countryList.add(countryName);

                                }
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
                        Toast.makeText(RegistrationActivity.this, "Internet not available !", Toast.LENGTH_LONG).show();
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

    private String registerState(String URL, final Map<String, String> params) {
        String response = "";
        stateList = new ArrayList<StateName>();
       // stateList.add(new StateName("","Select state...",""));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_LONG).show();
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
                        try {
                            JSONObject jsonError= new JSONObject(error.toString());
                           if(error.getMessage().equals("Please provide country id"))  {
                              // Toast.makeText(RegistrationActivity.this, "Please wait to populate spinner", Toast.LENGTH_LONG).show();
                           }
                            else
                           {
                              // Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                           }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

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
        cityList = new ArrayList<CityName>();
        //cityList.add(new CityName("","Select city...",""));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       //Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        CityName cityName;
                        try {
                            try {
                                JSONArray countryArray;
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getJSONObject("data").getJSONArray("city_list")!=null){
                                    countryArray = jsonObject.getJSONObject("data").getJSONArray("city_list");
                                    for (int i = 0; i < countryArray.length(); i++) {
                                        JSONObject obj = countryArray.getJSONObject(i);
                                        cityName = new CityName(obj.getString("city_id"), obj.getString("name"), obj.getString("state_id"));

                                        cityList.add(cityName);

                                    }
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        return response;
    }


}
