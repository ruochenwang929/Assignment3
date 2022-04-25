package com.example.assignment3.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.assignment3.R;
import com.example.assignment3.profile.service.UserProfile;
import com.example.assignment3.profile.service.UserProfileService;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUp";

    private FirebaseAuth mAuth;
    private UserProfileService userProfileService;

    private Button signUpButton;
    private Button cancelButton;
    private EditText emailText;
    private EditText passwordText;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText addressText;

    private ConstraintLayout signUpLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        userProfileService = new UserProfileService();

        signUpButton = findViewById(R.id.signUpButton);
        cancelButton = findViewById(R.id.cancelButton);
        emailText = findViewById(R.id.editTextEmailAddress);
        passwordText = findViewById(R.id.editTextPassword);
        firstNameText = findViewById(R.id.editTextFirstName);
        lastNameText = findViewById(R.id.editTextLastName);
        addressText = findViewById(R.id.editTextAddress);
        signUpLayout = findViewById(R.id.signUpLayout);

        signUpButton.setOnClickListener(v -> {
            if (validateSignUpForm()) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                createAccount(email, password);
            }
        });

        cancelButton.setOnClickListener(v -> {
            this.finish();
        });

        this.getSupportActionBar().hide();
    }

    private boolean validateSignUpForm() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String address = addressText.getText().toString();

        if (email.equals("") || password.equals("") || firstName.equals("") ||
            lastName.equals("") || address.equals("")) {
            Snackbar.make(signUpLayout, "Please fill all fields", Snackbar.LENGTH_LONG)
                    .show();

            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(signUpLayout, "Please enter a valid email", Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }

        return true;
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Add user profile
                        UserProfile profile = new UserProfile();
                        profile.setUserUID(user.getUid());
                        profile.setFirstName(firstNameText.getText().toString());
                        profile.setLastName(lastNameText.getText().toString());
                        profile.setAddress(addressText.getText().toString());
                        userProfileService.addUserProfile(profile);

                        // send email verification
                        sendEmailVerification();
                        // TODO: Jump to next activity

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Snackbar.make(signUpLayout,  task.getException().getLocalizedMessage(),
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void sendEmailVerification() {
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, task -> Snackbar.make(signUpLayout,
                        "A verification has been sent to your email." +
                                "Please back to login once verified", Snackbar.LENGTH_LONG).show());
    }
}