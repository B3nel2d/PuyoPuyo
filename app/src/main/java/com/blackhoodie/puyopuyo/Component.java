package com.blackhoodie.puyopuyo;

abstract class Component{

    public enum State{
        Active,
        Inactive
    }

    protected Actor owner;
    protected State state;
    protected int updateOrder;

    public Component(Actor owner, int updateOrder){
        this.owner = owner;
        owner.addComponent(this);

        state = State.Active;
        this.updateOrder = updateOrder;
    }
    public Component(Actor owner){
        this(owner, 0);
    }

    abstract void update();

    public Actor getOwner(){
        return owner;
    }

    public State getState(){
        return state;
    }
    public void setState(State state){
        this.state = state;
    }

    public int getUpdateOrder(){
        return updateOrder;
    }

}
