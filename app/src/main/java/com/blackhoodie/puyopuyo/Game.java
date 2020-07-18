package com.blackhoodie.puyopuyo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;

/**
 * ゲームの根幹部分の処理を担うシングルトンクラス
 */
public class Game{

    /** インスタンス */
    private static Game instance;

    /** ポーズ状態であるか */
    private boolean paused;

    /** フレームの開始時間（秒） */
    private long frameStartTime;
    /** フレームの終了時間（秒） */
    private long frameEndTime;
    /** そのフレームに掛かった時間（秒） */
    private float frameDeltaTime;
    /** 1フレームに必要な最低時間（秒） */
    private long sleepTime;
    /** 1秒当たりのフレーム数 */
    private float framePerSecond;

    /** フレームレート */
    public static final float frameRate = 200.0f;
    /** 最低フレームレート */
    public static final float minimumFrameRate = 10.0f;
    /** フレームの最低時間（秒） */
    public static final float minimumFrameTime = 1.0f / frameRate;
    /** フレームの最大時間（秒） */
    public static final float maximumFrameTime = 1.0f / minimumFrameRate;

    /** レベルとその名前のハッシュマップ */
    private HashMap<String, Level> levels;
    /** 現在実行対象のレベル */
    private Level currentLevel;

    /** 使用するGameSurfaceView */
    private GameSurfaceView view;
    /** 使用するContext */
    private Context context;

    /**
     * コンストラクタ
     */
    public Game(){

    }

    /**
     * シングルトンなインスタンスを作成する
     */
    public static void createInstance(){
        if(instance == null){
            instance = new Game();
        }
    }

    /**
     * 初期化処理を行う
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
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
        loadLevel("Title Level");
    }

    /**
     * レベルを追加する
     * @param level 追加するレベル
     */
    private void addLevel(Level level){
        levels.put(level.getName(), level);
    }

    /**
     * レベルを読み込む
     * @param levelName 読み込むレベルの名前
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void loadLevel(String levelName){
        if(!levels.containsKey(levelName)){
            return;
        }

        Level previousLevel = null;
        if(currentLevel != null){
            previousLevel = currentLevel;
        }

        Level newLevel = levels.get(levelName);
        currentLevel = newLevel;
        newLevel.initialize();

        if(previousLevel != null){
            previousLevel.dispose();
        }
    }

    /**
     * ゲームループ（1フレーム）を実行する
     * @throws InterruptedException
     */
    public void run() throws InterruptedException{
        frameEndTime = System.currentTimeMillis();
        frameDeltaTime = (frameEndTime - frameStartTime) / 1000.0f;

        if(frameDeltaTime < minimumFrameTime){
            sleepTime = (long)((minimumFrameTime - frameDeltaTime) * 1000.0f);
            Thread.sleep(sleepTime);

            return;
        }
        if(0.0f < frameDeltaTime){
            framePerSecond = (framePerSecond * 0.99f) + (0.01f / frameDeltaTime);
        }
        if(maximumFrameTime < frameDeltaTime){
            frameDeltaTime = maximumFrameTime;
        }

        frameStartTime = frameEndTime;

        if(currentLevel != null && currentLevel.isInitialized() && currentLevel.isLoadCompleted() && !paused){
            currentLevel.update();
            currentLevel.updateActors();
        }
    }

    /**
     * Canvasの描画を行う（アクティブなレベルへCanvasを渡す）
     * @param canvas 描画に使用するCanvas
     */
    public void render(Canvas canvas){
        if(currentLevel == null || !currentLevel.isLoadCompleted()){
            return;
        }

        canvas.drawColor(Color.WHITE);
        currentLevel.render(canvas);
    }

    /**
     * タッチイベント（アクティブなレベルへMotionEventを渡す）
     * @param motionEvent タッチイベントのMotionEvent
     */
    public void onTouchEvent(MotionEvent motionEvent){
        if(!paused && currentLevel != null){
            currentLevel.onTouchEvent(motionEvent);
        }
    }

    /**
     * リソースを解放する
     */
    public void dispose(){
        for(Map.Entry<String, Level> entry : levels.entrySet()){
            if(entry.getValue().getAudioManager() != null){
                entry.getValue().getAudioManager().dispose();
            }
        }
    }

    /**
     * インスタンスを取得する
     * @return
     */
    public static Game getInstance(){
        return instance;
    }

    /**
     * ポーズ状態を取得する
     * @return ポーズ状態かどうか
     */
    public boolean isPaused(){
        return paused;
    }
    /**
     * ポーズ状態を設定する
     * @param value 設定する値
     */
    public void setPaused(boolean value){
        this.paused = value;
    }

    /**
     * 前回のフレームに掛かった時間（秒）を取得する
     * @return フレーム時間
     */
    public float getFrameDeltaTime(){
        return frameDeltaTime;
    }

    /**
     * 1秒当たりのフレーム数を取得する
     * @return 1秒当たりのフレーム数
     */
    public float getFramePerSecond(){
        return framePerSecond;
    }

    /**
     * GameSurfaceViewを取得する
     * @return GameSurfaceView
     */
    public GameSurfaceView getView(){
        return view;
    }
    /**
     * GameSurfaceViewを設定する
     * @param view 設定するインスタンス
     */
    public void setView(GameSurfaceView view){
        this.view = view;
    }

    /**
     * Contextを取得する
     * @return Context
     */
    public Context getContext(){
        return context;
    }
    /**
     * Contextを設定する
     * @param context 設定するインスタンス
     */
    public void setContext(Context context){
        this.context = context;
    }

    /**
     * ゲーム画面のサイズを取得する
     * @return ゲーム画面のサイズ
     */
    public Vector2D getScreenSize(){
        return new Vector2D(view.getWidth(), view.getHeight());
    }

}
