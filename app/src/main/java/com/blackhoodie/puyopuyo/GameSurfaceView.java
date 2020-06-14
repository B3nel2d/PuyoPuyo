package com.blackhoodie.puyopuyo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    Thread thread;

    public GameSurfaceView(Context context){
        super(context);
        getHolder().addCallback(this);
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

            draw(getHolder());
        }
    }

    private void draw(SurfaceHolder surfaceHolder){
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas == null){
            return;
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

}
