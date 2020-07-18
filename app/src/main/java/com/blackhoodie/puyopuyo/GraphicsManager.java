package com.blackhoodie.puyopuyo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.RequiresApi;

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
        drawables = new CopyOnWriteArrayList<DrawableComponent>();
        pendingDrawables = new CopyOnWriteArrayList<DrawableComponent>();

        bitmaps = new HashMap<String, Bitmap>();

        rendering = false;
    }

    public void addDrawable(DrawableComponent drawable){
        if(rendering){
            pendingDrawables.add(drawable);
        }
        else{
            drawables.add(drawable);
            sortDrawables();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeDrawable(DrawableComponent drawable){
        drawables.removeIf(target -> target == drawable);
    }
    public void sortDrawables(){
        Collections.sort(drawables, new DrawableComparator());
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
            System.out.println("Could not find bitmap(" + name + ").");
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
            if(drawable.getOwner().getState() == Actor.State.Active && drawable.isVisible()){
                drawable.draw(canvas);
            }
        }
        rendering = false;

        for(DrawableComponent drawable : pendingDrawables){
            drawables.add(drawable);
            sortDrawables();
        }
        pendingDrawables.clear();
    }

    public void dispose(){
        drawables.clear();
        bitmaps.clear();
    }

}
