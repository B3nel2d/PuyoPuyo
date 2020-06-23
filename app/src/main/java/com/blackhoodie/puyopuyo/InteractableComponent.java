package com.blackhoodie.puyopuyo;

import android.view.MotionEvent;

import java.util.HashMap;

public class InteractableComponent extends Component{

    private UITransformComponent uiTransform;
    private boolean interactable;

    private HashMap<Integer, Runnable> tasks;

    public InteractableComponent(Actor owner, int updateOrder, UITransformComponent uiTransform, boolean interactable){
        super(owner, updateOrder);

        this.uiTransform = uiTransform;
        this.interactable = interactable;

        tasks = new HashMap<Integer, Runnable>();

        owner.getOwner().addInteractable(this);
    }
    public InteractableComponent(Actor owner, UITransformComponent uiTransform){
        this(owner, 0, uiTransform, true);
    }

    @Override
    public void update(){

    }

    public void onInteract(MotionEvent event){
        if(!interactable){
            return;
        }

        if(!tasks.containsKey(event.getAction())){
            return;
        }
        if(tasks.get(event.getAction()) == null){
            return;
        }

        if(event.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < event.getX()){
            return;
        }
        if(event.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < event.getY()){
            return;
        }

        tasks.get(event.getAction()).run();
    }

    public boolean isInteractable(){
        return interactable;
    }
    public void setInteractable(boolean value){
        this.interactable = value;
    }

    public void addTask(int action, Runnable task){
        tasks.put(action, task);
    }
    public void removeTask(int action){
        if(tasks.containsKey(action)){
            tasks.remove(action);
        }
    }
    public void clearTasks(){
        tasks.clear();
    }

}
