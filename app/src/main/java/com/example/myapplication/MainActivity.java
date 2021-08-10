package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //生命周期回调方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void hello(View view){
        EditText username =findViewById(R.id.username);
        System.out.println("Hello,"+username.getText());
    }
}