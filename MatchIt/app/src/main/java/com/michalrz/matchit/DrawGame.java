package com.michalrz.matchit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class DrawGame extends SurfaceView implements SurfaceHolder.Callback {

    public Bitmap player;
    public Bitmap enemyd;
    public Bitmap[] enemy;
    public Bitmap[] background;
    private Game game;
    private Signs signs;
    public Context context;

    public Bitmap drawSign;
    private float lastposx;
    private float lastposy;
    private float width;
    private float height;

    public DrawGame(Context context) {
        super(context);
        this.context=context;
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        enemy=new Bitmap[3];
        background=new Bitmap[2];
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        game.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        player=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_player),108,192,false);
        enemy[0]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_enemy1),108,192,false);
        enemy[1]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_enemy2),108,192,false);
        enemy[2]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_enemy3),108,192,false);
        enemyd=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_enemyd),108,192,false);
        background[0]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_background),1080,1920,false);
        background[1]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_background2),1080,1920,false);
        drawSign=Bitmap.createBitmap(1080,1920, Bitmap.Config.ARGB_8888);
        width= Resources.getSystem().getDisplayMetrics().widthPixels;
        height= Resources.getSystem().getDisplayMetrics().heightPixels;
        game=new Game(this,surfaceHolder);
        signs=new Signs(game,(GameActivity) context);
        game.start();
        signs.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            signs.join();
            game.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                lastposx=event.getX()/width*1080;
                lastposy=event.getY()/height*1920;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE :
                Canvas canvas=new Canvas(drawSign);
                Paint paint=new Paint();
                paint.setColor(Color.BLUE);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(15);
                canvas.drawLine(lastposx,lastposy,event.getX()/width*1080,event.getY()/height*1920,paint);
                lastposx=event.getX()/width*1080;
                lastposy=event.getY()/height*1920;
                invalidate();
                break;
            case MotionEvent.ACTION_UP :
                drawSign=Bitmap.createBitmap(1080,1920, Bitmap.Config.ARGB_8888);
                Toast.makeText(context,"up",Toast.LENGTH_SHORT).show();
                invalidate();
                break;
        }
        return true;
    }

    public void Shutdown()
    {
        game.shutDown();
        signs.shutDown();
    }
}
