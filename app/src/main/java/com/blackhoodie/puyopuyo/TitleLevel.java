package com.blackhoodie.puyopuyo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

public class TitleLevel extends Level{

    private TextActor titleText;
    private ImageActor testImage;
    private ButtonActor testButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleLevel(String name){
        super(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initializeActors(){
        titleText = new TextActor(this, "Title Text");
        titleText.getUITransformComponent().setPosition(new Vector2D(Game.getInstance().getScreenSize().x / 2.0f, 100));
        titleText.getTextComponent().setText("TITLE");
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
        testButton.getUITransformComponent().setSize(new Vector2D(300, 300));
        testButton.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2.0f, Game.getInstance().getScreenSize().y / 2.0f);
        Bitmap bitmap = BitmapFactory.decodeResource(Game.getInstance().getContext().getResources(), R.drawable.android_icon);
        testButton.getImageComponent().setBitmap(bitmap);
        testButton.getImageComponent().setPaint(new Paint());
        testButton.getInteractableComponent().addTask(MotionEvent.ACTION_DOWN, this::onTestButtonDown);
        audioManager.addAudio("Button Tap Sound", "audios/button_tap.wav");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onTestButtonDown(){
        audioManager.playAudio("Button Tap Sound");
    }

}
