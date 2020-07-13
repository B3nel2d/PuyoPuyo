package com.blackhoodie.puyopuyo;

import android.os.Build;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.function.Consumer;

import androidx.annotation.RequiresApi;

public class InteractableComponent extends Component{

    public enum TouchAction{
        Down,
        Move,
        Up,
        LongPress,
        DoubleTap
    }

    private UITransformComponent uiTransform;
    private boolean interactable;

    private HashMap<TouchAction, Runnable> tasks;
    private Consumer<FlickState> flickTask;

    public InteractableComponent(Actor owner, int updateOrder, UITransformComponent uiTransform, boolean interactable){
        super(owner, updateOrder);

        this.uiTransform = uiTransform;
        this.interactable = interactable;

        tasks = new HashMap<TouchAction, Runnable>();
        flickTask = null;

        owner.getOwner().addInteractable(this);
    }
    public InteractableComponent(Actor owner, UITransformComponent uiTransform){
        this(owner, 0, uiTransform, true);
    }

    @Override
    public void update(){

    }

    public void addTask(TouchAction touchAction, Runnable task){
        tasks.put(touchAction, task);
    }
    public void removeTask(TouchAction touchAction){
        if(tasks.containsKey(touchAction)){
            tasks.remove(touchAction);
        }
    }
    public void clearTasks(){
        tasks.clear();
    }

    public void setFlickTask(Consumer<FlickState> task){
        flickTask = task;
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

        TouchAction touchAction = null;
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchAction = TouchAction.Down;
                break;
            case MotionEvent.ACTION_MOVE:
                touchAction = TouchAction.Move;
                break;
            case MotionEvent.ACTION_UP:
                touchAction = TouchAction.Up;
                break;
        }

        if(touchAction != null){
            tasks.get(touchAction).run();
        }
    }

    public void onLongPress(){
        if(tasks.containsKey(TouchAction.LongPress)){
            tasks.get(TouchAction.LongPress).run();
        }
    }

    public void onDoubleTap(){
        if(tasks.containsKey(TouchAction.DoubleTap)){
            tasks.get(TouchAction.DoubleTap).run();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onFling(MotionEvent firstMotionEvent, MotionEvent lastMotionEvent, float horizontalVelocity, float verticalVelocity){
        if(!interactable || flickTask == null){
            return;
        }

        flickTask.accept(new FlickState(firstMotionEvent, lastMotionEvent, horizontalVelocity, verticalVelocity));
    }

    public boolean isInteractable(){
        return interactable;
    }
    public void setInteractable(boolean value){
        this.interactable = value;
    }

}
