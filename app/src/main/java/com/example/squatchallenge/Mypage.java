package com.example.squatchallenge;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Mypage extends AppCompatActivity {
    private ImageButton to_ach;
    private ImageButton to_record;
    private ImageButton to_setting;
    private ImageButton to_cuscen;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String nickName = intent.getStringExtra("name");
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(nickName);
        to_ach = findViewById(R.id.to_ach);
    }
}