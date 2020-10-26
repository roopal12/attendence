package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ForgetPassActivity extends AppCompatActivity {
    EditText forgetpassword;
    String empcode;
    Button Submitbtn;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        forgetpassword=findViewById(R.id.entereditcode);
        Submitbtn=findViewById(R.id.submit);
        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Otpverfication();
            }
        });
    }

    private void Otpverfication(){
        empcode=forgetpassword.getText().toString().trim();
        if(empcode.isEmpty())
        {
            forgetpassword.setError("Enter your employe Id");
        }
        else
        {
            url = "http://easy2billing.com/attendance/api/sendotp.php?emp_id="+empcode;

            Log.d("sendotp",url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("emp_view strrrrr",">>"+response);

                            Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(ForgetPassActivity.this,OtpverficationActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            // request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}