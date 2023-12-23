package com.example.final_project_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_Settings extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog dialog;
    Button ChangePasswordBtn;
    EditText OldPasswordET;
    EditText NewPasswordET;
    EditText RePasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent i = new Intent(Profile_Settings.this, Login.class);
            startActivity(i);
            finish();
            // Checks if the user is not logged in and redirects to the login activity
        }

        OldPasswordET = findViewById(R.id.OldPassword);
        NewPasswordET = findViewById(R.id.NewPassword);
        RePasswordET = findViewById(R.id.ReNewPassword);

        String Email = user.getEmail();

        ChangePasswordBtn = findViewById(R.id.btnChangePassword);

        ChangePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OldPasswordstr = OldPasswordET.getText().toString();
                String NewPasswordstr = NewPasswordET.getText().toString();
                String RePasswordstr= RePasswordET.getText().toString();

                if(!NewPasswordstr.equals(RePasswordstr)){
                    Toast.makeText(Profile_Settings.this, "Passwords Doesn't Match", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Profile_Settings.this,Profile_Settings.class);
                    startActivity(i);
                } else if (NewPasswordstr.length()<6) {
                    Toast.makeText(Profile_Settings.this, "Passwords Must be more or equal than 6 characters", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Profile_Settings.this,Profile_Settings.class);
                    startActivity(i);
                }else if (NewPasswordstr.equals("")||RePasswordstr.equals("") ||OldPasswordstr.equals("")){
                    Toast.makeText(Profile_Settings.this, "Passwords Must not be empty", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Profile_Settings.this,Profile_Settings.class);
                    startActivity(i);
                }

                AuthCredential credential = EmailAuthProvider.getCredential(Email, OldPasswordstr);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(NewPasswordstr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Profile_Settings.this, "Password Updated Successfully.", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(Profile_Settings.this,Login.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(Profile_Settings.this, "Password Update Failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(Profile_Settings.this, "Wrong old password.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}
