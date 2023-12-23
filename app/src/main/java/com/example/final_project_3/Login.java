package com.example.final_project_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button ButtonRegister, ButtonLogin;
    TextView LoginEmail, LoginPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    FirebaseUser user;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        ButtonRegister = findViewById(R.id.btnLoginRegister);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            // Redirect user to Register activity if click on ButtonRegister
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });
        LoginEmail = findViewById(R.id.etLoginEmail);
        LoginPassword = findViewById(R.id.etLoginPassword);

        progressBar = findViewById(R.id.lprogressbar);  // Initialize the progressBar

        ButtonLogin = findViewById(R.id.btnLogin);

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String Email = LoginEmail.getText().toString();
                String Password = LoginPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    Toast.makeText(Login.this, "Login Success.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Login.this, MainActivity.class); //if login successful redirect to main activity
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
