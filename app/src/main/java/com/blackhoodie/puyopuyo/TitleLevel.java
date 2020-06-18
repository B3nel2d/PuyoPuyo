package com.blackhoodie.puyopuyo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.Image;

public class TitleLevel extends Level{

    private TextActor titleText;
    private ImageActor testImage;

    public TitleLevel(String name){
        super(name);
    }

    @Override
    public void initialize(){
        titleText = new TextActor(this, "Title Text");
        titleText.getUITransformComponent().setPosition(new Vector2D(100, 100));
        titleText.getTextComponent().setText("TEST TEXT");
        titleText.getTextComponent().getPaint().setTextSize(50);

        testImage = new ImageActor(this, "Test Image");
        testImage.getUITransformComponent().setPosition(new Vector2D(0, 0));
        testImage.getUITransformComponent().setSize(new Vector2D(100, 100));
        testImage.getUITransformComponent().setRotation(45);
        Bitmap bitmap = BitmapFactory.decodeResource(Game.getInstance().getContext().getResources(), R.drawable.android_icon);
        testImage.getImageComponent().setBitmap(bitmap);
        testImage.getImageComponent().setPaint(new Paint());
    }

}
