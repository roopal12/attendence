package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassActivity extends AppCompatActivity{

    EditText editTextpswd,editTextcnfmpswd;
    String strpas,strcfmpass;
    CardView card1,card2,card3,card4,cardButtonSignUp;
    private ProgressDialog mProgress;
    private  boolean is8char=false, hasUpper=false, hasnum=false, hasSpecialSymbol =false, isSignupClickable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        editTextpswd=findViewById(R.id.etFullpas);
        editTextcnfmpswd=findViewById(R.id.etnew_password);

        card1 = (CardView)findViewById(R.id.card1);
        card2 = (CardView)findViewById(R.id.card2);
        card3 = (CardView)findViewById(R.id.card3);
        card4 = (CardView)findViewById(R.id.card4);
        cardButtonSignUp = (CardView)findViewById(R.id.cardsignup);
        mProgress =new ProgressDialog(this);
        String titleId="Password Changing......";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");
        cardButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordValidate();
            }
        });
    }
    @SuppressLint("ResourceType")
    private void passwordValidate(){
        strpas= editTextpswd.getText().toString();
        strcfmpass = editTextcnfmpswd.getText().toString();

        if(strpas.isEmpty())
        {
            editTextpswd.setError("Please Password");
        }
        if(strcfmpass.isEmpty())
        {
            editTextcnfmpswd.setError("Renter Password ");
        }
        // 8 character
        if(strpas.length()>= 8)
        {
            is8char = true;
            card1.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        //number
        if(strpas.matches("(.*[0-9].*)"))
        {
            hasnum = true;
            card2.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        //upper case
        if(strpas.matches("(.*[A-Z].*)"))
        {
            hasUpper = true;
            card3.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        //symbol
        if(strpas.matches("^(?=.*[_.()$&@]).*$")){
            hasSpecialSymbol = true;
            card4.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        // TODO Auto-generated method stub
        if (strpas.equals(strcfmpass)) {
            //password and confirm passwords equal.go to next step
            mProgress.show();
            Toast.makeText(getApplicationContext(),"Correct Password",Toast.LENGTH_LONG).show();

        }
        else
            {
                 is8char = false;
                 card1.setCardBackgroundColor(Color.parseColor(getString(R.color.grey)));

                 hasUpper = false;
                 card2.setCardBackgroundColor(Color.parseColor(getString(R.color.grey)));

                 hasUpper = false;
                 card3.setCardBackgroundColor(Color.parseColor(getString(R.color.grey)));

                 hasSpecialSymbol = false;
                 card4.setCardBackgroundColor(Color.parseColor(getString(R.color.grey)));

                 Toast.makeText(getApplicationContext(),"Enter Correct Password",Toast.LENGTH_LONG).show();

            }



    }
}