package com.example.multitakerapp.Auth;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.multitakerapp.MainActivity;
import com.example.multitakerapp.Model.UserProfile;
import com.example.multitakerapp.R;
import com.example.multitakerapp.Utlis.Constants;
import com.example.multitakerapp.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentLoginBinding.inflate(inflater, container, false);


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);


        binding.loginForgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = new EditText(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = 10;
                lp.rightMargin = 10;
                input.setLayoutParams(lp);
                input.setHint("Email");

                new AlertDialog.Builder(getActivity()).setTitle("Enter email ID").setView(input).setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        boolean isemailvalid = false;
                        if ((((input.getText()).toString()).trim()).equals("")) {
                            Constants.showMessageAlert(getActivity(), "Invalid Email", "Please enter valid email.", "Ok", (byte) 0);
                            isemailvalid = false;
                        } else {
                            if (Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(((input.getText()).toString()).trim()).matches()) {
                                isemailvalid = true;
                            } else {
                                Constants.showMessageAlert(getActivity(), "Invalid Email", "Please enter valid email.", "Ok", (byte) 0);
                                isemailvalid = false;
                            }
                        }
                        if (isemailvalid) {
                            progressDialog.setMessage("Please wait....");
                            progressDialog.show();
                            FirebaseAuth.getInstance().sendPasswordResetEmail(input.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Constants.showMessageAlert(getActivity(), "Reset Link Sent", "Password reset link has been sent to " + input.getText().toString().trim(), "Ok", (byte) 1);
                                    } else {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                            Constants.showMessageAlert(getActivity(), "Invalid User", "Account does'nt exists. Make sure you have activated your account", "OK", (byte) 0);
                                        } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                            Constants.showMessageAlert(getActivity(), "Invalid Password", "Your password is incorrect", "OK", (byte) 0);
                                        } catch (Exception e) {
                                            Constants.showMessageAlert(getActivity(), "Network Error", "Unable to login, Make sure you are connected to internet", "OK", (byte) 0);
                                        }
                                    }
                                }
                            });
                        }

                    }
                }).setNegativeButton("Cancel", null).show();
            }
        });


        binding.loginLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    progressDialog.setMessage("Authenticating, please wait...");
                    progressDialog.show();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.emailOutlinedTextField.getEditText().getText().toString().trim(), binding.passwordOutlinedTextField.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                                    progressDialog.dismiss();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    getActivity().finish();

                                } else {
                                    progressDialog.dismiss();
                                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                    alertDialog.setTitle("Incomplete Activation");
                                    alertDialog.setMessage("It seems that you haven't completed the verification phase in activation process, please click the verification link sent to your email, do you want re-send the link?");
                                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                FirebaseAuth.getInstance().signOut();
                                            } catch (Exception e) {
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Resend", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            progressDialog.setMessage("Sending link, please wait...");
                                            progressDialog.show();
                                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        try {
                                                            FirebaseAuth.getInstance().signOut();
                                                        } catch (Exception e) {
                                                        }
                                                        Constants.showMessageAlert(getActivity(), "Verify", "A verification link has been sent to " + binding.emailOutlinedTextField.getEditText().getText().toString().trim() + ".Please click and verify to activate your account", "OK", (byte) 1);
                                                    } else {
                                                        progressDialog.dismiss();
                                                        try {
                                                            FirebaseAuth.getInstance().signOut();
                                                        } catch (Exception e) {
                                                        }
                                                        Constants.showMessageAlert(getActivity(), "Network Error", "Unable to login, Make sure you are connected to internet", "OK", (byte) 0);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Constants.showMessageAlert(getActivity(), "Network Error", "Unable to login, Make sure you are connected to internet", "OK", (byte) 0);
                                                }
                                            });
                                        }
                                    });
                                    alertDialog.show();
                                }
                            } else {
                                progressDialog.dismiss();
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Constants.showMessageAlert(getActivity(), "Invalid User", "Account does'nt exists. Make sure you have activated your account", "OK", (byte) 0);
                                } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    Constants.showMessageAlert(getActivity(), "Invalid Password", "Your password is incorrect", "OK", (byte) 0);
                                } catch (Exception e) {
                                    Constants.showMessageAlert(getActivity(), "Network Error", "Unable to login, Make sure you are connected to internet", "OK", (byte) 0);
                                }
                            }
                        }
                    });
                }
            }
        });


        return binding.getRoot();
    }


    private boolean isValid() {
        boolean isvalid = false, isemailvalid = false, ispasswordsvalid = false;
        if ((((binding.passwordOutlinedTextField.getEditText().getText()).toString()).trim()).equals("")) {
            binding.passwordOutlinedTextField.setErrorEnabled(true);
            binding.passwordOutlinedTextField.setError("Please enter password");
            binding.passwordOutlinedTextField.requestFocus();
            ispasswordsvalid = false;
        } else {
            ispasswordsvalid = true;
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
        return (isemailvalid && ispasswordsvalid) ? true : false;
    }

}