package com.lognsys.toodit;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.lognsys.toodit.R;

import java.util.Map;

/**
 * Created by admin on 17-02-2017.
 */

public class RegistrationActivity extends Activity {
    private EditText etName, etEmail, etMobile, etPassword, etConfirmPasword;
    private Button btnRegister;
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
        etConfirmPasword=(EditText)findViewById(R.id.etconfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);


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
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();


                    }
                }


            }
        });

    }
  /*  public class  RegistrationRequest extends Request<String>
    {
        private Map<String, String> mParams;

    }*/
}
