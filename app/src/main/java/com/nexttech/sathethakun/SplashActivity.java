package com.nexttech.sathethakun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sdsmdg.harjot.longshadows.LongShadowsImageView;

public class SplashActivity extends AppCompatActivity {

    LongShadowsImageView splashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLogo = findViewById(R.id.splash_logo);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        },5000);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splash_animation);
        splashLogo.startAnimation(myanim);
    }
}