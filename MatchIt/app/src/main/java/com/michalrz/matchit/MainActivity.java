package com.michalrz.matchit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AnimationDrawable backgroundAnimation;
    private ScoreBoard scoreBoard;
    final int REQUEST_GAME_ACTIVITY = 42;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundAnimation=(AnimationDrawable) findViewById(R.id.sth).getBackground();
        backgroundAnimation.start();
        scoreBoard=new ScoreBoard();
        for(int i=0; i<10; i++)
        {
            scoreBoard.add((Row) getIntent().getExtras().getParcelable(String.valueOf(i)));
        }
        scoreBoard.print();
    }

    public void moveToGameActivity(View view) {
        Intent intent=new Intent(this,GameActivity.class);
        startActivityForResult(intent,REQUEST_GAME_ACTIVITY);
    }

    public void moveToScoreActivity(View view) {
        Intent intent=new Intent(this,ScoreActivity.class);
        for(int i=0; i<10; i++) intent.putExtra(String.valueOf(i),scoreBoard.get(i));
        startActivity(intent);
    }

    public void moveToInstructionActivity(View view) {
        Intent intent=new Intent(this,InstructionActivity.class);
        startActivity(intent);
    }

    public void moveToAboutActivity(View view) {
        Intent intent=new Intent(this,AboutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GAME_ACTIVITY && resultCode == RESULT_OK) {
            scoreBoard.add((Row) data.getExtras().getParcelable("row"));
            scoreBoard.print();
            scoreBoard.save(getSharedPreferences("Score",MODE_PRIVATE));
        }
    }
}