package com.blackhoodie.puyopuyo;

import android.graphics.Color;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.RequiresApi;

/**
 * ゲームレベルを定義するクラス
 */
public class GameLevel extends Level{

    /** ゲームの処理段階 */
    public enum Phase{
        Start,
        Control,
        Drop,
        Erase,
        GameOver
    }

    /** 方向 */
    public enum Direction{
        Up,
        Down,
        Left,
        Right
    }

    /** 現在の処理段階 */
    private Phase currentPhase;

    /** 軸ぷよ */
    private Puyo centerPuyo;
    /** 子ぷよ */
    private Puyo movablePuyo;

    /** 軸ぷよから見た子ぷよの方向 */
    private Direction movablePuyoDirection;

    /** 次（とその次）の軸ぷよの色 */
    private Puyo.Color[] nextCenterPuyoColors;
    /** 次（とその次）の子ぷよの色 */
    private Puyo.Color[] nextMovablePuyoColors;

    /** ぷよのリスト */
    private List<Puyo> puyos;

    /** フィールド */
    private Puyo[][] board;
    /** フィールドの横の単位 */
    public static final int boardRowCount = 6;
    /** フィールドの縦の単位 */
    public static final int boardColumnCount = 13;
    /** フィールドのサイズ */
    public static final Vector2D boardSize = new Vector2D(Puyo.size.x * boardRowCount, Puyo.size.y * boardColumnCount);

    /** 繋がったぷよが消える個数 */
    private final int puyoCountToErase = 4;
    /** ゲームオーバーになるフィールド座標 */
    private final Vector2D gameOverCoordinate = new Vector2D(2, 1);
    /** 最小のフリック加速度 */
    private final float minimumFlickVelocity = 10.0f;

    /** スコア */
    private int score;
    /** スコアの最大値 */
    private final int scoreLimit = 99999999;
    /** 連鎖数 */
    private int chainCount;

    /** スタートのタイマー */
    private float startTimer;
    /** スタートまでの時間（秒） */
    private final float timeToStart = 3.0f;

    /** ぷよ消去のタイマー */
    private float eraseTimer;
    /** ぷよ消去までの時間（秒） */
    private final float timeToErase = 1.25f;

    /** フィールド画像 */
    private ImageActor boardImage;
    /** 背景画像 */
    private ImageActor backgroundImage;
    /** カバー（フィールド13段目を隠す為）画像 */
    private ImageActor coverImage;

    /** スコアテキスト */
    private TextActor scoreText;
    /** 次のぷよのサインテキスト */
    private TextActor nextPuyoSignText;
    /** 次（とその次）の軸ぷよの画像 */
    private ImageActor[] nextCenterPuyoImages;
    /** 次（とその次）の子ぷよの画像 */
    private ImageActor[] nextMovablePuyoImages;
    /** スタートのカウントダウンテキスト */
    private TextActor startTimerCountDownText;

    /** ポップアップウィンドウ */
    private ImageActor popupWindow;
    /** ゲームオーバーのサインテキスト */
    private TextActor gameOverSignText;
    /** 注意書きのテキスト */
    private TextActor noticeText;

    /** メインタッチパネル */
    private TouchPanel mainPanel;
    /** 左タッチパネル */
    private TouchPanel leftPanel;
    /** 右タッチパネル */
    private TouchPanel rightPanel;

