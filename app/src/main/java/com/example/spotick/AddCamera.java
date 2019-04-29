package com.example.spotick;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class AddCamera extends Fragment implements SurfaceHolder.Callback {

    private static Uri tempUri;

    ImageView btnCapture;
    Camera camera;
    SurfaceView imgCapture;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback jpegCallback;
    ImageView preview;

    final int REQUEST_CODE= 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add_camera, container, false);

        btnCapture =(ImageView) view.findViewById(R.id.camera_button);
        imgCapture = (SurfaceView)  view.findViewById(R.id.camera);

        surfaceHolder = imgCapture.getHolder();
        if(ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.CAMERA}, REQUEST_CODE);
        }else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

        }else{
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                captureImage();
            }
        });


        jpegCallback = new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                preview = view.findViewById(R.id.camera_preview);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                Bitmap rotatedBitmap = rotate(decodedBitmap);

                preview.setImageBitmap(rotatedBitmap);
                camera.stopPreview();
                camera.release();

                tempUri = getImageUri(getActivity().getApplicationContext(), rotatedBitmap);
            }
        };

        return view;

    }

    private void captureImage(){
        camera.takePicture(null, null, jpegCallback);
    }

    private Bitmap rotate(Bitmap decodedBitmap){
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(270);
        matrix.preScale(1,-1);
        return Bitmap.createBitmap(decodedBitmap, 0, 0, w, h, matrix, true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        Camera.Size size = camera.new Size(640, 480);
        Camera.Parameters parameters;
        parameters = camera.getParameters();
        camera.setDisplayOrientation(90);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.startPreview();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    surfaceHolder.addCallback(this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                }else{
                    Toast.makeText(getContext(), "Brak dostępu do kamery!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG,100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Uri getCapturedImage() {
        return tempUri;
    }
}
