package com.example.ushopcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.ushopqrcodescanner.R;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UrlTilesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UrlListAdapter urlListAdapter;

    public static final String MyPREFERENCE = "SharedPrefUrlList";
    private SharedPreferences sharedPreferences;
//    private ArrayList<String> urlPathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_list);
        recyclerView = findViewById(R.id.rvUrlList);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        String url = getIntent().getStringExtra("URL_PATH");


        sharedPreferences = getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);

        String json2 = sharedPreferences.getString("MyPref3", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> urlPathList = gson.fromJson(json2, type);
        Log.d("URLList2", "gotIt" + urlPathList);


//        String value = sharedPreferences.getString("MyPref2", "");
//        if (value.isEmpty()) {
//            urlPathList = new ArrayList<>();
//            if(url != null){
//                urlPathList.add(url);
//            }
//
//            Gson gson = new Gson();
//            String json = gson.toJson(urlPathList);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("MyPref2",json);
//            editor.apply();
//        } else {
//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<String>>() {
//            }.getType();
//            urlPathList = gson.fromJson(value,type);
//            if(url != null){
//                urlPathList.add(url);
//            }
//
//            Gson gson1 = new Gson();
//            String json2 = gson1.toJson(urlPathList);
//            SharedPreferences.Editor editor2 = sharedPreferences.edit();
//            editor2.putString("MyPref2",json2);
//            editor2.apply();
//        }


        urlListAdapter = new UrlListAdapter(urlPathList, this);
        recyclerView.setAdapter(urlListAdapter);

    }

}