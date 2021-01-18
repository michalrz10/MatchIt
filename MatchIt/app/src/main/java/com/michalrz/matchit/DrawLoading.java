package com.michalrz.matchit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawLoading extends SurfaceView implements SurfaceHolder.Callback{
    private Context context;

    public DrawLoading(Context context)
    {
        super(context);
        this.context=context;
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        ((LoadingActivity)context).draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        ((LoadingActivity)context).assignSurfaceHolder(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
