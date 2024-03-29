package com.example.spotick;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent_main = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    return true;
                case R.id.navigation_dashboard:
                    if(currentUser != null){
                        Intent intent_add = new Intent(UserActivity.this, AddActivity.class);
                        startActivity(intent_add);
                    }else{
                        Intent intent_login = new Intent(UserActivity.this, LoginActivity.class);
                        startActivity(intent_login);
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(currentUser != null){
                        return true;
                    }else{
                        Intent intent_login = new Intent(UserActivity.this, LoginActivity.class);
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


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser != null){
            setContentView(R.layout.activity_user);

            String uid = currentUser.getUid();
            database = FirebaseDatabase.getInstance();
            usersRef = database.getReference().child("users").child(uid);

            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String name = user.getUsername();
                    String email = user.getEmail();
                    String color = user.getColor();
                    String avatar = String.valueOf(user.getUserFirstLetter());

                    TextView username_textview = findViewById(R.id.username);
                    TextView email_textview = findViewById(R.id.email);
                    TextView avatar_textview = findViewById(R.id.avatar);

                    username_textview.setText(name);
                    email_textview.setText(email);
                    avatar_textview.setText(avatar);

                    Drawable drawable = getResources().getDrawable(R.drawable.avatar);
                    drawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
                    avatar_textview.setBackgroundDrawable(drawable);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            usersRef.addValueEventListener(userListener);

            TabLayout tabLayout = findViewById(R.id.user_tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = findViewById(R.id.pager);
            final UserPostsTabsAdapter adapter = new UserPostsTabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        }else{
            Intent intent_login = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent_login);
        }

        // settings
        ImageButton settings = (ImageButton) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent_settings = new Intent(UserActivity.this, SettingsActivity.class);
                startActivity(intent_settings);
            }
        });

    }


}
