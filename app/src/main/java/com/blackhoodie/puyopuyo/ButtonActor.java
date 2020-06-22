package com.blackhoodie.puyopuyo;

public class ButtonActor extends Actor{

    private UITransformComponent uiTransformComponent;
    private ImageComponent imageComponent;
    private InteractableComponent interactableComponent;

    public ButtonActor(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        imageComponent = new ImageComponent(this, uiTransformComponent, 0);
        interactableComponent = new InteractableComponent(this, uiTransformComponent);
    }

    @Override
    public void update(){
        super.update();
    }

    public UITransformComponent getUITransformComponent(){
        return uiTransformComponent;
    }

    public ImageComponent getImageComponent(){
        return imageComponent;
    }

    public InteractableComponent getInteractableComponent(){
        return interactableComponent;
    }

}
