package com.example.squatchallenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private LinearLayout Quest;
    private LinearLayout questlist;
    private LinearLayout friendlist;
    private LinearLayout Solo;
    private LinearLayout Random;
    private HorizontalScrollView scrollview;
    private TextView tv_result;
    private TextView quest_name1;
    private TextView quest_name2;
    private TextView quest_name3;
    private ImageView iv_profile;
    private ImageView reroll1;
    private ImageView reroll2;
    private ImageView reroll3;
    private int questcnt;
    Map<Integer,String> questSet = new HashMap<>();
    quest[] today_quest = new quest[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quest q = new quest("",0,true);
        q.setTodayQuest();

        Intent intent = getIntent();
        final String nickName = intent.getStringExtra("name");        //loginactivity로부터 닉네임 전달받음
        String photoUrl = intent.getStringExtra("photoUrl");        //loginactivity로부터 프로필사진 Url전달받음
        final String email = intent.getStringExtra("Email");        //구글이메일

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
        reroll1 = findViewById(R.id.reroll1);
        reroll2 = findViewById(R.id.reroll2);
        reroll3 = findViewById(R.id.reroll3);
        reroll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questcnt>=8) questcnt=0;
                quest_name1.setText(questSet.get(questcnt));
                reroll1.setVisibility(View.INVISIBLE);
                questcnt++;
            }
        });
        reroll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questcnt>=8) questcnt=0;
                quest_name2.setText(questSet.get(questcnt));
                reroll2.setVisibility(View.INVISIBLE);
                questcnt++;
            }
        });
        reroll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(questcnt>=8) questcnt=0;
                quest_name3.setText(questSet.get(questcnt));
                reroll3.setVisibility(View.INVISIBLE);
                questcnt++;
            }
        });

        friendlist = findViewById(R.id.Friendlist);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Friend_list.class);
                intent.putExtra("Email" , email);
                startActivity(intent);
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
                intent.putExtra("name" , nickName);
            }
        });

        Solo = findViewById(R.id.Solo);
        Random = findViewById(R.id.Random);
        
        //솔로 플레이 버튼이 눌렸을때 (스피드모드)
        Solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), solo_speed_play.class);
                intent.putExtra("Email" , email);       //우선 id만 넘겨준다 가정
                startActivity(intent);
            }
        });
        //구현해야댐
        Random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public class quest{
        String name;
        int num;
        Boolean clear;

        quest (){
            this.name="";
            this.num=-1;
            this.clear=false;
        }

        quest (String q, int n, Boolean c){
            this.name=q;
            this.num=n;
            this.clear=c;
        }

        void setTodayQuest(){
            setQuest();
            int ran = (int)(Math.random()*9);
            for(int i=0;i<3;i++){
                if(ran>=8) ran=0;
                else if(ran<=0) ran=0;
                today_quest[i]= new quest(questSet.get(ran),ran+1,false);
                questSet.remove(ran);
                ran++;
            }
            questcnt=ran;
            quest_name1 = findViewById(R.id.quest_name1);
            quest_name1.setText(today_quest[0].name);
            quest_name2 = findViewById(R.id.quest_name2);
            quest_name2.setText(today_quest[1].name);
            quest_name3 = findViewById(R.id.quest_name3);
            quest_name3.setText(today_quest[2].name);
        }
    };

    public void setQuest(){
        questSet.put(1,"스쿼트 55회");
        questSet.put(2,"친선전 1회");
        questSet.put(3,"1대1 3회");
        questSet.put(4,"1대1 승리");
        questSet.put(5,"스쿼트 100회");
        questSet.put(6,"친구추가\n1회");
        questSet.put(7,"혼자하기\n3회");
        questSet.put(8,"랭커 영상\n1회 관전");
        questSet.put(0,"친선전 승리");
    }
}