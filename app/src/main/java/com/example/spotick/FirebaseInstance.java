package com.example.spotick;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseInstance extends android.app.Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
