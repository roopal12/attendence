package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.example.misapplication.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignotpActivity extends AppCompatActivity {

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
        ActivityCompat.requestPermissions(SignotpActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_SMS_PERMISSIONS);
        setContentView(R.layout.activity_signotp);

        sharedPrFRegis=getSharedPreferences("registration",MODE_PRIVATE);

        otpverify=findViewById(R.id.enterotpcode);
        buttonverify=findViewById(R.id.btn_get_data);

        new AsyncCaller().execute();

        buttonverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String empcode = sharedPrFRegis.getString("code", "");
                    String emmobile=sharedPrFRegis.getString("mobile", "");

                    String URLconf = "http://easy2billing.com/attendance/api/conf_reg.php?emp_id="+ empcode + "&mobile="+emmobile;
                    Log.d("getxxx",URLline);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URLconf,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("conf_reg strrrrr",">>"+response);

                                    Toast.makeText(getApplicationContext(),"response",Toast.LENGTH_LONG).show();

                                    if(response.equals("Success"))
                                    {
                                        Intent intent=new Intent(SignotpActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Enter otp does not match",Toast.LENGTH_LONG).show();
                                    }
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
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

            }
        });
    }

    private void verifyotp() {

        String empcode = sharedPrFRegis.getString("code", "");
        URLline = "http://easy2billing.com/attendance/api/emp_view.php?emp_id="+empcode;

        Log.d("getxxx",URLline);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("emp_view strrrrr",">>"+response);
                        parseData(response);
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

    private void parseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);
                      Rempcode= dataobj.getString("emp_id");
                      Rname= dataobj.getString("name");
                      Rstatus = dataobj.getString("status");
                      Rotp = dataobj.getString("otp");
                      Rmobile = dataobj.getString("status");

                    System.out.println("emp_id" + Rempcode);
                    System.out.println("name" + Rname);
                    System.out.println("otp" + Rotp);
                    System.out.println("email" + Rstatus);
                    System.out.println("mobile" + Rmobile);


                    OTpInpput=otpverify.getText().toString().trim();
                    Log.d("OTpInpput",OTpInpput);

                    if (!Rotp.equals(OTpInpput))
                    {

                        Toast.makeText(getApplicationContext(),"Otp Does not match",Toast.LENGTH_LONG).show();
                    }


                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            verifyotp();
            return null;
        }
    }
}