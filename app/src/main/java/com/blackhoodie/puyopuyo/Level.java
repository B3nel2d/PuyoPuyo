package com.blackhoodie.puyopuyo;

import java.util.ArrayList;
import java.util.List;

abstract class Level{

    private List<Actor> actors;
    private List<Actor> pendingActors;
    private boolean updatingActors;

    public Level(){
        actors = new ArrayList<Actor>();
        pendingActors = new ArrayList<Actor>();
        updatingActors = false;
    }

    abstract void initialize();

    public void addActor(Actor actor){
        if(updatingActors){
            pendingActors.add(actor);
        }
        else{
            actors.add(actor);
        }
    }

    public void removeActor(){

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
    }

}
