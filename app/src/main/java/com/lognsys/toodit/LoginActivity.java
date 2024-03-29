package com.lognsys.toodit;

/*
 * @author pdoshi
 * @date 01/02/2016
 * Description:
 * This is the login activity which contains Google, Facebook OAUTH
 * and custom authentication. It also has sign-up feature which calls activity registration form
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
 *                                             OR
 *
 *  Follow Steps 1 - 4
 *  5) Facebook call register api... if API responds user already exists then login directly
 *  6) google call register api.. if user already exists (
 *  (Common parameters .. email_id, cust_id) reset them when logged out
 *
 *
 *  CHANGE LOG :
 *
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
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
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.Constants;
import com.lognsys.toodit.util.LognSystemLocationService;
import com.lognsys.toodit.util.PropertyReader;
import com.lognsys.toodit.util.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";

    //faceboook callback manager
    private CallbackManager mCallbackManager;
    private static final int RC_SIGN_IN = 100;
    private static final int RC_NETWORK_DIALOG = 101;

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

    //Shared preference variables;
    private String email = "";
    private boolean login_status = false;
    private String oauthId = "";
    private String device_token_id = "";
    private TextView tvRegistr;

    //Properties
    private PropertyReader propertyReader;
    private Context context;
    private Properties properties;
    public static final String PROPERTIES_FILENAME = "toodit.properties";

    LognSystemLocationService gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize SharedPreferences
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //SharedPreferences Editor
        SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        //Property Initliazer
        context = this;
        propertyReader = new PropertyReader(context);
        properties = propertyReader.getMyProperties(PROPERTIES_FILENAME);

        //facebook initialize
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        //Add device token
        device_token_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("devicetoken","Rest device token "+ device_token_id);
      /*  sharedPrefEditor.putString(Constants.Shared.DEVICE_TOKEN_ID.name(), device_token_id);
        sharedPrefEditor.commit();
*/
      TooditApplication.getInstance().getPrefs().setDevice_token(device_token_id);

        //Add device type
        String manufacturer = Build.MANUFACTURER; //this one will work for you.
        String product = Build.PRODUCT;
        String model = Build.MODEL;
        String s="Manufacturer:"+manufacturer+",Product:"+product+" ,"+"model: "+model;
//        sharedPrefEditor.putString("device_type", "android tab");
//        sharedPrefEditor.commit();
        TooditApplication.getInstance().getPrefs().setDevice_type("android tab");


        //Use Case 1:If cust_id in shared pref then goto MainActivity
//        String cust_id = sharedpreferences.getString(Constants.Shared.CUSTOMER_ID.name(), null);

//        Monika added
        String cust_id = TooditApplication.getInstance().getPrefs().getCustomer_id();
        Log.d("devicetoken","Rest device cust_id "+ cust_id);
        Log.d("devicetoken","Rest device islogin "+ TooditApplication.getInstance().getPrefs().getIsLogin());
        if (cust_id != null && TooditApplication.getInstance().getPrefs().getIsLogin()==true) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
//            finish();
        }
