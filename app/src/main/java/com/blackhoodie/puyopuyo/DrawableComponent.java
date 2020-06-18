package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;

abstract class DrawableComponent extends Component{

    protected UITransformComponent uiTransform;
    protected int drawOrder;

    public DrawableComponent(Actor owner, int updateOrder, UITransformComponent uiTransform, int drawOrder){
        super(owner, updateOrder);

        this.uiTransform = uiTransform;
        this.drawOrder = drawOrder;

        owner.getOwner().addDrawable(this);
    }
    public DrawableComponent(Actor owner, UITransformComponent uiTransform, int drawOrder){
        this(owner, 0, uiTransform, drawOrder);
    }

    abstract void draw(Canvas canvas);

    public int getDrawOrder(){
        return drawOrder;
    }
    public void setDrawOrder(int drawOrder){
        this.drawOrder = drawOrder;
    }

}
