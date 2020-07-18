package com.blackhoodie.puyopuyo;

import android.os.Build;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.function.Consumer;

import androidx.annotation.RequiresApi;

/**
 * インタラクタブル（タッチイベントでインタラクト可能なオブジェクト）のコンポーネントクラス
 */
public class InteractableComponent extends Component{

    /** タッチアクション */
    public enum TouchAction{
        Down,
        LongPress,
        DoubleTap,
        SingleTapUp
    }

    /** UIトランスフォーム */
    private UITransformComponent uiTransform;
    /** インタラクト可能かどうか */
    private boolean interactable;

    /** タッチアクション毎のタスクのハッシュマップ */
    private HashMap<TouchAction, Runnable> tasks;
    /** 各タスクが実行されたかのハッシュマップ */
    private HashMap<TouchAction, Boolean> taskExecuteFlags;
    /** フリック時のタスク */
    private Consumer<FlickState> flickTask;
    /** フリック時タスクが実行されたか */
    private boolean flickTaskExecuted;

    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param updateOrder 更新の順番
     * @param uiTransform UIトランスフォーム
     * @param interactable インタラクト可能かどうか
     */
    public InteractableComponent(Actor owner, int updateOrder, UITransformComponent uiTransform, boolean interactable){
        super(owner, updateOrder);

        this.uiTransform = uiTransform;
        this.interactable = interactable;

        tasks = new HashMap<TouchAction, Runnable>();
        taskExecuteFlags = new HashMap<TouchAction, Boolean>();
        flickTask = null;
        flickTaskExecuted = false;

        owner.getOwner().addInteractable(this);
    }
    /**
     * コンストラクタ
     * @param owner 所属するアクター
     * @param uiTransform UIトランスフォーム
     */
    public InteractableComponent(Actor owner, UITransformComponent uiTransform){
        this(owner, 0, uiTransform, true);
    }

    @Override
    public void update(){
        if(!owner.getOwner().getTouchEventManager().isDowning()){
            resetTaskExecuteFlag(TouchAction.Down);
        }
        if(!owner.getOwner().getTouchEventManager().isLongPressing()){
            resetTaskExecuteFlag(TouchAction.LongPress);
        }
        if(!owner.getOwner().getTouchEventManager().isDoubleTapping()){
            resetTaskExecuteFlag(TouchAction.DoubleTap);
        }
        if(!owner.getOwner().getTouchEventManager().isSingleTappingUp()){
            resetTaskExecuteFlag(TouchAction.SingleTapUp);
        }
        if(!owner.getOwner().getTouchEventManager().isFling()){
            flickTaskExecuted = false;
        }
    }

    /**
     * タスク（タッチイベント時に実行する関数）を追加する
     * @param touchAction 対応するタッチアクション
     * @param task 追加するタスク
     */
    public void addTask(TouchAction touchAction, Runnable task){
        tasks.put(touchAction, task);
        taskExecuteFlags.put(touchAction, false);
    }
    /**
     * タスクを削除する
     * @param touchAction 削除するタスクに対応するタッチアクション
     */
    public void removeTask(TouchAction touchAction){
        if(tasks.containsKey(touchAction)){
            tasks.remove(touchAction);
            taskExecuteFlags.remove(touchAction);
        }
    }
    /**
     * 全てのタスクを削除する
     */
    public void clearTasks(){
        tasks.clear();
    }

    /**
     * フリック時のタスクを設定する
     * @param task 設定するタスク
     */
    public void setFlickTask(Consumer<FlickState> task){
        flickTask = task;
        flickTaskExecuted = false;
    }

    /**
     * 領域内押下時のイベント
     * @param motionEvent タッチ時のMotionEvent
     */
    public void onDown(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.Down)){
            taskExecuteFlags.put(TouchAction.Down, true);
            tasks.get(TouchAction.Down).run();
        }
    }

    /**
     * 領域内長押し時のイベント
     * @param motionEvent タッチ時のMotionEvent
     */
    public void onLongPress(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.LongPress)){
            taskExecuteFlags.put(TouchAction.LongPress, true);
            tasks.get(TouchAction.LongPress).run();
        }
    }

    /**
     * 領域内ダブルタップ時のイベント
     * @param motionEvent タッチ時のMotionEvent
     */
    public void onDoubleTap(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.DoubleTap)){
            taskExecuteFlags.put(TouchAction.DoubleTap, true);
            tasks.get(TouchAction.DoubleTap).run();
        }
    }

    /**
     * 領域内タップアップ時のイベント
     * @param motionEvent タッチ時のMotionEvent
     */
    public void onSingleTapUp(MotionEvent motionEvent){
        if(!interactable){
            return;
        }

        if(motionEvent.getX() < uiTransform.getPosition().x - uiTransform.getSize().x / 2.0f || uiTransform.getPosition().x + uiTransform.getSize().x / 2.0f < motionEvent.getX()){
            return;
        }
        if(motionEvent.getY() < uiTransform.getPosition().y - uiTransform.getSize().y / 2.0f || uiTransform.getPosition().y + uiTransform.getSize().y / 2.0f < motionEvent.getY()){
            return;
        }

        if(tasks.containsKey(TouchAction.SingleTapUp)){
            taskExecuteFlags.put(TouchAction.SingleTapUp, true);
            tasks.get(TouchAction.SingleTapUp).run();
        }
    }

    /**
     * 画面フリック時のイベント
     * @param firstMotionEvent フリックの最初のMotionEvent
     * @param lastMotionEvent フリックの最後のMotionEvent
     * @param horizontalVelocity 平行方向の加速度
     * @param verticalVelocity 垂直方向の加速度
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onFling(MotionEvent firstMotionEvent, MotionEvent lastMotionEvent, float horizontalVelocity, float verticalVelocity){
        if(!interactable || flickTask == null){
            return;
        }

        flickTaskExecuted = true;
        flickTask.accept(new FlickState(firstMotionEvent, lastMotionEvent, horizontalVelocity, verticalVelocity));
    }

    /**
     * インタラクト可能かどうか取得する
     * @return インタラクト可能かどうか
     */
    public boolean isInteractable(){
        return interactable;
    }
    /**
     * インタラクト可能か設定する
     * @param value 設定する値
     */
    public void setInteractable(boolean value){
        this.interactable = value;
    }

    /**
     * タスクが実行済みが取得する
     * @param touchAction タスクに対応したタッチアクション
     * @return タスクが実行済みか
     */
    public boolean isTaskExecuted(TouchAction touchAction){
        if(!tasks.containsKey(touchAction)){
            return false;
        }

        return taskExecuteFlags.get(touchAction);
    }
    /**
     * タスクの実行済みフラグをリセットする
     * @param touchAction タスクに対応したタッチアクション
     */
    public void resetTaskExecuteFlag(TouchAction touchAction){
        if(!tasks.containsKey(touchAction)){
            return;
        }

        taskExecuteFlags.put(touchAction, false);
    }

    /**
     * フリック時タスクが実行済みかどうか取得する
     * @return フリック時タスクが実行済みか
     */
    public boolean isFlickTaskExecuted(){
        return flickTaskExecuted;
    }

}
