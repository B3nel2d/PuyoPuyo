package com.blackhoodie.puyopuyo;

/**
 * テキスト表示用アクター
 */
public class TextActor extends Actor{

    /** UIトランスフォーム */
    private UITransformComponent uiTransformComponent;
    /** テキストコンポーネント */
    private TextComponent textComponent;

    /**
     * コンストラクタ
     * @param owner 所属するレベル
     * @param name アクター名
     */
    public TextActor(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        uiTransformComponent = new UITransformComponent(this);
        textComponent = new TextComponent(this, uiTransformComponent, 0);
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
     * テキストコンポーネントを取得する
     * @return テキストコンポーネント
     */
    public TextComponent getTextComponent(){
        return textComponent;
    }

}
