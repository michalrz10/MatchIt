package com.michalrz.matchit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    public final float[] accelerometer = new float[3];
    private Intent sound;
    private DrawGame drawGame;
    private String returnedNickname;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        drawGame=new DrawGame(this);
        this.setContentView(drawGame);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        sound=new Intent(GameActivity.this,BackgroundSoundService.class);
        startService(sound);
    }


    @Override
    public void onBackPressed() {
        endGeme(0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometer,
                    0, accelerometer.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get updates from the accelerometer and magnetometer at a constant rate.
        // To make batch operations more efficient and reduce power consumption,
        // provide support for delaying updates to the application.
        //
        // In this example, the sensor reporting delay is small enough such that
        // the application receives an update before the system checks the sensor
        // readings again.
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void endGeme(int score)
    {
        (new Thread(()->endGemePrivate(score))).start();
    }

    private void endGemePrivate(int score)
    {
        this.score=score;
        drawGame.Shutdown();
        EndGameDialog ed=new EndGameDialog(score);
        ed.show(getSupportFragmentManager(),"EndGameDialog");
    }

    public void returnNickname(String nickname)
    {
        stopService(sound);
        Intent _result=new Intent();
        _result.putExtra("row",new Row(nickname,score));
        setResult(RESULT_OK,_result);
        finish();
    }
}