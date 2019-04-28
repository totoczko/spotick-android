package com.example.spotick;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.UUID;

public class AddInfo  extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef, usersRef;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String userColor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_info, container, false);

        final AddActivity activity = (AddActivity) getActivity();
        final TextInputLayout inputShortText = (TextInputLayout) view.findViewById(R.id.short_text_input);
        final TextInputLayout inputGeo = (TextInputLayout) view.findViewById(R.id.geo_input);
        Button btnSend = (Button) view.findViewById(R.id.add_post_button);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("posts");
        usersRef = database.getReference("users");
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // autofill current location
        inputGeo.getEditText().setText(activity.getCity());

        // add post to firebase
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String shortText = inputShortText.getEditText().getText().toString().trim();
                final String geo = inputGeo.getEditText().getText().toString().trim();
                Date date= new Date();
                final long data = date.getTime();
                final String id = UUID.randomUUID().toString();
                final String imageId =  UUID.randomUUID().toString();
                final String img = "https://firebasestorage.googleapis.com/v0/b/spot-pwa.appspot.com/o/images%2F" + imageId + "?alt=media";
                StorageReference ref = storageReference.child("images/" + imageId);
                Uri filePath = activity.getImage();

                if(filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Dodawanie zdjęcia...");
                    progressDialog.show();

                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity.getApplicationContext(), "Dodano!", Toast.LENGTH_SHORT).show();

                                    if (currentUser != null) {
                                        final String userId = currentUser.getUid();
                                        final String userName = currentUser.getDisplayName();
                                        DatabaseReference colorRef = usersRef.child(userId).child("color");
                                        colorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                userColor = dataSnapshot.getValue(String.class);
                                                if(userColor != null){
                                                    writeNewPost(id, shortText, geo, data, imageId, img, userName, userColor, userId);
                                                }

                                                startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                        getActivity().finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity.getApplicationContext(), "Wystąpił błąd!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }


                if (TextUtils.isEmpty(shortText) || TextUtils.isEmpty(geo)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
                    return;
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