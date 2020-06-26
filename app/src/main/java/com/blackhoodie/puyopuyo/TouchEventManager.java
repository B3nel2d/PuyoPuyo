package com.blackhoodie.puyopuyo;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class TouchEventManager{

    private Level owner;

    private List<InteractableComponent> interactables;

    public TouchEventManager(Level owner){
        this.owner = owner;

        initialize();
    }

    public void initialize(){
        interactables = new ArrayList<InteractableComponent>();
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
            }
        }
    }

    public void dispose(){
        interactables.clear();
    }

}
