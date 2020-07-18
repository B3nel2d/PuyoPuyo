package com.blackhoodie.puyopuyo;

import android.graphics.Canvas;

/**
 * ドローアブル（描画コンテンツ）コンポーネントの抽象基底クラス
 */
abstract class DrawableComponent extends Component{

    /** UIトランスフォーム */
    protected UITransformComponent uiTransformComponent;
    /** 描画の順番 */
    protected int drawOrder;
    /** 可視状態であるか */
    protected boolean visible;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param updateOrder 更新の順番
     * @param uiTransformComponent UIトランスフォーム
     * @param drawOrder 描画の順番
     * @param visible 可視状態
     */
    public DrawableComponent(Actor owner, int updateOrder, UITransformComponent uiTransformComponent, int drawOrder, boolean visible){
        super(owner, updateOrder);

        this.uiTransformComponent = uiTransformComponent;
        this.drawOrder = drawOrder;
        this.visible = visible;

        owner.getOwner().addDrawable(this);
    }

    /**
     * 描画を行う
     * @param canvas 描画に使用するCanvas
     */
    abstract void draw(Canvas canvas);

    /**
     * 描画の順番を取得する
     * @return 描画の順番
     */
    public int getDrawOrder(){
        return drawOrder;
    }
    /**
     * 描画の順番を設定する
     * @param drawOrder 設定する値
     */
    public void setDrawOrder(int drawOrder){
        this.drawOrder = drawOrder;
        owner.getOwner().getGraphicsManager().sortDrawables();
    }

    /**
     * 可視状態であるか取得する
     * @return 可視状態であるか
     */
    public boolean isVisible(){
        return visible;
    }
    /**
     * 可視状態を設定する
     * @param value 設定する値
     */
    public void setVisible(boolean value){
        this.visible = value;
    }

}
