package com.example.multitakerapp.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multitakerapp.Model.UserHostelDetail;
import com.example.multitakerapp.R;
import com.example.multitakerapp.databinding.ActivityAddHostelBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddHostelActivity extends AppCompatActivity {

    ActivityAddHostelBinding binding;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;
    private static final int GALLERY_REQUEST = 1;

    private int uploads = 0;
    private Uri mImageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    public static final int RESULT_IMAGE = 10;
    private List<String> fileNameList;
    private List<String> fileDoneList;


    private FirebaseAuth firebaseAuth;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddHostelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(AddHostelActivity.this, LoginMainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);

                }
            }
        };
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("AddHostel").child(firebaseAuth.getUid());

        binding.addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_IMAGE);
            }
        });

        binding.submitHostelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String st_hostel_name = binding.hostelnameOutlinedTextField.getEditText().getText().toString().trim();
                final String st_contact_no = binding.hostelcontactOutlinedTextField.getEditText().getText().toString().trim();
                final String st_area = binding.hostelareaOutlinedTextField.getEditText().getText().toString().trim();
                final String st_rent = binding.hostelrentOutlinedTextField.getEditText().getText().toString().trim();
                final String st_no_of_rooms = binding.hostelroomOutlinedTextField.getEditText().getText().toString().trim();

                UserHostelDetail userHostelDetail = new UserHostelDetail(st_hostel_name, st_contact_no, st_area, st_rent, st_no_of_rooms);
                mDatabase.setValue(userHostelDetail);


                /*startPosting();*/
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_IMAGE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                //Toast.makeText(this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();
                int totalItems = data.getClipData().getItemCount();
                for (int i = 0; i < totalItems; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);

                    StorageReference fileToUpload = mStorageReference.child("hostel_Images").child(firebaseAuth.getUid()).child(fileName);
                    final int final_i = i;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddHostelActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else if (data.getData() != null) {
                Toast.makeText(this, "Selected Single File", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void requestPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        PermissionListener dialogPermissionListener =
                                DialogOnDeniedPermissionListener.Builder
                                        .withContext(AddHostelActivity.this)
                                        .withTitle("Read External Storage permission")
                                        .withMessage("Read External Storage  permission is needed")
                                        .withButtonText(android.R.string.ok)
                                        .withIcon(R.mipmap.ic_launcher)
                                        .build();


//                        Intent permIntent = new Intent();
//                        permIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package",getPackageName(),null);
//                        permIntent.setData(uri);
//                        startActivity(permIntent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


    /*private void startPosting() {


        imagename.putFile(ImageList.get(uploads)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mProgress.dismiss();

                        String url = String.valueOf(uri);
                        SendLink(url, st_hostel_name, st_contact_no, st_area, st_rent, st_no_of_rooms);
                    }
                });
      *//*  final String st_hostel_name = binding.hostelnameOutlinedTextField.getEditText().getText().toString().trim();
        final String st_contact_no = binding.hostelcontactOutlinedTextField.getEditText().getText().toString().trim();
        final String st_area = binding.hostelareaOutlinedTextField.getEditText().getText().toString().trim();
        final String st_rent = binding.hostelrentOutlinedTextField.getEditText().getText().toString().trim();
        final String st_no_of_rooms = binding.hostelroomOutlinedTextField.getEditText().getText().toString().trim();


        if (!TextUtils.isEmpty(st_hostel_name) && !TextUtils.isEmpty(st_contact_no) && mImageUri != null) {

            mProgress.show();

            StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadurl = Uri.parse(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());


                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("hostel_name").setValue(st_hostel_name);
                    newPost.child("contact_no").setValue(st_contact_no);

                    newPost.child("area").setValue(st_area);
                    newPost.child("rent").setValue(st_rent);
                    newPost.child("rooms").setValue(st_no_of_rooms);

                    newPost.child("images").setValue(downloadurl.toString());
                    mProgress.dismiss();

                    finish();

                }
            });
*//*
     *//*}*//*

            }
        });

    }*/

    private void SendLink(String url, String st_hostel_name, String contactNo, String st_area, String rent, String rooms) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("link", url);
        hashMap.put("hostel_name", st_hostel_name);
        hashMap.put("contactNo", contactNo);
        hashMap.put("area", st_area);
        hashMap.put("rent", rent);
        hashMap.put("rooms", rooms);
        mDatabase.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mProgress.dismiss();
                binding.errorTextview.setText("Image Uploaded Successfully");
                /*binding.submitHostelBtn.setVisibility(View.GONE);*/
                ImageList.clear();
            }
        });


    }
}