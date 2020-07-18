package com.blackhoodie.puyopuyo;

import android.os.Build;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.function.Consumer;

import androidx.annotation.RequiresApi;

public class InteractableComponent extends Component{

    public enum TouchAction{
        Down,
        LongPress,
        DoubleTap,
        SingleTapUp
    }

    private UITransformComponent uiTransform;
    private boolean interactable;

    private HashMap<TouchAction, Runnable> tasks;
    private HashMap<TouchAction, Boolean> taskExecuteFlags;
    private Consumer<FlickState> flickTask;
    private boolean flickTaskExecuted;

    public InteractableComponent(Actor owner, int updateOrder, UITransformComponent uiTransform, boolean interactable){
        super(owner, updateOrder);

        this.uiTransform = uiTransform;
        this.interactable = interactable;

        tasks = new HashMap<TouchAction, Runnable>();
        taskExecuteFlags = new HashMap<TouchAction, Boolean>();
        flickTask = null;
        flickTaskExecuted = false;

        owner.getOwner().addInteractable(this);
    }
    public InteractableComponent(Actor owner, UITransformComponent uiTransform){
        this(owner, 0, uiTransform, true);
    }

    @Override
    public void update(){
        if(!owner.getOwner().getTouchEventManager().isDowning()){
            resetTaskExecuteFlag(TouchAction.Down);
        }
        if(!owner.getOwner().getTouchEventManager().isLongPressing()){
            resetTaskExecuteFlag(TouchAction.LongPress);
        }
        if(!owner.getOwner().getTouchEventManager().isDoubleTapping()){
            resetTaskExecuteFlag(TouchAction.DoubleTap);
        }
        if(!owner.getOwner().getTouchEventManager().isSingleTappingUp()){
            resetTaskExecuteFlag(TouchAction.SingleTapUp);
        }
        if(!owner.getOwner().getTouchEventManager().isFling()){
            flickTaskExecuted = false;
        }
    }

    public void addTask(TouchAction touchAction, Runnable task){
        tasks.put(touchAction, task);
        taskExecuteFlags.put(touchAction, false);
    }
    public void removeTask(TouchAction touchAction){
        if(tasks.containsKey(touchAction)){
            tasks.remove(touchAction);
            taskExecuteFlags.remove(touchAction);
        }
    }
    public void clearTasks(){
        tasks.clear();
    }

    public void setFlickTask(Consumer<FlickState> task){
        flickTask = task;
        flickTaskExecuted = false;
    }

    public void onDown(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.Down)){
            taskExecuteFlags.put(TouchAction.Down, true);
            tasks.get(TouchAction.Down).run();
        }
    }

    public void onLongPress(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.LongPress)){
            taskExecuteFlags.put(TouchAction.LongPress, true);
            tasks.get(TouchAction.LongPress).run();
        }
    }

    public void onDoubleTap(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.DoubleTap)){
            taskExecuteFlags.put(TouchAction.DoubleTap, true);
            tasks.get(TouchAction.DoubleTap).run();
        }
    }

    public void onSingleTapUp(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.SingleTapUp)){
            taskExecuteFlags.put(TouchAction.SingleTapUp, true);
            tasks.get(TouchAction.SingleTapUp).run();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onFling(MotionEvent firstMotionEvent, MotionEvent lastMotionEvent, float horizontalVelocity, float verticalVelocity){
        if(!interactable || flickTask == null){
            return;
        }

        flickTaskExecuted = true;
        flickTask.accept(new FlickState(firstMotionEvent, lastMotionEvent, horizontalVelocity, verticalVelocity));
    }

    public boolean isInteractable(){
        return interactable;
    }
    public void setInteractable(boolean value){
        this.interactable = value;
    }

    public boolean isTaskExecuted(TouchAction touchAction){
        if(!tasks.containsKey(touchAction)){
            return false;
        }

        return taskExecuteFlags.get(touchAction);
    }
    public void resetTaskExecuteFlag(TouchAction touchAction){
        if(!tasks.containsKey(touchAction)){
            return;
        }

        taskExecuteFlags.put(touchAction, false);
    }

    public boolean isFlickTaskExecuted(){
        return flickTaskExecuted;
    }

}
