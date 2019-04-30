package com.example.spotick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PostActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent_main = new Intent(PostActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    return true;
                case R.id.navigation_dashboard:
                    if(currentUser != null){
                        Intent intent_add = new Intent(PostActivity.this, AddActivity.class);
                        startActivity(intent_add);
                    }else{
                        Intent intent_login = new Intent(PostActivity.this, LoginActivity.class);
                        startActivity(intent_login);
                    }
                    return true;
                case R.id.navigation_notifications:
                    if(currentUser != null){
                        Intent intent_user = new Intent(PostActivity.this, UserActivity.class);
                        startActivity(intent_user);
                    }else{
                        Intent intent_login = new Intent(PostActivity.this, LoginActivity.class);
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
        setContentView(R.layout.activity_post);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Bundle extras = getIntent().getExtras();

        ImageView postImage;
        TextView shortText, likesCounter, location, date, userName, avatar;

        final String post_id = (String) extras.get("post_id");
        String post_short_text = (String) extras.get("post_short_text");
        String post_geo = (String) extras.get("post_geo");
        Long post_data = (Long) extras.get("post_data");
        String post_image_id = (String) extras.get("post_image_id");
        String post_img = (String) extras.get("post_img");
        Long post_likes = (Long) extras.get("post_likes");
        String post_user_name = (String) extras.get("post_user_name");
        String post_user_color = (String) extras.get("post_user_color");
        String post_user_id = (String) extras.get("post_user_id");

        Post post = new Post(post_id, post_short_text, post_geo, post_data, post_image_id, post_img, post_likes, post_user_name, post_user_color, post_user_id);

        postImage = findViewById(R.id.post_image);
        shortText = findViewById(R.id.short_text);
        likesCounter = findViewById(R.id.likes_counter);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        userName = findViewById(R.id.username);
        avatar = findViewById(R.id.avatar);

        Glide.with(this).load(post.getImg())
                .into(postImage);
        shortText.setText(post.getShortText());
        likesCounter.setText(String.valueOf(post.getLikesCount()));
        location.setText(post.getGeo());
        date.setText(post.getDataString());
        userName.setText(post.getUserName());
        avatar.setText(String.valueOf(post.getUserFirstLetter()));
        String color = post.getUserColor();
        Drawable drawable = getResources().getDrawable(R.drawable.avatar);
        drawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        avatar.setBackgroundDrawable(drawable);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            database = FirebaseDatabase.getInstance();
            databaseRef = database.getReference("posts");
            ImageButton settings = findViewById(R.id.post_settings_button);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);

                    final String[] options = {"Usuń", "Anuluj"};

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String item = options[which];
                            if(item == "Usuń"){
                                databaseRef.child(post_id).removeValue();
                                Toast.makeText(PostActivity.this, "Usunięto", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
            });
        }


    }

}


