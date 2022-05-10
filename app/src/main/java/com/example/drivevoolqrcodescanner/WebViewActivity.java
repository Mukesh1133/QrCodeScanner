package com.example.drivevoolqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView wv1;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        wv1 = findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());

        String url = getIntent().getStringExtra("URL");
        String urlPath = getIntent().getStringExtra("URL_PATH");
//      String newUrlPath = String.format("http://", urlPath);
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setDomStorageEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(url);
        wv1.loadUrl(urlPath);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String urlPath = getIntent().getStringExtra("URL");
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                finish();
                return  true;
            case R.id.item2:
                Intent intent1 = new Intent(WebViewActivity.this,UrlListActivity.class).putExtra("URL_PATH",urlPath);
                startActivity(intent1);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlPath) {
            view.loadUrl(urlPath);
            return true;
        }
    }
}