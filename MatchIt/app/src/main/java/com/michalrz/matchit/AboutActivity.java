package com.michalrz.matchit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    AnimationDrawable backgroundAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        backgroundAnimation=(AnimationDrawable) findViewById(R.id.backabout).getBackground();
        backgroundAnimation.start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}