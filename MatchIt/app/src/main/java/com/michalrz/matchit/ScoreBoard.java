package com.michalrz.matchit;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.RequiresApi;

import static android.content.Context.MODE_PRIVATE;

public class ScoreBoard implements Parcelable {
    private ArrayList<Row> scoreboard;

    public ScoreBoard()
    {
        scoreboard=new ArrayList<Row>();
    }

    public ScoreBoard(ArrayList<Row> scoreboard)
    {
        this.scoreboard=scoreboard;
    }

    protected ScoreBoard(Parcel in) {
        scoreboard = in.createTypedArrayList(Row.CREATOR);
    }

    public void print()
    {
        for(int i=0; i<scoreboard.size(); i++)
        {
            Log.d(String.valueOf(i),scoreboard.get(i).getNickname()+" "+String.valueOf(scoreboard.get(i).getScore()));
        }
    }

    public int count()
    {
        return scoreboard.size();
    }

    public static final Creator<ScoreBoard> CREATOR = new Creator<ScoreBoard>() {
        @Override
        public ScoreBoard createFromParcel(Parcel in) {
            return new ScoreBoard(in);
        }

        @Override
        public ScoreBoard[] newArray(int size) {
            return new ScoreBoard[size];
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(String nickname, int score)
    {
        add(new Row(nickname, score));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void add(Row row)
    {
        scoreboard.add(row);
        sort();
        if(scoreboard.size()>10) scoreboard.remove(10);
    }

    public Row get(int index)
    {
        return scoreboard.get(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort()
    {
        /*scoreboard.sort(new Comparator<Row>() {
            @Override
            public int compare(Row row, Row t1) {
                return row.compareTo(t1);
            }
        });*/
        Collections.sort(scoreboard,Collections.reverseOrder());
    }

    public void save(SharedPreferences sharedPreferences)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(int i=0; i<10; i++)
        {
            editor.putString("Nick"+String.valueOf(i),scoreboard.get(i).getNickname());
            editor.putInt("Score"+String.valueOf(i),scoreboard.get(i).getScore());
        }
        editor.commit();
    }

    public void load(SharedPreferences sharedPreferences)
    {
        String nick;
        int score;
        for(int i=0; i<10; i++)
        {
            nick=sharedPreferences.getString("Nick"+String.valueOf(i),"No name");
            score=sharedPreferences.getInt("Score"+String.valueOf(i),0);
            scoreboard.add(new Row(nick,score));
        }
        Log.d("size",String.valueOf(scoreboard.size()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(scoreboard);
    }
}

class Row implements Comparable<Row>, Parcelable
{
    private String nickname;
    private int score;

    public Row(String nickname, int score)
    {
        this.nickname=nickname;
        this.score=score;
    }

    protected Row(Parcel in) {
        nickname = in.readString();
        score = in.readInt();
    }

    public static final Creator<Row> CREATOR = new Creator<Row>() {
        @Override
        public Row createFromParcel(Parcel in) {
            return new Row(in);
        }

        @Override
        public Row[] newArray(int size) {
            return new Row[size];
        }
    };

    public String getNickname()
    {
        return nickname;
    }

    public int getScore()
    {
        return score;
    }

    @Override
    public int compareTo(Row row) {
        return score-row.getScore();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nickname);
        parcel.writeInt(score);
    }
}
