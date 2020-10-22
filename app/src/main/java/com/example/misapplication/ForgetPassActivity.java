package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPassActivity extends AppCompatActivity {
    EditText forgetpassword;
    Button Submitbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        forgetpassword=findViewById(R.id.entereditcode);
        Submitbtn=findViewById(R.id.submit);
        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentpass=new Intent(ForgetPassActivity.this,OtpverficationActivity.class);
                startActivity(intentpass);
            }
        });


    }
}