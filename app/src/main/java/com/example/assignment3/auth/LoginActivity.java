package com.example.assignment3.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assignment3.MainActivity;
import com.example.assignment3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";

    private FirebaseAuth mAuth;

    private Button loginButton;
    private Button signupLinkButton;
    private EditText emailText;
    private EditText passwordText;
    private ConstraintLayout loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.loginButton);
        signupLinkButton = findViewById(R.id.signupLinkButton);
        emailText = findViewById(R.id.editTextEmailAddress);
        passwordText = findViewById(R.id.editTextPassword);
        loginLayout = findViewById(R.id.loginLayout);

        loginButton.setOnClickListener(v -> {
            if (validateLoginForm()) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                signIn(email, password);
            }
        });

        signupLinkButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        this.getSupportActionBar().hide();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean validateLoginForm() {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.equals("") || password.equals("")) {
            Snackbar.make(loginLayout,  "Please enter both email and password",
                    Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(loginLayout,  "Please enter a valid email",
                    Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user.isEmailVerified()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else {
                            Snackbar.make(loginLayout,  "Your account is not verified. " +
                                            "A verification has been sent to your email", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Snackbar.make(loginLayout,  task.getException().getLocalizedMessage(),
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}