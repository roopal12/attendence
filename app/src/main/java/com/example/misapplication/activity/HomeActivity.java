package com.example.misapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

import com.example.misapplication.R;
import com.example.misapplication.session.PrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;
@RequiresApi(api = Build.VERSION_CODES.Q)
public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewEname;
    Button buttonContinue;
    CircleImageView mImageView;

    SharedPreferences sharedPreferenceslogin,permissionStatus;

    PrefManager prefManager;

    private final int PICK_IMAGE_CAMERA = 1;

    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    private String[] permissionRequired = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_MEDIA_LOCATION};


    private static final String TAGa = HomeActivity.class.getSimpleName();
    private boolean sentToSettings = false;

   TextView et_image;
    int BITMAP_QUALITY = 100;
    int MAX_IMAGE_SIZE = 500;
    String TAG = "AddPerson";
    private Bitmap bitmap;
    private File destination = null;
    private String imgPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Session maintain.............
        prefManager=new PrefManager(this);
        prefManager.setFirstTimeLaunch(false);
        sharedPreferenceslogin=getSharedPreferences("logindetails",MODE_PRIVATE);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        inti();

    }

    private void inti() {

        textViewEname=findViewById(R.id.employeename);

        String name = sharedPreferenceslogin.getString("name", "");
        textViewEname.setText(name);

        buttonContinue=findViewById(R.id.btn_get_data);
        mImageView=findViewById(R.id.imageview_account_profile);
        et_image=findViewById(R.id.getpath);

        buttonContinue.setOnClickListener(this);
        mImageView.setOnClickListener(this);

        destination = new File(Constants.getFaceShapeModelPath() + "/temp.jpg");

    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_get_data:
                ContinueButton();
                break;
            case R.id.imageview_account_profile:
                ClickImge();
                break;
        }

    }

    private void ContinueButton(){

    }


    private void ClickImge()
    {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo","Cancel"};
              AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        }
                         else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                Bitmap scaledBitmap = scaleDown(bitmap, MAX_IMAGE_SIZE, true);
                et_image.setText(destination.getAbsolutePath());
                new detectAsync().execute(scaledBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    @Override
    protected void onStart() {
        super.onStart();

        if ((ActivityCompat.checkSelfPermission(this, permissionRequired[0]) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, permissionRequired[1]) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, permissionRequired[2]) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, permissionRequired[3]) != PackageManager.PERMISSION_GRANTED)) {

            Log.e("onPermission", "Please Check All Permissions");


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionRequired[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permissionRequired[1]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permissionRequired[2]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permissionRequired[3])) {

                Log.e("onStart ", "Permission Allow");

            } else if (permissionStatus.getBoolean(permissionRequired[0], false)) {
                sentToSettings = true;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);


                Log.e("onStart", "Please Allow All Permissions");

                Toast.makeText(HomeActivity.this, "Allow Storage Permission", Toast.LENGTH_LONG).show();

                permissionDialog();
            }

            ActivityCompat.requestPermissions(this, permissionRequired, STORAGE_PERMISSION_CODE);

        }else {
            Log.e("onPermission", "Else All Permissions");

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_PERMISSION_SETTING:
                for (int grantResult : grantResults) {

                    if (grantResult == PackageManager.PERMISSION_GRANTED) {

                        Log.e("onPermission", "Access");
                    } else {
                        // Snackbar.make(recyclerView, "Allow Storage Permission", Snackbar.LENGTH_SHORT).show();
                        Log.e("onPermission", "Please Allow All Permissions");
                        Toast.makeText(HomeActivity.this, "Allow All  PERMISSIONED", Toast.LENGTH_LONG).show();


                    }
                }
        }
    }

    private void permissionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Allow Permission!");
        builder.setMessage("Allow All Permissions");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    FaceDet faceDet ;

    private class detectAsync extends AsyncTask<Bitmap, Void, String> {
        ProgressDialog dialog = new ProgressDialog(HomeActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Detecting face...");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(Bitmap... bp) {
            faceDet = new FaceDet(Constants.getFaceShapeModelPath());
            List<VisionDetRet> results;
            results = faceDet.detect(bp[0]);
            String msg = null;
            if (results.size()==0) {
                msg = "No face was detected or face was too small. Please select a different image";
            } else if (results.size() > 1) {
                msg = "More than one face was detected. Please select a different image";
            } else {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bp[0].compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY, bytes);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgPath = destination.getAbsolutePath();
            }
            return msg;
        }

        protected void onPostExecute(String result) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
                if (result!=null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(HomeActivity.this);
                    builder1.setMessage(result);
                    builder1.setCancelable(true);
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    imgPath = null;
                    et_image.setText("");
                }
//                enableSubmitIfReady();
            }

        }
    }



}