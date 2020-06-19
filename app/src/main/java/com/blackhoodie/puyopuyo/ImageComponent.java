package com.blackhoodie.puyopuyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class ImageComponent extends DrawableComponent{

    private Bitmap bitmap;
    private Paint paint;

    public ImageComponent(Actor owner, UITransformComponent uiTransform, int drawOrder){
        super(owner, 0, uiTransform, drawOrder);
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        if(bitmap != null && paint != null){
            Matrix matrix = new Matrix();
            bitmap = Bitmap.createScaledBitmap(bitmap, (int)uiTransform.getSize().x, (int)uiTransform.getSize().y, true);
            matrix.setRotate(uiTransform.getRotation(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            matrix.postTranslate(uiTransform.getPosition().x, uiTransform.getPosition().y);

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
