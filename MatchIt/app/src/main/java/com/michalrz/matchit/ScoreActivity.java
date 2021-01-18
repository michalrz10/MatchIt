package com.michalrz.matchit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    AnimationDrawable backgroundAnimation;
    private ScoreBoard scoreBoard;
    private ScoreAdapter sa;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        backgroundAnimation=(AnimationDrawable) findViewById(R.id.backscore).getBackground();
        backgroundAnimation.start();
        scoreBoard=new ScoreBoard();
        for(int i=0; i<10; i++)
        {
            scoreBoard.add((Row) getIntent().getExtras().getParcelable(String.valueOf(i)));
        }
        scoreBoard.print();
        Log.d("score","no dziala");
        sa=new ScoreAdapter(this);
        ((ListView)findViewById(R.id.listview)).setAdapter(sa);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class ScoreAdapter extends BaseAdapter{

        private LayoutInflater inflater;
        private Context ctx;

        public ScoreAdapter(Context ctx)
        {
            super();
            this.ctx=ctx;
            this.inflater=(LayoutInflater) ctx.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        }

        @Override
        public int getCount() {
            return scoreBoard.count();
        }

        @Override
        public Object getItem(int i) {
            return scoreBoard.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Log.d("getview","view");
            View vieww;
            if(view==null)
            {
                vieww=new View(ctx);
                vieww=inflater.inflate(R.layout.score_row,null);
            }
            else
            {
                vieww=(View) view;
            }
            TextView nickname=(TextView)vieww.findViewById(R.id.textView);
            TextView score=(TextView)vieww.findViewById(R.id.textView4);
            nickname.setText(scoreBoard.get(i).getNickname());
            score.setText(String.valueOf(scoreBoard.get(i).getScore()));
            return vieww;
        }
    }
}