package com.lognsys.toodit;

/*
 * @author pdoshi
 * @date 01/02/2016
 * Description:
 *  This is the login activity which contains Google, Facebook OAUTH
 *  and custom authentication. It also has sign-up feature which calls activity registration form
 *
 *  DONE (1)  : Shared preference to save boolean login status, tokenID (facebook and Google)
 *  DONE (2)  : Get device token-id
 *  DONE (3a) : Scenario 1: Authentication using Google at first and then Facebook with same EMAIL_ID causes an FirebaseAuthCollisionException
 *  DONE (3b) : Scenario 2: Authentication using Facebook at first and then Google (No Exception)
 *
 *  Use Case On sucessful login and calling Main Activity :
 *  1) If cust_id in shared pref then goto MainActivity
 *  2) if calling login API and successful login set cust_id
 *  3) New Facebook login set cust_id replace old one if exists
 *  4) New Google Login set cust_id and replace old one if exists
 *  5) Facebook Login with same email-id as already registered user email-id saved in shared pref
 *     & copy registered user_cust_id to facebook cust_id
 *  6) Google Login with same email-id as already registered user email-id saved in shared pref
 *      & copy registered user cust_id to Google cust_id
 *  7) Facebook login email-id match with google login email-id  if present and copy Google cust_id to facebook cust_id
 *  8) Google login email-id match with facebook login email-id if present and copy facebook cust_id to Google cust_id
 *
 *
 *  OR
 *  Follow Steps 1 - 4
 *  5) Facebook call register api... if API responds user already exists then login directly
 *  6) google call register api.. if user already exists (
 *  (Common parameters .. email_id, cust_id) reset them when logged out
 */

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lognsys.toodit.Dialog.NetworkStatusDialog;
import com.lognsys.toodit.model.FBUser;
import com.lognsys.toodit.util.Constants;
import com.lognsys.toodit.util.Services;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    //faceboook callback manager
    private CallbackManager mCallbackManager;
    private static final int RC_SIGN_IN = 100;


    //login_activity UI variable
    private EditText inputUserName, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ImageView fbSignIn, googSignIn;
    private LoginButton loginButton;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;

    //firebase variable declaration
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Google API variable
    private GoogleApiClient mGoogleApiClient;

    //Initilalize SharedPeference
    private static final String SHARED_PREF_FILENAME = Constants.Shared.TOODIT_SHARED_PREF.name();
    SharedPreferences sharedpreferences;

    private static final int RC_NETWORK_DIALOG = 101;


    //Shared preference variables;
    private String email = "";
    private boolean login_status = false;
    private String oauthId = "";
    private String device_token_id = "";
    private TextView tvRegistr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize SharedPreferences
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //SharedPreferences Editor
        SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        //facebook initialize
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        //Add device token
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sharedPrefEditor.putString(Constants.Shared.DEVICE_TOKEN_ID.name(), android_id);
        sharedPrefEditor.commit();

        //Use Case 1:If cust_id in shared pref then goto MainActivity
        String cust_id = sharedpreferences.getString(Constants.Shared.CUSTOMER_ID.name(), null);
        if (cust_id != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        //TODO :  Make API CALL for login and goto Main Activity
        //Login:1 Simple Login Mechanism using username and password


        //FB: Initialize callbackmanager factory object
        mCallbackManager = CallbackManager.Factory.create();

        //FB: Facebook authentication code
        loginButton = (LoginButton) findViewById(R.id.fb_image);
        loginButton.setBackgroundResource(R.drawable.fb);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setReadPermissions("email", "user_birthday", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                //call graph user
                AsyncTask.Status status = saveFacebookData(loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken()); // method call after sucessfull authorisation
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                return;
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

                //DONE 5 : Return Dialog box with network error
                DialogFragment dialog = new NetworkStatusDialog();
                Bundle args = new Bundle();
                args.putString("title", getString(R.string.text_network_title));
                args.putString("message", getString(R.string.text_network_msg));
                dialog.setArguments(args);
                dialog.setTargetFragment(dialog, RC_NETWORK_DIALOG);
                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment TAG");
                return;
            }
        });

        //GOOG:1 Google authentication code
        googSignIn = (ImageView) findViewById(R.id.google_image);
        googSignIn.setOnClickListener(this);

        //GOOG:2 sign-in builder pattern with tokenid, email attributes
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //GOOG:3 Pass google "signInOption" variable with attributes to googleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        //Initialize FirebaseAuth
        //You need to include google-services.json (downloaded from firebase console) file under the "app" folder of this project.
        mAuth = FirebaseAuth.getInstance();


        // firebase authentication listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Log.d(TAG, "onAuthStateChanged:Provider:" + user.getProviderId());

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };




        // Resgitration Link
        tvRegistr = (TextView) findViewById(R.id.tvRegister);
        tvRegistr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });


        //Case : 2) if calling login API and successful login
        //User registraion through API
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        tilUsername = (TextInputLayout) findViewById(R.id.til_username);
        inputUserName = (EditText) findViewById(R.id.username);
        tilUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    tilUsername.setErrorEnabled(true);
                    tilUsername.setError(getString(R.string.text_username_empty_msg));
                }

                if (s.length() > 0) {
                    tilUsername.setError(null);
                    tilUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && Services.isEmailValid(inputUserName.getText().toString().trim()) || Services.isValidMobileNo(inputUserName.getText().toString().trim())) {
                    tilUsername.setError(null);
                    tilUsername.setErrorEnabled(false);
                } else {

                    tilUsername.setErrorEnabled(true);
                    tilUsername.setError(getString(R.string.text_username_invalid_msg));
                }
            }
        });


        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        inputPassword = (EditText) findViewById(R.id.password);
        tilPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    tilPassword.setErrorEnabled(true);
                    tilPassword.setError(getString(R.string.text_password_empty_msg));
                }

                if (s.length() > 0) {
                    tilPassword.setError(null);
                    tilPassword.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //TODO: Do Authentication of name and email address and call API to validate User
        //This is the Sign-In button for already registered users
        //Test conditions if username & password is not blank

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = false;

                if ((!Services.isEmpty(inputPassword.getText().toString()) && !Services.isEmpty(inputUserName.getText().toString()))
                        && (Services.isEmailValid(inputUserName.getText().toString()) || Services.isValidMobileNo(inputUserName.getText().toString()))) {
                    isValid = true;
                }



                if (isValid) {
                    Log.d(TAG, "IS VALID - " + isValid);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else return;

            }
        });
    }


    /**
     * View a root element used to call sub elements
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_image:
                signIn();
                break;
            default:
                return;
        }
    }

    /**
     * Signin method with GoogleSignInApi
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //After redirecting from current activity (LoginActivity) to Google OAuth
    //or facebook OAuth after authentication it will call this method immediately
    //Based on the request code it will call the activity either google or facebook

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {

                // Google Sign In failed, update UI appropriately
                DialogFragment dialog = new NetworkStatusDialog();
                Bundle args = new Bundle();
                args.putString("title", getString(R.string.text_google_error_title));
                args.putString("message", getString(R.string.text_google_error_msg));
                dialog.setArguments(args);
                dialog.setTargetFragment(dialog, RC_NETWORK_DIALOG);
                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment TAG");
                return;

            }
        }

    }


    /**
     * This method is called from onActvityResult#GoogleSignInResult
     *
     * @param acct
     */
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        //Check if Goog with same email-id already present
        String checkFacebookEmail = sharedpreferences.getString(Constants.FacebookFields.FB_EMAIL_ID.name(), "");
        if ((!checkFacebookEmail.equals("")) && checkFacebookEmail.equals(acct.getEmail()))
            sharedPrefEditor.putBoolean(Constants.Shared.IS_SIMILAR_EMAILID.name(), true);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:Google:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "Google Login Success...");
                            String firebaseUID = mAuth.getCurrentUser().getUid();
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_UID.name(), firebaseUID);
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_DISPLAY_NAME.name(), acct.getDisplayName());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_EMAIL_ID.name(), acct.getEmail());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_GIVEN_NAME.name(), acct.getGivenName());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_PHOTO_URL.name(), acct.getPhotoUrl().toString());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_TOKEN_ID.name(), acct.getIdToken());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_SERVE_AUTHCODE.name(), acct.getServerAuthCode());
                            sharedPrefEditor.commit();

                            //start activity MainActivity.class
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                    }
                });
    }

    /**
     * call this method by facebook callbackmanager on successful login authorisation.
     *
     * @param token
     */

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token.getUserId());

        //Adding facebook userid in shared_preferences
        final SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete Facebook:" + task.isSuccessful());

                        //FB:UID
                        String firebaseUID = mAuth.getCurrentUser().getUid();
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_UID.name(), firebaseUID);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            try {
                                Log.e(TAG, "signInWithCredential", task.getException());
                                throw task.getException();

                            } catch (FirebaseAuthUserCollisionException fe) {

                                //Google account already created in Firebase with same EmailId as Facebook EmailId
                                Log.e(TAG, "#handleFacebookAccessToken#FacebookAuthUserCollision - " + fe.getMessage());

                                sharedPrefEditor.putBoolean(Constants.Shared.IS_SIMILAR_EMAILID.name(), true);

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            } catch (Exception e) {
                                Log.e(TAG, "handleFacebookAccessToken#Facebook Login Exception - " + e.getMessage());
                            }

                        } else {

                            Log.d(TAG, "Facebook Login Successful...");
                            Log.d(TAG, "Starting MainActivity...");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
    }

    /**
     * @param token
     */
    public AsyncTask.Status saveFacebookData(AccessToken token) {

        final SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();
        final FBUser fbuser = new FBUser();

        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                Log.v(TAG, " Facebook API response - " + response.toString());

                JSONObject json = response.getJSONObject();

                try {
                    if (json != null) {

                        fbuser.setId(json.getString("id"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_ID.name(), fbuser.getId());

                        fbuser.setEmail(json.getString("email"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_EMAIL_ID.name(), fbuser.getEmail());

                        fbuser.setName(json.getString("name"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_NAME.name(), fbuser.getName());

                        fbuser.setFirst_name(json.getString("first_name"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_FIRST_NAME.name(), fbuser.getFirst_name());

                        fbuser.setLast_name(json.getString("last_name"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_LAST_NAME.name(), fbuser.getLast_name());

                        fbuser.setLink(json.getString("link"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_LINK.name(), fbuser.getLink());

                        fbuser.setPicture(json.getJSONObject("picture").getJSONObject("data").getString("url"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_PICTURE.name(), fbuser.getPicture());

                        fbuser.setTimezone(json.getString("timezone"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_TIME_ZONE.name(), fbuser.getTimezone());

                        sharedPrefEditor.commit();

                        Log.d(TAG, "Facebook User : " + fbuser.toString());

                    } else {
                        Log.e(TAG, "Could not retrieve data from facebook graphapi...");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, cover, name, first_name, last_name, link, " +
                "gender, picture, timezone, updated_time, email");
        request.setParameters(parameters);

        AsyncTask.Status status = request.executeAsync().getStatus();
        return status;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Unresolved Error occurred....", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }
}
