package com.d.tapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText registerFullName,registerEmail,registerPassword,registerConfirmPass;
    Button registerUserBtn,gotoLogin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerFullName=findViewById(R.id.registerFullName);
        registerEmail=findViewById(R.id.registerEmail);
        registerPassword=findViewById(R.id.registerPassword);
        registerConfirmPass=findViewById(R.id.confirmPassword);
        registerUserBtn=findViewById(R.id.registerButton);
        gotoLogin=findViewById(R.id.gotologin);

        firebaseAuth = FirebaseAuth.getInstance();

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = registerFullName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confPass = registerConfirmPass.getText().toString();

                if(fullName.isEmpty()){
                    registerFullName.setError("Full Name is Required");
                }

                if(email.isEmpty()){
                    registerEmail.setError("Email is Required");
                }

                if(password.isEmpty()){
                    registerPassword.setError("Password is Required");
                }

                if(confPass.isEmpty()){
                    registerConfirmPass.setError("Password Confirmation is Required");
                }

                if(!password.equals(confPass)){
                    registerConfirmPass.setError("Password Do Not Match");
                }

                Toast.makeText(Register.this,"Validating Data",Toast.LENGTH_SHORT).show();

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}