package com.blackhoodie.puyopuyo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class Game{

    private static Game instance;

    private boolean paused;

    private long frameStartTime;
    private long frameEndTime;
    private float frameDeltaTime;
    private long sleepTime;
    private float framePerSecond;

    private HashMap<String, Level> levels;
    private Level currentLevel;

    private GameSurfaceView view;
    private Context context;

    public Game(){

    }

    public static void createInstance(){
        if(instance == null){
            instance = new Game();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initialize(){
        if(instance == null){
            instance = null;
        }

        paused = false;
        frameStartTime = System.currentTimeMillis();
        levels = new HashMap<String, Level>();
        currentLevel = null;

        addLevel(new TitleLevel("Title Level"));
        addLevel(new GameLevel("Game Level"));
        loadLevel("Game Level");
    }

    private void addLevel(Level level){
        levels.put(level.getName(), level);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void loadLevel(String levelName){
        if(currentLevel != null){
            currentLevel.dispose();
        }

        Level newLevel = levels.get(levelName);
        newLevel.initialize();
        currentLevel = newLevel;
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

        if(currentLevel != null && currentLevel.isLoadCompleted() && !paused){
            currentLevel.update();
            currentLevel.updateActors();
        }
    }

    public void render(Canvas canvas){
        if(currentLevel == null || !currentLevel.isLoadCompleted()){
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

    public void dispose(){
        for(Map.Entry<String, Level> entry : levels.entrySet()){
            if(entry.getValue().getAudioManager() != null){
                entry.getValue().getAudioManager().dispose();
            }
        }
    }

    public static Game getInstance(){
        return instance;
    }

    public boolean isPaused(){
        return paused;
    }
    public void setPaused(boolean value){
        this.paused = value;
    }

    public float getFrameDeltaTime(){
        return frameDeltaTime;
    }

    public float getFramePerSecond(){
        return framePerSecond;
    }

    public GameSurfaceView getView(){
        return view;
    }
    public void setView(GameSurfaceView view){
        this.view = view;
    }

    public Context getContext(){
        return context;
    }
    public void setContext(Context context){
        this.context = context;
    }

    public Vector2D getScreenSize(){
        return new Vector2D(view.getWidth(), view.getHeight());
    }

}
