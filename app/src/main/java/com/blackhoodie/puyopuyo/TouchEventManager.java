package com.blackhoodie.puyopuyo;

import android.os.Build;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class TouchEventManager{

    private Level owner;

    private GestureDetector gestureDetector;

    private List<InteractableComponent> interactables;

    public TouchEventManager(Level owner){
        this.owner = owner;

        initialize();
    }

    public void initialize(){
        interactables = new ArrayList<InteractableComponent>();
        gestureDetector = new GestureDetector(Game.getInstance().getContext(), onGestureListener);
    }

    public void addInteractable(InteractableComponent interactable){
        interactables.add(interactable);
    }
    public void removeInteractable(InteractableComponent interactable){
        if(interactables.contains(interactable)){
            interactables.remove(interactable);
        }
    }

    public void onTouchEvent(MotionEvent motionEvent){
        for(InteractableComponent interactable : interactables){
            if(interactable.isInteractable()){
                interactable.onInteract(motionEvent);
                gestureDetector.onTouchEvent(motionEvent);
            }
        }
    }

    private final GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public void onLongPress(MotionEvent motionEvent){
            for(InteractableComponent interactableComponent : interactables){
                interactableComponent.onLongPress();
            }

            super.onLongPress(motionEvent);
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent){
            for(InteractableComponent interactableComponent : interactables){
                interactableComponent.onDoubleTap();
            }

            return super.onDoubleTap(motionEvent);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onFling(MotionEvent firstMotionEvent, MotionEvent lastMotionEvent, float horizontalVelocity, float verticalVelocity){
            for(InteractableComponent interactableComponent : interactables){
                if(interactableComponent.isInteractable()){
                    interactableComponent.onFling(firstMotionEvent, lastMotionEvent, horizontalVelocity, verticalVelocity);
                }
            }

            return super.onFling(firstMotionEvent, lastMotionEvent, horizontalVelocity, verticalVelocity);
        }
    };

    public void dispose(){
        interactables.clear();
    }

}
