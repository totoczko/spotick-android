package com.example.spotick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create channel to show notifications.
//            String channelId  = "Spotick";
//            String channelName = "Spotick";
//            NotificationManager notificationManager =
//                    getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
//                    channelName, NotificationManager.IMPORTANCE_LOW));
//        }
//
//        // If a notification message is tapped, any data accompanying the notification
//        // message is available in the intent extras. In this sample the launcher
//        // intent is fired when the notification is tapped, so any accompanying data would
//        // be handled here. If you want a different intent fired, set the click_action
//        // field of the notification message to the desired intent. The launcher intent
//        // is used when no click_action is specified.
//        //
//        // Handle possible data accompanying notification message.
//        // [START handle_data_extras]
//        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Log.d(TAG, "Key: " + key + " Value: " + value);
//            }
//        }
//        // [END handle_data_extras]
//
//        Button subscribeButton = findViewById(R.id.subscribeButton);
//        subscribeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Subscribing to weather topic");
//                // [START subscribe_topics]
//                FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                String msg = "Subscribed!";
//                                if (!task.isSuccessful()) {
//                                    msg = "Failed!";
//                                }
//                                Log.d(TAG, msg);
//                                Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                // [END subscribe_topics]
//            }
//        });
//
//        Button logTokenButton = findViewById(R.id.logTokenButton);
//        logTokenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get token
//                // [START retrieve_current_token]
//                String token = FirebaseInstanceId.getInstance().getToken();
//                // Log and toast
//                String msg = "New token " + token;
//                Log.d(TAG, msg);
//                Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
//                // [END retrieve_current_token]
//            }
//        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            String uid = currentUser.getUid();
            database = FirebaseDatabase.getInstance();
            usersRef = database.getReference().child("users").child(uid);

        }
    }

}