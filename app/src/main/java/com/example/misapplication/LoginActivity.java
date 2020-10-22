package com.example.misapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.R)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewforgetp,textViewregistered;
    EditText EmpName,Empasword;
    Button buttonlogin;
    CheckBox checkBoxMe;
    SharedPreferences sharedPreferencesLogin;

    String PasswordHolder, CodeHolder;
    String finalResult ;

    String HttpURL = "https://androidjsonblog.000webhostapp.com/User/UserLogin.php";

    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferencesLogin=getSharedPreferences("login",0);
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
                Intent intent=new Intent(this,SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                if(isConnected(this))
                {
                    showCustumDailog();
                }
//                CheckEditTextIsEmptyOrNot();
//                if(CheckEditText){
//
////                    UserLoginFunction(CodeHolder, PasswordHolder);
//                    Toast.makeText(LoginActivity.this, " form fields.", Toast.LENGTH_LONG).show();
//
//                }
//                else {
//
//                    Toast.makeText(LoginActivity.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
//
//                }
                break;
            case R.id.forgetpassword:
                Intent intentfg=new Intent(this,ForgetPassActivity.class);
                startActivity(intentfg);
                break;
        }

    }
    //    check internet connection
    private boolean isConnected(LoginActivity loginActivity) {
        ConnectivityManager connectivityManager= (ConnectivityManager)loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonnection=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo moibleconnection=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wificonnection!=null && wificonnection.isConnected() &&  moibleconnection!=null && moibleconnection.isConnected()){
            return true ;

        }
        else{
            return false;
        }

    }

//    if not connected..................
    private void showCustumDailog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Please connect to the  internet to procced futher").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });


    }



    private void CheckEditTextIsEmptyOrNot() {
        CodeHolder = EmpName.getText().toString();
        PasswordHolder = Empasword.getText().toString();

        if(TextUtils.isEmpty(CodeHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }
    }

    private void UserLoginFunction(String codeHolder, String passwordHolder) {
        class UserLoginClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(LoginActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Data Matched")){

                    finish();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);

                }
                else{

                    Toast.makeText(LoginActivity.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("empcode",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();

        userLoginClass.execute(codeHolder,passwordHolder);
    }
}