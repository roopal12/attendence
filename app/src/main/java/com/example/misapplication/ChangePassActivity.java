package com.example.misapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class ChangePassActivity extends AppCompatActivity{

    EditText editTextpswd,editTextcnfmpswd;
    String strpas,strcfmpass;
    CardView card1,card2,card3,card4,cardButtonSignUp;
    private ProgressDialog mProgress;
    private  boolean is8char=false, hasUpper=false, hasnum=false, hasSpecialSymbol =false, isSignupClickable = false;
    private RequestQueue rQueue;

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
        if (strpas.equals(strcfmpass)){
            //password and confirm passwords equal.go to next step
            mProgress.show();
            final String sessionempId = getIntent().getStringExtra("emp_id");
            final String sessionempmobile = getIntent().getStringExtra("mobile");


            String changespas="http://easy2billing.com/attendance/api/update_pass.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, changespas,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {

                            rQueue.getCache().clear();

                            try{

                                System.out.println("response :" + response);

                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mProgress.dismiss();

                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {

                             Toast.makeText(ChangePassActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("emp_id",sessionempId);
                    params.put("mobile",sessionempmobile);
                    params.put("new_pass",strcfmpass);
                    return params;
                }

            };
            rQueue = Volley.newRequestQueue(ChangePassActivity.this);
            rQueue.add(stringRequest);
        }

        else {
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