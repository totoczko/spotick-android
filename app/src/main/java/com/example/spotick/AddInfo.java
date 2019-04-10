package com.example.spotick;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddInfo  extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth auth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_info, container, false);

        // sending post to firebase
        Button btnSend = (Button) view.findViewById(R.id.add_post_button);

        final TextInputLayout inputShortText = (TextInputLayout) view.findViewById(R.id.short_text_input);
        final TextInputLayout inputGeo = (TextInputLayout) view.findViewById(R.id.geo_input);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("posts");
        auth = FirebaseAuth.getInstance();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String shortText = inputShortText.getEditText().getText().toString().trim();
                final String geo = inputGeo.getEditText().getText().toString().trim();
                long data = 1554216329192L;
                String id = "16326f51-b2c9-41f4-9911-bca110c03e97";
                String imageId = "a538f0b9-0417-48d4-ab44-0a0eae5c3d63";
                String img = "https://firebasestorage.googleapis.com/v0/b/spot-pwa.appspot.com/o/images%2Fc538f0b9-0417-48d4-ab44-0a0eae5c3d63?alt=media&token=4fc1fa98-d38c-47c9-b156-036691e7e0ed";


                if (TextUtils.isEmpty(shortText) || TextUtils.isEmpty(geo)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    String userName = currentUser.getDisplayName();
                    String userColor = "#989898";

                    writeNewPost(id, shortText, geo, data, imageId, img, userName, userColor, userId);

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            }

        });

        return view;
    }

    private void writeNewPost(String id, String shortText, String geo, Long data, String imageId, String img, String userName, String userColor, String userId) {
        Post post = new Post(id, shortText, geo, data, imageId, img, (long) 0, userName, userColor, userId);
        databaseRef.child(id).setValue(post);
        databaseRef.child(id).child("user").child("id").setValue(userId);
        databaseRef.child(id).child("user").child("name").setValue(userName);
        databaseRef.child(id).child("user").child("color").setValue(userColor);
        databaseRef.child(id).child("likes").child("count").setValue(0);
    }
}