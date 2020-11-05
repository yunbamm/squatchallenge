package com.example.squatchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Dictionary;

public class MainActivity extends AppCompatActivity {
    private LinearLayout Quest;
    private LinearLayout questlist;
    private LinearLayout friendlist;
    private HorizontalScrollView scrollview;
    private TextView tv_result;
    private ImageView iv_profile;
    String[] questSet = {"스쿼트 55회","1대1 3회","친구추가\n1회","스쿼트 100회", "혼자하기\n3회","1대1 5회",
            "1대1 3회\n승리","혼자하기\n5회","친선전 1회"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("name");        //loginactivity로부터 닉네임 전달받음
        String photoUrl = intent.getStringExtra("photoUrl");        //loginactivity로부터 프로필사진 Url전달받음

        tv_result = findViewById(R.id.tv_name);
        tv_result.setText(nickName);        //닉네임 text를 텍스트뷰에 세팅
        iv_profile = findViewById(R.id.iv_profile);
        Glide.with(this).load(photoUrl).into(iv_profile);       //프로필 url을 이미지뷰에 세팅

        Quest = findViewById(R.id.Quest);
        questlist = findViewById(R.id.questlist);
        scrollview = findViewById(R.id.scrollview);
        Quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questlist.getVisibility()==View.INVISIBLE) {
                    questlist.setVisibility(View.VISIBLE);
                    scrollview.setVisibility(View.INVISIBLE);
                }
                else{
                    questlist.setVisibility(View.INVISIBLE);
                    scrollview.setVisibility(View.VISIBLE);
                }
            }
        });

        friendlist = findViewById(R.id.Friendlist);
        friendlist.setOnClickListener(new View.OnClickListener() {      //구글 로그인 버튼을 클릭했을때 이곳을 수행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), friend_list.class);
                startActivity(intent);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {      //구글 로그인 버튼을 클릭했을때 이곳을 수행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });
    }

    /*public void setQuest(){
        boolean[] uesd = new boolean[9];
        TextView view;
        String quest_name;
        for(int i=0;i<3;i++){
            int random = (int)(Math.random()*9);
             quest_name= "quest_name"+"i";
            view = findViewById(R.id.quest_name);
        }
    }*/

    public void rerollquest(LinearLayout view, int i){

    }
}