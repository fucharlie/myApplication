package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        WebView login = findViewById(R.id.login);
        WebSettings settings = login.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        //login允许js调用java类
        login.addJavascriptInterface(new LoginScript(),"login");
        login.loadUrl("file:///android_asset/login.html");
    }
    public class LoginScript{
        @JavascriptInterface
        public void toOrderPage(){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,OrderActivity.class);
            startActivity(intent);
            finish();
        }
    }
}