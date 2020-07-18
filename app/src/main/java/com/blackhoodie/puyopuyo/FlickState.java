package com.blackhoodie.puyopuyo;

import android.view.MotionEvent;

/**
 * フリック情報を保持するクラス
 */
public class FlickState{

    /** フリックの最初のMotionEvent */
    private MotionEvent firstMotionEvent;
    /** フリックの最後のMotionEvent */
    private MotionEvent lastMotionEvent;
    /** 平行方向の加速度 */
    private float horizontalVelocity;
    /** 垂直方向の加速度 */
    private float verticalVelocity;

    /**
     * コンストラクタ
     * @param firstMotionEvent フリックの最初のMotionEvent
     * @param lastMotionEvent フリックの最後のMotionEvent
     * @param horizontalVelocity 平行方向の加速度
     * @param verticalVelocity 垂直方向の加速度
     */
    public FlickState(MotionEvent firstMotionEvent, MotionEvent lastMotionEvent, float horizontalVelocity, float verticalVelocity){
        this.firstMotionEvent = firstMotionEvent;
        this.lastMotionEvent = lastMotionEvent;
        this.horizontalVelocity = horizontalVelocity;
        this.verticalVelocity = verticalVelocity;
    }

    /**
     * フリックの最初のMotionEventを取得する
     * @return フリックの最初のMotionEvent
     */
    public MotionEvent getFirstMotionEvent(){
        return firstMotionEvent;
    }

    /**
     * フリックの最後のMotionEventを取得する
     * @return フリックの最後のMotionEvent
     */
    public MotionEvent getLastMotionEvent(){
        return lastMotionEvent;
    }

    /**
     * 平行方向の加速度を取得する
     * @return 平行方向の加速度
     */
    public float getHorizontalVelocity(){
        return horizontalVelocity;
    }

    /**
     * 垂直方向の加速度を取得する
     * @return 垂直方向の加速度
     */
    public float getVerticalVelocity(){
        return verticalVelocity;
    }

}
