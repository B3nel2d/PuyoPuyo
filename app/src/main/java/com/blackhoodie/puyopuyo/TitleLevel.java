package com.blackhoodie.puyopuyo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;

import java.util.function.Consumer;
import java.util.function.Supplier;

import androidx.annotation.RequiresApi;

public class TitleLevel extends Level{

    private TextActor titleText;
    private ImageActor testImage;
    private ButtonActor testButton;

    public TitleLevel(String name){
        super(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initialize(){
        super.initialize();

        titleText = new TextActor(this, "Title Text");
        titleText.getUITransformComponent().setPosition(new Vector2D(100, 100));
        titleText.getTextComponent().setText("TEST TEXT");
        titleText.getTextComponent().getPaint().setTextSize(50);
        /*
        testImage = new ImageActor(this, "Test Image");
        testImage.getUITransformComponent().setPosition(new Vector2D(0, 0));
        testImage.getUITransformComponent().setSize(new Vector2D(100, 100));
        testImage.getUITransformComponent().setRotation(45);
        Bitmap bitmap = BitmapFactory.decodeResource(Game.getInstance().getContext().getResources(), R.drawable.android_icon);
        testImage.getImageComponent().setBitmap(bitmap);
        testImage.getImageComponent().setPaint(new Paint());
        */
        testButton = new ButtonActor(this, "Test Button");
        testButton.getUITransformComponent().setSize(new Vector2D(500, 500));
        Bitmap bitmap = BitmapFactory.decodeResource(Game.getInstance().getContext().getResources(), R.drawable.android_icon);
        testButton.getImageComponent().setBitmap(bitmap);
        testButton.getImageComponent().setPaint(new Paint());
        testButton.getInteractableComponent().setAction(this::ButtonEvent);
    }

    private void ButtonEvent(Object object){
        testButton.getUITransformComponent().translate(100, 0);
    }

}
