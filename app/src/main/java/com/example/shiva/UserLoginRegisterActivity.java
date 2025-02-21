package com.example.shiva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserLoginRegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText loginUsername, loginPassword, registerUsername, registerPassword, registerEmail;
    private LinearLayout loginLayout, registerLayout;
    private TextView loginErrorTextView, registerErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_register);

        mAuth = FirebaseAuth.getInstance();
        initializeUI();

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        findViewById(R.id.toggle_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLayout();
            }
        });
    }

    private void initializeUI() {
        loginLayout = findViewById(R.id.login_layout);
        registerLayout = findViewById(R.id.register_layout);
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        registerUsername = findViewById(R.id.register_username);
        registerPassword = findViewById(R.id.register_password);
        registerEmail = findViewById(R.id.register_email);
        loginErrorTextView = findViewById(R.id.login_error_text_view);
        registerErrorTextView = findViewById(R.id.register_error_text_view);
    }

    private void loginUser() {
        String email = loginUsername.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            loginErrorTextView.setText("Please fill in all fields.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Check if email is verified
                            if (user != null && user.isEmailVerified()) {
                                Intent intent = new Intent(UserLoginRegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(UserLoginRegisterActivity.this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            loginErrorTextView.setText("Login failed. Check your credentials.");
                        }
                    }
                });
    }

    private void registerUser() {
        String username = registerUsername.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            registerErrorTextView.setText("Please fill in all fields.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Send verification email
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(UserLoginRegisterActivity.this, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show();

                                                    // Redirect to login after registration
                                                    loginLayout.setVisibility(View.VISIBLE);
                                                    registerLayout.setVisibility(View.GONE);
                                                    ((Button) findViewById(R.id.toggle_button)).setText("Switch to Register");

                                                } else {
                                                    registerErrorTextView.setText("Failed to send verification email.");
                                                }
                                            }
                                        });
                            }
                        } else {
                            registerErrorTextView.setText("Registration failed. Try again.");
                        }
                    }
                });
    }

    private void toggleLayout() {
        if (loginLayout.getVisibility() == View.VISIBLE) {
            loginLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.toggle_button)).setText("Switch to Login");
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            registerLayout.setVisibility(View.GONE);
            ((Button) findViewById(R.id.toggle_button)).setText("Switch to Register");
        }
    }
}
