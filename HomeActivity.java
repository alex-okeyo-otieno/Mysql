package com.example.okeyobrains.loginapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textView=findViewById(R.id.welcome_txt);
        String message=getIntent().getStringExtra("message");
        textView.setText(message);
    }
}
