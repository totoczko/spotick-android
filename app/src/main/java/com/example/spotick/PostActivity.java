package com.example.spotick;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PostActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent_main = new Intent(PostActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent_add = new Intent(PostActivity.this, AddActivity.class);
                    startActivity(intent_add);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent_user = new Intent(PostActivity.this, UserActivity.class);
                    startActivity(intent_user);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle extras = getIntent().getExtras();
        Post post = (Post) extras.get("post");
        Toast.makeText(this, "Clicked " + post.getLikesCount(), Toast.LENGTH_SHORT).show();

        ImageView postImage;
        TextView shortText, likesCounter, location, date, userName, avatar;

        postImage = findViewById(R.id.post_image);
        shortText = findViewById(R.id.short_text);
        likesCounter = findViewById(R.id.likes_counter);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        userName = findViewById(R.id.username);
        avatar = findViewById(R.id.avatar);

//        Glide.with(mContext)
//                .load(post.getImg())
//                .into(postImage);
        shortText.setText(post.getShortText());
        likesCounter.setText(String.valueOf(post.getLikesCount()));
        location.setText(post.getGeo());
        date.setText(post.getDataString());
        userName.setText(post.getUserName());
//        avatar.setText(String.valueOf(post.getUserFirstLetter()));
//        String color = post.getUserColor();
//        Drawable drawable = mContext.getResources().getDrawable(R.drawable.avatar);
//        drawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
//        avatar.setBackgroundDrawable(drawable);

    }

}
