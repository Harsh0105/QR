package com.example.qr;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class covidqr2 extends Application {



    @Override
    public void onCreate() {
        super.onCreate();


        getInstance().setPersistenceEnabled(true);
    }
}