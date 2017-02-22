package com.lognsys.toodit;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by pdoshi on 20/02/17.
 */

public class TooditApplication extends Application {

    private FirebaseAuth firebaseAuth;

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;

    }

    public FirebaseAuth getFirebaseAuth() {
        return this.firebaseAuth;
    }

}
