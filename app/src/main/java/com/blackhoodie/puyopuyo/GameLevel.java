package com.blackhoodie.puyopuyo;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class GameLevel extends Level{

    private Puyo[][] field;

    private ImageActor fieldImage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameLevel(String name){
        super(name);
    }

    @Override
    public void initializeActors(){
        field = new Puyo[6][13];
        /*
        fieldImage = new ImageActor(this, "Field Image");
        fieldImage.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2, Game.getInstance().getScreenSize().y / 2);
        fieldImage.getUITransformComponent().setSize(600, 1300);
        fieldImage.getImageComponent().setBitmap();
        */
    }

    @Override
    public void update(){


        super.update();
    }

}
