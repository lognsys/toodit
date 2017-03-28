package com.lognsys.toodit;

import android.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lognsys.toodit.fragment.CartFragment;
import com.lognsys.toodit.fragment.HomeFragment;
import com.lognsys.toodit.fragment.NotificationFragment;
import com.lognsys.toodit.fragment.SettingFragment;
import com.lognsys.toodit.model.CityName;
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.FragmentTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String SELECTED_ITEM = "arg_selected_item";
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;
    private BottomNavigationViewEx mBottomNav;
    private int mSelectedItem; //index of bottom nagivation bar
    //    adding permission
    GoogleApiClient client;
    static final Integer LOCATION = 0x1;
    PendingResult<LocationSettingsResult> result;
    static final Integer GPS_SETTINGS = 0x7;

    LocationManager locationManager;
    LocationRequest mLocationRequest;
    LatLng latLng;
    boolean isGPSEnabled = false;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    MenuItem selectedItem = null;
    GoogleApiClient mGoogleApiClient;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First we need to check availability of play services

        mBottomNav = (BottomNavigationViewEx) findViewById(R.id.nav);
        mBottomNav.enableAnimation(false);
        mBottomNav.enableShiftingMode(false);
        mBottomNav.enableItemShiftingMode(false);


        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });


        if (savedInstanceState != null) {
            initLocationService(selectedItem,savedInstanceState);
        } else {
            initLocationService(selectedItem,savedInstanceState);
        }


    }

    private void initLocationService( MenuItem selectedItem, Bundle savedInstanceState) {

        if (checkPlayServices()) {
            bundle=savedInstanceState;
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                Log.d("TAG", "status initLocationService  locationManager" + locationManager + " isGPSEnabled " + isGPSEnabled);

                buildGoogleApiClient();

                setNetworkStatus();

                if (savedInstanceState != null) {
                    mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM);
                    selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
                } else {
                    selectedItem = mBottomNav.getMenu().getItem(0);
                }
                selectFragment(selectedItem);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void setNetworkStatus() {
        TextView tx = new TextView(this);
//        Log.d("TAG", "status setNetworkStatus checkLocationSettings() " + checkLocationSettings());

        tx.setText(String.valueOf(checkLocationSettings()));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("TAG", "status invokeService onConnected ");

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d("TAG", "status invokeService onConnected mLastLocation "+mLastLocation);
        if (mLastLocation != null) {
            //place marker at current position
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            GetLocationAddressFromLatLong(mLastLocation.getLatitude(), mLastLocation.getLongitude(),bundle,selectedItem);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000); //15 seconds
        mLocationRequest.setFastestInterval(15000); //10 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("TAG", "status invokeService onConnected mLastLocation inside ");

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        GetLocationAddressFromLatLong(location.getLatitude(), location.getLongitude(),bundle,selectedItem);
        //If you only need one location, unregister the listener
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    public void GetLocationAddressFromLatLong(double latitude, double longitude, Bundle savedInstanceState, MenuItem selectedItem) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            Address returnedAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");

            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
            }
            String city =returnedAddress.getLocality().toString();
            TooditApplication.getInstance().getPrefs().setCity(city);
            if(TooditApplication.getInstance().getPrefs().getCity()!=null){
                if (savedInstanceState != null) {
                    mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM);
                    selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
                } else {
                    selectedItem = mBottomNav.getMenu().getItem(0);
                }
                selectFragment(selectedItem);
            }
         /*   //Get address from Google api
            try {

                Double [] myTaskParams = { latitude, longitude};
                GetLocationByUrl a = new GetLocationByUrl();
                a.execute(myTaskParams);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }
    private class GetLocationByUrl extends AsyncTask<Double, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Fetching Address\nPlease wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        public String getJSONfromURL(String url, int timeout) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(url);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(timeout);
                c.setReadTimeout(timeout);
                c.connect();
                int status = c.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected String doInBackground(Double... arg0) {
            String response = "";
            try{
                String readJSON = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + arg0[0] + ","
                        + arg0[1] + "&sensor=true",6 * 10 * 1000);
                JSONObject jsonObj = new JSONObject(readJSON);
                String Status = jsonObj.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject location = Results.getJSONObject(0);
                    response =  location.getString("formatted_address");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            String text=(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }


    @SuppressLint("InlinedApi")
    public String checkLocationSettings() {
        //KitKat code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int locationMode = 0;
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                Log.d("", "locationMode:" + locationMode);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            Log.d("", "locationMode: Settings.Secure.LOCATION_MODE_OFF " + Settings.Secure.LOCATION_MODE_OFF);
            Log.d("", "locationMode: Settings.Secure.LOCATION_MODE_HIGH_ACCURACY " + Settings.Secure.LOCATION_MODE_HIGH_ACCURACY);
            Log.d("", "locationMode:" + locationMode);
            Log.d("", "locationMode:" + locationMode);

            if (locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                return "Accurate Settings. Mode: " + locationMode;
            } else {
                showSettingsAlert(true);
                return "IN_CORRECT_SETTINGS";
            }

        }
        else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                //System.out.println("In Show Setting Alert=>=>");
                showSettingsAlert(false);
                return "GPS_OFF";
            } else {
                return "GPS is ON";
            }

        }

    }

    private void showSettingsAlert(boolean isKitkat) {

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MainActivity.this);
        // Setting Dialog Title
        if (isKitkat) {
            alertDialog.setTitle("Location Mode settings");
            // Setting Dialog Message
            alertDialog.setMessage("High Accuracy mode is not enabled. Do you want to go to Location settings menu?");

        } else {
            alertDialog.setTitle("GPS settings");
            // Sendtting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        }

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                dialog.cancel();
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.d("TAG", "status buildGoogleApiClient  mGoogleApiClient" + mGoogleApiClient);

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleapiavailability = GoogleApiAvailability.getInstance();
        Log.d("", "status checkPlayServices googleapiavailability" + googleapiavailability);
        int i = googleapiavailability.isGooglePlayServicesAvailable(this);
        Log.d("", "status checkPlayServices i " + i);
        if (i != 0) {
            Log.d("", "status checkPlayServices googleapiavailability.isUserResolvableError(i) " + googleapiavailability.isUserResolvableError(i));
            if (googleapiavailability.isUserResolvableError(i)) {
                googleapiavailability.getErrorDialog(this, i, 1000).show();
                Log.d("", "status checkPlayServices googleapiavailability ");
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  this);
        }
    }

    /**
     * This will save the mSelectedItem value of bottom nav bar
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }




    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * select fragment gets item from bottom navigation bar
     * and displays fragment
     *
     * @param item
     */
    private void selectFragment(MenuItem item) {
        Fragment fragment = null;
        CallAPI callAPI = new CallAPI();
//       callAPI.invokeService(MainActivity.this);
        String fragmentTag = "";

        switch (item.getItemId()) {
            case R.id.menu_home:
                item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                fragment = HomeFragment.newInstance();
                fragmentTag = FragmentTag.FRAGMENT_HOME.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);

                break;
            case R.id.menu_notifications:
                item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                 /*fragment = NotificationFragment.newInstance(getString(R.string.text_notification),

                        getColorFromRes(R.color.colorAccent));*/
                fragment = new NotificationFragment();

                fragmentTag = FragmentTag.FRAGMENT_NOTIFICATION.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);

                break;
            case R.id.menu_cart:
                item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                fragment = new CartFragment();
                fragmentTag = FragmentTag.FRAGMENT_CART.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);

                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();*/
                break;
            case R.id.menu_settings:
                item.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                fragment = new SettingFragment();
                fragmentTag = FragmentTag.FRAGMENT_SETTING.getFragmentTag();
                callAPI.updateToolbarText(fragmentTag,MainActivity.this);

                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();


        // uncheck the other items.
        for (int i = 0; i < mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);

            if (menuItem.getItemId() != mSelectedItem) {
                menuItem.setChecked(false);
                menuItem.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
        }

        //Update ActionBar Based on Fragment
        //updateToolbarText(item.getTitle());

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.container, fragment, fragmentTag);
              ft.replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

//            ft.commit();
        }
    }

   /* //Update Action Bar
    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            actionBar.setLogo(R.drawable.toodit_logo);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }*/

    private int getColorFromRes(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }

    @Override
    public void onStop() {
        super.onStop();
//        client.disconnect();
    }


}

