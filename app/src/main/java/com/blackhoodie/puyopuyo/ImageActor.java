package com.blackhoodie.puyopuyo;

/**
 * 画像表示用のアクタークラス
 */
public class ImageActor extends Actor{

    /** UIトランスフォーム */
    private UITransformComponent uiTransformComponent;
    /** イメージコンポーネント */
    private ImageComponent imageComponent;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param name アクター名
     */
    public ImageActor(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        imageComponent = new ImageComponent(this, uiTransformComponent, 0);
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

}
