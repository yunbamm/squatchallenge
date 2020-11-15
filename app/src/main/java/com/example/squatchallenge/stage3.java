package com.example.squatchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class stage3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage3);

        ImageView downto_stage2 = findViewById(R.id.downto_stage2); // 화살표로 페이지 이동
        downto_stage2.setOnClickListener(view -> {
            Intent intent13 = new Intent(getApplicationContext(), stage2.class);
            startActivity(intent13);
        });
    }
}