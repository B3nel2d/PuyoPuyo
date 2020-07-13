package com.blackhoodie.puyopuyo;

public class ImageActor extends Actor{

    private UITransformComponent uiTransformComponent;
    private ImageComponent imageComponent;

    public ImageActor(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        imageComponent = new ImageComponent(this, uiTransformComponent, 0);
    }

    @Override
    public void update(){

    }

    public UITransformComponent getUITransformComponent(){
        return uiTransformComponent;
    }

    public ImageComponent getImageComponent(){
        return imageComponent;
    }

}
