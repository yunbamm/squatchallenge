package com.example.squatchallenge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

    public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

        private SignInButton btn_google;        //구글 로그인 버튼
        private FirebaseAuth auth;              //파이어베이스 인증 객체
        private GoogleApiClient googleApiClient;    //구글 api 클라이언트 객체
        private static final int REQ_SIGN_GOOGLE = 100;     //구글 로그인 결과 코드

        //----------------------------test----------------------------------
        private String userEmail;
        private String userName;
        DatabaseReference DB;

        //-------------------------------------------------------------------

        @Override
    protected void onCreate(Bundle savedInstanceState) {        //앱이 실행될때 처음 수행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();      //파이어베이스 인증객체 초기화

        btn_google=findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {      //구글 로그인 버튼을 클릭했을때 이곳을 수행
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);      //구글에서 제공하는 인증화면
                startActivityForResult(intent , REQ_SIGN_GOOGLE);           //?
            }
        });
    }

    //---------------------------------------------------------------
    //함수부분
    //DB내의 중복아이디 검사
    public void check(final String userEmail , final String username) {
        DB = FirebaseDatabase.getInstance().getReference("users");         //완탐을 위해
        DB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exist = false;

                Iterator<DataSnapshot> child = snapshot.getChildren().iterator();

                while (child.hasNext()) {
                    //존재할때
                    if (child.next().getKey().equals(userEmail)) {
                        exist = true;
                        break;
                    }
                }

                if(!exist){
                    writeNewUser(userEmail , username);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //새로운 사용자 등록
    void writeNewUser(final String userEmail , String username) {
        DB = FirebaseDatabase.getInstance().getReference();        //mDatabase가 root라고 생각
        User user = new User(username);
        DB.child("users").child(userEmail).setValue(user);           //객체 user
    }

    //이메일 @앞까지 파싱
    private String parsingEmail(String userEmail){
        String tmpEmail = "";
        for(int i=0;i<userEmail.length();i++){
            if(userEmail.charAt(i) == '@') break;
            tmpEmail+=userEmail.charAt(i);
        }
        return tmpEmail;
    }

    //---------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {       //구글 로그인 인증을 요청했을때  값을 되돌려 받는곳
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){     //인증 결과가 성공적이면
                GoogleSignInAccount account = result.getSignInAccount();        //account는 구글 로그인 정보를 담고있다(닉네임,프사,이메일주소..)
                resultLogin(account);       //로그인 결과값 출력 수행하라는 메소드
            }
        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){    //로그인이 성공했으면
                            Toast.makeText(LoginActivity.this , "로그인 성공",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Mypage.class);
                            intent.putExtra("name" , account.getDisplayName());         //key값 :Nickname
                            intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl()));      //String.valueOf : 특정 자료형을 String형태로 변환
                            intent.putExtra("Email",parsingEmail(account.getEmail()));

                            //----------------------------userid와 username을 가져온다
                            userEmail = account.getEmail();
                            userName = account.getDisplayName();
                            userEmail = parsingEmail(userEmail);

                            //userEmail이 DB에 존재하는 email인지 확인
                            check(userEmail , userName);
                            //--------------------------------------------------------
                            startActivity(intent);
                        }
                        else{       //로그인 실패했으면
                            Toast.makeText(LoginActivity.this , "로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}