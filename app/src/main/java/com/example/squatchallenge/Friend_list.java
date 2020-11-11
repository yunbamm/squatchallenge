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
    private View add_friend_chang;
    private TextView friends_num;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference DB;
    String email="";
    String[] friendSet = {"친구를 추가하세요"};

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

        add_friend_chang = findViewById(R.id.add_friend_chang);
        add_friend_chang.setVisibility(View.INVISIBLE);
        add_friend_button = findViewById(R.id.add_friend_button);
        add_friend = findViewById(R.id.add_friend);
        friends_num = findViewById(R.id.friends_num);

        add_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_friend_chang.setVisibility(View.VISIBLE);
            }
        });

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText add_friend_name = findViewById(R.id.add_friend_name);
                String _add_friend_name = add_friend_name.getText().toString();
                newfriend(_add_friend_name);
                add_friend_chang.setVisibility(View.INVISIBLE);
            }
        });
        recyclerView = findViewById(R.id.list_friend);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        make_friendSet();
    }

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

                while (child.hasNext()) {
                    if (child.next().getValue().equals(fname)) {
                        exist = true;
                        break;
                    }
                }

                if(!exist){
                    DB.push().setValue(fname);
                    friendSet[friends] = fname;
                    adapter = new MyAdapter_friend(friendSet);
                    recyclerView.setAdapter(adapter);
                    friends_num.setText("친구 2 / "+ (friends+1));
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
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();
                int friends = (int)snapshot.getChildrenCount();
                friends_num.setText("친구 2 / "+ friends);

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