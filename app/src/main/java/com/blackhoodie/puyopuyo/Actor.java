package com.blackhoodie.puyopuyo;

import android.os.Build;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.RequiresApi;

abstract class Actor{

    public enum State{
        Active,
        Inactive,
        Deleted
    }

    protected Level owner;

    protected String name;
    protected State state;

    protected List<Component> components;

    protected List<Actor> children;

    public Actor(Level owner, String name, State state){
        this.owner = owner;
        owner.addActor(this);

        this.name = name;
        this.state = state;

        components = new CopyOnWriteArrayList<Component>();

        children = new CopyOnWriteArrayList<Actor>();

        initialize();
    }
    public Actor(Level owner, String name){
        this(owner, name, State.Active);
    }

    abstract void initialize();

    public void addComponent(Component component){
        components.add(component);
        Collections.sort(components, (component1, component2) -> component1.getUpdateOrder() - component2.getUpdateOrder());
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeComponent(Component component){
        components.removeIf(target -> target == component);
    }

    public void addChild(Actor actor){
        if(!children.contains(actor)){
            children.add(actor);
        }
    }
    public void removeChild(Actor actor){
        if(children.contains(actor)){
            children.remove(actor);
        }
    }

    abstract void update();

    public void updateComponents(){
        if(state != State.Active){
            return;
        }

        for(Component component : components){
            component.update();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(){
        state = State.Deleted;
        dispose();

        for(Actor actor : children){
            actor.delete();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dispose(){
        for(Component component : components){
            if(component instanceof DrawableComponent){
                owner.getGraphicsManager().removeDrawable((DrawableComponent)component);
            }

            if(component instanceof InteractableComponent){
                owner.getTouchEventManager().removeInteractable((InteractableComponent)component);
            }
        }

        components.clear();
        children.clear();
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

        for(Actor actor : children){
            actor.setState(state);
        }
    }

}
