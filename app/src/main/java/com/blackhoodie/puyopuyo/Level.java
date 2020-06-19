package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Level{

    private String name;

    private List<Actor> actors;
    private List<Actor> pendingActors;
    private boolean updatingActors;

    private List<DrawableComponent> drawables;

    public Level(String name){
        this.name = name;

        actors = new ArrayList<Actor>();
        pendingActors = new ArrayList<Actor>();
        updatingActors = false;

        drawables = new ArrayList<DrawableComponent>();
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
        if(actors.contains(actor)){
            actors.remove(actor);
        }
    }

    public void addDrawable(DrawableComponent drawable){
        drawables.add(drawable);
        Collections.sort(drawables, (drawable1, drawable2) -> drawable1.getDrawOrder() - drawable2.getDrawOrder());
    }

    public void removeDrawable(DrawableComponent drawable){
        drawables.remove(drawable);
    }

    public void initialize(){

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

        List<Actor> deletedActors = new ArrayList<Actor>();
        for(Actor actor : actors){
            if(actor.getState() == Actor.State.Deleted){
                deletedActors.add(actor);
            }
        }

        for(Actor actor : deletedActors){
            actor = null;
        }
    }

    public void render(Canvas canvas){
        for(DrawableComponent drawable : drawables){
            drawable.draw(canvas);
        }
    }

    public String getName(){
        return name;
    }

}
