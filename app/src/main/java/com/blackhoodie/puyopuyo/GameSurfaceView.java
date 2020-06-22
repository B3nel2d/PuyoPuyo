package com.blackhoodie.puyopuyo;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private Thread thread;

    public GameSurfaceView(Context context){
        super(context);
        getHolder().addCallback(this);

        Game.createInstance();
        Game.getInstance().setContext(context);
        Game.getInstance().initialize();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder){
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder){
        thread = null;
    }

    @Override
    public void run(){
        while(thread != null){
            try{
                Game.getInstance().run();
            }
            catch(InterruptedException exception){
                exception.printStackTrace();
            }

            render(getHolder());
        }
    }

    private void render(SurfaceHolder surfaceHolder){
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas == null){
            return;
        }

        Game.getInstance().render(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        Game.getInstance().onTouchEvent(event);

        return true;
    }

}
