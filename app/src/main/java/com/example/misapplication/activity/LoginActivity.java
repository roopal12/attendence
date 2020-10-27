package com.example.misapplication.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.misapplication.ForgetPassActivity;
import com.example.misapplication.R;
import com.example.misapplication.session.PrefManager;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.R)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewforgetp,textViewregistered;
    EditText EmpName,Empasword;
    Button buttonlogin;
    CheckBox checkBoxMe;
    SharedPreferences sharedPreferencesLogin;
    String PasswordHolder, CodeHolder;
    String Loginurl = "http://easy2billing.com/attendance/api/logincheck.php";
    ProgressDialog progressDialog;
    private RequestQueue rQueue;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferencesLogin=getSharedPreferences("login",MODE_PRIVATE);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        inti();
    }

    private void inti()
    {
        EmpName=findViewById(R.id.editname);
        Empasword=findViewById(R.id.password);
        checkBoxMe=findViewById(R.id.checkin);
        textViewforgetp=findViewById(R.id.forgetpassword);
        buttonlogin=findViewById(R.id.login);
        textViewregistered=findViewById(R.id.Signup);
        buttonlogin.setOnClickListener(this);
        textViewforgetp.setOnClickListener(this);
        textViewregistered.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Signup:
                Intent intent=new Intent(this, SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                    CodeHolder=EmpName.getText().toString().trim();
                    PasswordHolder=Empasword.getText().toString().trim();
                    if(CodeHolder.isEmpty())
                    {
                        EmpName.setError("Enter Employee Code");
                    }
                    if (PasswordHolder.isEmpty())
                    {
                        Empasword.setError("Enter Employee Code");
                    }
                    if(!CodeHolder.isEmpty() && !PasswordHolder.isEmpty())
                    {
                        Loginfrom(CodeHolder,PasswordHolder);
                    }
                break;
            case R.id.forgetpassword:
                CodeHolder=EmpName.getText().toString().trim();
                if(CodeHolder.isEmpty())
                {
                    EmpName.setError("Enter Employee Code");
                }
                else
                {
                    Intent intentfg=new Intent(this, ForgetPassActivity.class);
                    startActivity(intentfg);
                    finish();
                }
                break;
        }

    }

    private void Loginfrom(final String codeHolder, final String passwordHolder){
        System.out.println("Login from ");

        /**
         * Progress Dialog for User Interaction
         */
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loding");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Loginurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        rQueue.getCache().clear();
                        try {
                            System.out.println("response :" + response);
                            // Show images in Toast prompt.
                            showImageInToast(response);
                            parseData(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("empcode",codeHolder);
                params.put("password",passwordHolder);
                return params;
            }

        };

        rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(stringRequest);

    }

    private void showImageInToast(String response) {
        StyleableToast.makeText(this,response,R.style.exampleToast).show();

    }

    private void parseData(String response) {
        try {

            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString("success").equals("1")) {
                JSONArray dataArray = jsonObject.getJSONArray("login");
                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject dataobj = dataArray.getJSONObject(i);

                    String publicempcode = dataobj.getString("empcode");
                    String publicname = dataobj.getString("name");
                    String publicmobile = dataobj.getString("mobile");
                    String publicemail = dataobj.getString("email");
                    String publicdesignation= dataobj.getString("designation");

                    // Login  session........................
                    SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
                    editor.putString("name",publicname);
                    editor.putString("mobile",publicmobile);
                    editor.apply();
                    editor.commit();

                    System.out.println("empcode" + publicempcode);
                    System.out.println("name" + publicname);
                    System.out.println("mobile" + publicmobile);
                    System.out.println("email" + publicemail);
                    System.out.println("designation" + publicdesignation);
                }

                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();

            }

            else
            {
                Toast.makeText(LoginActivity.this,"Invalid User and Password",Toast.LENGTH_LONG).show();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}