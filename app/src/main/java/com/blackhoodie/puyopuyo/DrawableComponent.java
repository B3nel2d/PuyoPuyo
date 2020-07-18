package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;

abstract class DrawableComponent extends Component{

    protected UITransformComponent uiTransformComponent;
    protected int drawOrder;
    protected boolean visible;

    public DrawableComponent(Actor owner, int updateOrder, UITransformComponent uiTransformComponent, int drawOrder, boolean visible){
        super(owner, updateOrder);

        this.uiTransformComponent = uiTransformComponent;
        this.drawOrder = drawOrder;
        this.visible = visible;

        owner.getOwner().addDrawable(this);
    }

    abstract void draw(Canvas canvas);

    public int getDrawOrder(){
        return drawOrder;
    }
    public void setDrawOrder(int drawOrder){
        this.drawOrder = drawOrder;
        owner.getOwner().getGraphicsManager().sortDrawables();
    }

    public boolean isVisible(){
        return visible;
    }
    public void setVisible(boolean value){
        this.visible = value;
    }

}
