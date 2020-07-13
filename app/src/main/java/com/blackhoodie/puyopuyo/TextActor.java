package com.blackhoodie.puyopuyo;

public class TextActor extends Actor{

    private UITransformComponent uiTransformComponent;
    private TextComponent textComponent;

    public TextActor(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        textComponent = new TextComponent(this, uiTransformComponent, 0);
    }

    @Override
    public void update(){

    }

    public UITransformComponent getUITransformComponent(){
        return uiTransformComponent;
    }

    public TextComponent getTextComponent(){
        return textComponent;
    }

}
