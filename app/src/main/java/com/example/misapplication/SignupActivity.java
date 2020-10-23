package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText EditName,EditCode,EditPassword,EditEmail,EditMobile,EditDesignation;
    Button buttonSignup;
    TextView textViewLogin;
    String  S_Name_Holder,  S_Code_Holder,  S_Email_Holder,  S_PassHolder, S_MobileHolder, S_DesignationHolder;
    ProgressDialog progressDialog;
    String HttpRegis = "http://easy2billing.com/attendance/api/add_emp.php";
    private RequestQueue rQueue;
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
        EditEmail=findViewById(R.id.textInputEditTextemailid);
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
                //registration  validation........
                S_Name_Holder=EditName.getText().toString().trim();

                S_Code_Holder=EditCode.getText().toString().trim();

                S_Email_Holder=EditEmail.getText().toString().trim();

                S_PassHolder=EditPassword.getText().toString().trim();

                S_MobileHolder=EditMobile.getText().toString().trim();

                S_DesignationHolder=EditDesignation.getText().toString().trim();


                if(S_Name_Holder.isEmpty())
                {
                    EditName.setError("Enter Name");
                    EditName.setInputType(InputType.TYPE_CLASS_TEXT);


                }
                if(S_Code_Holder.isEmpty())
                {
                    EditCode.setError("Enter Employe Code");
                    EditCode.setInputType(InputType.TYPE_CLASS_TEXT);

                }

                if(S_Email_Holder.isEmpty())
                {
                    EditEmail.setError("Enter Email id");
                    EditEmail.setInputType(InputType.TYPE_CLASS_TEXT);

                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                        EditEmail.getText().toString()).matches()) {
                    // Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid Email",
                            Toast.LENGTH_LONG).show();
                    EditEmail.setError("Invalid Email");


                }
                if (S_PassHolder.length() <= 7) {

                    Toast.makeText(getApplicationContext(),
                            "Password must be 8 characters above",
                            Toast.LENGTH_LONG).show();
                    EditPassword.setError("Password must be 8 characters above");
                }
                if (S_MobileHolder.isEmpty()) {
                    EditMobile.setError("Enter Mobile number");
                    EditMobile.setInputType(InputType.TYPE_CLASS_NUMBER);

                }
                if (S_MobileHolder.length()<10) {
                    EditMobile.setError("Enter 10 digit Mobile number");

                }

                if(S_DesignationHolder.isEmpty())
                {
                    EditDesignation.setError("Enter Name");
                    EditDesignation.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                if(!S_Name_Holder.isEmpty() &&
                   !S_Code_Holder.isEmpty() &&
                   !S_Email_Holder.isEmpty() &&
                   !S_PassHolder.isEmpty() && S_PassHolder.length() >= 7  && !S_MobileHolder.isEmpty() && S_MobileHolder.length()>=10 &&
                   !S_DesignationHolder.isEmpty())
                {

                    Registrationfrom(S_Code_Holder,S_PassHolder,S_Name_Holder,S_MobileHolder,S_Email_Holder,S_DesignationHolder);
                }


                break;
            case R.id.Loginpage:
                Intent intent=new Intent(this, LoginActivity.class);
                this.startActivity(intent);
                break;
        }

    }

    private void Registrationfrom(final String s_code_holder, final String s_passHolder, final String s_name_holder, final String s_mobileHolder, final String s_email_holder, final String s_designationHolder) {
        System.out.println("Registration from ");
        /**
         * Progress Dialog for User Interaction
         */
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRegis,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        rQueue.getCache().clear();

                        Toast.makeText(SignupActivity.this,response,Toast.LENGTH_LONG).show();

                        try {
                            progressDialog.dismiss();
                            System.out.println("response :" + response);

                                Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("empcode",s_code_holder);
                params.put("password",s_passHolder);
                params.put("empname",s_name_holder);
                params.put("mobile",s_mobileHolder);
                params.put("emailid",s_email_holder);
                params.put("designation",s_designationHolder);
                return params;
            }

        };

        rQueue = Volley.newRequestQueue(SignupActivity.this);
        rQueue.add(stringRequest);

    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}