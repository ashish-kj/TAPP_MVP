package com.d.tapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class bridgeActivity extends AppCompatActivity {

    Button bridgeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        bridgeBtn = findViewById(R.id.bridgeBtn);

        bridgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCallingFunction();
            }
        });
    }

    private void intentCallingFunction() {
        Intent intent = new Intent(this,startpaymentActivity.class);
        startActivity(intent);
    }
}