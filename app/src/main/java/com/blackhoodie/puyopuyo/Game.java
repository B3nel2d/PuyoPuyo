package com.blackhoodie.puyopuyo;

import java.util.ArrayList;
import java.util.List;

public class Game{

    private static final Game instance = new Game();

    private boolean paused;

    private long frameStartTime;
    private long frameEndTime;
    private float frameDeltaTime;
    private long sleepTime;
    private float framePerSecond;

    private List<Level> levels;
    private Level currentLevel;

    public Game(){
        initialize();
    }

    private void initialize(){
        paused = false;
        frameStartTime = System.currentTimeMillis();
        levels = new ArrayList<>();
    }

    public void run() throws InterruptedException{
        frameEndTime = System.currentTimeMillis();
        frameDeltaTime = (frameEndTime - frameStartTime) / 1000.0f;

        if(frameDeltaTime < Configuration.MINIMUM_FRAME_TIME){
            sleepTime = (long)((Configuration.MINIMUM_FRAME_TIME - frameDeltaTime) * 1000.0f);
            Thread.sleep(sleepTime);

            return;
        }
        if(0.0f < frameDeltaTime){
            framePerSecond = (framePerSecond * 0.99f) + (0.01f / frameDeltaTime);
        }
        if(Configuration.MAXIMUM_FRAME_TIME < frameDeltaTime){
            frameDeltaTime = Configuration.MAXIMUM_FRAME_TIME;
        }

        frameStartTime = frameEndTime;
    }

    public static Game getInstance(){
        return instance;
    }

    public float getFrameDeltaTime(){
        return frameDeltaTime;
    }

    public float getFramePerSecond(){
        return framePerSecond;
    }

}
