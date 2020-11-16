package com.example.squatchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class stage2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage2);

        ImageView upto_stage3 = findViewById(R.id.upto_stage3); // 화살표로 페이지 이동
        upto_stage3.setOnClickListener(view -> {
            Intent intent12 = new Intent(getApplicationContext(), stage3.class);
            startActivity(intent12);
        });

        ImageView downto_stage1 = findViewById(R.id.downto_stage1); // 화살표로 페이지 이동
        downto_stage1.setOnClickListener(view -> {
            Intent intent13 = new Intent(getApplicationContext(), stage1.class);
            startActivity(intent13);
        });
    }
}