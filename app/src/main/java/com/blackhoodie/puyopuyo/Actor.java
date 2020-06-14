package com.blackhoodie.puyopuyo;

abstract class Actor{

    Level owner;

    public Actor(Level level){
        owner = level;
        owner.addActor(this);
    }

    abstract void update();

}
