package com.example.squatchallenge;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.Any;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Friend_list extends AppCompatActivity {

    private Button add_friend;
    private Button add_friend_button;
    private LinearLayout add_friend_chang;
    private LinearLayout friend_linear;
    private TextView friends_num;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference DB;
    String email="";
    String[] friendSet = {"친구를\n추가하세요"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        Intent intent = getIntent();
        email = intent.getStringExtra("Email");
        DB = FirebaseDatabase.getInstance().getReference("users").child(email);

        add_friend_button = findViewById(R.id.add_friend_button); // 메인의 친구추가 버튼
        friend_linear = findViewById(R.id.friend_linear); // 메인의 상단layer
        add_friend_chang = findViewById(R.id.add_friend_chang); // 친구추가 창
        add_friend = findViewById(R.id.add_friend); // 친구추가 창의 친구추가 버튼
        friends_num = findViewById(R.id.friends_num); // 친구 수

        add_friend_button.setOnClickListener(view -> { // 친구추가 누르면 친구추가 창 뜸
            add_friend_chang.setVisibility(View.VISIBLE);
            friend_linear.setVisibility(View.INVISIBLE);
        });

        add_friend.setOnClickListener(view -> { // 친구추가 창에서 아이디 치고 친구추가 누르면
            EditText add_friend_name = findViewById(R.id.add_friend_name); // 화면 돌아오면서 추가
            String _add_friend_name = add_friend_name.getText().toString();
            newfriend(_add_friend_name);
            add_friend_chang.setVisibility(View.INVISIBLE);
            friend_linear.setVisibility(View.VISIBLE);
        });
        // recyclerview 국룰
        recyclerView = findViewById(R.id.list_friend);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //친구목록 구현
        make_friendSet();
    }

    // recyclerview 국룰
    public class MyAdapter_friend extends RecyclerView.Adapter<MyAdapter_friend.MyViewHolder> {
        private String[] textSet ={};

        public MyAdapter_friend(String[] textSet){
            this.textSet = textSet;
        }

        public class MyViewHolder extends  RecyclerView.ViewHolder{
            public TextView textView;

            public MyViewHolder(View view){
                super(view);
                this.textView = view.findViewById(R.id.friend_name);
            }
        }

        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list, viewGroup, false);
            MyViewHolder myViewHolder = new MyViewHolder(holderView);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            myViewHolder.textView.setText(this.textSet[i]);
        }

        @Override
        public int getItemCount() {
            return textSet.length;
        }
    }

    public void newfriend(final String fname) {
        DB = FirebaseDatabase.getInstance().getReference("users/" + email + "/friend_list");
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exist = false;
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                int friends = (int)snapshot.getChildrenCount();

                // 친구 이미 있나 검사
                while (child.hasNext()) {
                    if (child.next().getValue().equals(fname)) {
                        exist = true;
                        break;
                    }
                }

                // push로 중복넣기, 배열에 추가하고 동기화
                if(!exist){
                    DB.push().setValue(fname);
                    friendSet[friends] = fname;
                    adapter = new MyAdapter_friend(friendSet);
                    recyclerView.setAdapter(adapter);
                    friends_num.setText("친구 0 / "+ (friends+1));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void make_friendSet() {

        DB = FirebaseDatabase.getInstance().getReference("users/" + email + "/friend_list");
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // datasnapshot 정확히 모르겠는데 걍 쓰는중
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                int friends = (int)snapshot.getChildrenCount(); // 친구수
                friends_num.setText("친구 0 / "+ friends);

                // child 돌면서 value 가져와서 배열에 넣음
                for(int i=0;i<friends;i++) {
                    friendSet[i] = (String)snapshot.getValue();
                    child.next();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new MyAdapter_friend(friendSet);
        recyclerView.setAdapter(adapter);
    }
}