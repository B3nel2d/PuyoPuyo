package com.blackhoodie.puyopuyo;

/**
 * コンポーネント（アクターの機能）の抽象基底クラス
 */
abstract class Component{

    /** コンポーネントの状態（アクティブであれば更新される） */
    public enum State{
        Active,
        Inactive
    }

    /** 所属するアクター */
    protected Actor owner;
    /** 状態 */
    protected State state;
    /** 更新の順番 */
    protected int updateOrder;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param updateOrder 更新の順番
     */
    public Component(Actor owner, int updateOrder){
        this.owner = owner;
        owner.addComponent(this);

        state = State.Active;
        this.updateOrder = updateOrder;
    }
    /**
     * コンストラクタ
     * @param owner 所属するアクター
     */
    public Component(Actor owner){
        this(owner, 0);
    }

    /**
     * 毎フレームの更新処理を行う
     */
    abstract void update();

    /**
     * 所属するアクターを取得する
     * @return 所属するアクター
     */
    public Actor getOwner(){
        return owner;
    }

    /**
     * 状態を取得する
     * @return 状態
     */
    public State getState(){
        return state;
    }
    /**
     * 状態を設定する
     * @param state 設定する値
     */
    public void setState(State state){
        this.state = state;
    }

    /**
     * 更新の順番を取得する
     * @return 更新の順番
     */
    public int getUpdateOrder(){
        return updateOrder;
    }

}
