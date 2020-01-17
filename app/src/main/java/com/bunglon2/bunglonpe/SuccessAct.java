package com.bunglon2.bunglonpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessAct extends AppCompatActivity {

    Animation app_splash, btt;
    ImageView success;
    TextView txtsucess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);

        success = findViewById(R.id.success);
        txtsucess = findViewById(R.id.txtsucess);

        success.startAnimation(app_splash);
        txtsucess.startAnimation(btt);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SuccessAct.this, SignInAct.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
