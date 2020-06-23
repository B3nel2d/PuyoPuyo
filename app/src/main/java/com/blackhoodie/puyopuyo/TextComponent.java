package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TextComponent extends DrawableComponent{

    private String text;
    private Paint paint;

    public TextComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder, boolean visible){
        super(owner, 0, uiTransformComponent, drawOrder, visible);

        text = "";

        paint = new Paint();
        paint.setTextSize(10);
        paint.setColor(Color.BLACK);
    }
    public TextComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder){
        this(owner, uiTransformComponent, drawOrder, true);
    }
    public TextComponent(Actor owner, UITransformComponent uiTransformComponent){
        this(owner, uiTransformComponent, 0, true);
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        super.draw(canvas);

        if(text != null && paint != null){
            canvas.drawText(text, uiTransformComponent.getPosition().x - uiTransformComponent.getSize().x / 2.0f, uiTransformComponent.getPosition().y - uiTransformComponent.getSize().y / 2.0f, paint);
        }
    }

    public String getText() {
        return text;
    }
    public void setText(String text){
        this.text = text;
    }

    public Paint getPaint(){
        return paint;
    }
    public void setPaint(Paint paint){
        this.paint = paint;
    }

}