    /**
     * コンストラクタ
     * @param name レベル名
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameLevel(String name){
        super(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initializeActors(){
        currentPhase = Phase.Start;

        centerPuyo = null;
        movablePuyo = null;

        movablePuyoDirection = null;

        nextCenterPuyoColors = new Puyo.Color[2];
        nextMovablePuyoColors = new Puyo.Color[2];

        puyos = new ArrayList<Puyo>();
        board = new Puyo[boardRowCount][boardColumnCount];

        score = 0;
        chainCount = 0;

        startTimer = timeToStart;
        eraseTimer = timeToErase;

        nextCenterPuyoImages = new ImageActor[2];
        nextMovablePuyoImages = new ImageActor[2];

        graphicsManager.addBitmap("images/black-square.png", "Background");
        graphicsManager.addBitmap("images/white-square.png", "Board");
        graphicsManager.addBitmap("images/framed-square.png", "Window");
        graphicsManager.addBitmap("images/red-puyo.png", "Red Puyo");
        graphicsManager.addBitmap("images/green-puyo.png", "Green Puyo");
        graphicsManager.addBitmap("images/blue-puyo.png", "Blue Puyo");
        graphicsManager.addBitmap("images/magenta-puyo.png", "Magenta Puyo");
        graphicsManager.addBitmap("images/yellow-puyo.png", "Yellow Puyo");

        audioManager.addAudio("audios/game-start.wav", "Game Start");
        audioManager.addAudio("audios/puyo-rotate.wav", "Puyo Rotate");
        audioManager.addAudio("audios/puyo-ground.wav", "Puyo Ground");
        audioManager.addAudio("audios/puyo-erase.wav", "Puyo Erase");
        audioManager.addAudio("audios/game-over.wav", "Game Over");

        backgroundImage = new ImageActor(this, "Background Image");
        backgroundImage.getUITransformComponent().setSize(Game.getInstance().getScreenSize());
        backgroundImage.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2.0f, Game.getInstance().getScreenSize().y / 2.0f);
        backgroundImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Background"));
        backgroundImage.getImageComponent().setDrawOrder(0);

        boardImage = new ImageActor(this, "Board Image");
        boardImage.getUITransformComponent().setSize(boardSize);
        boardImage.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2.0f, Game.getInstance().getScreenSize().y / 2.0f - 50);
        boardImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Board"));
        boardImage.getImageComponent().setDrawOrder(1);

        coverImage = new ImageActor(this, "Cover Image");
        coverImage.getUITransformComponent().setSize(boardSize.x, Puyo.size.y * 2);
        coverImage.getUITransformComponent().setPosition(boardImage.getUITransformComponent().getPosition().x, boardImage.getUITransformComponent().getPosition().y - boardImage.getUITransformComponent().getSize().y / 2.0f);
        coverImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Background"));
        coverImage.getImageComponent().setDrawOrder(3);

        scoreText = new TextActor(this, "Score Text");
        scoreText.getUITransformComponent().setPosition(boardImage.getUITransformComponent().getPosition().x, boardImage.getUITransformComponent().getPosition().y + boardSize.y / 2.0f + 30);
        scoreText.getTextComponent().setText("SCORE: " + score);
        scoreText.getTextComponent().getPaint().setTextSize(50);
        scoreText.getTextComponent().getPaint().setColor(Color.WHITE);
        scoreText.getTextComponent().setDrawOrder(1);

        nextPuyoSignText = new TextActor(this, "Next Puyo Sign Text");
        nextPuyoSignText.getUITransformComponent().setPosition(990, 250);
        nextPuyoSignText.getTextComponent().setText("NEXT");
        nextPuyoSignText.getTextComponent().getPaint().setTextSize(50);
        nextPuyoSignText.getTextComponent().getPaint().setColor(Color.WHITE);
        nextPuyoSignText.getTextComponent().setDrawOrder(1);

        nextCenterPuyoImages[1] = new ImageActor(this, "Next Center Puyo Image 2");
        nextCenterPuyoImages[1].getUITransformComponent().setSize(Puyo.size);
        nextCenterPuyoImages[1].getUITransformComponent().setPosition(nextPuyoSignText.getUITransformComponent().getPosition().add(0, 100 + Puyo.size.y));
        nextCenterPuyoImages[1].getImageComponent().setDrawOrder(1);

        nextMovablePuyoImages[1] = new ImageActor(this, "Next Movable Puyo Image 2");
        nextMovablePuyoImages[1].getUITransformComponent().setSize(Puyo.size);
        nextMovablePuyoImages[1].getUITransformComponent().setPosition(nextPuyoSignText.getUITransformComponent().getPosition().add(0, 100));
        nextMovablePuyoImages[1].getImageComponent().setDrawOrder(1);

        nextCenterPuyoImages[0] = new ImageActor(this, "Next Center Puyo Image 1");
        nextCenterPuyoImages[0].getUITransformComponent().setSize(Puyo.size);
        nextCenterPuyoImages[0].getUITransformComponent().setPosition(nextCenterPuyoImages[1].getUITransformComponent().getPosition().add(100, 300));
        nextCenterPuyoImages[0].getImageComponent().setDrawOrder(1);

        nextMovablePuyoImages[0] = new ImageActor(this, "Next Movable Puyo Image 1");
        nextMovablePuyoImages[0].getUITransformComponent().setSize(Puyo.size);
        nextMovablePuyoImages[0].getUITransformComponent().setPosition(nextCenterPuyoImages[0].getUITransformComponent().getPosition().add(0, -Puyo.size.y));
        nextMovablePuyoImages[0].getImageComponent().setDrawOrder(1);

        startTimerCountDownText = new TextActor(this, "Start Timer Count Down Text");
        startTimerCountDownText.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f).subtract(0, 500));
        startTimerCountDownText.getTextComponent().getPaint().setTextSize(100);
        startTimerCountDownText.getTextComponent().getPaint().setColor(Color.BLACK);
        startTimerCountDownText.getTextComponent().setDrawOrder(4);

        popupWindow = new ImageActor(this, "Popup Window");
        popupWindow.getUITransformComponent().setSize(800, 500);
        popupWindow.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f));
        popupWindow.getImageComponent().setBitmap(graphicsManager.getBitmap("Window"));
        popupWindow.getImageComponent().setDrawOrder(4);

        gameOverSignText = new TextActor(this, "Game Over Sign Text");
        gameOverSignText.getUITransformComponent().setPosition(popupWindow.getUITransformComponent().getPosition().subtract(0, 100));
        gameOverSignText.getTextComponent().setText("-GAME OVER-");
        gameOverSignText.getTextComponent().getPaint().setTextSize(100);
        gameOverSignText.getTextComponent().getPaint().setColor(Color.BLACK);
        gameOverSignText.getTextComponent().setDrawOrder(5);

        noticeText = new TextActor(this, "Notice Text");
        noticeText.getUITransformComponent().setPosition(popupWindow.getUITransformComponent().getPosition().add(0, 100));
        noticeText.getTextComponent().setText("Tap screen to return to title");
        noticeText.getTextComponent().getPaint().setTextSize(50);
        noticeText.getTextComponent().getPaint().setColor(Color.BLACK);
        noticeText.getTextComponent().setDrawOrder(5);

        popupWindow.addChild(gameOverSignText);
        popupWindow.addChild(noticeText);
        popupWindow.setState(Actor.State.Inactive);

        mainPanel = new TouchPanel(this, "Touch Panel");
        mainPanel.getUiTransformComponent().setSize(Game.getInstance().getScreenSize());
        mainPanel.getUiTransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f));
        mainPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.LongPress, this::dropPuyo);
        mainPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.Down, this::goBackToTitle);
        mainPanel.getInteractableComponent().setFlickTask(this::onFling);

        leftPanel = new TouchPanel(this, "Left Panel");
        leftPanel.getUiTransformComponent().setSize(Game.getInstance().getScreenSize().multiply(0.5f, 1.0f));
        leftPanel.getUiTransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f).subtract(Game.getInstance().getScreenSize().multiply(0.25f).x, 0));
        leftPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.SingleTapUp, this::rotatePuyoCounterClockwise);

        rightPanel = new TouchPanel(this, "Right Panel");
        rightPanel.getUiTransformComponent().setSize(Game.getInstance().getScreenSize().multiply(0.5f, 1.0f));
        rightPanel.getUiTransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f).add(Game.getInstance().getScreenSize().multiply(0.25f).x, 0));
        rightPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.SingleTapUp, this::rotatePuyoClockwise);

        updateNextPuyos();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(){
        switch(currentPhase){
            case Start:
                startTimer -= Game.getInstance().getFrameDeltaTime();
                if(0 <= startTimer){
                    updateStartTimerCountDownText(String.valueOf((int)Math.ceil(startTimer)));
                }
                else{
                    startTimerCountDownText.setState(Actor.State.Inactive);
                    startGame();
                }

                break;
            case Control:
                if(centerPuyo.isFixed() || movablePuyo.isFixed()){
                    dropAllPuyos();
                }

                break;
            case Drop:
                if(areAllPuyosFixed()){
                    if(isGameOver()){
                        finishGame();
                    }
                    else{
                        eraseTimer -= Game.getInstance().getFrameDeltaTime();
                        if(eraseTimer < 0){
                            eraseAllPuyos();
                            eraseTimer = timeToErase;
                        }
                    }
                }

                break;
        }
    }

    /**
     * スタートのカウントダウンテキストの更新
     * @param text 新しいテキスト
     */
    private void updateStartTimerCountDownText(String text){
        startTimerCountDownText.getTextComponent().setText(text);
    }

