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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {

    EditText userPassword,userConfirmedPassword;
    Button savePasswordBtn;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userPassword=findViewById(R.id.newUserPassword);
        userConfirmedPassword=findViewById(R.id.newConfirmPAss);
        savePasswordBtn=findViewById(R.id.resetPasswordBtn);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userPassword.getText().toString().isEmpty()){
                    userPassword.setError("Required Field");
                    return;
                }

                if(userConfirmedPassword.getText().toString().isEmpty()){
                    userConfirmedPassword.setError("Required Field");
                    return;
                }

                if(!userPassword.getText().toString().equals(userConfirmedPassword.getText().toString())){
                    userConfirmedPassword.setError("Password Does Not Match!!");
                    return;
                }

                firebaseUser.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPassword.this,"Password updated",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}