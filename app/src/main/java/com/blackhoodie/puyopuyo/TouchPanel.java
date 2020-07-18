package com.blackhoodie.puyopuyo;

/**
 * タッチパネル用のアクター
 */
public class TouchPanel extends Actor{

    /** UIトランスフォーム */
    private UITransformComponent uiTransformComponent;
    /** インタラクタブルコンポーネント */
    private InteractableComponent interactableComponent;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param name アクター名
     */
    public TouchPanel(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        interactableComponent = new InteractableComponent(this, uiTransformComponent);
    }

    @Override
    public void update(){

    }

    /**
     * UIトランスフォームを取得する
     * @return UIトランスフォーム
     */
    public UITransformComponent getUiTransformComponent(){
        return uiTransformComponent;
    }

    /**
     * インタラクタブルコンポーネントを取得する
     * @return インタラクタブルコンポーネント
     */
    public InteractableComponent getInteractableComponent(){
        return interactableComponent;
    }

}