    /**
     * 次（とその次）のぷよを更新する
     */
    private void updateNextPuyos(){
        if(nextCenterPuyoColors[0] == null){
            nextCenterPuyoColors[1] = Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)];
        }
        else{
            nextCenterPuyoColors[1] = nextCenterPuyoColors[0];
        }
        nextCenterPuyoImages[1].getImageComponent().setBitmap(graphicsManager.getBitmap(nextCenterPuyoColors[1] + " Puyo"));

        if(nextMovablePuyoColors[0] == null){
            nextMovablePuyoColors[1] = Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)];
        }
        else{
            nextMovablePuyoColors[1] = nextMovablePuyoColors[0];
        }
        nextMovablePuyoImages[1].getImageComponent().setBitmap(graphicsManager.getBitmap(nextMovablePuyoColors[1] + " Puyo"));

        nextCenterPuyoColors[0] = Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)];
        nextCenterPuyoImages[0].getImageComponent().setBitmap(graphicsManager.getBitmap(nextCenterPuyoColors[0] + " Puyo"));

        nextMovablePuyoColors[0] = Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)];
        nextMovablePuyoImages[0].getImageComponent().setBitmap(graphicsManager.getBitmap(nextMovablePuyoColors[0] + " Puyo"));
    }

    /**
     * ゲームを開始する
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startGame(){
        spawnPuyo();
        audioManager.playAudio("Game Start");
    }

    /**
     * 操作ぷよを出現させる
     */
    private void spawnPuyo(){
        currentPhase = Phase.Control;

        centerPuyo = new Puyo(this, "Puyo");
        puyos.add(centerPuyo);
        centerPuyo.getUiTransformComponent().setPosition(getPositionFromCoordinate(2, 0));
        centerPuyo.setColor(nextCenterPuyoColors[1]);
        centerPuyo.getImageComponent().setDrawOrder(2);

        movablePuyo = new Puyo(this, "Puyo");
        puyos.add(movablePuyo);
        movablePuyo.getUiTransformComponent().setPosition(getPositionFromCoordinate(2, -1));
        movablePuyo.setColor(nextMovablePuyoColors[1]);
        movablePuyo.getImageComponent().setDrawOrder(2);

        movablePuyoDirection = Direction.Up;

        updateNextPuyos();
    }

    /**
     * 全てのぷよを自由落下させる
     */
    public void dropAllPuyos(){
        currentPhase = Phase.Drop;

        for(int row = 0; row < boardRowCount; row++){
            boolean aboveSpace = false;

            for(int column = boardColumnCount - 1; 0 <= column; column--){
                if(getPuyo(row, column) == null){
                    aboveSpace = true;
                }
                else{
                    if(aboveSpace){
                        getPuyo(row, column).setFixed(false);
                        setPuyo(row, column, null);
                    }
                }
            }
        }
    }

    /**
     * 全てのぷよが固定されているか取得する
     * @return 全てのぷよが固定されているか
     */
    private boolean areAllPuyosFixed(){
        for(Puyo puyo : puyos){
            if(!puyo.isFixed()){
                return false;
            }
        }

        return true;
    }

    /**
     * ゲームオーバー条件を満たしているか取得する
     * @return ゲームオーバー条件を満たしているか
     */
    private boolean isGameOver(){
        if(getPuyo(gameOverCoordinate) != null){
            return true;
        }

        return false;
    }

    /**
     * ゲームを終了する
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void finishGame(){
        currentPhase = Phase.GameOver;

        popupWindow.setState(Actor.State.Active);
        audioManager.playAudio("Game Over");
    }

    /**
     * タイトルレベルへ戻る
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void goBackToTitle(){
        if(currentPhase != Phase.GameOver){
            return;
        }

        Game.getInstance().loadLevel("Title Level");
    }

    /**
     * 全ての消去対象のぷよを消去する
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void eraseAllPuyos(){
        currentPhase = Phase.Erase;

        List<Puyo> puyosToErase = new ArrayList<Puyo>();
        boolean erased = false;

        int connectBonus = 0;

        for(int row = 0; row < boardRowCount; row++){
            for(int column = 0; column < boardColumnCount; column++){
                ArrayList<Puyo> connectedPuyos = new ArrayList<Puyo>();

                Puyo puyo = getPuyo(row, column);
                if(puyo != null && !puyosToErase.contains(puyo)){
                    getConnectedPuyos(puyo, connectedPuyos);

                    if(puyoCountToErase <= connectedPuyos.size()){
                        for(Puyo connectedPuyo : connectedPuyos){
                            if(!puyosToErase.contains(connectedPuyo)){
                                puyosToErase.add(connectedPuyo);
                            }
                        }

                        connectBonus += calculateConnectBonus(connectedPuyos.size());

                        erased = true;
                    }
                }
            }
        }

        if(erased){
            int colorCount = 0;
            for(Puyo.Color color : Puyo.Color.values()){
                if(puyosToErase.stream().anyMatch(target -> target.getColor() == color)){
                    colorCount++;
                }
            }

            chainCount++;
            addScore(puyosToErase.size(), calculateChainBonus(chainCount), connectBonus, calculateColorBonus(colorCount));

            for(Puyo puyo : puyosToErase){
                erasePuyo(puyo);
            }

            audioManager.playAudio("Puyo Erase");

            dropAllPuyos();
        }
        else{
            chainCount = 0;
            spawnPuyo();
        }
    }

    /**
     * ぷよを消去する
     * @param puyo 消去するぷよ
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void erasePuyo(Puyo puyo){
        if(puyo == null){
            return;
        }

        puyos.remove(puyo);
        setPuyo(getCoordinateFromPosition(puyo.getUiTransformComponent().getPosition()), null);
        puyo.delete();
    }

    /**
     * 同じ色のぷよと隣接しているぷよを取得する（引数のリストに保存する）
     * @param puyo 対象のぷよ
     * @param connectedPuyos 取得したぷよを保存するリスト
     */
    private void getConnectedPuyos(Puyo puyo, ArrayList<Puyo> connectedPuyos){
        List<Vector2D> adjacentCoordinates = new ArrayList<Vector2D>();
        Vector2D targetPuyoCoordinate = getCoordinateFromPosition(puyo.getUiTransformComponent().getPosition());

        adjacentCoordinates.add(new Vector2D(targetPuyoCoordinate.x + 1, targetPuyoCoordinate.y));
        adjacentCoordinates.add(new Vector2D(targetPuyoCoordinate.x, targetPuyoCoordinate.y + 1));
        adjacentCoordinates.add(new Vector2D(targetPuyoCoordinate.x - 1, targetPuyoCoordinate.y));
        adjacentCoordinates.add(new Vector2D(targetPuyoCoordinate.x, targetPuyoCoordinate.y - 1));

        for(Vector2D coordinate : adjacentCoordinates){
            Puyo adjacentPuyo = getPuyo(coordinate);
            if(adjacentPuyo != null && adjacentPuyo.getColor() == puyo.getColor()){
                if(!connectedPuyos.contains(puyo)){
                    connectedPuyos.add(puyo);
                }
                if(!connectedPuyos.contains(adjacentPuyo)){
                    getConnectedPuyos(adjacentPuyo, connectedPuyos);
                }
            }
        }
    }

    /**
     * フリック時のイベント
     * @param flickState フリック時の情報
     */
    private void onFling(FlickState flickState){
        Vector2D flickVelocity = new Vector2D(flickState.getHorizontalVelocity(), flickState.getVerticalVelocity());

        if(flickVelocity.getSquaredMagnitude() <= minimumFlickVelocity){
            return;
        }

        if(Math.abs(flickVelocity.y) <= Math.abs(flickVelocity.x)){
            if(flickVelocity.x <= 0){
                movePuyo(Direction.Left);
            }
            else{
                movePuyo(Direction.Right);
            }
        }
    }

    /**
     * 操作中のぷよを水平移動させる
     * @param direction 移動する方向
     */
    private void movePuyo(Direction direction){
        if(currentPhase != Phase.Control){
            return;
        }
        if(centerPuyo == null || movablePuyo == null){
            return;
        }

        switch(direction){
            case Left:
                if(adjacentSpaceExists(centerPuyo, Direction.Left) && adjacentSpaceExists(movablePuyo, Direction.Left)){
                    centerPuyo.getUiTransformComponent().translate(-Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(-Puyo.size.x, 0);
                }

                break;
            case Right:
                if(adjacentSpaceExists(centerPuyo, Direction.Right) && adjacentSpaceExists(movablePuyo, Direction.Right)){
                    centerPuyo.getUiTransformComponent().translate(Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(Puyo.size.x, 0);
                }

                break;
        }
    }

    /**
     * 子ぷよを回転させる（時計回り）
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rotatePuyoClockwise(){
        if(currentPhase != Phase.Control){
            return;
        }
        if(centerPuyo == null || movablePuyo == null){
            return;
        }

        boolean rotated = false;
        switch(movablePuyoDirection){
            case Up:
                if(adjacentSpaceExists(centerPuyo, Direction.Right)){
                    movablePuyo.getUiTransformComponent().translate(Puyo.size.x, Puyo.size.y);

                    movablePuyoDirection = Direction.Right;
                    rotated = true;
                }
                else if(adjacentSpaceExists(centerPuyo, Direction.Left)){
                    centerPuyo.getUiTransformComponent().translate(-Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, Puyo.size.y);

                    movablePuyoDirection = Direction.Right;
                    rotated = true;
                }

                break;
            case Right:
                if(adjacentSpaceExists(centerPuyo, Direction.Down) && adjacentSpaceExists(movablePuyo, Direction.Down)){
                    movablePuyo.getUiTransformComponent().translate(-Puyo.size.x, Puyo.size.y);

                    movablePuyoDirection = Direction.Down;
                    rotated = true;
                }

                break;
            case Down:
                if(adjacentSpaceExists(centerPuyo, Direction.Left) && adjacentSpaceExists(movablePuyo, Direction.Left)){
                    movablePuyo.getUiTransformComponent().translate(-Puyo.size.x, -Puyo.size.y);

                    movablePuyoDirection = Direction.Left;
                    rotated = true;
                }
                else if(adjacentSpaceExists(centerPuyo, Direction.Right)){
                    centerPuyo.getUiTransformComponent().translate(Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, -Puyo.size.y);

                    movablePuyoDirection = Direction.Left;
                    rotated = true;
                }

                break;
            case Left:
                movablePuyo.getUiTransformComponent().translate(Puyo.size.x, -Puyo.size.y);

                movablePuyoDirection = Direction.Up;
                rotated = true;

                break;
        }

        if(rotated){
            audioManager.playAudio("Puyo Rotate");
        }
    }

    /**
     * 子ぷよを回転させる（反時計回り）
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rotatePuyoCounterClockwise(){
        if(currentPhase != Phase.Control){
            return;
        }
        if(centerPuyo == null || movablePuyo == null){
            return;
        }

        boolean rotated = false;
        switch(movablePuyoDirection){
            case Up:
                if(adjacentSpaceExists(centerPuyo, Direction.Left)){
                    movablePuyo.getUiTransformComponent().translate(-Puyo.size.x, Puyo.size.y);

                    movablePuyoDirection = Direction.Left;
                    rotated = true;
                }
                else if(adjacentSpaceExists(centerPuyo, Direction.Right)){
                    centerPuyo.getUiTransformComponent().translate(Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, Puyo.size.y);

                    movablePuyoDirection = Direction.Left;
                    rotated = true;
                }

                break;
            case Left:
                if(adjacentSpaceExists(centerPuyo, Direction.Down) && adjacentSpaceExists(movablePuyo, Direction.Down)){
                    movablePuyo.getUiTransformComponent().translate(Puyo.size.x, Puyo.size.y);

                    movablePuyoDirection = Direction.Down;
                    rotated = true;
                }

                break;
            case Down:
                if(adjacentSpaceExists(centerPuyo, Direction.Right) && adjacentSpaceExists(movablePuyo, Direction.Right)){
                    movablePuyo.getUiTransformComponent().translate(Puyo.size.x, -Puyo.size.y);

                    movablePuyoDirection = Direction.Right;
                    rotated = true;
                }
                else if(adjacentSpaceExists(centerPuyo, Direction.Left)){
                    centerPuyo.getUiTransformComponent().translate(-Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, -Puyo.size.y);

                    movablePuyoDirection = Direction.Right;
                    rotated = true;
                }

                break;
            case Right:
                movablePuyo.getUiTransformComponent().translate(-Puyo.size.x, -Puyo.size.y);

                movablePuyoDirection = Direction.Up;
                rotated = true;

                break;
        }

        if(rotated){
            audioManager.playAudio("Puyo Rotate");
        }
    }

    /**
     * 操作中のぷよを落とす
     */
    private void dropPuyo(){
        if(currentPhase != Phase.Control){
            return;
        }
        if(centerPuyo == null || movablePuyo == null){
            return;
        }

        dropAllPuyos();
    }

    /**
     * 対象ぷよの隣が空いているか取得する
     * @param puyo 対象のぷよ
     * @param direction 方向
     * @return 隣が空いているか
     */
    private boolean adjacentSpaceExists(Puyo puyo, Direction direction){
        if(puyo == null){
            return false;
        }

        Vector2D coordinate = getCoordinateFromPosition(puyo.getUiTransformComponent().getPosition());

        switch(direction){
            case Down:
                coordinate.y += 1;
                if(coordinate.y < boardColumnCount && getPuyo(coordinate) == null){
                    return true;
                }

                break;
            case Left:
                coordinate.x -= 1;
                if(0 <= coordinate.x && getPuyo(coordinate) == null){
                    return true;
                }

                break;
            case Right:
                coordinate.x += 1;
                if(coordinate.x < boardRowCount && getPuyo(coordinate) == null){
                    return true;
                }

                break;
        }

        return false;
    }

    /**
     * フィールドを初期化する
     */
    private void clearBoard(){
        for(int i = 0; i < boardRowCount; i++){
            for(int j = 0; j < boardColumnCount; j++){
                board[i][j] = null;
            }
        }
    }

    /**
     * 連鎖ボーナスを計算する
     * @param chainCount 連鎖回数
     * @return 連鎖ボーナス
     */
    private int calculateChainBonus(int chainCount){
        if(chainCount <= 1){
            return 0;
        }
        else if(chainCount <= 5){
            return (int)Math.pow(2, chainCount + 1);
        }

        return 64 + 32 * (chainCount - 5);
    }

    /**
     * 連結ボーナスを計算する
     * @param connectCount 連結数
     * @return 連鎖ボーナス
     */
    private int calculateConnectBonus(int connectCount){
        if(connectCount <= 4){
            return 0;
        }
        else if(connectCount <= 10){
            return 1 + connectCount - 4;
        }

        return 10;
    }

    /**
     * 色数ボーナスを計算する
     * @param colorCount 色数
     * @return 色数ボーナス
     */
    private int calculateColorBonus(int colorCount){
        if(colorCount <= 1){
            return 0;
        }

        return 3 * (int)Math.pow(2, colorCount - 2);
    }

    /**
     * スコアを計算し追加する
     * @param erasedCount 消去したぷよの数
     * @param chainBonus 連鎖ボーナス
     * @param connectBonus 連鎖ボーナス
     * @param colorBonus 色数ボーナス
     */
    private void addScore(int erasedCount, int chainBonus, int connectBonus, int colorBonus){
        int additionalScore = erasedCount * (chainBonus + connectBonus + colorBonus) * 10;
        if(additionalScore == 0){
            additionalScore = 1;
        }

        score += additionalScore;
        if(scoreLimit < score){
            score = scoreLimit;
        }

        updateScoreText();
    }

    /**
     * スコアテキストを更新する
     */
    private void updateScoreText(){
        scoreText.getTextComponent().setText("SCORE: " + score);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void dispose(){
        super.dispose();

        puyos.clear();
        clearBoard();
    }

    /**
     * フィールド座標からぷよの位置を取得する
     * @param coordinate フィールド座標
     * @return ぷよの位置
     */
    public Vector2D getPositionFromCoordinate(Vector2D coordinate){
        Vector2D position = boardImage.getUITransformComponent().getPosition().subtract(boardSize.multiply(0.5f));
        position = position.add(coordinate.multiply(Puyo.size));
        position = position.add(Puyo.size.multiply(0.5f));

        return position;
    }
    /**
     * フィールド座標からぷよの位置を取得する
     * @param x フィールド座標のx成分
     * @param y フィールド座標のy成分
     * @return ぷよの位置
     */
    public Vector2D getPositionFromCoordinate(int x, int y){
        return getPositionFromCoordinate(new Vector2D(x, y));
    }

    /**
     * 位置からぷよのフィールド座標を取得する
     * @param position 位置
     * @return ぷよのフィールド座標
     */
    public Vector2D getCoordinateFromPosition(Vector2D position){
        Vector2D fixedPosition = position.subtract(boardImage.getUITransformComponent().getPosition()).add(boardSize.multiply(0.5f));
        fixedPosition = fixedPosition.subtract(Puyo.size.multiply(0.5f));

        Vector2D coordinate = fixedPosition.devide(Puyo.size);
        coordinate.x = (float)Math.round(coordinate.x);
        coordinate.y = (float)Math.round(coordinate.y);

        return coordinate;
    }
    /**
     * 位置からぷよのフィールド座標を取得する
     * @param x 位置のx成分
     * @param y 位置のy成分
     * @return ぷよのフィールド座標
     */
    public Vector2D getCoordinateFromPosition(float x, float y){
        return getCoordinateFromPosition(new Vector2D(x, y));
    }

    /**
     * 現在の処理段階を取得する
     * @return 現在の処理段階
     */
    public Phase getCurrentPhase(){
        return currentPhase;
    }

    /**
     * フィールド座標にあるぷよを取得する
     * @param coordinate フィールド座標
     * @return ぷよ
     */
    public Puyo getPuyo(Vector2D coordinate){
        if(coordinate.x < 0 || boardRowCount <= coordinate.x || coordinate.y < 0 || boardColumnCount <= coordinate.y){
            return null;
        }

        return board[(int)coordinate.x][(int)coordinate.y];
    }
    /**
     * フィールド座標にあるぷよを取得する
     * @param x フィールド座標のx成分
     * @param y フィールド座標のy成分
     * @return ぷよ
     */
    public Puyo getPuyo(int x, int y){
        return this.getPuyo(new Vector2D(x, y));
    }
    /**
     * フィールド座標にぷよを設定する
     * @param coordinate フィールド座標
     * @param puyo ぷよ
     */
    public void setPuyo(Vector2D coordinate, Puyo puyo){
        if(coordinate.x < 0 || boardRowCount <= coordinate.x || coordinate.y < 0 || boardColumnCount <= coordinate.y){
            return;
        }

        board[(int)coordinate.x][(int)coordinate.y] = puyo;
    }
    /**
     * フィールド座標にぷよを設定する
     * @param x フィールド座標のx成分
     * @param y フィールド座標のy成分
     * @param puyo ぷよ
     */
    public void setPuyo(int x, int y, Puyo puyo){
        this.setPuyo(new Vector2D(x, y), puyo);
    }

}
