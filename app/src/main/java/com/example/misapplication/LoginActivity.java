package com.example.misapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
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
        sharedPreferencesLogin=getSharedPreferences("login",0);
        inti();
        prefManager=new PrefManager(this);
        if(!prefManager.isFirstTimeLaunch()){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }

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
                Intent intent=new Intent(this,SignupActivity.class);
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
                    Empasword.setError("Enter Employee Password");
                }
                if(!CodeHolder.isEmpty() && !PasswordHolder.isEmpty())
                {
                    Loginfrom(CodeHolder,PasswordHolder);
                }
                break;
            case R.id.forgetpassword:
                Intent intentfg=new Intent(this,ForgetPassActivity.class);
                startActivity(intentfg);
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

                        Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();

                        try {
                            System.out.println("response :" + response);
                            parseData(response);
                            showMessage(response);


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

    private void showMessage(String response) {
        // Creating new instance of Toast
        Toast toast
                = Toast.makeText(
                LoginActivity.this,
                response,
                Toast.LENGTH_SHORT);

        // Getting the View
        View view = toast.getView();

        // Finding the textview in Toast view
        TextView text
                = (TextView)view.findViewById(
                android.R.id.message);

        // Setting the Text Appearance
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.M) {
            text.setTextAppearance(
                    R.style.toastTextStyle);
        }

        // Showing the Toast Message
        toast.show();

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


                    SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
                    editor.putString("empcode", publicempcode);
                    editor.putString("name",publicname);
                    editor.putString("mobile", publicmobile);
                    editor.putString("email",publicemail);
                    editor.putString("designation",publicdesignation);
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