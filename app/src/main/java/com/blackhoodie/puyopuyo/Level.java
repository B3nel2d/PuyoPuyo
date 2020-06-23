package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Level{

    private String name;

    private List<Actor> actors;
    private List<Actor> pendingActors;
    private boolean updatingActors;

    private List<DrawableComponent> drawables;
    private List<InteractableComponent> interactables;

    public Level(String name){
        this.name = name;

        actors = new ArrayList<Actor>();
        pendingActors = new ArrayList<Actor>();
        updatingActors = false;

        drawables = new ArrayList<DrawableComponent>();
        interactables = new ArrayList<InteractableComponent>();
    }

    public void addActor(Actor actor){
        if(updatingActors){
            pendingActors.add(actor);
        }
        else{
            actors.add(actor);
        }
    }
    public void removeActor(Actor actor){
        if(actors.contains(actor) && actor.getState() == Actor.State.Deleted){
            for(DrawableComponent drawable : actor.getDrawables()){
                removeDrawable(drawable);
            }
            for(InteractableComponent interactable : actor.getInteractables()){
                removeInteractable(interactable);
            }

            actors.remove(actor);
        }
    }

    public void addDrawable(DrawableComponent drawable){
        drawables.add(drawable);
        Collections.sort(drawables, (drawable1, drawable2) -> drawable1.getDrawOrder() - drawable2.getDrawOrder());
    }
    public void removeDrawable(DrawableComponent drawable){
        if(drawables.contains(drawable)){
            drawables.remove(drawable);
        }
    }

    public void addInteractable(InteractableComponent interactable){
        interactables.add(interactable);
    }
    public void removeInteractable(InteractableComponent interactable){
        if(interactables.contains(interactable)){
            interactables.remove(interactable);
        }
    }

    public void initialize(){
        actors = new ArrayList<Actor>();
        pendingActors = new ArrayList<Actor>();
        updatingActors = false;

        drawables = new ArrayList<DrawableComponent>();
        interactables = new ArrayList<InteractableComponent>();
    }

    public void update(){
        updatingActors = true;
        for(Actor actor: actors){
            actor.update();
        }
        updatingActors = false;

        for(Actor actor: pendingActors){
            actors.add(actor);
        }
        pendingActors.clear();

        for(Actor actor : actors){
            if(actor.getState() == Actor.State.Deleted){
                removeActor(actor);
            }
        }
    }

    public void render(Canvas canvas){
        for(DrawableComponent drawable : drawables){
            drawable.draw(canvas);
        }
    }

    public void onTouchEvent(MotionEvent event){
        for(InteractableComponent interactable : interactables){
            if(interactable.isInteractable()){
                interactable.onInteract(event);
            }
        }
    }

    public String getName(){
        return name;
    }

}
