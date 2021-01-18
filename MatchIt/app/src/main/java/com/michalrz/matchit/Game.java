package com.michalrz.matchit;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

public class Game extends Thread{
    private final int NUMBER_OF_SIGNS=4;
    private final int DRAW_EACH_MILISECONDS=50;
    private final int SPAWN_ENEMY=250;
    public double move=1;
    private int score=0;
    private int map=0;


    private final SurfaceHolder surfaceHolder;
    private final DrawGame drawgame;
    private boolean running=true;
    private final Person player;
    private final ArrayList<Person> enemies=new ArrayList<Person>();
    private final ArrayList<Person> dead=new ArrayList<Person>();
    private final Random random=new Random();
    public boolean[] signs;
    private final SoundPool sp;
    private final int[] soundIds = new int[2];

    public Game(DrawGame drawgame,SurfaceHolder surfaceHolder){
        player=new Person(540,960,0,this);
        this.drawgame=drawgame;
        signs=new boolean[NUMBER_OF_SIGNS];
        for(int i=0; i<NUMBER_OF_SIGNS; i++) signs[i]=false;
        this.surfaceHolder=surfaceHolder;
        for(int i=0; i<5; i++) addEnemy();
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sp= new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(attrs)
                .build();
        soundIds[0]=sp.load(drawgame.context,R.raw.fire,1);
        soundIds[1]=sp.load(drawgame.context,R.raw.die,1);
    }

    public void addEnemy() {
        if(random.nextBoolean()) {
            if(random.nextBoolean()){
                enemies.add(new Person(0,(float)random.nextInt(1920),random.nextInt(NUMBER_OF_SIGNS),this));
            }
            else {
                enemies.add(new Person(1080,(float)random.nextInt(1920),random.nextInt(NUMBER_OF_SIGNS),this));
            }
        }
        else {
            if(random.nextBoolean()){
                enemies.add(new Person((float)random.nextInt(1080),0,random.nextInt(NUMBER_OF_SIGNS),this));
            }
            else {
                enemies.add(new Person((float)random.nextInt(1080),1920,random.nextInt(NUMBER_OF_SIGNS),this));
            }
        }
    }

    public boolean iteration(){
        synchronized (signs) {
            for(int i=dead.size()-1; i>=0; i--)
            {
                if(dead.get(i).stage==4)
                {
                    dead.remove(i);
                }
                else
                {
                    dead.get(i).stage++;
                }
            }
            boolean match=false;
            for (int i = 0; i < NUMBER_OF_SIGNS; i++) {
                if (signs[i]) {
                    int j = 0;
                    while (j < enemies.size()) {
                        if (enemies.get(j).getSign() == i) {
                            enemies.get(j).stage=1;
                            dead.add(enemies.get(j));
                            enemies.remove(j);
                            score+=1;
                            move+=0.05;
                            match=true;
                        } else {
                            j++;
                        }
                    }
                    signs[i] = false;
                }
            }
            if(match)
            {
                sp.play(soundIds[0],1,1, 1,0,1.0f);
            }
        }

        for(int i=0; i<enemies.size(); i++){
            if(enemies.get(i).moveTowards(player.getX(),player.getY()))
            {
                sp.play(soundIds[1],1,1, 1,0,1.0f);
                return true;
            }
        }
        return false;
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap=Bitmap.createBitmap(1080,1920, Bitmap.Config.ARGB_8888);
        Canvas can=new Canvas(bitmap);
        if(map<10)
        {
            can.drawBitmap(drawgame.background[0],0,0,null);
            map++;
        }
        else
        {
            can.drawBitmap(drawgame.background[1],0,0,null);
            map++;
            if(map==20) map=0;
        }
        can.drawBitmap(drawgame.player,player.getX()-drawgame.player.getWidth()/2,player.getY()-drawgame.player.getHeight()/2,null);
        Paint paint=new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(25);
        for(int i=0; i<enemies.size(); i++) {
            can.drawBitmap(drawgame.enemy[enemies.get(i).stage-1],enemies.get(i).getX()-drawgame.enemy[0].getWidth()/2,enemies.get(i).getY()-drawgame.enemy[0].getHeight()/2,null);
            if(enemies.get(i).getSign()==0)
                can.drawText("pion",enemies.get(i).getX(),enemies.get(i).getY()-drawgame.enemy[0].getHeight()/4*3,paint);
            else if(enemies.get(i).getSign()==1)
                can.drawText("-pion",enemies.get(i).getX(),enemies.get(i).getY()-drawgame.enemy[0].getHeight()/4*3,paint);
            else if(enemies.get(i).getSign()==2)
                can.drawText("lewy bok",enemies.get(i).getX(),enemies.get(i).getY()-drawgame.enemy[0].getHeight()/4*3,paint);
            else if(enemies.get(i).getSign()==3)
                can.drawText("prawy bok",enemies.get(i).getX(),enemies.get(i).getY()-drawgame.enemy[0].getHeight()/4*3,paint);
        }
        Paint p=new Paint();
        p.setColor(ContextCompat.getColor(drawgame.context, R.color.beam));
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(7);
        for(int i=0; i<dead.size(); i++)
        {
            can.drawBitmap(drawgame.enemyd,dead.get(i).getX()-drawgame.enemyd.getWidth()/2,dead.get(i).getY()-drawgame.enemyd.getHeight()/2,null);
            can.drawLine(player.getX(),player.getY(), (float) (player.getX()+(dead.get(i).getX()-player.getX())/4.0*dead.get(i).stage), (float) (player.getY()+(dead.get(i).getY()-player.getY())/4.0*dead.get(i).stage),p);
        }

        Paint paint2=new Paint();
        paint2.setColor(Color.RED);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setTextSize(40);
        can.drawText(String.valueOf(score),1030,50,paint2);

        final Paint paintt = new Paint();
        paintt.setAlpha(255);
        can.drawBitmap(drawgame.drawSign, 0, 0, paintt);

        Rect dstRect = new Rect();
        canvas.getClipBounds(dstRect);
        canvas.drawBitmap(bitmap, null, dstRect, null);
    }

    @Override
    public void run() {
        long start=System.nanoTime() ;
        long spawn=System.nanoTime() ;
        while(running){
            Canvas canvas= null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (canvas) {
                    if(iteration())
                    {
                        ((GameActivity)drawgame.context).endGeme(score);
                        continue;
                    }
                    drawgame.draw(canvas);
                }
            }catch(Exception e){
                //nothing
            } finally {
                if(canvas!= null)  {
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.nanoTime() ;
            long waitTime = (now - start)/1000000;
            if(waitTime<DRAW_EACH_MILISECONDS){
                waitTime=DRAW_EACH_MILISECONDS-waitTime;
                try {
                    sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            start=System.nanoTime() ;
            if((now-spawn)/1000000>SPAWN_ENEMY)
            {
                addEnemy();
                spawn=System.nanoTime();
            }
        }
        sp.release();
    }

    public void shutDown(){
        running=false;
    }
}
