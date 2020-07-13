package com.blackhoodie.puyopuyo;

import android.view.MotionEvent;

public class FlickState{

    private MotionEvent firstMotionEvent;
    private MotionEvent lastMotionEvent;
    private float horizontalVelocity;
    private float verticalVelocity;

    public FlickState(MotionEvent firstMotionEvent, MotionEvent lastMotionEvent, float horizontalVelocity, float verticalVelocity){
        this.firstMotionEvent = firstMotionEvent;
        this.lastMotionEvent = lastMotionEvent;
        this.horizontalVelocity = horizontalVelocity;
        this.verticalVelocity = verticalVelocity;
    }

    public MotionEvent getFirstMotionEvent(){
        return firstMotionEvent;
    }

    public MotionEvent getLastMotionEvent(){
        return lastMotionEvent;
    }

    public float getHorizontalVelocity(){
        return horizontalVelocity;
    }

    public float getVerticalVelocity(){
        return verticalVelocity;
    }

}
