package com.michalrz.matchit;

import android.graphics.Bitmap;
import android.util.Log;

public class Person {
    private final Game game;
    private float x;
    private float y;
    private final int sign;
    public int stage;
    public int nextstage;
    private boolean site=true;

    public Person(float x, float y,int sign, Game game){
        this.stage=1;
        this.nextstage=3;
        this.x=x;
        this.y=y;
        this.sign=sign;
        this.game=game;
    }

    public boolean moveTowards(float targetX,float targetY) {
        double diagonal=Math.sqrt((targetX-x)*(targetX-x)+(targetY-y)*(targetY-y));
        if(diagonal<=game.move)
        {
            return true;
        }
        x-= (float) ((x-targetX)*game.move/diagonal);
        y-= (float) ((y-targetY)*game.move/diagonal);
        if(nextstage<=0)
        {
            if(stage==1 && site)
            {
                stage=2;
            }
            else if(stage==1 && !site)
            {
                stage=3;
            }
            else if(stage==2)
            {
                stage=1;
                site=false;
            }
            else if(stage==3)
            {
                stage=1;
                site=true;
            }
            nextstage=4;
        }
        nextstage--;

        return false;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getSign() { return sign; }
}
