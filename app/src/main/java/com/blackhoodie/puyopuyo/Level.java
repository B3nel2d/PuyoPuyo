package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.RequiresApi;

abstract class Level{

    protected String name;

    private boolean initialized;
    protected boolean loadCompleted;

    protected GraphicsManager graphicsManager;
    protected AudioManager audioManager;
    protected TouchEventManager touchEventManager;

    private List<Actor> actors;
    private List<Actor> pendingActors;
    private boolean updatingActors;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Level(String name){
        this.name = name;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initialize(){
        initialized = false;
        loadCompleted = false;

        graphicsManager = new GraphicsManager(this);
        audioManager = new AudioManager(this);
        touchEventManager = new TouchEventManager(this);

        actors = new ArrayList<Actor>();
        pendingActors = new ArrayList<Actor>();
        updatingActors = false;

        initializeActors();

        audioManager.initialize();

        initialized = true;
    }

    abstract void initializeActors();

    public void onLoadComplete(){
        loadCompleted = true;
    }

    public void addActor(Actor actor){
        if(updatingActors){
            pendingActors.add(actor);
        }
        else{
            actors.add(actor);
        }
    }

    public void addDrawable(DrawableComponent drawable){
        if(graphicsManager != null){
            graphicsManager.addDrawable(drawable);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeDrawable(DrawableComponent drawable){
        if(graphicsManager != null){
            graphicsManager.removeDrawable(drawable);
        }
    }

    public void addInteractable(InteractableComponent interactable){
        if(touchEventManager != null){
            touchEventManager.addInteractable(interactable);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeInteractable(InteractableComponent interactable){
        if(touchEventManager != null){
            touchEventManager.removeInteractable(interactable);
        }
    }

    protected void skipLoad(){
        loadCompleted = true;
    }

    abstract void update();

    public void updateActors(){
        updatingActors = true;
        for(Actor actor: actors){
            actor.update();
            actor.updateComponents();
        }
        updatingActors = false;

        for(Actor actor: pendingActors){
            actors.add(actor);
        }
        pendingActors.clear();

        Iterator iterator = actors.iterator();
        while(iterator.hasNext()){
            Actor actor = (Actor)iterator.next();
            if(actor.getState() == Actor.State.Deleted){
                iterator.remove();
            }
        }
    }

    public void render(Canvas canvas){
        if(graphicsManager == null){
            return;
        }

        graphicsManager.render(canvas);
    }

    public void onTouchEvent(MotionEvent motionEvent){
        touchEventManager.onTouchEvent(motionEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dispose(){
        for(Actor actor : actors){
            actor.dispose();
        }

        actors.clear();
        pendingActors.clear();

        graphicsManager.dispose();
        audioManager.dispose();
        touchEventManager.dispose();
    }

    public String getName(){
        return name;
    }

    public GraphicsManager getGraphicsManager(){
        return graphicsManager;
    }

    public AudioManager getAudioManager(){
        return audioManager;
    }

    public TouchEventManager getTouchEventManager(){
        return touchEventManager;
    }

    public boolean isInitialized(){
        return initialized;
    }
    public boolean isLoadCompleted(){
        return loadCompleted;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Actor getActor(String name){
        return actors.stream().filter(target -> target.getName() == name).findFirst().orElse(null);
    }

}
