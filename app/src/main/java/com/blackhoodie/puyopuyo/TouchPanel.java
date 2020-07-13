package com.blackhoodie.puyopuyo;

public class TouchPanel extends Actor{

    private UITransformComponent uiTransformComponent;
    private InteractableComponent interactableComponent;

    public TouchPanel(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        interactableComponent = new InteractableComponent(this, uiTransformComponent);
    }

    @Override
    public void update(){

    }

    public UITransformComponent getUiTransformComponent(){
        return uiTransformComponent;
    }

    public InteractableComponent getInteractableComponent(){
        return interactableComponent;
    }

}
