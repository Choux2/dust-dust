package com.yerim.dust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    ImageView imageview;
    Animation ani1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        ani1 = AnimationUtils.loadAnimation(this, R.anim.translate);
        imageview = (ImageView) findViewById(R.id.iv);
        imageview.startAnimation(ani1);
        start();
    }
    public void start()
    {
        //이벤트 핸들러를 통해서 몇 초 후 동작하기
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(IntroActivity.this, MainActivity2.class);
                startActivity(intent1);
                finish(); //현재 액티비티 종료
            }
        }, 1700); //1초 후 인트로 실행
    }
}