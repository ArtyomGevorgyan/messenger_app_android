package com.android.gevart.awesomechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.gevart.awesomechat.R;
import com.android.gevart.awesomechat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText reapeatPasswordEditText;
    private Button loginSignUpButton;
    private TextView toggleLogInSignUpTextView;
    private boolean loginModeIsActive;
    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        database = FirebaseDatabase.getInstance();
        usersDatabaseReference = database.getReference().child("users");
        auth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        reapeatPasswordEditText = findViewById(R.id.reapeatPasswordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        loginSignUpButton = findViewById(R.id.loginSignUpButton);
        toggleLogInSignUpTextView = findViewById(R.id.toggleLogInSignUpTextView);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, UsersListActivity.class));
        }

        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });
    }

    private void loginSignUpUser(String email, String password) {
        if (loginModeIsActive) {
            if (email.equals("")) {
                Toast.makeText(this, "Please input your email", Toast.LENGTH_LONG).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            } else {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    //FirebaseUser user = auth.getCurrentUser();
                                    startActivity(new Intent(SignInActivity.this, UsersListActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        } else {
            if (nameEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please input your name", Toast.LENGTH_LONG).show();
            } else if (email.equals("")) {
                Toast.makeText(this, "Please input your email", Toast.LENGTH_LONG).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            } else if (!password.equals(reapeatPasswordEditText.getText().toString().trim())) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    createUser(user);
                                    Intent intent = new Intent(SignInActivity.this, UsersListActivity.class);
                                    intent.putExtra("senderUserName", nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed. Maybe you already have an account",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }
    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());

        usersDatabaseReference.push().setValue(user);
    }

    public void toggleLoginMode(View view) {
        if (loginModeIsActive) {
            loginModeIsActive = false;
            loginSignUpButton.setText("Sign Up");
            toggleLogInSignUpTextView.setText("Tap to log in");
            reapeatPasswordEditText.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.VISIBLE);
        } else {
            loginModeIsActive = true;
            loginSignUpButton.setText("Log In");
            toggleLogInSignUpTextView.setText("Create account");
            reapeatPasswordEditText.setVisibility(View.GONE);
            nameEditText.setVisibility(View.GONE);
        }
    }
}
