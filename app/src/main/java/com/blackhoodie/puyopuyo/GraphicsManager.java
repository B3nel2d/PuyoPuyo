package com.blackhoodie.puyopuyo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GraphicsManager{

    private Level owner;

    private List<DrawableComponent> drawables;
    private List<DrawableComponent> pendingDrawables;

    private HashMap<String, Bitmap> bitmaps;

    private boolean rendering;

    public GraphicsManager(Level owner){
        this.owner = owner;

        initialize();
    }

    public void initialize(){
        drawables = new ArrayList<DrawableComponent>();
        pendingDrawables = new ArrayList<DrawableComponent>();

        bitmaps = new HashMap<String, Bitmap>();

        rendering = false;
    }

    public void addDrawable(DrawableComponent drawable){
        if(rendering){
            pendingDrawables.add(drawable);
        }
        else{
            drawables.add(drawable);
            Collections.sort(drawables, (drawable1, drawable2) -> drawable1.getDrawOrder() - drawable2.getDrawOrder());
        }
    }
    public void removeDrawable(DrawableComponent drawable){
        if(drawables.contains(drawable)){
            drawables.remove(drawable);
        }
    }

    public void addBitmap(String filePath, String name){
        if(bitmaps.containsKey(name)){
            return;
        }

        AssetManager assetManager = Game.getInstance().getContext().getResources().getAssets();
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open(filePath);
        }
        catch(IOException exception){
            throw new IllegalStateException();
        }

        bitmaps.put(name, BitmapFactory.decodeStream(inputStream));
    }
    public Bitmap getBitmap(String name){
        if(!bitmaps.containsKey(name)){
            return null;
        }

        return bitmaps.get(name);
    }
    public void removeBitmap(String name){
        if(!bitmaps.containsKey(name)){
            return;
        }

        bitmaps.remove(name);
    }

    public void render(Canvas canvas){
        rendering = true;
        for(DrawableComponent drawable : drawables){
            if(drawable.isVisible()){
                drawable.draw(canvas);
            }
        }
        rendering = false;

        for(DrawableComponent drawable : pendingDrawables){
            drawables.add(drawable);
        }
        pendingDrawables.clear();
    }

    public void dispose(){
        drawables.clear();
        bitmaps.clear();
    }

}
