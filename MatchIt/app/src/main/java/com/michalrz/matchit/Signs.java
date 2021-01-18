package com.michalrz.matchit;

import android.util.Log;

public class Signs extends Thread {
    private final Game game;
    private final GameActivity ga;
    private boolean running=true;

    public Signs(Game game,GameActivity ga){
        this.game=game;
        this.ga=ga;
    }

    @Override
    public void run() {
        while(running)
        {
            synchronized (game.signs) {
                if(ga.accelerometer[1]>8){
                    game.signs[0]=true;
                }
                else if(ga.accelerometer[1]<-8){
                    game.signs[1]=true;
                }
                else if(ga.accelerometer[0]>8){
                    game.signs[2]=true;
                }
                else if(ga.accelerometer[0]<-8){
                    game.signs[3]=true;
                }
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutDown(){
        running=false;
    }

}
