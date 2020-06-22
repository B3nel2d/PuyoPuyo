package com.blackhoodie.puyopuyo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import java.util.HashMap;

public class Game{

    private static Game instance;
    private Context context;

    private boolean paused;

    private long frameStartTime;
    private long frameEndTime;
    private float frameDeltaTime;
    private long sleepTime;
    private float framePerSecond;

    private HashMap<String, Level> levels;
    private Level currentLevel;

    private TitleLevel titleLevel;

    public Game(){

    }

    public static void createInstance(){
        if(instance == null){
            instance = new Game();
        }
    }

    public void initialize(){
        if(instance == null){
            instance = null;
        }

        paused = false;
        frameStartTime = System.currentTimeMillis();
        levels = new HashMap<String, Level>();
        currentLevel = null;

        addLevel(new TitleLevel("Title Level"));
        addLevel(new TitleLevel("Sample Level"));

        loadLevel("Title Level");
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

        if(!paused && currentLevel != null){
            currentLevel.update();
        }
    }

    public void render(Canvas canvas){
        if(currentLevel == null){
            return;
        }

        canvas.drawColor(Color.WHITE);
        currentLevel.render(canvas);
    }

    public void onTouchEvent(MotionEvent event){
        if(!paused && currentLevel != null){
            currentLevel.onTouchEvent(event);
        }
    }

    public static Game getInstance(){
        return instance;
    }

    public Context getContext(){
        return context;
    }
    public void setContext(Context context){
        this.context = context;
    }

    public boolean isPaused(){
        return paused;
    }
    public void setPaused(boolean paused){
        this.paused = paused;
    }

    public float getFrameDeltaTime(){
        return frameDeltaTime;
    }

    public float getFramePerSecond(){
        return framePerSecond;
    }

    private void addLevel(Level level){
        levels.put(level.getName(), level);
    }
    public void loadLevel(String levelName){
        Level newLevel = levels.get(levelName);

        newLevel.initialize();
        currentLevel = newLevel;
    }

}
