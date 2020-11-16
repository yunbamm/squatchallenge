package com.example.squatchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class Solo_selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_selection);

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");

        LinearLayout to_speed = findViewById(R.id.to_speed);
        to_speed.setOnClickListener(view -> {
            Intent intent13 = new Intent(getApplicationContext(), solo_speed_play.class);
            intent13.putExtra("Email" , email);       //우선 id만 넘겨준다 가정
            startActivity(intent13);
        });

        LinearLayout to_stage = findViewById(R.id.to_stage);
        to_stage.setOnClickListener(view -> {
            Intent intent14 = new Intent(getApplicationContext(), stage1.class);
            intent14.putExtra("Email" , email);       //우선 id만 넘겨준다 가정
            startActivity(intent14);
        });
    }
}