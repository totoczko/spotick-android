package com.example.spotick;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class AddActivity extends AppCompatActivity {

    // firebase
    private FirebaseDatabase database;
    private DatabaseReference databaseRef, usersRef;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String userColor;
    private FirebaseFunctions mFunctions;

    // geo
    private LocationManager locationManager;
    private Location location;
    private String city = "";
    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;

    // images
    final int REQUEST_CODE_CAMERA = 2;
    final int REQUEST_CODE_STORAGE= 3;
    final int OPEN_CAMERA = 4;
    final int OPEN_STORAGE= 5;
    private Uri imageUri;
    private ImageView imageView;
    String currentPhotoPath;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent_main = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                        Intent intent_user = new Intent(AddActivity.this, UserActivity.class);
                        startActivity(intent_user);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        auth = FirebaseAuth.getInstance();
        mFunctions = FirebaseFunctions.getInstance();
        final FirebaseUser currentUser = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("posts");
        usersRef = database.getReference("users");
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        imageView = (ImageView) findViewById(R.id.uploaded_image);
        ImageView camera_button = (ImageView) findViewById(R.id.camera_button);
        final TextInputLayout inputShortText = (TextInputLayout) findViewById(R.id.short_text_input);
        final TextInputLayout inputGeo = (TextInputLayout) findViewById(R.id.geo_input);
        Button btnSend = (Button) findViewById(R.id.add_post_button);

        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(AddActivity.this);
            }
        });

        // get city
        if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        }else{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                city = getLocation(location.getLatitude(), location.getLongitude());
                inputGeo.getEditText().setText(city);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //info
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

                if(imageUri != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(AddActivity.this);
                    progressDialog.setTitle("Dodawanie zdjęcia...");
                    progressDialog.show();

                    ref.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
//                                    Toast.makeText(activity.getApplicationContext(), "Dodano!", Toast.LENGTH_SHORT).show();

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

                                                startActivity(new Intent(AddActivity.this, MainActivity.class));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        startActivity(new Intent(AddActivity.this, LoginActivity.class));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddActivity.this, "Wystąpił błąd!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddActivity.this, "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

        });
    }

    private void selectImage(Context context) {

        final CharSequence[] options = { "Zrób zdjęcie", "Wybierz z galerii","Anuluj" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Wybierz zdjęcie");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Zrób zdjęcie")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                    "com.example.spotick.fileprovider",
                                    photoFile);
                            imageUri = photoURI;
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePicture, OPEN_CAMERA);
                        }
                    }


                } else if (options[item].equals("Wybierz z galerii")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , OPEN_STORAGE);

                } else if (options[item].equals("Anuluj")) {
                    dialog.dismiss();
                }
            }
        });

        if(ActivityCompat.checkSelfPermission(AddActivity.this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {android.Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }else if (ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
        }else{
            builder.show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        try{
                            city = getLocation(location.getLatitude(), location.getLongitude());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public String getLocation(double lat, double lon){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if(addresses.size() > 0){
                city = addresses.get(0).getLocality();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return city;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case OPEN_CAMERA:
                    if (resultCode == RESULT_OK) {
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                        Bitmap rotatedBitmap = rotate(bitmap);
                        imageView.setImageBitmap(rotatedBitmap);
                    }

                    break;
                case OPEN_STORAGE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        imageUri = selectedImage;
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap rotate(Bitmap decodedBitmap){
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        matrix.preScale(1,-1);
        return Bitmap.createBitmap(decodedBitmap, 0, 0, w, h, matrix, true);
    }

    private void writeNewPost(String id, String shortText, String geo, Long data, String imageId, String img, String userName, String userColor, String userId) {
        Post post = new Post(id, shortText, geo, data, imageId, img, (long) 0, null, userName, userColor, userId);
        databaseRef.child(id).setValue(post);
        databaseRef.child(id).child("user").child("id").setValue(userId);
        databaseRef.child(id).child("user").child("name").setValue(userName);
        databaseRef.child(id).child("user").child("color").setValue(userColor);
        databaseRef.child(id).child("likes").child("count").setValue(0);
    }

}
