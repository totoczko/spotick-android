package com.example.spotick;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword, inputUsername;
    private Button btnRegister;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent_main = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    return true;
                case R.id.navigation_dashboard:
                    if(currentUser != null){
                        Intent intent_add = new Intent(RegisterActivity.this, AddActivity.class);
                        startActivity(intent_add);
                    }else{
                        Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent_login);
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(currentUser != null){
                        Intent intent_user = new Intent(RegisterActivity.this, UserActivity.class);
                        startActivity(intent_user);
                    }else{
                        Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent_login);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.register_button);
        inputEmail = (TextInputLayout) findViewById(R.id.email_input);
        inputPassword = (TextInputLayout) findViewById(R.id.password_input);
        inputUsername = (TextInputLayout) findViewById(R.id.username_input);
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("users");

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email = inputEmail.getEditText().getText().toString().trim();
                final String password = inputPassword.getEditText().getText().toString().trim();
                final String username = inputUsername.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "To pole jest wymagane!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = auth.getCurrentUser();
                           if(currentUser != null){
                               String id =  currentUser.getUid();
                               String color = generateColor();

                               writeNewUser(id, email, username, color);

                               UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                       .setDisplayName(username)
                                       .build();

                               currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                       finish();
                                   }
                               });
                           }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void switchToLogin(View v){
        Intent intent_login = new Intent(this, LoginActivity.class);
        startActivity(intent_login);
    }

    private void writeNewUser(String id, String email, String username, String color) {
        User user = new User(id, email, username, color);
        databaseRef.child(id).setValue(user);
    }
    
    private String generateColor() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        String color = String.format("#%06x", nextInt);
        return color;
    }
    

}
