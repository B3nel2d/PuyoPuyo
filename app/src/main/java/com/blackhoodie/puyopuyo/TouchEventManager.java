package com.blackhoodie.puyopuyo;

import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.RequiresApi;

public class TouchEventManager{

    private Level owner;

    private List<InteractableComponent> interactables;

    private GestureDetector gestureDetector;

    private boolean downing;
    private boolean longPressing;
    private boolean doubleTapping;
    private boolean singleTappingUp;
    private boolean fling;

    public TouchEventManager(Level owner){
        this.owner = owner;
        initialize();
    }

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

    public void addInteractable(InteractableComponent interactable){
        interactables.add(interactable);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeInteractable(InteractableComponent interactable){
        interactables.removeIf(target -> target == interactable);
    }

    public void onTouchEvent(MotionEvent motionEvent){
        for(InteractableComponent interactable : interactables){
            if(interactable.getOwner().getState() == Actor.State.Active && interactable.isInteractable()){
                gestureDetector.onTouchEvent(motionEvent);
            }
        }
    }

    public void dispose(){
        interactables.clear();
    }

    public boolean isDowning(){
        return downing;
    }

    public boolean isLongPressing(){
        return longPressing;
    }

    public boolean isDoubleTapping(){
        return doubleTapping;
    }

    public boolean isSingleTappingUp(){
        return singleTappingUp;
    }

    public boolean isFling(){
        return fling;
    }

}
