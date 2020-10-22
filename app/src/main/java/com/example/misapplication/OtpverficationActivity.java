package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OtpverficationActivity extends AppCompatActivity {
    EditText editTextOtp;
    Button verfiybtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverfication);
        editTextOtp=findViewById(R.id.enterotpcode);
        verfiybtn=findViewById(R.id.btn_get_data);
        verfiybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(OtpverficationActivity.this,ChangePassActivity.class);
              startActivity(intent);
            }
        });
    }
}