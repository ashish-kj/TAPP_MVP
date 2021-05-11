package com.d.tapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView verifyMsg;
    Button verifyEmailBtn;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder resetAlert;
    LayoutInflater inflater;
    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logoutBtn);
        verifyMsg=findViewById(R.id.verifyEmailMsg);
        verifyEmailBtn=findViewById(R.id.verifyEmailBtn);
        scanButton = findViewById(R.id.scanButton);

        resetAlert = new AlertDialog.Builder(this);
        inflater=this.getLayoutInflater();

        if(!firebaseAuth.getCurrentUser().isEmailVerified()){
            verifyEmailBtn.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCaptureActivity();
            }
        });

        verifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Verification Mail Sent!", Toast.LENGTH_SHORT).show();
                        verifyEmailBtn.setVisibility(View.GONE);
                        verifyMsg.setVisibility(View.GONE);

                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
    }

    public void openCaptureActivity(){
        Intent intent = new Intent(this,captureActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(),ResetPassword.class));
        }

        if(item.getItemId()==R.id.updateEmailMenu){
            View view=inflater.inflate(R.layout.reset_pop,null);
            resetAlert.setTitle("Update Email")
                    .setMessage("Enter new Email Address")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText email = view.findViewById(R.id.resetEmailPop);
                            if(email.getText().toString().isEmpty()){
                                email.setError("Required Field!");
                                return;
                            }

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel",null)
                    .setView(view)
                    .create().show();
        }

        if(item.getItemId()==R.id.deleteAccountMenu){
            resetAlert.setTitle("Delete Account Permanently")
                    .setMessage("Are you sure you want to proceed with Account Deletion ?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user =firebaseAuth.getCurrentUser();
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "User Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    firebaseAuth.signOut();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel",null)
                    .create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}