package com.example.squatchallenge;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;

public class Mypage extends AppCompatActivity {
    private LinearLayout to_setting;
    private ImageView to_cuscen;
    private Button ach1;
    private Button ach2;
    private Button ach3;
    private Button rec1;
    private Button rec2;
    private Button rec3;
    private TextView tv_name;
    private TextView tv_myach;
    private ImageView iv_profile;
    DatabaseReference DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String nickName = intent.getStringExtra("name");
        final String photoUrl = intent.getStringExtra("photoUrl");
        final String email = intent.getStringExtra("Email");

        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(nickName);
        tv_myach = findViewById(R.id.tv_myach);

        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photoUrl).into(iv_profile);

        ach1 = findViewById(R.id.ach1);
        ach2 = findViewById(R.id.ach2);
        ach3 = findViewById(R.id.ach3);
        ach1.setOnClickListener(view -> select_myach(ach1));
        ach2.setOnClickListener(view -> select_myach(ach2));
        ach3.setOnClickListener(view -> select_myach(ach3));

        rec1 = findViewById(R.id.rec1);
        rec2 = findViewById(R.id.rec2);
        rec3 = findViewById(R.id.rec3);
        rec1.setOnClickListener(view -> { // 각 기록 누르면 db가서 값 가져옴
            DB = FirebaseDatabase.getInstance().getReference("users/" + email + "/total_count");
            DB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long c = (long) snapshot.getValue();
                    String cnt = c + "개";
                    rec1.setText(cnt);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });
        rec2.setOnClickListener(view -> {
            DB = FirebaseDatabase.getInstance().getReference("users/" + email + "/speed_time");
            DB.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long t = (long) snapshot.getValue();
                    String time = t + "초";
                    rec2.setText(time);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });
        rec3.setOnClickListener(view -> {
            String stage;
            stage = "스테이지\n3";
            rec3.setText(stage);
        });

        to_setting.findViewById(R.id.to_setting);
        to_setting.setOnClickListener(view -> { // 원래 설정으로 이동해야 되는데 지금 친구목록으로 해둠
            Intent intent1 = new Intent(getApplicationContext(), Friend_list.class);
            startActivity(intent1);
            intent1.putExtra("name" , nickName);
            intent1.putExtra("photoUrl",photoUrl);
            intent1.putExtra("Email",email);
        });

        to_cuscen.findViewById(R.id.to_cuscen);
        to_cuscen.setOnClickListener(view -> { // 원래 고객센터 팝업인데 지금 스테이지로 해둠
            Intent intent1 = new Intent(getApplicationContext(), stage.class);
            startActivity(intent1);
            intent1.putExtra("name" , nickName);
            intent1.putExtra("photoUrl",photoUrl);
            intent1.putExtra("Email",email);
        });
    }

    public void select_myach(Button button){ // 업적 눌렀을때 눌린 버튼만 강조 및 내 업적으로 설정
        button.setOnClickListener(view -> { // 나머지는 평범하게
            if(button == findViewById(R.id.ach1)) {
                button.setTextColor(0xAA575757);
                button.setBackgroundColor(0xAAFFE607);
                tv_myach.setText(button.getText());
                button.setBackgroundResource(R.drawable.buttonshape4);
                ach2.setTextColor(0xAAFFE607);
                ach2.setBackgroundColor(0xAA575757);
                ach2.setBackgroundResource(R.drawable.buttonshape3);
                ach3.setTextColor(0xAAFFE607);
                ach3.setBackgroundColor(0xAA575757);
                ach3.setBackgroundResource(R.drawable.buttonshape3);
            }
            else if(button == findViewById(R.id.ach2)) {
                button.setTextColor(0xAA575757);
                button.setBackgroundColor(0xAAFFE607);
                tv_myach.setText(button.getText());
                button.setBackgroundResource(R.drawable.buttonshape4);
                ach1.setTextColor(0xAAFFE607);
                ach1.setBackgroundColor(0xAA575757);
                ach1.setBackgroundResource(R.drawable.buttonshape3);
                ach3.setTextColor(0xAAFFE607);
                ach3.setBackgroundColor(0xAA575757);
                ach3.setBackgroundResource(R.drawable.buttonshape3);
            }
            else {
                button.setTextColor(0xAA575757);
                button.setBackgroundColor(0xAAFFE607);
                tv_myach.setText(button.getText());
                button.setBackgroundResource(R.drawable.buttonshape4);
                ach1.setTextColor(0xAAFFE607);
                ach1.setBackgroundColor(0xAA575757);
                ach1.setBackgroundResource(R.drawable.buttonshape3);
                ach2.setTextColor(0xAAFFE607);
                ach2.setBackgroundColor(0xAA575757);
                ach2.setBackgroundResource(R.drawable.buttonshape3);
            }
        });
    }
}