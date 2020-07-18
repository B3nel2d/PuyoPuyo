package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * テキストドローアブルのクラス
 */
public class TextComponent extends DrawableComponent{

    /** テキスト */
    private String text;
    /** Paint */
    private Paint paint;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param uiTransformComponent UIトランスフォーム
     * @param drawOrder 描画の順番
     * @param visible 可視状態
     */
    public TextComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder, boolean visible){
        super(owner, 0, uiTransformComponent, drawOrder, visible);

        text = "";

        paint = new Paint();
        paint.setTextSize(10);
        paint.setColor(Color.BLACK);
    }
    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param uiTransformComponent UIトランスフォーム
     * @param drawOrder 描画の順番
     */
    public TextComponent(Actor owner, UITransformComponent uiTransformComponent, int drawOrder){
        this(owner, uiTransformComponent, drawOrder, true);
    }
    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param uiTransformComponent UIトランスフォーム
     */
    public TextComponent(Actor owner, UITransformComponent uiTransformComponent){
        this(owner, uiTransformComponent, 0, true);
    }

    /**
     * 毎フレームの更新処理を行う
     */
    public void update(){

    }

    /**
     * テキストの描画を行う
     * @param canvas 描画に使用するCanvas
     */
    public void draw(Canvas canvas){
        if(text != null && paint != null){
            Vector2D position = uiTransformComponent.getPosition();
            position = position.subtract(paint.measureText(text) / 2.0f, (paint.getFontMetrics().ascent + paint.getFontMetrics().descent) / 2.0f);

            canvas.drawText(text, position.x, position.y, paint);
        }
    }

    /**
     * テキストを取得する
     * @return テキスト
     */
    public String getText() {
        return text;
    }
    /**
     * テキストを設定する
     * @param text 設定する値
     */
    public void setText(String text){
        this.text = text;
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
