package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OtpverficationActivity extends AppCompatActivity {

    Button buttonverify;
    EditText otpverify;
    public final int REQUEST_CODE_SMS_PERMISSIONS = 1001;
    String URLline;
    SharedPreferences sharedPrFRegis;
    String Rempcode,Rname,Rstatus,Rotp,Rmobile;
    public static String OTpInpput=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(OtpverficationActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_SMS_PERMISSIONS);
        setContentView(R.layout.activity_otpverfication);

        otpverify=findViewById(R.id.enterotpcode);
        buttonverify=findViewById(R.id.btn_get_data);
    }
}