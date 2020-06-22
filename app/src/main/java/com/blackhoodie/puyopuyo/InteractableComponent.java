package com.blackhoodie.puyopuyo;

import android.os.Build;
import android.view.MotionEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

import androidx.annotation.RequiresApi;

public class InteractableComponent extends Component{

    private UITransformComponent uiTransform;
    private boolean interactable;

    private Consumer<?> action;

    public InteractableComponent(Actor owner, int updateOrder, UITransformComponent uiTransform, boolean interactable){
        super(owner, updateOrder);

        this.uiTransform = uiTransform;
        this.interactable = interactable;

        action = null;

        owner.getOwner().addInteractable(this);
    }
    public InteractableComponent(Actor owner, UITransformComponent uiTransform){
        this(owner, 0, uiTransform, true);
    }

    @Override
    public void update(){

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onInteract(MotionEvent event){
        if(action == null){
            return;
        }

        if(event.getX() < uiTransform.getPosition().x || uiTransform.getPosition().x + uiTransform.getSize().x < event.getX()){
            return;
        }
        if(event.getY() < uiTransform.getPosition().y || uiTransform.getPosition().y + uiTransform.getSize().y < event.getY()){
            return;
        }

        action.accept(null);
    }

    public boolean isInteractable(){
        return interactable;
    }
    public void setInteractable(boolean value){
        this.interactable = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAction(Consumer<?> action){
        this.action = action;
    }
    public void clearAction(){
        this.action = null;
    }

}

