package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TextComponent extends DrawableComponent{

    private String text;
    private Paint paint;

    public TextComponent(Actor owner, UITransformComponent uiTransform, int drawOrder){
        super(owner, 0, uiTransform, drawOrder);

        text = "";

        paint = new Paint();
        paint.setTextSize(10);
        paint.setColor(Color.BLACK);
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        if(text != null && paint != null){
            canvas.drawText(text, uiTransform.getPosition().x, uiTransform.getPosition().y, paint);
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
