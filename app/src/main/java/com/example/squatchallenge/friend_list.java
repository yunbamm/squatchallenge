package com.example.squatchallenge;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class friend_list extends AppCompatActivity {

    private Button add_friend;
    private Button add_friend_button;
    private View add_friend_chang;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

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
                add_friend_chang.setVisibility(View.INVISIBLE);
            }
        });
        recyclerView = findViewById(R.id.list_friend);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] friendSet =  {"endeoddl9","dopax","greenday","akaps","pnpm"};
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
}