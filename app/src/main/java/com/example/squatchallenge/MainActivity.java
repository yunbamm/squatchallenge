package com.example.squatchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private TextView tv_result;     //닉네임 text
    private ImageView iv_profile;       //이미지 뷰
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        String nickName = intent.getStringExtra("name");        //loginactivity로부터 닉네임 전달받음
        String photoUrl = intent.getStringExtra("photoUrl");        //loginactivity로부터 프로필사진 Url전달받음

        tv_result = findViewById(R.id.tv_name);
        tv_result.setText(nickName);        //닉네임 text를 텍스트뷰에 세팅

        //프로필 사진 가져올때 glide가 쓰인다
        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photoUrl).into(iv_profile);       //프로필 url을 이미지뷰에 세팅

    }
}