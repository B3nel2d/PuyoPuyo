package com.blackhoodie.puyopuyo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Actor{

    public enum State{
        Active,
        Inactive,
        Deleted
    };

    private Level owner;

    private String name;
    private State state;

    private List<Component> components;

    public Actor(Level owner, String name){
        this.owner = owner;
        owner.addActor(this);

        this.name = name;
        state = State.Active;

        components = new ArrayList<Component>();

        initialize();
    }

    public void addComponent(Component component){
        components.add(component);
        Collections.sort(components, (component1, component2) -> component1.getUpdateOrder() - component2.getUpdateOrder());
    }

    public void removeComponent(Component component){
        if(components.contains(component)){
            components.remove(component);
        }
    }

    abstract void initialize();

    public void update(){
        if(state != State.Active){
            return;
        }

        if(!components.isEmpty()){
            for(Component component : components){
                component.update();
            }
        }
    }

    public Level getOwner(){
        return owner;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public State getState(){
        return state;
    }
    public void setState(State state){
        this.state = state;
    }

}
