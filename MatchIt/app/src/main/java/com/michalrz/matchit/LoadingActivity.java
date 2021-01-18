package com.michalrz.matchit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.ProgressBar;

public class LoadingActivity extends AppCompatActivity {

    private DrawLoading dl;
    private SurfaceHolder surfaceHolder;
    private Bitmap background;
    private Bitmap foreground;
    private int load=15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dl=new DrawLoading(this);
        setContentView(dl);

        background=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_loading),1080,1920,false);
        foreground=Bitmap.createBitmap(1080,1920, Bitmap.Config.ARGB_8888);
        Loader loader=new Loader();
        loader.start();

    }

    public void assignSurfaceHolder(SurfaceHolder surfaceHolder)
    {
        this.surfaceHolder=surfaceHolder;
    }

    public void draw(Canvas canvas)
    {
        Bitmap bitmap=Bitmap.createBitmap(1080,1920, Bitmap.Config.ARGB_8888);
        Canvas can=new Canvas(bitmap);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        can.drawRect(0,0,1080,1920,p);

        Paint paint = new Paint();
        Shader grad=new LinearGradient(0,0,5+1080*load/100,5+1920*load/100, 0xffffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        Shader bit=new BitmapShader(background,Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(new ComposeShader(grad, bit, PorterDuff.Mode.SRC_IN));
        can.drawRect(0,0,1080,1920,paint);

        Rect dstRect = new Rect();
        canvas.getClipBounds(dstRect);
        canvas.drawBitmap(bitmap, null, dstRect, null);
    }

    private class Loader extends Thread{

        private ScoreBoard scoreBoard;

        @Override
        public void run() {
            scoreBoard=new ScoreBoard();
            final int loaderIterator=50;
            for(int i=0; i<loaderIterator; i++)
            {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Canvas canvas= null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (canvas) {
                        dl.draw(canvas);
                        load+=100/loaderIterator;
                    }
                }catch(Exception e){
                    //nothing
                } finally {
                    if(canvas!= null)  {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            scoreBoard.load(getSharedPreferences("Score",MODE_PRIVATE));
            Intent intent=new Intent(LoadingActivity.this,MainActivity.class);
            for(int i=0; i<10; i++) intent.putExtra(String.valueOf(i),scoreBoard.get(i));
            LoadingActivity.this.startActivity(intent);
            LoadingActivity.this.finish();
        }
    }
}

