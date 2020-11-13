package com.example.squatchallenge;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Mypage extends AppCompatActivity {
    private LinearLayout achiv;
    private LinearLayout to_setting;
    private LinearLayout to_cuscen;
    private Button ach1;
    private Button ach2;
    private Button ach3;
    private Button rec1;
    private Button rec2;
    private Button rec3;
    private TextView tv_name;
    private TextView tv_myach;
    private ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String nickName = intent.getStringExtra("name");
        final String photoUrl = intent.getStringExtra("photoUrl");
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(nickName);
        tv_myach = findViewById(R.id.tv_myach);

        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photoUrl).into(iv_profile);

        ach1 = findViewById(R.id.ach1);
        ach2 = findViewById(R.id.ach2);
        ach3 = findViewById(R.id.ach3);
        ach1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_myach(ach1);
            }
        });
        ach2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_myach(ach2);
            }
        });
        ach3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_myach(ach3);
            }
        });
    }

    public void select_myach(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button.getTextScaleX()==1.05){
                    button.setTextColor(0xAAFFE607);
                    button.setBackgroundColor(0xAA575757);
                    button.setScaleX(1);
                }
                else {
                    button.setTextColor(0xAA575757);
                    button.setBackgroundColor(0xAAFFE607);
                    tv_myach.setText(button.getText());
                    button.setScaleX((float) 1.05);
                }
            }
        });
    }
}