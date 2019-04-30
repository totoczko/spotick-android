package com.example.spotick;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword;
    private Button btnLogin;
    private FirebaseAuth auth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    return true;
                case R.id.navigation_dashboard:
                    if(currentUser != null){
                        Intent intent_add = new Intent(LoginActivity.this, AddActivity.class);
                        startActivity(intent_add);
                    }else{
                        return true;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(currentUser != null){
                        Intent intent_user = new Intent(LoginActivity.this, UserActivity.class);
                        startActivity(intent_user);
                    }else {
                        return true;
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

        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.login_button);
        inputEmail = (TextInputLayout) findViewById(R.id.email_input);
        inputPassword = (TextInputLayout) findViewById(R.id.password_input);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email = inputEmail.getEditText().getText().toString().trim();
                final String password = inputPassword.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "To pole jest wymagane!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Log.w("Login failed", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void switchToRegister(View v){
        Intent intent_register = new Intent(this, RegisterActivity.class);
        startActivity(intent_register);
    }

}
