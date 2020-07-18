package com.blackhoodie.puyopuyo;

import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * 「ぷよ」を定義するクラス
 */
public class Puyo extends Actor{

    /** ぷよの色 */
    public enum Color{
        Red,
        Green,
        Blue,
        Yellow,
        Magenta,
    }

    /** 色 */
    private Color color;

    /** 自由落下速度（ピクセル/フレーム） */
    private float dropSpeed;
    /** 自由落下加速度 */
    private final float dropAcceleration = 3.0f;
    /** 操作中の落下速度（ピクセル/フレーム） */
    private final float fallSpeed = 5.0f;

    /** 固定されているか */
    private boolean fixed;

    /** サイズ */
    public static final Vector2D size = new Vector2D(125, 125);

    /** UIトランスフォーム */
    private UITransformComponent uiTransformComponent;
    /** イメージコンポーネント */
    private ImageComponent imageComponent;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param name アクター名
     */
    public Puyo(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        color = null;
        dropSpeed = 0.0f;
        fixed = false;

        uiTransformComponent = new UITransformComponent(this);
        uiTransformComponent.setSize(size);

        imageComponent = new ImageComponent(this, uiTransformComponent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(){
        drop();
        fall();
        ground();
    }

    /**
     * 自由落下する
     */
    private void drop(){
        if(fixed || ((GameLevel)owner).getCurrentPhase() != GameLevel.Phase.Drop){
            return;
        }

        dropSpeed += dropAcceleration;
        uiTransformComponent.translate(0, dropSpeed);
    }

    /**
     * 落下する（操作中）
     */
    private void fall(){
        if(fixed || ((GameLevel)owner).getCurrentPhase() != GameLevel.Phase.Control){
            return;
        }

        uiTransformComponent.translate(0, fallSpeed);
    }

    /**
     * 着地する
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ground(){
        if(fixed){
            return;
        }

        int row = (int)((GameLevel)owner).getCoordinateFromPosition(uiTransformComponent.getPosition()).x;
        int groundHeight = GameLevel.boardColumnCount - 1;

        for(int column = GameLevel.boardColumnCount - 1; -1 <= column; column--){
            if(((GameLevel)owner).getPuyo(row, column) == null){
                groundHeight = column;
                break;
            }
        }

        if(((GameLevel)owner).getPositionFromCoordinate(row, groundHeight).y <= uiTransformComponent.getPosition().y){
            if(0 <= groundHeight){
                uiTransformComponent.setPosition(((GameLevel)owner).getPositionFromCoordinate(row, groundHeight));
                ((GameLevel)owner).setPuyo(row, groundHeight, this);

                fixed = true;

                owner.getAudioManager().playAudio("Puyo Ground");
            }
            else{
                ((GameLevel)owner).erasePuyo(this);
            }

            resetDropSpeed();
        }
    }

    /**
     * 色を取得する
     * @return 色
     */
    public Color getColor(){
        return color;
    }
    /**
     * 色を設定する
     * @param color 設定する値
     */
    public void setColor(Color color){
        this.color = color;
        imageComponent.setBitmap(owner.getGraphicsManager().getBitmap(color + " Puyo"));
    }

    /**
     * 自由落下速度をリセットする
     */
    public void resetDropSpeed(){
        dropSpeed = 0.0f;
    }

    /**
     * 固定されているかを取得する
     * @return 固定されているか
     */
    public boolean isFixed(){
        return fixed;
    }
    /**
     * 固定されているかを設定する
     * @param value 設定する値
     */
    public void setFixed(boolean value){
        fixed = value;
    }

    /**
     * UIトランスフォームを取得する
     * @return UIトランスフォーム
     */
    public UITransformComponent getUiTransformComponent(){
        return uiTransformComponent;
    }

    /**
     * イメージコンポーネントを取得する
     * @return イメージコンポーネント
     */
    public ImageComponent getImageComponent(){
        return imageComponent;
    }

}
