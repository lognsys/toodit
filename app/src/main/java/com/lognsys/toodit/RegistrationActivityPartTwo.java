package com.lognsys.toodit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lognsys.toodit.Dialog.NetworkStatusDialog;
import com.lognsys.toodit.fragment.HomeFragment;
import com.lognsys.toodit.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by admin on 28-02-2017.
 */

public class RegistrationActivityPartTwo extends AppCompatActivity {
    private EditText etUsername, etPassword, etConfirmPasword;
    private Button btnRegister;
    private HashMap<String, String> hashMapRegister;
    //private String name, email, city_id, state_id, country_id, mobile_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_part_two);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPasword = (EditText) findViewById(R.id.etconfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        Intent i = getIntent();
       hashMapRegister = (HashMap<String, String>) i.getSerializableExtra("hashMapRegister");


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etUsername.getText().toString().length() == 0) {
                    etUsername.setError("Please enter username");
                    etUsername.requestFocus();
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
                    // Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
                    // HashMap<String, String> hashMapRegister = new HashMap<String, String>();
                    // hashMapRegister.put("name", name);
                   // hashMapRegister.put("username", etUsername.getText().toString().trim());
                    hashMapRegister.put("password", etPassword.getText().toString().trim());
                    int i=hashMapRegister.size();
                    String pas=hashMapRegister.get("password");
                    Log.e("password", pas);
                    // hashMapRegister.put("city_id", city_id);
                    //hashMapRegister.put("state_id", state_id);
                    //hashMapRegister.put("country_id", country_id);
                   // hashMapRegister.put("username", etUsername.getText().toString().trim());
                    // hashMapRegister.put("mobile", mobile_no);
                    //hashMapRegister.put("email", email);
                    //hashMapRegister.put("device_token", Constants.Shared.DEVICE_TOKEN_ID.name());


                    String response = registerUser("http://food.swatinfosystem.com/api/Customer_registration", hashMapRegister);




                      /*  try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject.get("status");
                            jsonObject.get("message");
                            if(jsonObject.get("status").equals("failed"))
                            {
                                Toast.makeText(RegistrationActivityPartTwo.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(RegistrationActivityPartTwo.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException je) {
                            je.printStackTrace();
                        }*/
                }
            }

        });
    }


    private String registerUser(String URL, final Map<String, String> params) {
        String response = "";
        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivityPartTwo.this);
        ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(RegistrationActivityPartTwo.this, response, Toast.LENGTH_LONG).show();
                        response = response;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // jsonObject.get("status");
                            //jsonObject.get("message");
                           /* if(jsonObject.get("status").equals("failed"))
                            {
                                Toast.makeText(RegistrationActivityPartTwo.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                            }*/
                            // else
                            //{
                            //Toast.makeText(RegistrationActivityPartTwo.this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                            //}
                            // dialog(jsonObject.getString("message"));
                            if (!jsonObject.getString("message").equals("Customer added successfully")) {
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
                                args.putString(NetworkStatusDialog.ARG_TITLE, "Registration successfull");
                                args.putString(NetworkStatusDialog.ARG_MSG, jsonObject.getString("message"));
                                dialog.setArguments(args);
                                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                                Log.e(TAG, "Username Invalid!");
                                Intent i= new Intent(RegistrationActivityPartTwo.this, MainActivity.class);


                                startActivity(i);

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
                      //  Toast.makeText(RegistrationActivityPartTwo.this, error.toString(), Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivityPartTwo.this);
        requestQueue.add(stringRequest);
        //initialize the progress dialog and show it

        progressDialog.setMessage("Registering please wait....");
        progressDialog.show();
        return response;
    }


}
