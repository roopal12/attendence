package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText EditName,EditCode,EditPassword,EditCPassword,EditMobile,EditDesignation;
    Button buttonSignup;
    TextView textViewLogin;
    String E_Name_Holder, E_Code_Holder, E_CPassHolder, E_PassHolder,E_MobileHolder,E_DesignationHolder;
    ProgressDialog progressDialog;
    Boolean CheckEditText;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String finalResult ;
    String HttpURL = "https://androidjsonblog.000webhostapp.com/User/UserRegistration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inti();
    }

    private void inti() {
        EditName=findViewById(R.id.textInputEditTextName);
        EditCode=findViewById(R.id.textInputEditTextCode);
        EditPassword=findViewById(R.id.textInputEditTextPassword);
        EditCPassword=findViewById(R.id.textInputEditTextConfirmPassword);
        EditMobile=findViewById(R.id.textInputEditTextMobileno);
        EditDesignation=findViewById(R.id.textInputEditTextDesgination);
        buttonSignup=findViewById(R.id.register);
        textViewLogin=findViewById(R.id.Loginpage);


        buttonSignup.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        String name = getColoredSpanned("*Are you member?", "#ffcc0000");
        String Loginblue = getColoredSpanned("Login","#0073A6");
        textViewLogin.setText(Html.fromHtml(name+" "+Loginblue));


    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.register:
                Registrationfrom();
                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.

//                    UserRegisterFunction(E_Name_Holder, E_Code_Holder, E_CPassHolder, E_PassHolder,E_MobileHolder,E_DesignationHolder);
                    Toast.makeText(SignupActivity.this, "form fields.", Toast.LENGTH_LONG).show();
                }
                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(SignupActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.Loginpage:
                Intent intent=new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                break;
        }

    }

    public void Registrationfrom() {

        Log.d("Signup","Registrationfrom");

        E_Name_Holder=EditName.getText().toString();
        E_Code_Holder=EditCode.getText().toString();
        E_CPassHolder=EditCPassword.getText().toString();
        E_PassHolder=EditPassword.getText().toString();
        E_MobileHolder=EditMobile.getText().toString();
        E_DesignationHolder=EditDesignation.getText().toString();

        if(TextUtils.isEmpty(E_Name_Holder) || TextUtils.isEmpty(E_Code_Holder) ||
           TextUtils.isEmpty(E_CPassHolder) || TextUtils.isEmpty(E_PassHolder) ||
           TextUtils.isEmpty(E_MobileHolder) || TextUtils.isEmpty(E_DesignationHolder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }

//    public void UserRegisterFunction(String e_name_holder, String e_code_holder, String e_cPassHolder, String e_passHolder, String e_mobileHolder, String e_designationHolder) {
//        class UserRegisterFunctionClass extends AsyncTask<String,Void,String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//                progressDialog = ProgressDialog.show(SignupActivity.this,"Loading Data",null,true,true);
//            }
//
//            @Override
//            protected void onPostExecute(String httpResponseMsg) {
//
//                super.onPostExecute(httpResponseMsg);
//
//                progressDialog.dismiss();
//
//                Toast.makeText(SignupActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                hashMap.put("f_name",params[0]);
//
//                hashMap.put("L_name",params[1]);
//
//                hashMap.put("email",params[2]);
//
//                hashMap.put("password",params[3]);
//
//                finalResult = httpParse.postRequest(hashMap, HttpURL);
//
//                return finalResult;
//            }
//        }
//
//        UserRegisterFunctionClass userRegisterFunctionClass = new UserRegisterFunctionClass();
//
//        userRegisterFunctionClass.execute(e_name_holder,e_code_holder,e_cPassHolder,e_passHolder,e_mobileHolder,e_designationHolder);
//    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}