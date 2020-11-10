package com.example.squatchallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class solo_speed_play extends AppCompatActivity implements Camera.PreviewCallback{

    //이메일을 알고있어야 db연동
    Intent intent = getIntent();
    final String email = intent.getStringExtra("Email");        //구글이메일

    //카운트를 위해(시간)
    public TextView tv_timer;
    private CountDownTimer countDownTimer;
    private long start_time , now_time;
    private boolean game_end = false;
    private boolean game_start = false;

    //카메라를 위해
    private static CameraPreview surfaceView;
    private SurfaceHolder holder;
    private static Button camera_preview_button;
    private static Camera mCamera;
    private int RESULT_PERMISSIONS = 100;
    public static solo_speed_play getInstance;
    //모델 판단을 위해
    Interpreter tflite;
    public TextView tv_result;
    public TextView tv_count;
    public int count = 0;
    public int []arr = new int[3];      //0(up) 1(down)
    public int index = 0;
    public boolean up = true;
    public boolean down = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo_speed_play);

        // 카메라 프리뷰를  전체화면으로 보여주기 위해 셋팅한다.
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //모델 로드
        tflite = getTfliteInterpreter("model.tflite");


        // 안드로이드 6.0 이상 버전에서는 CAMERA 권한 허가를 요청한다.
        requestPermissionCamera();

        //카운트 시작 (준비시간 5초)
        tv_timer = findViewById(R.id.tv_timer);
        countDownTimer();
        countDownTimer.start();

    }

    //카운트를 위해
    public void countDownTimer () {
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                //0초면 토스트 메세지 띄운다
                if(l/1000 == 0){
                    Toast.makeText(getApplicationContext() , "Start!",Toast.LENGTH_SHORT).show();
                }
                else tv_timer.setText(Long.toString(l/1000));
            }
            @Override
            public void onFinish() {
                //0.0부터 타이머가 시작해야하므로
                start_time = SystemClock.elapsedRealtime();
                game_start = true;
                handler.sendEmptyMessage(0);
            }
        };
    }
    //카운터 계산부분
    private String getTime(){
        //경과된 시간 체크

        now_time = SystemClock.elapsedRealtime();
        //시스템이 부팅된 이후의 시간?
        long overTime = now_time - start_time;

        long m = overTime/1000/60;
        long s = (overTime/1000)%60;

        long ms = overTime % 1000;

        //3분넘어가면 게임종료
        if(m >= 3) game_end = true;

        String recTime = String.format("%d:%02d:%03d",m,s,ms);

        return recTime;
    }
    //handler (카운터)
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            tv_timer.setText(getTime());

            //30개를 했거나 시간이 3분이 넘어가면
            if(game_end){
                Toast.makeText(getApplicationContext() , "game끝!" , Toast.LENGTH_SHORT).show();
            }
            else handler.sendEmptyMessage(0);
        }
    };
    //판단 (up,down) 카운팅
    public  void determine(int n){
        if(index==0 || index == 1) {
            arr[index] = n;
            index++;
        }
        //index가 2이면 마지막꺼만 비어있는 상태
        else {
            //우선 넣어주고
            arr[index] = n;
            //판단
            if(arr[0] == arr[1] && arr[1]==arr[2]){
                //down이 true인 상태에서 up인상태이면 count++
                if(arr[0] == 0){
                    if(down){
                        down = false;
                        count++;
                        tv_count = findViewById(R.id.tv_count);
                        tv_count.setText(Integer.toString(count));

                        //10개 채웠으면 game_end!
                        if(count>=10) game_end = true;
                    }
                }
                //down
                else{
                    down = true;
                }
            }
            //앞에껄 지우고 옮겨줌
            arr[0] = arr[1];
            arr[1] = arr[2];
            arr[2] = -1;
        }

    }
    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Parameters params = mCamera.getParameters();

        //게임 끝이 아니고 게임이 시작됐다면
        //결과값 출력을 위해
        if(!game_end && game_start) {
            tv_result = findViewById(R.id.tv_result);
            tv_count = findViewById(R.id.tv_count);

            int w = params.getPreviewSize().width;
            int h = params.getPreviewSize().height;

            final int[] rgb = decodeYUV420SP(bytes, w, h);
            Bitmap bmp = Bitmap.createBitmap(rgb, w, h, Bitmap.Config.ARGB_8888);
            bmp = Bitmap.createScaledBitmap(bmp, 224, 224, false);

            Matrix rotateMatrix = new Matrix();
            rotateMatrix.postRotate(90);

            bmp = Bitmap.createBitmap(bmp, 0, 0,
                    bmp.getWidth(), bmp.getHeight(), rotateMatrix, false);

            //모델 적용을 위해
            float[][][][] inputs = new float[1][224][224][3];       //1 * width * height * RGB
            float[][] outputs = new float[1][2];
            for (int x = 0; x < 224; x++) {
                for (int y = 0; y < 224; y++) {
                    int pixel = bmp.getPixel(x, y);
                    inputs[0][y][x][0] = (Color.red(pixel)) / 255.0f;
                    inputs[0][y][x][1] = (Color.green(pixel)) / 255.0f;
                    inputs[0][y][x][2] = (Color.blue(pixel)) / 255.0f;
                }
            }
            tflite.run(inputs, outputs);

            //서있을때
            if (outputs[0][0] > outputs[0][1]) {
                tv_result.setText("up");
                determine(0);
            }
            //앉아있을때
            else {
                tv_result.setText("down");
                determine(1);
            }
        }
    }


    //bytes에서 rgb정보 추출
    public int[] decodeYUV420SP( byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;

        int rgb[]=new int[width*height];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0; else if (r > 262143) r = 262143;
                if (g < 0) g = 0; else if (g > 262143) g = 262143;
                if (b < 0) b = 0; else if (b > 262143) b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) &
                        0xff00) | ((b >> 10) & 0xff);

            }
        }
        return rgb;
    }
    //카메라 부분
    public static Camera getCamera(){
        return mCamera;
    }
    private void setInit() {
        getInstance = this;

        // 카메라 객체를 R.layout.activity_main의 레이아웃에 선언한 SurfaceView에서 먼저 정의해야 함으로 setContentView 보다 먼저 정의한다.
        mCamera = Camera.open();

        setContentView(R.layout.activity_main);

        // SurfaceView를 상속받은 레이아웃을 정의한다.
        surfaceView = (CameraPreview) findViewById(R.id.preview);
        //리스너 설정
        mCamera.setPreviewCallback(this::onPreviewFrame);
        // SurfaceView 정의 - holder와 Callback을 정의한다.
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    //허가
    private boolean requestPermissionCamera() {
        int sdkVersion = Build.VERSION.SDK_INT;
        if(sdkVersion >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(solo_speed_play.this,
                        new String[]{Manifest.permission.CAMERA},
                        RESULT_PERMISSIONS);

            }else {
                setInit();
            }
        }else{  // version 6 이하일때
            setInit();
            return true;
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (RESULT_PERMISSIONS == requestCode) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 허가시
                setInit();
            } else {
                // 권한 거부시
            }
            return;
        }

    }


    //모델 로드 국룰
    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(solo_speed_play.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}