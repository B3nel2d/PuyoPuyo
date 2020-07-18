package com.blackhoodie.puyopuyo;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class TitleLevel extends Level{

    private TextActor titleText;

    private ButtonActor startButton;
    private TextActor startButtonText;

    private ImageActor backgroundImage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleLevel(String name){
        super(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initializeActors(){
        graphicsManager.addBitmap("images/white-square.png", "Background");
        graphicsManager.addBitmap("images/black-square.png", "Button");

        audioManager.addAudio("audios/button-tap.wav", "Button Tap");

        backgroundImage = new ImageActor(this, "Background Image");
        backgroundImage.getUITransformComponent().setSize(Game.getInstance().getScreenSize());
        backgroundImage.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f));
        backgroundImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Background"));
        backgroundImage.getImageComponent().setDrawOrder(0);

        titleText = new TextActor(this, "Title Text");
        titleText.getUITransformComponent().setSize(0, 0);
        titleText.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2.0f, 500);
        titleText.getTextComponent().setText("とことんぷよぷよ");
        titleText.getTextComponent().getPaint().setTextSize(100);
        titleText.getTextComponent().getPaint().setColor(Color.BLACK);
        titleText.getTextComponent().setDrawOrder(1);

        startButton = new ButtonActor(this, "Start Button");
        startButton.getUITransformComponent().setSize(new Vector2D(500, 200));
        startButton.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2.0f, Game.getInstance().getScreenSize().y / 2.0f);
        startButton.getImageComponent().setBitmap(graphicsManager.getBitmap("Button"));
        startButton.getImageComponent().setDrawOrder(1);
        startButton.getInteractableComponent().addTask(InteractableComponent.TouchAction.Down, this::startGame);

        startButtonText = new TextActor(this, "Start Button Text");
        startButtonText.getUITransformComponent().setPosition(startButton.getUITransformComponent().getPosition());
        startButtonText.getTextComponent().setText("START");
        startButtonText.getTextComponent().getPaint().setTextSize(100);
        startButtonText.getTextComponent().getPaint().setColor(Color.WHITE);
        startButtonText.getTextComponent().setDrawOrder(2);

        startButton.addChild(startButtonText);
    }

    @Override
    public void update(){

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startGame(){
        audioManager.playAudio("Button Tap");
        Game.getInstance().loadLevel("Game Level");
    }

}
