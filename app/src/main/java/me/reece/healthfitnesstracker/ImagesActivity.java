package me.reece.healthfitnesstracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImagesActivity extends AppCompatActivity {

    private Button ChooseImage, cameraCapture, UploadImage, viewUploads;
    private StorageReference mStorage;
    String userID;
    private ProgressDialog PD;

    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 3;
    private EditText ImageName;
    private ImageView mImageView;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStorage = FirebaseStorage.getInstance().getReference();


        // BTN CHOOSE IMAGE
        ChooseImage = findViewById(R.id.btnChooseImage);
        // IMAGE NAME
        ImageName = findViewById(R.id.editTextChooseImageName);
        // CHOSEN IMAGE
        mImageView = findViewById(R.id.imageViewChosenImage);
        // UPLOAD CHOSEN IMAGE
        UploadImage = findViewById(R.id.btnUploadImage);
        // VIEW UPLOADS
        viewUploads = findViewById(R.id.btnViewUpload);

        // BTN CLICK ON UPLOAD NEW IMAGE
        ChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();

            }
        });

        // UPLOAD CHOSEN IMAGE
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PD.setMessage("Uploading...");
                PD.setCancelable(true);
                PD.setCanceledOnTouchOutside(false);

                PD.show();
                Upload();
            }
        });

        // VIEW UPLOADS
        viewUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ImagesActivity.this,"THIS FEATURE IS A WORK IN PROGRESS", Toast.LENGTH_LONG).show();
                DisplayImages();
            }
        });


        // BTN ON CLICK CAMERA - THIS DOES CRASH THE APP
        cameraCapture = findViewById(R.id.btnCameraCapture);
        cameraCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, CAMERA_PERMISSION_CODE);

//                if(ContextCompat.checkSelfPermission(ImagesActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(ImagesActivity.this,"You already have permissions", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(i, CAMERA_PERMISSION_CODE);
//
//                }else{
//
//                    requestCameraPermission();
//                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(i, CAMERA_PERMISSION_CODE);
//                }

            }
        });
    }

    private void openFileChooser(){
        Intent i = new Intent((Intent.ACTION_PICK));

        i.setType("image/*");

        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }

    private void Upload(){

        try {
            // IMAGE HAS NAME
            if (ImageName.getText().toString().length() > 0) {

                StorageReference sRef = mStorage.child("Images").child(userID).child(ImageName.getText().toString());

                mImageView.setImageURI(mUri);
                sRef.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ImagesActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                        PD.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        PD.dismiss();
                        Toast.makeText(ImagesActivity.this, "Upload Incomplete", Toast.LENGTH_LONG).show();
                    }
                });

            } else if (ImageName.getText().toString().length() <= 0) {
                ImageName.setError("image needs a name");
            }
        }catch (Exception ex){
            ImageName.setError("No image selected");
            ChooseImage.setError("No image selected");
        }
        PD.dismiss();

    }

    private void DisplayImages(){
        Intent i = new Intent(this, ViewImagesActivity.class);
        startActivity(i);
    }

    // CAMERA PERMISSION
    private void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("This permission is needed for camera capture")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(ImagesActivity.this, new String [] {Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE );
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
        }else{
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if(requestCode == CAMERA_PERMISSION_CODE){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(ImagesActivity.this,"Permission Granted", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(ImagesActivity.this,"Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        PD = new ProgressDialog(this);
        PD.setMessage("Uploading . . .");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);


        //  GALLERY UPLOAD
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            PD.show();

            mUri = data.getData();
            FirebaseAuth mAuth =  FirebaseAuth.getInstance();
            final FirebaseUser firebaseUser = mAuth.getCurrentUser();
            userID = firebaseUser.getUid();

            mImageView.setImageURI(mUri);
            PD.dismiss();
        }


        // TODO : FIX ERROR WITH CAMERA CAPTURE
        // UPLOAD CAMERA CAPTURE - UNSUCCESSFUL
//        if(requestCode == CAMERA_PERMISSION_CODE && resultCode == RESULT_OK){
//
//            //PD.show();
//
//            Uri uri = data.getData();
//            FirebaseAuth mAuth =  FirebaseAuth.getInstance();
//            final FirebaseUser firebaseUser = mAuth.getCurrentUser();
//            userID = firebaseUser.getUid();
//
//            StorageReference sRef = mStorage.child("Images").child(userID).child(uri.getLastPathSegment());
//
//            sRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    //PD.dismiss();
//                    Toast.makeText(ImagesActivity.this,"Upload Complete", Toast.LENGTH_LONG).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    //PD.dismiss();
//                    Toast.makeText(ImagesActivity.this,"Upload Incomplete", Toast.LENGTH_LONG).show();
//                }
//            });
//        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //getActionBar().setTitle("Hello world App");
        getSupportActionBar().setTitle("Images");
    }

    // Menu expand
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Menu options selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings: // go to settings
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.sign_out: // sign current user out
                //currentUser = null;;
                return true;
            case R.id.profile: // Go to user profile
                Intent j = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(j);
                return true;
            case android.R.id.home:
                Intent h = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(h);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