//      Monika added here to get location
        initLocation();
        /****************************** FACEBOOK AUTH *****************************************/
        //FB:1 Initialize callbackmanager factory object
        mCallbackManager = CallbackManager.Factory.create();

        //FB:2 Facebook authentication code
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
                Log.d(TAG, "facebook:onSuccess: loginResult.getAccessToken() " + loginResult.getAccessToken());
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
                dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");

                return;
            }
        });
        /******************************************************************************/

        /*********************************GOOGLE AUTH*************************************/

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
        Log.d("devicetoken","Rest device mGoogleApiClient.isConnected() "+ mGoogleApiClient.isConnected());


        /******************************************************************************/


        /*********************************FIREBASE************************************/

        //Initialize FirebaseAuth
        //You need to include google-services.json (downloaded from firebase console) file under the "app" folder of this project.
        mAuth = FirebaseAuth.getInstance();


        // firebase authentication listener will chek if user is authenticated.
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(TAG, "onAuthStateChanged:signed_in:" +user);
//              user=com.google.android.gms.internal.zzbnf@1f578fe
                if (user != null) {

                    // User is signed in
//                   user.getUid() : XQl3QB8ZPqUB8YPMIiMeMvXYjQo1
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    //                  Provider:firebase
                    Log.d(TAG, "onAuthStateChanged:Provider:" + user.getProviderId());

                  /* Monika commented 0n 27/03/17
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();*/

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
//                    finish();


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        /******************************************************************************/


        /******************** USER REGISTATION SIGN IN  & VALIDATION **********************/


        //Case : 2) if calling login API and successful login
        //User registration through API
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        tilUsername = (TextInputLayout) findViewById(R.id.til_username);
        inputUserName = (EditText) findViewById(R.id.username);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        inputPassword = (EditText) findViewById(R.id.password);


        //TODO: Do Authentication of name and email address and call API to validate User
        //This is the Sign-In button for already registered users
        //Test conditions if username & password is not blank

        /**
         * Button Sign_in function
         * params : username, password, device_token
         */

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValid = true;

                String username = inputUserName.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
               // username="9876543210";
               // password="sachin";

                if (Services.isEmpty(username) || Services.isEmpty(password)) {
                    DialogFragment dialog = new NetworkStatusDialog();
                    Bundle args = new Bundle();
                    args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_authentication_failure_title));
                    args.putString(NetworkStatusDialog.ARG_MSG, getString(R.string.text_username_password_empty_msg));
                    dialog.setArguments(args);
                    dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                    dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                    Log.e(TAG, "Username empty!");

                    isValid = false;
                }
              else  if ( !Services.isValidMobileNo(username) && !Services.isEmailValid(username)) {
                        Log.v(TAG + "#Email", Services.isEmailValid(username) + "");

                        //Log.v(TAG + "#Mobile", Services.isValidMobileNo(username) + "");
                        DialogFragment dialog = new NetworkStatusDialog();
                        Bundle args = new Bundle();
                        args.putString(NetworkStatusDialog.ARG_TITLE, getString(R.string.text_authentication_failure_title));
                        args.putString(NetworkStatusDialog.ARG_MSG, getString(R.string.text_username_invalid_msg));
                        dialog.setArguments(args);
                        dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
                        dialog.show(getSupportFragmentManager(), "NetworkDialogFragment");
                        isValid = false;
                        Log.e(TAG, "Username Invalid!");
                    }


                    String login_url = properties.getProperty(Constants.API_URL.customer_login_url.name());

                    if (isValid) {
                        CallAPI callAPI = new CallAPI();
                        String response = callAPI.callCustomerLoginURL(properties.getProperty
                                        (Constants.API_URL.customer_login_url.name()), username, password,
                                device_token_id, LoginActivity.this);



                    } else return;
                    return;




            }
        });

        /*******************************************************************/


        /*********************CALL REGISTRATION PAGE ***********************/
        tvRegistr = (TextView) findViewById(R.id.tvRegister);
        tvRegistr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        /*******************************************************************/


    }

    private void initLocation() {
        gps = new LognSystemLocationService(LoginActivity.this);

        Log.d("","Rest btnSignIn gps.canGetLocation() "+gps.canGetLocation());
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            gps.getLocationAddressFromLatLong(latitude,longitude);
            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Your City is -  " + TooditApplication.getInstance().getPrefs().getCity(), Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }


    /**
     * CLick On Google IMAGE
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
            Log.d("","Rest onActivityResult requestCode "+requestCode+" RC_SIGN_IN"+RC_SIGN_IN);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("","Rest onActivityResult result "+result+" data"+data);
            Log.d("","Rest onActivityResult  result.isSuccess()"+result.isSuccess());

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                Log.d("","Rest onActivityResult  result.getSignInAccount()"+ result.getSignInAccount());
                GoogleSignInAccount account = result.getSignInAccount();
                if(account!=null){
                    Log.d("","Rest onActivityResult  account"+ account);
                    firebaseAuthWithGoogle(account);

                }

            } else {

                // Google Sign In failed, update UI appropriately
                DialogFragment dialog = new NetworkStatusDialog();
                Bundle args = new Bundle();
                args.putString("title", getString(R.string.text_google_error_title));
                args.putString("message", getString(R.string.text_google_error_msg));
                dialog.setArguments(args);
                dialog.setTargetFragment(dialog, Constants.REQUEST_CODE.RC_NETWORK_DIALOG.requestCode);
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
        Log.d(TAG, "Rest firebaseAuthWithGoogle:" + acct.getId());

     //   final SharedPreferences.Editor sharedPrefEditor = sharedpreferences.edit();

        //Check if Goog with same email-id already present
//        String checkFacebookEmail = sharedpreferences.getString(Constants.FacebookFields.FB_EMAIL_ID.name(), "");
        String checkFacebookEmail =TooditApplication.getInstance().getPrefs().getEmail();
//        Log.d(TAG, "Rest firebaseAuthWithGoogle: checkFacebookEmail" + checkFacebookEmail);
        Log.d(TAG, "Rest firebaseAuthWithGoogle: acct.getEmail()" + acct.getEmail());

        if ((checkFacebookEmail!=null)&&(!checkFacebookEmail.equals("")) && checkFacebookEmail.equals(acct.getEmail())) {
//            sharedPrefEditor.putBoolean(Constants.Shared.IS_SIMILAR_EMAILID.name(), true);
            TooditApplication.getInstance().getPrefs().setIsSimilarEmailID(true);
            TooditApplication.getInstance().getPrefs().setIsGoogleLogin(true);

        }
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Rest signInWithCredential:onComplete:Google:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Rest signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d(TAG, "Rest Google Login Success...");
                            String firebaseUID = mAuth.getCurrentUser().getUid();
//                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_UID.name(), firebaseUID);
                           TooditApplication.getInstance().getPrefs().setCustomer_id(firebaseUID);
//                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_DISPLAY_NAME.name(), acct.getDisplayName());
                            TooditApplication.getInstance().getPrefs().setName(acct.getDisplayName());

//                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_EMAIL_ID.name(), acct.getEmail());
                            TooditApplication.getInstance().getPrefs().setEmail(acct.getEmail());

//                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_GIVEN_NAME.name(), acct.getGivenName());
                            TooditApplication.getInstance().getPrefs().setFirst_Name(acct.getGivenName());

//                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_PHOTO_URL.name(), acct.getPhotoUrl().toString());
                            TooditApplication.getInstance().getPrefs().setImage(acct.getPhotoUrl().toString());

//                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_TOKEN_ID.name(), acct.getIdToken());
                            TooditApplication.getInstance().getPrefs().setGoogTokenId(acct.getIdToken());

//                            sharedPrefEditor.putString(Constants.GoogleFields.GOOG_SERVE_AUTHCODE.name(), acct.getServerAuthCode());
                            TooditApplication.getInstance().getPrefs().setGoogServerAuthcode(acct.getServerAuthCode());
                            TooditApplication.getInstance().getPrefs().setIsGoogleLogin(true);
                            TooditApplication.getInstance().getPrefs().setIsLogin(true);
//                            sharedPrefEditor.commit();

                            //start activity MainActivity.class
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            finish();
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
        Log.d(TAG, "handleFacebookAccessToken: sharedPrefEditor " + sharedPrefEditor);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d(TAG, "handleFacebookAccessToken: credential " + credential);
        Log.d(TAG, "handleFacebookAccessToken:  mAuth.signInWithCredential(credential) " +  mAuth.signInWithCredential(credential));

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
                        Log.d(TAG, "handleFacebookAccessToken: task.isSuccessful( " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            try {
                                Log.e(TAG, "signInWithCredential", task.getException());
                                throw task.getException();

                            } catch (FirebaseAuthUserCollisionException fe) {

                                //Google account already created in Firebase with same EmailId as Facebook EmailId
                                Log.e(TAG, "#handleFacebookAccessToken#FacebookAuthUserCollision - " + fe.getMessage());

//                                sharedPrefEditor.putBoolean(Constants.Shared.IS_SIMILAR_EMAILID.name(), true);
                                TooditApplication.getInstance().getPrefs().setIsSimilarEmailID(true);
                                TooditApplication.getInstance().getPrefs().setIsFacebookLogin(true);

                                TooditApplication.getInstance().getPrefs().setIsLogin(true);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
//                                finish();

                            } catch (Exception e) {
                                Log.e(TAG, "handleFacebookAccessToken#Facebook Login Exception - " + e.getMessage());
                            }

                        } else {
                            TooditApplication.getInstance().getPrefs().setIsLogin(true);
                            TooditApplication.getInstance().getPrefs().setIsFacebookLogin(true);

                            Log.d(TAG, "Facebook Login Successful...");
                            Log.d(TAG, "Starting MainActivity...");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
//                            finish();
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
                        TooditApplication.getInstance().getPrefs().setCustomer_id( fbuser.getId());

                        fbuser.setEmail(json.getString("email"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_EMAIL_ID.name(), fbuser.getEmail());
                        TooditApplication.getInstance().getPrefs().setEmail(fbuser.getEmail());

                        fbuser.setName(json.getString("name"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_NAME.name(), fbuser.getName());
                        TooditApplication.getInstance().getPrefs().setName(fbuser.getName());

                        fbuser.setFirst_name(json.getString("first_name"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_FIRST_NAME.name(), fbuser.getFirst_name());
                        TooditApplication.getInstance().getPrefs().setFirst_Name(fbuser.getFirst_name());

                        fbuser.setLast_name(json.getString("last_name"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_LAST_NAME.name(), fbuser.getLast_name());
                        TooditApplication.getInstance().getPrefs().setLast_Name(fbuser.getLast_name());

                        fbuser.setLink(json.getString("link"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_LINK.name(), fbuser.getLink());
                        TooditApplication.getInstance().getPrefs().setLink(fbuser.getLink());

                        fbuser.setPicture(json.getJSONObject("picture").getJSONObject("data").getString("url"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_PICTURE.name(), fbuser.getPicture());
                        TooditApplication.getInstance().getPrefs().setImage(fbuser.getPicture());
                        TooditApplication.getInstance().getPrefs().setPicture(fbuser.getPicture());

                        fbuser.setTimezone(json.getString("timezone"));
                        sharedPrefEditor.putString(Constants.FacebookFields.FB_TIME_ZONE.name(), fbuser.getTimezone());
                        TooditApplication.getInstance().getPrefs().setTimezone(fbuser.getTimezone());

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
    protected void onPause() {
        super.onPause();

//        initLocation();
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
