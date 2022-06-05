package com.example.multitakerapp.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.multitakerapp.Model.UserProfile;
import com.example.multitakerapp.R;
import com.example.multitakerapp.Utlis.Constants;
import com.example.multitakerapp.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    FragmentSignUpBinding binding;

    /*static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;*/

    private ProgressDialog progressDialog;
    private StorageReference ProductImagesRef;
    String selectUser;
    DatabaseReference firebaseDatabase;
    FirebaseAuth auth;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        // ProductImagesRef = FirebaseStorage.getInstance().getReference().child("All Images");
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("LoginInfo");


        selectUser = getActivity().getIntent().getStringExtra(Constants.SELECT_USER);

        if (selectUser.contains(Constants.USER)) {
            binding.cityOutlinedTextField.setVisibility(View.GONE);
        } else if (selectUser.contains(Constants.CHIEF)) {
            binding.cityOutlinedTextField.setVisibility(View.VISIBLE);
        } else if (selectUser.contains(Constants.OWNER_HOSTEL)) {
            binding.cityOutlinedTextField.setVisibility(View.VISIBLE);
        }

        /*if (getActivity().getIntent().getBooleanExtra(Constants.USER, true)) {
        } else if (getActivity().getIntent().getBooleanExtra(Constants.CHIEF, true)) {

        } else if (getActivity().getIntent().getBooleanExtra(Constants.OWNER_HOSTEL, true)) {
            binding.cityOutlinedTextField.setVisibility(View.VISIBLE);
        }
*/

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        binding.signupSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    String firstName = binding.signupNameEditText.getText().toString().trim();
                    String mobileNo = binding.signupMobileEditText.getText().toString().trim();
                    String email = binding.signupEmailEditText.getText().toString().trim();
                    String pass = binding.signupPasswordEditText.getText().toString().trim();
                    String city = binding.signupCityEditText.getText().toString().trim();

                    progressDialog.setMessage("Registering, please wait...");
                    progressDialog.show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("Email", email);
                                        hashMap.put("password", pass);
                                        hashMap.put("MobileNo", mobileNo);
                                        hashMap.put("FirstName", firstName);
                                        hashMap.put("city", city);
                                        hashMap.put("UserType", selectUser);
                                        FirebaseDatabase.getInstance().getReference().child("LoginInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Constants.showMessageAlert(getActivity(), "Partially Completed", "To complete the registration click the verification link sent to you email (" + email + ").", "OK", (byte) 1);
                                                getUserData(firstName);
                                                try {
                                                    FirebaseAuth.getInstance().signOut();
                                                } catch (Exception e) {
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                try {
                                                    FirebaseAuth.getInstance().signOut();
                                                } catch (Exception SS) {
                                                }
                                                Constants.showMessageAlert(getActivity(), "Network Error", "Could'nt register, Please make sure you are connected with internet", "OK", (byte) 0);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Constants.showMessageAlert(getActivity(), "Network Error", "Couldn't register, Please make sure you are connected with internet", "OK", (byte) 0);
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                    progressDialog.dismiss();
                                    Constants.showMessageAlert(getActivity(), "Weak Password", "Your password is weak", "OK", (byte) 0);
                                } catch (FirebaseAuthUserCollisionException existEmail) {
                                    progressDialog.dismiss();
                                    Constants.showMessageAlert(getActivity(), "Error in Registration", "This email is already registered", "OK", (byte) 0);
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Constants.showMessageAlert(getActivity(), "Network Error", "Couldn't register, Please make sure you are connected with internet", "OK", (byte) 0);
                                }
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });


        return binding.getRoot();
    }

    private void getUserData(String name) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LoginInfo");
        reference.orderByChild("FirstName").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String familyname = datas.child("Email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    protected boolean isValid() {
        boolean ispass1fill = false, ispass2fill = false, isemailvalid = false, isphonevalid = false, ispasswordsvalid = false;

        if ((((binding.confirmPasswordOutlinedTextField.getEditText().getText()).toString()).trim()).equals("")) {
            binding.confirmPasswordOutlinedTextField.setErrorEnabled(true);
            binding.confirmPasswordOutlinedTextField.setError("Please enter password confirmation");
            binding.confirmPasswordOutlinedTextField.requestFocus();
            ispass2fill = false;
        } else {
            ispass2fill = true;
        }

        if ((((binding.passwordOutlinedTextField.getEditText().getText()).toString()).trim()).equals("")) {
            binding.passwordOutlinedTextField.setErrorEnabled(true);
            binding.passwordOutlinedTextField.setError("Please enter password");
            binding.passwordOutlinedTextField.requestFocus();
            ispass1fill = false;
        } else {
            ispass1fill = true;
        }

        if (ispass1fill && ispass2fill) {
            if ((((binding.passwordOutlinedTextField.getEditText().getText()).toString()).trim()).equals(((binding.confirmPasswordOutlinedTextField.getEditText().getText()).toString()).trim())) {
                ispasswordsvalid = true;
            } else {
                binding.confirmPasswordOutlinedTextField.setErrorEnabled(true);
                binding.confirmPasswordOutlinedTextField.setError("Password doesn't match");
                binding.confirmPasswordOutlinedTextField.requestFocus();
                ispasswordsvalid = false;
            }
        }

        if ((((binding.mobileOutlinedTextField.getEditText().getText()).toString()).trim()).equals("")) {
            binding.mobileOutlinedTextField.setErrorEnabled(true);
            binding.mobileOutlinedTextField.setError("Please enter mobile number");
            binding.mobileOutlinedTextField.requestFocus();
            isphonevalid = false;
        } else {
            if (((binding.mobileOutlinedTextField.getEditText().getText()).toString()).trim().length() > 13) {
                binding.mobileOutlinedTextField.setErrorEnabled(true);
                binding.mobileOutlinedTextField.setError("Invalid mobile number");
                binding.mobileOutlinedTextField.requestFocus();
                isphonevalid = false;
            } else {
                isphonevalid = true;
            }
        }

        if ((((binding.emailOutlinedTextField.getEditText().getText()).toString()).trim()).equals("")) {
            binding.emailOutlinedTextField.setErrorEnabled(true);
            binding.emailOutlinedTextField.setError("Please enter email address");
            binding.emailOutlinedTextField.requestFocus();
            isemailvalid = false;
        } else {
            if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(((binding.emailOutlinedTextField.getEditText().getText()).toString()).trim()).matches()) {
                isemailvalid = true;
            } else {
                binding.emailOutlinedTextField.setErrorEnabled(true);
                binding.emailOutlinedTextField.setError("Please enter a valid email address");
                binding.emailOutlinedTextField.requestFocus();
                isemailvalid = false;
            }
        }
        return (isemailvalid && isphonevalid && ispasswordsvalid) ? true : false;
    }

   /* @Override
    public void onBackPressed() {
        startActivity(new Intent(PatientRegister.this,PatientLoginRegisterChoice.class));
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }*/


//    private void sendUserData(){
//        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//        DatabaseReference myref = firebaseDatabase.getReference(firebaseAuth.getUid());
//        StorageReference imageReference =storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic");
//        UploadTask uploadTask=imageReference.putFile(imagePath);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(registrationactivity.this,"Upload Failed!",Toast.LENGTH_SHORT).show();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                Toast.makeText(registrationactivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        UserProfile userProfile=new UserProfile(uname,uemail,uenroll,ucollege,uphone,uroom,ubranch);
//        myref.setValue(userProfile);
//    }
}