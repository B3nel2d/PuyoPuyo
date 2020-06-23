package com.blackhoodie.puyopuyo;

import android.graphics.drawable.Drawable;

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

    public Actor(Level owner, String name, State state){
        this.owner = owner;
        owner.addActor(this);

        this.name = name;
        this.state = state;

        components = new ArrayList<Component>();

        initialize();
    }
    public Actor(Level owner, String name){
        this(owner, name, State.Active);
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

        for(Component component : components){
            component.update();
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

    public List<DrawableComponent> getDrawables(){
        List<DrawableComponent> drawables = new ArrayList<DrawableComponent>();
        for(Component component : components){
            if(component instanceof DrawableComponent){
                drawables.add((DrawableComponent)component);
            }
        }

        return drawables;
    }

    public List<InteractableComponent> getInteractables(){
        List<InteractableComponent> interactables = new ArrayList<InteractableComponent>();
        for(Component component : components){
            if(component instanceof InteractableComponent){
                interactables.add((InteractableComponent) component);
            }
        }

        return interactables;
    }

}
