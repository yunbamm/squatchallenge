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
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        Intent intent = getIntent();
        final String email = intent.getStringExtra("Email");
        DB = FirebaseDatabase.getInstance().getReference("users").child(email);

        add_friend_chang = findViewById(R.id.add_friend_chang);
        add_friend_chang.setVisibility(View.INVISIBLE);
        add_friend_button = findViewById(R.id.add_friend_button);
        add_friend = findViewById(R.id.add_friend);

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
                flistcheck(_add_friend_name);
                add_friend_chang.setVisibility(View.INVISIBLE);
            }
        });
        recyclerView = findViewById(R.id.list_friend);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] friendSet = {"endeoddl9","dopax","greenday","akaps","pnpm"};
        int[] imgSet = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground};

        adapter = new MyAdapter_friend(friendSet, imgSet);
        recyclerView.setAdapter(adapter);
    }

    public class MyAdapter_friend extends RecyclerView.Adapter<MyAdapter_friend.MyViewHolder> {
        private String[] textSet ={};
        private int[] imgSet ={};

        public MyAdapter_friend(String[] textSet, int[] imgSet){
            this.textSet = textSet;
            this.imgSet = imgSet;
        }

        public class MyViewHolder extends  RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView;

            public MyViewHolder(View view){
                super(view);
                this.imageView = view.findViewById(R.id.friend_img);
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
            myViewHolder.imageView.setBackgroundResource(this.imgSet[i]);
        }

        @Override
        public int getItemCount() {
            return Math.max(textSet.length, imgSet.length);
        }
    }

    public void new_friend(String fname){
        User friend = new User(fname);
        DB.child("friend_list").child(fname).push().setValue(friend);
    }

    public void fcheck(final String fname) {
        DB = DB.child("friend_list");
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exist = false;
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();

                while (child.hasNext()) {
                    if (child.next().getKey().equals(fname)) {
                        exist = true;
                        break;
                    }
                }

                if(!exist){
                    new_friend(fname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void flistcheck(final String fname) {
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exist = false;
                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();

                while (child.hasNext()) {
                    if (child.next().getKey()=="friend_list") {
                        fcheck(fname);
                        break;
                    }
                }

                if(!exist){
                    Map<String, Object> friend_list = new HashMap<>();
                    DB.updateChildren(friend_list);
                    new_friend(fname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}