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

import java.util.HashMap;
import java.util.Map;

public class OtpverficationActivity extends AppCompatActivity {

    Button buttonverify;
    EditText otpverify;
    public final int REQUEST_CODE_SMS_PERMISSIONS = 1001;
    String URLline;
    SharedPreferences sharedPrFRegis;
    String Rempcode,Rname,Rstatus,Rotp,Rmobile;
    public static String OTpInpput=null;
    private RequestQueue rQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(OtpverficationActivity.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_SMS_PERMISSIONS);
        setContentView(R.layout.activity_otpverfication);

        otpverify=findViewById(R.id.enterotpcode);
        buttonverify=findViewById(R.id.btn_get_data);

        sharedPrFRegis=getSharedPreferences("registration",MODE_PRIVATE);

        otpverify=findViewById(R.id.enterotpcode);
        buttonverify=findViewById(R.id.btn_get_data);
        verifyotp();

        buttonverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String empcode = sharedPrFRegis.getString("code", "");

                OTpInpput=otpverify.getText().toString();
                System.out.println("OTpInsert:::" + OTpInpput);

                System.out.println("empRetriveotp::::" + Rotp);

                if(OTpInpput.equals(Rotp)) {
                    String URLconf = "http://easy2billing.com/attendance/api/conf_reg.php";

                    Log.d("post  configuration ", URLline);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLconf,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    rQueue.getCache().clear();

                                    try {

                                        System.out.println("response :" + response);
                                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                                    } catch(Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Toast.makeText(OtpverficationActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("emp_id",empcode);
                            return params;
                        }

                    };
                    rQueue = Volley.newRequestQueue(OtpverficationActivity.this);
                    rQueue.add(stringRequest);

                    Intent intent=new Intent(OtpverficationActivity.this,ChangePassActivity.class);
                    intent.putExtra("emp_id",empcode);
                    intent.putExtra("mobile",Rmobile);
                    System.out.println("mobile" + Rmobile);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(OtpverficationActivity.this,"Otp does not match",Toast.LENGTH_LONG).show();
                }
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

                    Rempcode = dataobj.getString("emp_id");
                    Rname = dataobj.getString("name");
                    Rstatus = dataobj.getString("status");
                    Rotp = dataobj.getString("otp");
                    Rmobile = dataobj.getString("mobile");

                    System.out.println("emp_id" + Rempcode);
                    System.out.println("name" + Rname);
                    System.out.println("otp" + Rotp);
                    System.out.println("status" + Rstatus);
                    System.out.println("mobile" + Rmobile);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}