package com.blackhoodie.puyopuyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class ImageComponent extends DrawableComponent{

    private Bitmap bitmap;
    private Paint paint;

    public ImageComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder, boolean visible){
        super(owner, 0, uiTransformComponent, drawOrder, visible);
    }
    public ImageComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder){
        this(owner, uiTransformComponent, drawOrder, true);
    }
    public ImageComponent(Actor owner, UITransformComponent uiTransformComponent){
        this(owner, uiTransformComponent, 0, true);
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        if(bitmap != null && paint != null){
            Matrix matrix = new Matrix();
            bitmap = Bitmap.createScaledBitmap(bitmap, (int)uiTransformComponent.getSize().x, (int)uiTransformComponent.getSize().y, true);
            matrix.setRotate(uiTransformComponent.getRotation(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            matrix.postTranslate(uiTransformComponent.getPosition().x - bitmap.getWidth() / 2, uiTransformComponent.getPosition().y - bitmap.getHeight() / 2);

            canvas.drawBitmap(bitmap, matrix, paint);
        }
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Paint getPaint(){
        return paint;
    }
    public void setPaint(Paint paint){
        this.paint = paint;
    }

}
