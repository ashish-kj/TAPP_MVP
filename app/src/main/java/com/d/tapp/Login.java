package com.d.tapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button createAccountBtn,loginButton,forgotPasswordBtn;
    EditText username,password;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder resetAlert;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        resetAlert = new AlertDialog.Builder(this);
        inflater=this.getLayoutInflater();

        createAccountBtn = findViewById(R.id.createAccountButton);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        username=findViewById(R.id.loginEmail);
        password=findViewById(R.id.loginPassword);
        loginButton=findViewById(R.id.loginButton);
        forgotPasswordBtn=findViewById(R.id.forgotPasswordButton);

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=inflater.inflate(R.layout.reset_pop,null);
                resetAlert.setTitle("Reset Your Password!!")
                        .setMessage("Enter your valid Email to get Password Reset link")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText email = view.findViewById(R.id.resetEmailPop);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("Required Field!");
                                    return;
                                }

                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Login.this,"Reset Email Sent",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null)
                        .setView(view)
                        .create().show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()){
                    username.setError("Email is Missing!!");
                    return;
                }

                if(password.getText().toString().isEmpty()){
                    password.setError("Password us Missing!!");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}