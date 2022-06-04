package com.example.ushopcodescanner;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.ushopqrcodescanner.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    ImageView imgMenu;
    public static final String MyPREFERENCE = "SharedPrefUrlList";
    private SharedPreferences sharedPreferences1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codeScannerView = findViewById(R.id.scannerView);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String token = sharedPreferences.getString("Token", "");
        Log.d("Tag", "Token" + token);

        sharedPreferences1 = getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
        String json = sharedPreferences1.getString("MyPref3", "");
        boolean isJsonValid = null != json && !json.isEmpty() && !json.equals("[]");
        if (token != null && !token.isEmpty() && !token.equals("null") && isJsonValid) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> urlList1 = gson.fromJson(json, type);
            Log.d("TAG", "List " + urlList1);
            if (urlList1.size() <= 1) {
                String url2 = urlList1.get(0);
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class).putExtra("URL2", url2);
                startActivity(intent);
                Log.d("fun", "onCreate:"+ "first");
            }else{
                String url1 = urlList1.get(urlList1.size() - 1);
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class).putExtra("URL1", url1);
                Log.d("TAG", "It reached." + url1);
                Log.d("fun", "onCreate:"+ "second");
                startActivity(intent);
            }


        }

        codeScanner = new CodeScanner(this, codeScannerView);


        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        txtUrl.setText(result.getText().toString());
//                        Log.d("MeterId", "id" + result);

                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class).putExtra("URL", result.getText().toString());
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Log.d("fun", "onCreate:"+ "third");
                    }
                });
            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}