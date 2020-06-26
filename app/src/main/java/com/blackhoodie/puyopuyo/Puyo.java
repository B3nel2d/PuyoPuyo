package com.blackhoodie.puyopuyo;

public class Puyo extends Actor{

    public enum Color{
        Red,
        Green,
        Blue,
        Yellow,
        Magenta,
        None
    }

    private UITransformComponent uiTransformComponent;
    private ImageComponent imageComponent;

    public Puyo(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){

    }

}
