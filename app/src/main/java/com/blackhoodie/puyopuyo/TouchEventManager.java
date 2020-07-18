package com.blackhoodie.puyopuyo;

import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.RequiresApi;

/**
 * タッチイベント関連の処理を担うクラス
 */
public class TouchEventManager{

    /** 所属するレベル */
    private Level owner;

    /** インタラクタブルのリスト */
    private List<InteractableComponent> interactables;

    /** GestureDetector */
    private GestureDetector gestureDetector;

    /** 画面を押下しているか */
    private boolean downing;
    /** 画面を長押ししているか */
    private boolean longPressing;
    /** 画面をダブルタップしているか */
    private boolean doubleTapping;
    /** 画面をタップアップしているか */
    private boolean singleTappingUp;
    /** 画面をフリックしているか */
    private boolean fling;

    /**
     * コンストラクタ
     * @param owner 所属するレベル
     */
    public TouchEventManager(Level owner){
        this.owner = owner;
        initialize();
    }

    /**
     * 初期化処理を行う
     */
    public void initialize(){
        interactables = new CopyOnWriteArrayList<InteractableComponent>();

        GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent motionEvent){
                downing = true;
                for(InteractableComponent interactable : interactables){
                    if(!interactable.isTaskExecuted(InteractableComponent.TouchAction.Down)){
                        interactable.onDown(motionEvent);
                    }
                }
                downing = false;

                return super.onDown(motionEvent);
            }

            @Override
            public void onLongPress(MotionEvent motionEvent){
                longPressing = true;
                for(InteractableComponent interactable : interactables){
                    if(!interactable.isTaskExecuted(InteractableComponent.TouchAction.LongPress)){
                        interactable.onLongPress(motionEvent);
                    }
                }
                longPressing = false;

                super.onLongPress(motionEvent);
            }

            @Override
            public boolean onDoubleTap(MotionEvent motionEvent){
                doubleTapping = true;
                for(InteractableComponent interactable : interactables){
                    if(!interactable.isTaskExecuted(InteractableComponent.TouchAction.DoubleTap)){
                        interactable.onDoubleTap(motionEvent);
                    }
                }
                doubleTapping = false;

                return super.onDoubleTap(motionEvent);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent){
                singleTappingUp = true;
                for(InteractableComponent interactable : interactables){
                    if(!interactable.isTaskExecuted(InteractableComponent.TouchAction.SingleTapUp)){
                        interactable.onSingleTapUp(motionEvent);
                    }
                }
                singleTappingUp = false;

                return super.onSingleTapUp(motionEvent);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onFling(MotionEvent firstMotionEvent, MotionEvent lastMotionEvent, float horizontalVelocity, float verticalVelocity){
                fling = true;
                for(InteractableComponent interactable : interactables){
                    if(!interactable.isFlickTaskExecuted()){
                        interactable.onFling(firstMotionEvent, lastMotionEvent, horizontalVelocity, verticalVelocity);
                    }
                }
                fling = false;

                return super.onFling(firstMotionEvent, lastMotionEvent, horizontalVelocity, verticalVelocity);
            }
        };
        gestureDetector = new GestureDetector(Game.getInstance().getContext(), onGestureListener);

        downing = false;
        longPressing = false;
        doubleTapping = false;
        singleTappingUp = false;
    }

    /**
     * インタラクタブルを追加する
     * @param interactable 追加するインタラクタブル
     */
    public void addInteractable(InteractableComponent interactable){
        interactables.add(interactable);
    }
    /**
     * インタラクタブルを削除する
     * @param interactable 削除するインタラクタブル
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeInteractable(InteractableComponent interactable){
        interactables.removeIf(target -> target == interactable);
    }

    /**
     * タッチイベント（各インタラクタブルのタッチイベント時処理を呼び出す）
     * @param motionEvent タッチイベントのMotionEvent
     */
    public void onTouchEvent(MotionEvent motionEvent){
        for(InteractableComponent interactable : interactables){
            if(interactable.getOwner().getState() == Actor.State.Active && interactable.isInteractable()){
                gestureDetector.onTouchEvent(motionEvent);
            }
        }
    }

    /**
     * リソースを開放する
     */
    public void dispose(){
        interactables.clear();
    }

    /**
     * 画面を押下しているか取得する
     * @return 画面を押下しているか
     */
    public boolean isDowning(){
        return downing;
    }

    /**
     * 画面を長押ししているか取得する
     * @return 画面を長押ししているか
     */
    public boolean isLongPressing(){
        return longPressing;
    }

    /**
     * 画面をダブルタップしているか取得する
     * @return 画面をダブルタップしているか
     */
    public boolean isDoubleTapping(){
        return doubleTapping;
    }

    /**
     * 画面をタップアップしているか取得する
     * @return 画面をタップアップしているか
     */
    public boolean isSingleTappingUp(){
        return singleTappingUp;
    }

    /**
     * 画面をフリックしているか取得する
     * @return 画面をフリックしているか
     */
    public boolean isFling(){
        return fling;
    }

}
