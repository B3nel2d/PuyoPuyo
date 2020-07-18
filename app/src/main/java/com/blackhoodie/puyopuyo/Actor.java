package com.blackhoodie.puyopuyo;

import android.os.Build;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.RequiresApi;

/**
 * アクター（ゲームオブジェクト）の抽象基底クラス
 */
abstract class Actor{

    /** アクターの状態（アクティブであれば更新される） */
    public enum State{
        Active,
        Inactive,
        Deleted
    }

    /** 所属するレベル */
    protected Level owner;

    /** 名前 */
    protected String name;
    /** 状態 */
    protected State state;

    /** 所有するコンポーネントのリスト */
    protected List<Component> components;

    /** 子アクターのリスト */
    protected List<Actor> children;

    /**
     * コンストラクタ
     * @param owner 所属するレベル
     * @param name 名前
     * @param state 状態
     */
    public Actor(Level owner, String name, State state){
        this.owner = owner;
        owner.addActor(this);

        this.name = name;
        this.state = state;

        components = new CopyOnWriteArrayList<Component>();

        children = new CopyOnWriteArrayList<Actor>();

        initialize();
    }
    /**
     * コンストラクタ
     * @param owner 所属するレベル
     * @param name 名前
     */
    public Actor(Level owner, String name){
        this(owner, name, State.Active);
    }

    /**
     * 初期化処理を行う
     */
    abstract void initialize();

    /**
     * コンポーネントを追加する
     * @param component 追加するコンポーネント
     */
    public void addComponent(Component component){
        components.add(component);
        Collections.sort(components, (component1, component2) -> component1.getUpdateOrder() - component2.getUpdateOrder());
    }
    /**
     * コンポーネントを削除する
     * @param component 削除するコンポーネント
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeComponent(Component component){
        components.removeIf(target -> target == component);
    }

    /**
     * 子アクターを追加する
     * @param actor 追加する子アクター
     */
    public void addChild(Actor actor){
        if(!children.contains(actor)){
            children.add(actor);
        }
    }
    /**
     * 子アクターを削除する
     * @param actor 削除する子アクター
     */
    public void removeChild(Actor actor){
        if(children.contains(actor)){
            children.remove(actor);
        }
    }

    /**
     * 毎フレームの更新処理を行う
     */
    abstract void update();

    /**
     * 各コンポーネントを更新する
     */
    public void updateComponents(){
        if(state != State.Active){
            return;
        }

        for(Component component : components){
            component.update();
        }
    }

    /**
     * 自身を削除する
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delete(){
        state = State.Deleted;
        dispose();

        for(Actor actor : children){
            actor.delete();
        }
    }

    /**
     * リソースを開放する
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dispose(){
        for(Component component : components){
            if(component instanceof DrawableComponent){
                owner.getGraphicsManager().removeDrawable((DrawableComponent)component);
            }

            if(component instanceof InteractableComponent){
                owner.getTouchEventManager().removeInteractable((InteractableComponent)component);
            }
        }

        components.clear();
        children.clear();
    }

    /**
     * 所属するレベルを取得する
     * @return 所属するレベル
     */
    public Level getOwner(){
        return owner;
    }

    /**
     * アクター名を取得する
     * @return アクター名
     */
    public String getName(){
        return name;
    }
    /**
     * アクター名を設定する
     * @param name 設定する値
     */
    public void setName(String name){
        this.name = name;
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

        for(Actor actor : children){
            actor.setState(state);
        }
    }

}
