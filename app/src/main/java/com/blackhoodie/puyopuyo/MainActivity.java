package com.blackhoodie.puyopuyo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * メインアクティビティクラス
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(new GameSurfaceView(this));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Game.getInstance().dispose();
    }

}
