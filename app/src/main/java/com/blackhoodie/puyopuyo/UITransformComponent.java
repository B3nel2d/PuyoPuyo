package com.blackhoodie.puyopuyo;

/**
 * UI上のトランスフォーム（位置・回転・拡大縮小）のコンポーネントクラス
 */
public class UITransformComponent extends Component{

    /** サイズ（ピクセル） */
    private Vector2D size;
    /** 位置（ピクセル） */
    private Vector2D position;
    /** 回転（度） */
    private float rotation;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param updateOrder 更新される順番
     */
    public UITransformComponent(Actor owner, int updateOrder){
        super(owner, updateOrder);

        size = new Vector2D(1.0f, 1.0f);
        position = new Vector2D(0.0f, 0.0f);
        rotation = 0.0f;
    }
    /**
     * コンストラクタ
     * @param owner 所属するアクター
     */
    public UITransformComponent(Actor owner){
        this(owner, 0);
    }

    /**
     * 毎フレームの更新処理を行う
     */
    public void update(){

    }

    /**
     * 平行移動する
     * @param translation 移動ベクトル
     */
    public void translate(Vector2D translation){
        position.x += translation.x;
        position.y += translation.y;
    }
    /**
     * 平行移動する
     * @param x 平行方向の移動距離
     * @param y 垂直方向の移動距離
     */
    public void translate(float x, float y){
        translate(new Vector2D(x, y));
    }

    /**
     * サイズを取得する
     * @return サイズ
     */
    public Vector2D getSize(){
        return size;
    }
    /**
     * サイズを設定する
     * @param size 設定する値
     */
    public void setSize(Vector2D size){
        this.size = size;

        if(size.x < 0){
            this.size.x = 0;
        }
        if(size.y < 0){
            this.size.y = 0;
        }
    }
    /**
     * サイズを設定する
     * @param x 設定する値のx成分
     * @param y 設定する値のy成分
     */
    public void setSize(float x, float y){
        setSize(new Vector2D(x, y));
    }

    /**
     * 位置を取得する
     * @return 位置
     */
    public Vector2D getPosition(){
        return position;
    }
    /**
     * 位置を設定する
     * @param position 設定する値
     */
    public void setPosition(Vector2D position){
        this.position = position;
    }
    /**
     * 位置を設定する
     * @param x 設定する値のx成分
     * @param y 設定する値のy成分
     */
    public void setPosition(float x, float y){
        setPosition(new Vector2D(x, y));
    }

    /**
     * 回転を取得する
     * @return 回転
     */
    public float getRotation(){
        return rotation;
    }
    /**
     * 回転を設定する
     * @param rotation 設定する値
     */
    public void setRotation(float rotation){
        this.rotation = rotation;

        while(rotation < 0){
            this.rotation += 360;
        }
        while(360 < rotation){
            this.rotation -= 360;
        }
    }

}
