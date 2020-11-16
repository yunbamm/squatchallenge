package com.example.squatchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class stage1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage);

        ImageView upto_stage2 = findViewById(R.id.upto_stage2); // 화살표로 페이지 이동
        upto_stage2.setOnClickListener(view -> {
            Intent intent12 = new Intent(getApplicationContext(), stage2.class);
            startActivity(intent12);
        });

        TextView stage1 = findViewById(R.id.stage1);
        stage1.setOnClickListener(view -> {
            stage_clear(stage1);
        });
    }

    public void stage_clear(TextView textview){
        textview.setBackgroundResource(R.drawable.check1);
        textview.setText("");
    }
}