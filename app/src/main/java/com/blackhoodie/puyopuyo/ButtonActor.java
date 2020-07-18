package com.blackhoodie.puyopuyo;

/**
 * ボタン用のアクター
 */
public class ButtonActor extends Actor{

    /** UIトランスフォーム */
    private UITransformComponent uiTransformComponent;
    /** イメージコンポーネント */
    private ImageComponent imageComponent;
    /** インタラクタブルコンポーネント */
    private InteractableComponent interactableComponent;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param name アクター名
     */
    public ButtonActor(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        imageComponent = new ImageComponent(this, uiTransformComponent, 0);
        interactableComponent = new InteractableComponent(this, uiTransformComponent);
    }

    @Override
    public void update(){

    }

    /**
     * UIトランスフォームを取得する
     * @return UIトランスフォーム
     */
    public UITransformComponent getUITransformComponent(){
        return uiTransformComponent;
    }

    /**
     * イメージコンポーネントを取得する
     * @return イメージコンポーネント
     */
    public ImageComponent getImageComponent(){
        return imageComponent;
    }

    /**
     * インタラクタブルコンポーネントを取得する
     * @return インタラクタブルコンポーネント
     */
    public InteractableComponent getInteractableComponent(){
        return interactableComponent;
    }

}
