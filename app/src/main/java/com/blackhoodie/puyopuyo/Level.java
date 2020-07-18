package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.RequiresApi;

/**
 * レベル（ゲームのセクション、リソースのひとまとまり）の抽象基底クラス
 */
abstract class Level{

    /** レベル名 */
    protected String name;

    /** 初期化されているか */
    private boolean initialized;
    /** ロードを完了しているか */
    protected boolean loadCompleted;

    /** グラフィックマネージャー */
    protected GraphicsManager graphicsManager;
    /** オーディオマネージャー */
    protected AudioManager audioManager;
    /** タッチイベントマネージャー */
    protected TouchEventManager touchEventManager;

    /** アクターのリスト */
    private List<Actor> actors;
    /** 追加待ちのアクターのリスト */
    private List<Actor> pendingActors;
    /** アクターの更新中であるか */
    private boolean updatingActors;

    /**
     * コンストラクタ
     * @param name レベル名
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Level(String name){
        this.name = name;
    }

    /**
     * 初期化処理を行う
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initialize(){
        initialized = false;
        loadCompleted = false;

        graphicsManager = new GraphicsManager(this);
        audioManager = new AudioManager(this);
        touchEventManager = new TouchEventManager(this);

        actors = new ArrayList<Actor>();
        pendingActors = new ArrayList<Actor>();
        updatingActors = false;

        initializeActors();

        audioManager.initialize();

        initialized = true;
    }

    /**
     * 各アクター等の初期化処理を行う
     */
    abstract void initializeActors();

    /**
     * ロード完了時のイベント
     */
    public void onLoadComplete(){
        loadCompleted = true;
    }

    /**
     * アクターを追加する
     * @param actor 追加するアクター
     */
    public void addActor(Actor actor){
        if(updatingActors){
            pendingActors.add(actor);
        }
        else{
            actors.add(actor);
        }
    }

    /**
     * ドローアブルを追加する
     * @param drawable 追加するドローアブル
     */
    public void addDrawable(DrawableComponent drawable){
        if(graphicsManager != null){
            graphicsManager.addDrawable(drawable);
        }
    }
    /**
     * ドローアブルを削除する
     * @param drawable 削除するドローアブル
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeDrawable(DrawableComponent drawable){
        if(graphicsManager != null){
            graphicsManager.removeDrawable(drawable);
        }
    }

    /**
     * インタラクタブルを追加する
     * @param interactable 追加するインタラクタブル
     */
    public void addInteractable(InteractableComponent interactable){
        if(touchEventManager != null){
            touchEventManager.addInteractable(interactable);
        }
    }
    /**
     * インタラクタブルを削除する
     * @param interactable 削除するインタラクタブル
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeInteractable(InteractableComponent interactable){
        if(touchEventManager != null){
            touchEventManager.removeInteractable(interactable);
        }
    }

    /**
     * ロードをスキップする（オーディオを1つも追加しない場合に使用する）
     */
    protected void skipLoad(){
        loadCompleted = true;
    }

    /**
     * 毎フレームの更新処理を行う
     */
    abstract void update();

    /**
     * 毎フレームの各アクターの更新処理を行う
     */
    public void updateActors(){
        updatingActors = true;
        for(Actor actor: actors){
            if(actor.getState() == Actor.State.Active){
                actor.update();
                actor.updateComponents();
            }
        }
        updatingActors = false;

        for(Actor actor: pendingActors){
            actors.add(actor);
        }
        pendingActors.clear();

        Iterator iterator = actors.iterator();
        while(iterator.hasNext()){
            Actor actor = (Actor)iterator.next();
            if(actor.getState() == Actor.State.Deleted){
                iterator.remove();
            }
        }
    }

    /**
     * 描画を行う（グラフィックマネージャーの描画処理を呼び出す）
     * @param canvas 描画に使用するCanvas
     */
    public void render(Canvas canvas){
        if(graphicsManager == null){
            return;
        }

        graphicsManager.render(canvas);
    }

    /**
     * タッチイベント（タッチイベントマネージャーのタッチイベント時処理を呼び出す）
     * @param motionEvent タッチイベントのMotionEvent
     */
    public void onTouchEvent(MotionEvent motionEvent){
        touchEventManager.onTouchEvent(motionEvent);
    }

    /**
     * 各リソースを開放する（レベルがアクティブでなくなった時に使用する）
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dispose(){
        for(Actor actor : actors){
            actor.dispose();
        }

        actors.clear();
        pendingActors.clear();

        graphicsManager.dispose();
        audioManager.dispose();
        touchEventManager.dispose();
    }

    /**
     * レベル名を取得する
     * @return レベル名
     */
    public String getName(){
        return name;
    }

    /**
     * グラフィックマネージャーを取得する
     * @return グラフィックマネージャー
     */
    public GraphicsManager getGraphicsManager(){
        return graphicsManager;
    }

    /**
     * オーディオマネージャーを取得する
     * @return オーディオマネージャー
     */
    public AudioManager getAudioManager(){
        return audioManager;
    }

    /**
     * タッチイベントマネージャーを取得する
     * @return タッチイベントマネージャー
     */
    public TouchEventManager getTouchEventManager(){
        return touchEventManager;
    }

    /**
     * 初期化を完了しているか取得する
     * @return 初期化を完了しているか
     */
    public boolean isInitialized(){
        return initialized;
    }
    /**
     * ロードを完了しているか取得する
     * @return ロードを完了しているか
     */
    public boolean isLoadCompleted(){
        return loadCompleted;
    }

    /**
     * アクターを取得する
     * @param name 取得するアクターの名前
     * @return 名前に一致するアクター
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Actor getActor(String name){
        return actors.stream().filter(target -> target.getName() == name).findFirst().orElse(null);
    }

}
