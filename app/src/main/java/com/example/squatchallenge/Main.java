package com.example.squatchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity {

    private TextView quest_name1;
    private TextView quest_name2;
    private TextView quest_name3;
    private ImageView reroll1;
    private ImageView reroll2;
    private ImageView reroll3;
    private int questcnt;
    Map<Integer,String> questSet = new HashMap<>();
    Main.quest[] today_quest = new Main.quest[3];

    //카메라 권한을 위해
    private int RESULT_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Main.quest q = new Main.quest("",0,true);
        q.setTodayQuest();

        // 안드로이드 6.0 이상 버전에서는 CAMERA 권한 허가를 요청한다.
        requestPermissionCamera();

        Intent intent = getIntent();
        final String nickName = intent.getStringExtra("name");        //loginactivity로부터 닉네임 전달받음
        final String photoUrl = intent.getStringExtra("photoUrl");        //loginactivity로부터 프로필사진 Url전달받음
        final String email = intent.getStringExtra("Email");        //구글이메일

        LinearLayout Quest = findViewById(R.id.Quest);
        LinearLayout questlist = findViewById(R.id.questlist);
        LinearLayout main_linear = findViewById(R.id.main_linear);

        Quest.setOnClickListener(view -> { // 퀘스트 누르면 위로 오버랩되게 처리
            questlist.setVisibility(View.VISIBLE);
            main_linear.setVisibility(View.INVISIBLE);
        });
        questlist.setOnClickListener(view -> {
            questlist.setVisibility(View.INVISIBLE);
            main_linear.setVisibility(View.VISIBLE);
        });

        reroll1 = findViewById(R.id.reroll1);
        reroll2 = findViewById(R.id.reroll2);
        reroll3 = findViewById(R.id.reroll3);
        reroll1.setOnClickListener(view -> reroll(reroll1));
        reroll2.setOnClickListener(view -> reroll(reroll2));
        reroll3.setOnClickListener(view -> reroll(reroll3));

        LinearLayout friendlist = findViewById(R.id.Friendlist); // 친구목록으로
        friendlist.setOnClickListener(view -> {
            Intent intent12 = new Intent(getApplicationContext(), Friend_list.class);
            intent12.putExtra("Email" , email);
            startActivity(intent12);
        });

        LinearLayout my_page = null;
        my_page = findViewById(R.id.My_page); // 마이페이지로
        my_page.setOnClickListener(view -> {
            Intent intent1 = new Intent(getApplicationContext(), Mypage.class);
            intent1.putExtra("name" , nickName);
            intent1.putExtra("photoUrl",photoUrl);
            intent1.putExtra("Email",email);
            startActivity(intent1);
        });

        LinearLayout solo = findViewById(R.id.Solo);
        LinearLayout random = findViewById(R.id.Random);

        //솔로 플레이 버튼이 눌렸을때 (스피드모드)
        solo.setOnClickListener(view -> {
            Intent intent14 = new Intent(getApplicationContext(), solo_speed_play.class);
            intent14.putExtra("Email" , email);       //우선 id만 넘겨준다 가정
            startActivity(intent14);
        });
        //랜덤(협력)
        random.setOnClickListener(view -> {
            Intent intent13 = new Intent(getApplicationContext(), team_play.class);
            intent13.putExtra("Email" , email);       //우선 id만 넘겨준다 가정
            startActivity(intent13);
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

        void setTodayQuest(){ // 퀘스트 랜덤하게 선정
            setQuest();
            int ran = (int)(Math.random()*9);
            for(int i=0;i<3;i++){
                if(ran>=8) ran=0;
                else if(ran<=0) ran=0;
                today_quest[i]= new Main.quest(questSet.get(ran),ran+1,false);
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

    public void reroll(ImageView imageView){ // 리롤하면 버튼 사라지고 퀘스트 교체
        imageView.setOnClickListener(view -> {
            if(imageView == findViewById(R.id.reroll1)) {
                if(questcnt>=8) questcnt=0;
                quest_name1.setText(questSet.get(questcnt));
                reroll1.setVisibility(View.INVISIBLE);
                questcnt++;
            }
            else if(imageView == findViewById(R.id.reroll2)) {
                if(questcnt>=8) questcnt=0;
                quest_name2.setText(questSet.get(questcnt));
                reroll2.setVisibility(View.INVISIBLE);
                questcnt++;
            }
            else {
                if(questcnt>=8) questcnt=0;
                quest_name3.setText(questSet.get(questcnt));
                reroll3.setVisibility(View.INVISIBLE);
                questcnt++;
            }
        });
    }

    //카메라라
    private boolean requestPermissionCamera() {
        int sdkVersion = Build.VERSION.SDK_INT;
        if(sdkVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Main.this,
                        new String[]{Manifest.permission.CAMERA},
                        RESULT_PERMISSIONS);

            }
//            else {
//                setInit();
//            }
        }
//        else{  // version 6 이하일때
//            setInit();
//            return true;
//        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (RESULT_PERMISSIONS == requestCode) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허가시
                //setInit();
            } else {
                // 권한 거부시
            }
            return;
        }

    }
}