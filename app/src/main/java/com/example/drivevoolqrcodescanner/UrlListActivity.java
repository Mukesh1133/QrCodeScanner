package com.example.drivevoolqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UrlListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UrlListAdapter urlListAdapter;

    public static final String MyPREFERENCE = "MyPrefs";
    public static final String URLPATH = "urlPath";
    private SharedPreferences sharedPreferences;
    private ArrayList<String> urlPathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_list);
        recyclerView = findViewById(R.id.rvUrlList);

        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        String url = getIntent().getStringExtra("URL_PATH");

        sharedPreferences = getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(MyPREFERENCE, "");
        if (value.isEmpty()) {
            urlPathList = new ArrayList<>();
            if(url != null){
                urlPathList.add(url);
            }

            Gson gson = new Gson();
            String json = gson.toJson(urlPathList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MyPREFERENCE,json);
            editor.commit();
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            urlPathList = gson.fromJson(value,type);
            if(url != null){
                urlPathList.add(url);
            }

            Gson gson1 = new Gson();
            String json2 = gson1.toJson(urlPathList);
            SharedPreferences.Editor editor2 = sharedPreferences.edit();
            editor2.putString(MyPREFERENCE,json2);
            editor2.commit();
        }

        urlListAdapter = new UrlListAdapter(urlPathList,this);
        recyclerView.setAdapter(urlListAdapter);


//        urlPathList.add(urlList);
//        Gson gson = new Gson();
//        String json = gson.toJson(urlPathList);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(URLPATH, json);
//        urlListAdapter = new UrlListAdapter(urlPathList, this);
//        recyclerView.setAdapter(urlListAdapter);
//        editor.commit();

//        buttonUrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerView.setVisibility(View.INVISIBLE);
//                recyclerView1.setVisibility(View.VISIBLE);
//                Gson gson = new Gson();
//                String json = sharedPreferences.getString(URLPATH, "");
//                if (json.isEmpty()) {
//                    Toast.makeText(UrlListActivity.this, "No URL to show.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Type type = new TypeToken<ArrayList<String>>() {
//                    }.getType();
//                    ArrayList<String> urlListData = gson.fromJson(json, type);
//                    recyclerView1.setLayoutManager(new LinearLayoutManager(UrlListActivity.this));
//                    urlListAdapter1 = new UrlListAdapter(urlListData, UrlListActivity.this);
//                    recyclerView1.setAdapter(urlListAdapter1);
//
//                }
//            }
//        });

    }

}