package com.example.final_project_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;
import android.content.Context;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText editTextEmail, editTextPassword , editTextConfirmPassword ,editTextFullName  ;
    Button buttonRegister;
    Button buttonLogin;

    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar progressBar;

    SharedPreferences sp;
     EditText FullName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        editTextFullName=findViewById(R.id.etFullName);  //User Full name
        editTextEmail= findViewById(R.id.etEmail);       //User Email
        editTextPassword = findViewById(R.id.etPassword); //User Password
        editTextConfirmPassword=findViewById(R.id.etConfirmPassword); //User Confirm Password

        buttonRegister = findViewById(R.id.btnRegister);       // Register Button
        buttonLogin = findViewById(R.id.btnRegisterLogin);      // Back To login page button

        progressBar =findViewById(R.id.progressbar);          //Progress Bar


        sp=getSharedPreferences("MY_NAMES" ,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);     //set progress bar visible when button clicked

                String Email , Password ,ConfirmPassword,FullName;
                Email = editTextEmail.getText().toString();
                Password= editTextPassword.getText().toString();
                FullName=editTextFullName.getText().toString();
                ConfirmPassword=editTextConfirmPassword.getText().toString();

                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; // check the vaildation of the email using regex
                Pattern pattern = Pattern.compile(emailRegex);


                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(Register.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                } else if (!pattern.matcher(Email).matches()) {
                    Toast.makeText(Register.this, "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(Register.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                } else if(Password.length() < 6) {
                    Toast.makeText(Register.this, "Password must be at least 6 characters long and contain at least one number", Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(FullName)){
                    Toast.makeText(Register.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
                }
                if (!Password.equals(ConfirmPassword)) {
                    Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }



                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);     //set progress bar invisible when success
                                if (task.isSuccessful()) {

                                    user = mAuth.getCurrentUser();
                                    String UserId = user.getUid(); //get user id

                                    Log.d("MainActivity", "User ID from register: " + UserId); //to test

                                    String FullNamestr = editTextFullName.getText().toString();

                                    editor.putString(UserId,FullNamestr); // map userid as key to username
                                    editor.apply();

                                    Toast.makeText(Register.this, "Account Registered Successfully ",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Register.this, Login.class);
                                    startActivity(i);



                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }

        });


    }

}