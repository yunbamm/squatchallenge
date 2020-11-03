package com.example.squatchallenge;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    private View friend_plus;
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

        add_friend_button.setOnClickListener(new View.OnClickListener() {      //구글 로그인 버튼을 클릭했을때 이곳을 수행
            @Override
            public void onClick(View view) {
                add_friend_chang.setVisibility(View.VISIBLE);
            }
        });

        add_friend.setOnClickListener(new View.OnClickListener() {      //구글 로그인 버튼을 클릭했을때 이곳을 수행
            @Override
            public void onClick(View view) {
                add_friend_chang.setVisibility(View.INVISIBLE);
            }
        });
        recyclerView = findViewById(R.id.list_friend);

        // 리사이클러뷰의 notify()처럼 데이터가 변했을 때 성능을 높일 때 사용한다.
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] textSet =  {"endeoddl9","dopax","greenday","akaps"};
        int[] imgSet = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground};

        // 어댑터 할당, 어댑터는 기본 어댑터를 확장한 커스텀 어댑터를 사용할 것이다.
        adapter = new MyAdapter(textSet, imgSet);
        recyclerView.setAdapter(adapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        // 이 데이터들을 가지고 각 뷰 홀더에 들어갈 텍스트 뷰에 연결할 것
        private String[] textSet;
        private int[] imgSet;

        // 생성자
        public MyAdapter(String[] textSet, int[] imgSet){
            this.textSet = textSet;
            this.imgSet = imgSet;
        }

        // 리사이클러뷰에 들어갈 뷰 홀더, 그리고 그 뷰 홀더에 들어갈 아이템들을 지정
        public class MyViewHolder extends  RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView;

            public MyViewHolder(View view){
                super(view);
                this.imageView = view.findViewById(R.id.friend_img);
                this.textView = view.findViewById(R.id.friend_name);
            }
        }

        // 어댑터 클래스 상속시 구현해야할 함수 3가지 : onCreateViewHolder, onBindViewHolder, getItemCount
        // 리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adater<>에서 <>안에들어가는 타입을 따른다.

        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list, viewGroup, false);
            MyViewHolder myViewHolder = new MyViewHolder(holderView);
            return myViewHolder;
        }

        // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            myViewHolder.textView.setText(this.textSet[i]);
            myViewHolder.imageView.setBackgroundResource(this.imgSet[i]);
        }

        // iOS의 numberOfRows, 리사이클러뷰안에 들어갈 뷰 홀더의 개수
        @Override
        public int getItemCount() {
            return textSet.length > imgSet.length ? textSet.length : imgSet.length;
        }
    }
}