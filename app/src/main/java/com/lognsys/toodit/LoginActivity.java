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
 *  TODO (4)  : Scenario 3: Simple Email Authentication via API with same Email-Id (if same email_id do nothing)
 *
 */

import android.app.ProgressDialog;
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
    private EditText inputEmail, inputName, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ImageView fbSignIn, googSignIn;
    private LoginButton loginButton;
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;

    //firebase variable declaration
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Google API variable
    private GoogleApiClient mGoogleApiClient;

    //Initilalize SharedPeference
    private static final String SHARED_PREF_FILENAME = Constants.Shared.TOODIT_SHARED_PREF.name();
    SharedPreferences sharedpreferences;

    private static final int RC_NETWORK_DIALOG = 101;


    //Shared preference varaibles;
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

        //
        SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        //facebook initialize
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);


        //Add device token
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sharedPrefEditor.putString(Constants.Shared.DEVICE_TOKEN_ID.name(), android_id);
        sharedPrefEditor.commit();


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

        // firebase authentication listener
        //Sign-In button will be used for registered users
        btnSignIn = (Button) findViewById(R.id.sign_in_button);


        // Register...
        tvRegistr = (TextView) findViewById(R.id.tvRegister);
        tvRegistr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });


        //DONE : Input name and email for authentication,
        tilName = (TextInputLayout) findViewById(R.id.til_name);
        inputName = (EditText) findViewById(R.id.name);


        tilName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    tilName.setErrorEnabled(true);
                    tilName.setError(getString(R.string.text_name_empty_msg));
                }

                if (s.length() > 0) {
                    tilName.setError(null);
                    tilName.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        inputEmail = (EditText) findViewById(R.id.email);

        tilEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    tilEmail.setErrorEnabled(true);
                    tilEmail.setError(getString(R.string.text_email_empty_msg));
                }

                if (s.length() > 0) {
                    tilEmail.setError(null);
                    tilEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //TODO: Do Authentication of name and email address and call API to validate User
        //This is the Sign-In button for already registered users


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;


                if (!Services.isNameValid(inputName.getText().toString())) {
                    tilEmail.setErrorEnabled(true);
                    tilName.setError(getString(R.string.text_name_invalid_msg));
                    isValid = false;
                    return;
                } else {
                    tilEmail.setError(null);
                    tilEmail.setErrorEnabled(false);
                }


                if (!Services.isEmailValid(inputEmail.getText().toString())) {
                    tilEmail.setErrorEnabled(true);
                    tilEmail.setError(getString(R.string.text_email_invalid_msg));
                    isValid = false;
                    return;
                } else {
                    tilEmail.setError(null);
                    tilEmail.setErrorEnabled(false);
                }


                if (isValid) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    return;
                }
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

        //GOOG:UID
        String firebaseUID = mAuth.getCurrentUser().getUid();
        sharedPrefEditor.putString(Constants.GoogleFields.GOOG_UID.name(), firebaseUID);

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
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_DISPLAY_NAME.name(), acct.getDisplayName());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_EMAIL_ID.name(), acct.getEmail());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_GIVEN_NAME.name(), acct.getGivenName());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_PHOTO_URL.name(), acct.getPhotoUrl().toString());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_TOKEN_ID.name(), acct.getIdToken());
                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_SERVE_AUTHCODE.name(), acct.getServerAuthCode());
                            sharedPrefEditor.commit();
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

        //FB:UID
        String firebaseUID = mAuth.getCurrentUser().getUid();
        sharedPrefEditor.putString(Constants.FacebookFields.FB_UID.name(), firebaseUID);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete Facebook:" + task.isSuccessful());

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
