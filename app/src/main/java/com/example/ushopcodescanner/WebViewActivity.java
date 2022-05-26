package com.example.ushopcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ushopqrcodescanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebViewActivity extends AppCompatActivity {

    private WebView wv1;
    private ArrayList<String> urlPathList2;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
//        Toolbar toolbar = findViewById(R.id.custom_toolbar);
//        setSupportActionBar(toolbar);


        wv1 = findViewById(R.id.webView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        wv1.setWebViewClient(new WebViewClient());
        String url = getIntent().getStringExtra("URL");
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences("SharedPrefUrlList", MODE_PRIVATE);

        String value = sharedPreferences2.getString("MyPref3", "");

        if (value.isEmpty()) {
            urlPathList2 = new ArrayList<>();
            if (url != null) {
                urlPathList2.add(url);
            }
            Gson gson = new Gson();
            String json = gson.toJson(urlPathList2);
            SharedPreferences.Editor editor = sharedPreferences2.edit();
            editor.putString("MyPref3", json);
            editor.apply();
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            urlPathList2 = gson.fromJson(value, type);
            if (url != null) {
                urlPathList2.add(url);
            }

            Gson gson1 = new Gson();
            String json2 = gson1.toJson(urlPathList2);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString("MyPref3", json2);
            editor2.apply();
        }
        Log.d("TAG", "url2" + urlPathList2);


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e("TAG", "FAILED TO GET THE TOKEN");
                } else {
                    String token = task.getResult();
                    Log.e("TAG1", "Token :" + token);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Token", token);
                    editor.apply();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG3", "FAILED TO GET THE TOKEN :" + e.getLocalizedMessage());
            }
        });


        String urlPath = getIntent().getStringExtra("URL_PATH");
        String url1 = getIntent().getStringExtra("URL1");
        String url2 = getIntent().getStringExtra("URL2");
        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings webViewSettings = wv1.getSettings();
        webViewSettings.setLoadsImagesAutomatically(true);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setDomStorageEnabled(true);
        webViewSettings.getUserAgentString();
        String userAgent;
        userAgent = System.getProperty("http.agent");
        webViewSettings.setUserAgentString(userAgent);


        // Enable Cookies
        CookieManager.getInstance().setAcceptCookie(true);
        if (android.os.Build.VERSION.SDK_INT >= 21)
            CookieManager.getInstance().setAcceptThirdPartyCookies(wv1, true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        wv1.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

//                 wv1.evaluateJavascript("javascript:window.localStorage.getItem('uid')",
//                        value -> Log.e("uid", value));
//                return super.onConsoleMessage(consoleMessage);

                wv1.evaluateJavascript("javascript:window.localStorage.getItem('uid')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("OnReceive", value.replace("\"", ""));
                        String newValue = value.replace("\"", "");

                        if (!newValue.isEmpty() && newValue != null && !newValue.equals("null")) {
                            String token = sharedPreferences.getString("Token", "");
                            Log.e("FCM_TOKEN", "token:" + token);
                            postData(newValue, token);
                        }

                    }
                });
                return super.onConsoleMessage(consoleMessage);
            }


        });

        wv1.clearCache(false);
        wv1.goBack();
//        wv1.loadUrl("https://bhola-kirana.web.app/store.html?id=Bhola_Kirana_Store0uh#");
        wv1.loadUrl(url);
        wv1.loadUrl(urlPath);
        wv1.loadUrl(url1);
        wv1.loadUrl(url2);
    }


    private void postData(String uid, String token) {

//        https://us-central1-drivoop.cloudfunctions.net/fcm?u=VYSvwBLr6KVLJAyuhF4SUGOUjMv1&f=FCM_TOKEN
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-drivoop.cloudfunctions.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<String> call = retrofitAPI.createPosts(uid, token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseFromApi = response.body();
                Log.d("pushed to firebase", "the Auth:" + responseFromApi);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("unable to push", "Error_Occurred" + call);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    public String getDomainName(String url) throws URISyntaxException, MalformedURLException {
//        String urlPath = getIntent().getStringExtra("URL");
        URL uri = new URL(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String urlPath = getIntent().getStringExtra("URL");

        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                finish();
                return true;
            case R.id.item2:
                Intent intent1 = new Intent(WebViewActivity.this, UrlTilesActivity.class).putExtra("URL_PATH", urlPath);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}