package com.michalrz.matchit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

public class InstructionActivity extends AppCompatActivity {

    AnimationDrawable backgroundAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        backgroundAnimation=(AnimationDrawable) findViewById(R.id.backinstr).getBackground();
        backgroundAnimation.start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}