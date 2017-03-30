package com.lognsys.toodit;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.lognsys.toodit.util.CallAPI;
import com.lognsys.toodit.util.Preference;

/**
 * Created by pdoshi on 20/02/17.
 */

public class TooditApplication extends Application {

    private FirebaseAuth firebaseAuth;
    Preference prefs;
    static TooditApplication tooditApplication;
    PendingIntent pendingIntent;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        tooditApplication = this;
        prefs = new Preference(this);

    }
    public static TooditApplication getInstance()
    {
        return tooditApplication;
    }
    public Preference getPrefs() {
        return prefs;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;

    }

    public FirebaseAuth getFirebaseAuth() {
        return this.firebaseAuth;
    }

   /*@TargetApi(Build.VERSION_CODES.N)
    public void invokeService(Context context) {
        Log.d("TAG", "status invokeService");
       Intent serviceIntent = new Intent(context, LognsystemLocationService.class);

        context.startService(serviceIntent);

        pendingIntent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 00);
       alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*10, pendingIntent);

    }*/

}
