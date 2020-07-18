package com.blackhoodie.puyopuyo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 画像のドローアブルコンポーネント
 */
public class ImageComponent extends DrawableComponent{

    /** ビットマップ */
    private Bitmap bitmap;
    /** Paint */
    private Paint paint;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param uiTransformComponent UIトランスフォーム
     * @param drawOrder 描画の順番
     * @param visible 可視状態
     */
    public ImageComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder, boolean visible){
        super(owner, 0, uiTransformComponent, drawOrder, visible);
    }
    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param uiTransformComponent UIトランスフォーム
     * @param drawOrder 描画の順番
     */
    public ImageComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder){
        this(owner, uiTransformComponent, drawOrder, true);
    }
    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param uiTransformComponent UIトランスフォーム
     */
    public ImageComponent(Actor owner, UITransformComponent uiTransformComponent){
        this(owner, uiTransformComponent, 0, true);
    }

    /**
     * 毎フレームの更新処理を行う
     */
    public void update(){

    }

    /**
     * ビットマップの描画を行う
     * @param canvas 描画に使用するCanvas
     */
    public void draw(Canvas canvas){
        if(bitmap == null){
            return;
        }
        if(paint == null){
            paint = new Paint();
        }

        Matrix matrix = new Matrix();
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)uiTransformComponent.getSize().x, (int)uiTransformComponent.getSize().y, true);
        matrix.setRotate(uiTransformComponent.getRotation(), bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        matrix.postTranslate(uiTransformComponent.getPosition().x - bitmap.getWidth() / 2, uiTransformComponent.getPosition().y - bitmap.getHeight() / 2);

        canvas.drawBitmap(bitmap, matrix, paint);
    }

    /**
     * ビットマップを取得する
     * @return ビットマップ
     */
    public Bitmap getBitmap(){
        return bitmap;
    }
    /**
     * ビットマップを設定する
     * @param bitmap 設定する値
     */
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    /**
     * Paintを取得する
     * @return Paint
     */
    public Paint getPaint(){
        return paint;
    }

    /**
     * Paintを設定する
     * @param paint 設定する値
     */
    public void setPaint(Paint paint){
        this.paint = paint;
    }

}
