package com.blackhoodie.puyopuyo;

import android.graphics.Color;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.RequiresApi;

public class GameLevel extends Level{

    public enum Phase{
        Start,
        Control,
        Drop,
        Erase,
        GameOver
    }

    public enum Direction{
        Up,
        Down,
        Left,
        Right
    }

    private Phase currentPhase;

    private Puyo centerPuyo;
    private Puyo movablePuyo;

    private Direction movablePuyoDirection;

    private List<Puyo> puyos;

    private Puyo[][] board;
    public static final int boardRowCount = 6;
    public static final int boardColumnCount = 13;
    public static final Vector2D boardSize = new Vector2D(Puyo.size.x * boardRowCount, Puyo.size.y * boardColumnCount);

    private final int puyoCountToErase = 4;
    private final Vector2D gameOverCoordinate = new Vector2D(2, 1);
    private final float minimumFlickVelocity = 10.0f;

    private int score;
    private final int scoreLimit = 99999999;
    private int chainCount;

    private ImageActor backgroundImage;
    private ImageActor boardImage;
    private ImageActor coverImage;

    private TextActor scoreText;
    private TouchPanel touchPanel;

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

        puyos = new ArrayList<Puyo>();
        board = new Puyo[boardRowCount][boardColumnCount];

        score = 0;
        chainCount = 0;

        graphicsManager.addBitmap("images/background.png", "Background");
        graphicsManager.addBitmap("images/board.png", "Board");
        graphicsManager.addBitmap("images/red-puyo.png", "Red Puyo");
        graphicsManager.addBitmap("images/green-puyo.png", "Green Puyo");
        graphicsManager.addBitmap("images/blue-puyo.png", "Blue Puyo");
        graphicsManager.addBitmap("images/magenta-puyo.png", "Magenta Puyo");
        graphicsManager.addBitmap("images/yellow-puyo.png", "Yellow Puyo");

        skipLoad();

