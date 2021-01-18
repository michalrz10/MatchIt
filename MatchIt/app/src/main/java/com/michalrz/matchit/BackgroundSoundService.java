package com.michalrz.matchit;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {
    private MediaPlayer mp;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp=MediaPlayer.create(this,R.raw.background);
        mp.setLooping(true);
        mp.setVolume(0.5f,0.5f);
        mp.start();
    }

    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
    }
}
