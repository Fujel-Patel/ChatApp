package com.example.whatsp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private TextView btnLogin, btnSignup;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        userEmail = findViewById(R.id.emailText);
        userPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.Login);
        btnSignup = findViewById(R.id.signUp);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    signIn(userEmail.getText().toString().trim(), userPassword.getText().toString().trim());
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(userEmail.getText())) {
            userEmail.setError("Please enter your email");
            userEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userPassword.getText())) {
            userPassword.setError("Please enter your password");
            userPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String username = user.getDisplayName();
                                navigateToMainActivity(username);
                            }
                        } else {
                            handleSignInFailure(task.getException());
                        }
                    }
                });
    }

    private void navigateToMainActivity(String username) {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.putExtra("name", username);
        startActivity(intent);
        finish();
    }

    private void handleSignInFailure(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(SignInActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SignInActivity.this, "Authentication failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity(currentUser.getDisplayName());
        }
    }
}