        backgroundImage = new ImageActor(this, "Background Image");
        backgroundImage.getUITransformComponent().setSize(Game.getInstance().getScreenSize());
        backgroundImage.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2.0f, Game.getInstance().getScreenSize().y / 2.0f);
        backgroundImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Background"));
        backgroundImage.getImageComponent().setDrawOrder(0);

        boardImage = new ImageActor(this, "Board Image");
        boardImage.getUITransformComponent().setSize(boardSize);
        boardImage.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2.0f, Game.getInstance().getScreenSize().y / 2.0f + -50);
        boardImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Board"));
        boardImage.getImageComponent().setDrawOrder(0);

        coverImage = new ImageActor(this, "Cover Image");
        coverImage.getUITransformComponent().setSize(boardSize.x, Puyo.size.y * 2);
        coverImage.getUITransformComponent().setPosition(boardImage.getUITransformComponent().getPosition().x, boardImage.getUITransformComponent().getPosition().y - boardImage.getUITransformComponent().getSize().y / 2.0f);
        coverImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Background"));
        coverImage.getImageComponent().setDrawOrder(3);

        scoreText = new TextActor(this, "Score Text");
        scoreText.getUITransformComponent().setPosition(boardImage.getUITransformComponent().getPosition().x - 100, boardImage.getUITransformComponent().getPosition().y + boardImage.getUITransformComponent().getSize().y / 2 + 50);
        scoreText.getTextComponent().setText("SCORE: " + score);
        scoreText.getTextComponent().getPaint().setTextSize(50);
        scoreText.getTextComponent().getPaint().setColor(Color.WHITE);
        scoreText.getTextComponent().setDrawOrder(1);

        touchPanel = new TouchPanel(this, "Touch Panel");
        touchPanel.getUiTransformComponent().setSize(Game.getInstance().getScreenSize());
        touchPanel.getUiTransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f));
        touchPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.DoubleTap, this::rotatePuyoClockwise);
        touchPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.LongPress, this::dropPuyo);
        touchPanel.getInteractableComponent().setFlickTask(this::onFling);

        startGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(){
        switch(currentPhase){
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
                        eraseAllPuyos();
                    }
                }

                break;
        }
    }

    private void startGame(){
        spawnPuyo();
    }

    private void spawnPuyo(){
        currentPhase = Phase.Control;

        centerPuyo = new Puyo(this, "Puyo");
        puyos.add(centerPuyo);
        centerPuyo.getUiTransformComponent().setPosition(getPositionFromCoordinate(2, 0));
        centerPuyo.setColor(Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)]);
        centerPuyo.getImageComponent().setDrawOrder(1);

        movablePuyo = new Puyo(this, "Puyo");
        puyos.add(movablePuyo);
        movablePuyo.getUiTransformComponent().setPosition(getPositionFromCoordinate(2, -1));
        movablePuyo.setColor(Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)]);
        movablePuyo.getImageComponent().setDrawOrder(1);

        movablePuyoDirection = Direction.Up;
    }

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

    private boolean areAllPuyosFixed(){
        for(Puyo puyo : puyos){
            if(!puyo.isFixed()){
                return false;
            }
        }

        return true;
    }

    private boolean isGameOver(){
        if(getPuyo(gameOverCoordinate) != null){
            return true;
        }

        return false;
    }

    private void finishGame(){

    }

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

            dropAllPuyos();
        }
        else{
            chainCount = 0;
            spawnPuyo();
        }
    }

    public void erasePuyo(Puyo puyo){
        if(puyo == null){
            return;
        }

        puyos.remove(puyo);
        setPuyo(getCoordinateFromPosition(puyo.getUiTransformComponent().getPosition()), null);
        puyo.delete();
    }

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

    private void rotatePuyoClockwise(){
        if(currentPhase != Phase.Control){
            return;
        }
        if(centerPuyo == null || movablePuyo == null){
            return;
        }

        switch(movablePuyoDirection){
            case Up:
                if(adjacentSpaceExists(centerPuyo, Direction.Right)){
                    movablePuyo.getUiTransformComponent().translate(Puyo.size.x, Puyo.size.y);

                    movablePuyoDirection = Direction.Right;
                }
                else if(adjacentSpaceExists(centerPuyo, Direction.Left)){
                    centerPuyo.getUiTransformComponent().translate(-Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, Puyo.size.y);

                    movablePuyoDirection = Direction.Right;
                }

                break;
            case Right:
                if(adjacentSpaceExists(centerPuyo, Direction.Down)){
                    movablePuyo.getUiTransformComponent().translate(-Puyo.size.x, Puyo.size.y);

                    movablePuyoDirection = Direction.Down;
                }

                break;
            case Down:
                if(adjacentSpaceExists(centerPuyo, Direction.Left)){
                    movablePuyo.getUiTransformComponent().translate(-Puyo.size.x, -Puyo.size.y);

                    movablePuyoDirection = Direction.Left;
                }
                else if(adjacentSpaceExists(centerPuyo, Direction.Right)){
                    centerPuyo.getUiTransformComponent().translate(Puyo.size.x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, -Puyo.size.y);

                    movablePuyoDirection = Direction.Left;
                }

                break;
            case Left:
                movablePuyo.getUiTransformComponent().translate(Puyo.size.x, -Puyo.size.y);

                movablePuyoDirection = Direction.Up;

                break;
        }
    }

    private void dropPuyo(){
        if(currentPhase != Phase.Control){
            return;
        }
        if(centerPuyo == null || movablePuyo == null){
            return;
        }

        dropAllPuyos();
    }

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

    private void clearBoard(){
        for(int i = 0; i < boardRowCount; i++){
            for(int j = 0; j < boardColumnCount; j++){
                board[i][j] = null;
            }
        }
    }

    private int calculateChainBonus(int chainCount){
        if(chainCount <= 1){
            return 0;
        }
        else if(chainCount <= 5){
            return (int)Math.pow(2, chainCount + 1);
        }

        return 64 + 32 * (chainCount - 5);
    }

    private int calculateConnectBonus(int connectCount){
        if(connectCount <= 4){
            return 0;
        }
        else if(connectCount <= 10){
            return 1 + connectCount - 4;
        }

        return 10;
    }

    private int calculateColorBonus(int colorCount){
        if(colorCount <= 1){
            return 0;
        }

        return 3 * (int)Math.pow(2, colorCount - 2);
    }

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

    private void updateScoreText(){
        scoreText.getTextComponent().setText("SCORE: " + score);
    }

    @Override
    public void dispose(){
        super.dispose();

        puyos.clear();
        clearBoard();
    }

    public Vector2D getPositionFromCoordinate(Vector2D coordinate){
        Vector2D position = boardImage.getUITransformComponent().getPosition().subtract(boardSize.multiply(0.5f));
        position = position.add(coordinate.multiply(Puyo.size));
        position = position.add(Puyo.size.multiply(0.5f));

        return position;
    }
    public Vector2D getPositionFromCoordinate(int x, int y){
        return getPositionFromCoordinate(new Vector2D(x, y));
    }

    public Vector2D getCoordinateFromPosition(Vector2D position){
        Vector2D fixedPosition = position.subtract(boardImage.getUITransformComponent().getPosition()).add(boardSize.multiply(0.5f));
        fixedPosition = fixedPosition.subtract(Puyo.size.multiply(0.5f));

        Vector2D coordinate = fixedPosition.devide(Puyo.size);
        coordinate.x = (float)Math.round(coordinate.x);
        coordinate.y = (float)Math.round(coordinate.y);

        return coordinate;
    }
    public Vector2D getCoordinateFromPosition(float x, float y){
        return getCoordinateFromPosition(new Vector2D(x, y));
    }

    public Phase getCurrentPhase(){
        return currentPhase;
    }

    public Puyo getPuyo(Vector2D coordinate){
        if(coordinate.x < 0 || boardRowCount <= coordinate.x || coordinate.y < 0 || boardColumnCount <= coordinate.y){
            return null;
        }

        return board[(int)coordinate.x][(int)coordinate.y];
    }
    public Puyo getPuyo(int x, int y){
        return this.getPuyo(new Vector2D(x, y));
    }
    public void setPuyo(Vector2D coordinate, Puyo puyo){
        if(coordinate.x < 0 || boardRowCount <= coordinate.x || coordinate.y < 0 || boardColumnCount <= coordinate.y){
            return;
        }

        board[(int)coordinate.x][(int)coordinate.y] = puyo;
    }
    public void setPuyo(int x, int y, Puyo puyo){
        this.setPuyo(new Vector2D(x, y), puyo);
    }

    public Puyo getCenterPuyo(){
        return centerPuyo;
    }
    public void setCenterPuyo(Puyo puyo){
        centerPuyo = puyo;
    }

    public Puyo getMovablePuyo(){
        return movablePuyo;
    }
    public void setMovablePuyo(Puyo puyo){
        movablePuyo = puyo;
    }

}
