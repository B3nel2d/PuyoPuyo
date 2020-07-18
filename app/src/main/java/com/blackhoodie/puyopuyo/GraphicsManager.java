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

/**
 * レベルの描画処理を担うクラス
 */
public class GraphicsManager{

    /** 所属するレベル */
    private Level owner;

    /** ドローアブルのリスト */
    private List<DrawableComponent> drawables;
    /** 追加待ちのドローアブルのリスト */
    private List<DrawableComponent> pendingDrawables;

    /** ビットマップのリスト */
    private HashMap<String, Bitmap> bitmaps;

    /** 描画処理中であるか */
    private boolean rendering;

    /**
     * コンストラクタ
     * @param owner 所属するレベル
     */
    public GraphicsManager(Level owner){
        this.owner = owner;

        initialize();
    }

    /**
     * 初期化処理を行う
     */
    public void initialize(){
        drawables = new CopyOnWriteArrayList<DrawableComponent>();
        pendingDrawables = new CopyOnWriteArrayList<DrawableComponent>();

        bitmaps = new HashMap<String, Bitmap>();

        rendering = false;
    }

    /**
     * ドローアブルを追加する
     * @param drawable 追加するドローアブル
     */
    public void addDrawable(DrawableComponent drawable){
        if(rendering){
            pendingDrawables.add(drawable);
        }
        else{
            drawables.add(drawable);
            sortDrawables();
        }
    }
    /**
     * ドローアブルを削除する
     * @param drawable 削除するドローアブル
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeDrawable(DrawableComponent drawable){
        drawables.removeIf(target -> target == drawable);
    }
    /**
     * ドローアブルのリストを描画順に並べる
     */
    public void sortDrawables(){
        Collections.sort(drawables, new DrawableComparator());
    }

    /**
     * ビットマップを追加する
     * @param filePath 追加するビットマップのファイルパス（assets内）
     * @param name 追加するビットマップの名前
     */
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
    /**
     * ビットマップを削除する
     * @param name 削除するビットマップの名前
     */
    public void removeBitmap(String name){
        if(!bitmaps.containsKey(name)){
            return;
        }

        bitmaps.remove(name);
    }

    /**
     * 各ドローアブルを描画する
     * @param canvas 描画に使用するCanvas
     */
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

    /**
     * リソースを解放する
     */
    public void dispose(){
        drawables.clear();
        bitmaps.clear();
    }

    /**
     * ビットマップを取得する
     * @param name 取得するビットマップの名前
     * @return 名前に一致するビットマップ
     */
    public Bitmap getBitmap(String name){
        if(!bitmaps.containsKey(name)){
            System.out.println("Could not find bitmap(" + name + ").");
            return null;
        }

        return bitmaps.get(name);
    }

}
