package com.lognsys.toodit.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lognsys.toodit.LoginActivity;
import com.lognsys.toodit.MainActivity;
import com.lognsys.toodit.R;
import com.lognsys.toodit.TooditApplication;
import com.lognsys.toodit.adapter.ProfileListAdapter;
import com.lognsys.toodit.util.Constants;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SettingFragment extends Fragment {

    private static final String ARG_NAME = "username";
    private static final String ARG_LOC = "location";
    private String name;
    private String loc;
    private Button logout;
    private Button update;
    private ImageView profile_image;
    //Google API variable
    private GoogleApiClient mGoogleApiClient;
    ListView profileListView;
    String[] titles;
    TextView textName;

    //Profile listview images in order
    Integer[] imgid = {
            R.drawable.bag,
            R.drawable.message,
            R.drawable.privacy_policy,
            R.drawable.terms_cond,
            R.drawable.transperant_image,
            R.drawable.transperant_image,
            R.drawable.transperant_image
    };

    public SettingFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            loc = getArguments().getString(ARG_LOC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        logout = (Button) view.findViewById(R.id.logout);
        update = (Button) view.findViewById(R.id.edit_profile);
        profile_image=(ImageView)view.findViewById(R.id.profile_image);
        textName=(TextView) view.findViewById(R.id.textName);
        if(TooditApplication.getInstance().getPrefs().getName()!=null){
            textName.setText(TooditApplication.getInstance().getPrefs().getName());
        }
        if(TooditApplication.getInstance().getPrefs().getImage()!=null){
            Picasso.with(getContext()).load(TooditApplication.getInstance().getPrefs().getImage()).into(profile_image);
            profile_image.setVisibility(View.VISIBLE);
        }
//        new SettingFragment.ImageLoadTask(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.FacebookFields.FB_PICTURE.name(), ""),profile_image ).execute();
//
//        new SettingFragment.ImageLoadTask(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.GoogleFields.GOOG_PHOTO_URL.name(), ""),profile_image ).execute();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new UpdateFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //firebase variable declaration
                             if(TooditApplication.getInstance().getPrefs().getIsFacebookLogin()){
                                 FirebaseAuth.getInstance().signOut();
                                 LoginManager.getInstance().logOut();
                             }
                             if(TooditApplication.getInstance().getPrefs().getIsGoogleLogin()){
                                 Log.d("","Rest logging out getIsGoogleLogin "+ Auth.GoogleSignInApi.signOut(mGoogleApiClient));
                              Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                 mGoogleApiClient.disconnect();
                                 mGoogleApiClient=null;
                             }
                                TooditApplication.getInstance().getPrefs().setIsGoogleLogin(false);
                                TooditApplication.getInstance().getPrefs().setIsFacebookLogin(false);
                                TooditApplication.getInstance().getPrefs().setIsLogin(false);
                                TooditApplication.getInstance().getPrefs().setCustomer_id(null);
                                TooditApplication.getInstance().getPrefs().setName(null);
                                TooditApplication.getInstance().getPrefs().setMobile(null);
                                TooditApplication.getInstance().getPrefs().setEmail(null);
                                TooditApplication.getInstance().getPrefs().setCity(null);
                                TooditApplication.getInstance().getPrefs().setImage(null);
                                TooditApplication.getInstance().getPrefs().setTimezone(null);
                                TooditApplication.getInstance().getPrefs().setPicture(null);
                                TooditApplication.getInstance().getPrefs().setLink(null);
                                TooditApplication.getInstance().getPrefs().setLast_Name(null);
                                TooditApplication.getInstance().getPrefs().setGoogServerAuthcode(null);
                                TooditApplication.getInstance().getPrefs().setGoogTokenId(null);
                                TooditApplication.getInstance().getPrefs().setIsSimilarEmailID(false);

                                Log.d("","Rest logging out getcontext"+getContext());
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getContext().startActivity(intent);


//                                SharedPreferences settings = getActivity().getSharedPreferences(Constants.Shared.TOODIT_SHARED_PREF.name(), Context.MODE_PRIVATE);
//                                settings.edit().clear().commit();
                              /*  Intent i = new Intent(getActivity(), LoginActivity.class);
//                                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);*/


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return view;
    }

    private void logoutFaceBook() {
        FirebaseAuth.getInstance().signOut();
//      Firebase sign out
        LoginManager.getInstance().logOut();
    }

    private void logoutGoogle() {

    }

    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ((MainActivity) getActivity()).getSupportActionBar().hide();
        ProfileListAdapter adapter = new ProfileListAdapter(getActivity(), titles, imgid);
        profileListView = (ListView) getView().findViewById(R.id.list);
        profileListView.setAdapter(adapter);
        profileListView.setDivider(null);
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedTitle = titles[+position];
               //Toast.makeText(getActivity(), selectedTitle, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        titles = new String[4];
        titles[0] = getContext().getResources().getString(R.string.text_profile_title_order);
        titles[1] = getContext().getResources().getString(R.string.text_profile_title_notification);
        titles[2] = getContext().getResources().getString(R.string.text_profile_title_privacy);
        titles[3] = getContext().getResources().getString(R.string.text_profile_title_tc);

    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  mViewHolder.progressView15.setType(GeometricProgressView.TYPE.TRIANGLE);
            mViewHolder.progressView15.setFigurePaddingInDp(1);
            mViewHolder.progressView15.setNumberOfAngles(30);
            mViewHolder.progressView15.setVisibility(View.VISIBLE);*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result!=null) {
                imageView.setImageBitmap(result);
            }
            //mViewHolder.progressView15.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);

        }
    }
    public class ImageConverter {

        public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }

}