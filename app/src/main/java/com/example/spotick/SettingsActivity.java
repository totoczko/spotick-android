package com.example.spotick;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            String uid = currentUser.getUid();
            database = FirebaseDatabase.getInstance();
            usersRef = database.getReference().child("users").child(uid);

            final String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            final TextView currentLogin = findViewById(R.id.login_h);
            final TextView currentEmail = findViewById(R.id.email_h);

            currentLogin.setText(name);
            currentEmail.setText(email);

            Button saveLogin = findViewById(R.id.change_login);
            final EditText editLogin = findViewById(R.id.edit_login);
            Button saveEmail = findViewById(R.id.change_email);
            final EditText editEmail = findViewById(R.id.edit_email);
            Button savePassword = findViewById(R.id.change_password);
            final EditText editPassword = findViewById(R.id.edit_password);

            saveLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final String newLogin = String.valueOf(editLogin.getText());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newLogin)
                            .build();

                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        currentLogin.setText(newLogin);
                                        Toast.makeText(SettingsActivity.this, "Profil zaktualizowany!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            saveEmail.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final String newEmail = String.valueOf(editEmail.getText());
                    currentUser.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        currentEmail.setText(newEmail);
                                        Toast.makeText(SettingsActivity.this, "Profil zaktualizowany!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SettingsActivity.this, "Zaloguj się ponownie aby móc zmienić email!", Toast.LENGTH_SHORT).show();
                                        Intent intent_login = new Intent(SettingsActivity.this, LoginActivity.class);
                                        startActivity(intent_login);
                                    }
                                }
                            });
                }
            });


            savePassword.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    final String newPassword = String.valueOf(editPassword.getText());
                    currentUser.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "Profil zaktualizowany!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SettingsActivity.this, "Zaloguj się ponownie aby móc zmienić hasło!", Toast.LENGTH_SHORT).show();
                                        Intent intent_login = new Intent(SettingsActivity.this, LoginActivity.class);
                                        startActivity(intent_login);
                                    }
                                }
                            });
                }
            });


            Button signOut = findViewById(R.id.sign_out);

            signOut.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    Toast.makeText(SettingsActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                    Intent intent_main = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    finish();
                }
            });

        }
    }

}