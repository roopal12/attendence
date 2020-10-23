package com.example.misapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import com.soundcloud.android.crop.BuildConfig;
import com.soundcloud.android.crop.Crop;
@RequiresApi(api = Build.VERSION_CODES.Q)
public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewEname;
    Button buttonContinue;
    CircleImageView mImageView;

    SharedPreferences sharedPreferenceslogin,permissionStatus;

    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    private String[] permissionRequired = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_MEDIA_LOCATION};

    private static final String TAG = HomeActivity.class.getSimpleName();
    private boolean sentToSettings = false;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        buttonContinue.setOnClickListener(this);
        mImageView.setOnClickListener(this);

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

    private void ContinueButton() {

    }


    private void ClickImge(){


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

}