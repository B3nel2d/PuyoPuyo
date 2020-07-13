package com.blackhoodie.puyopuyo;

import android.os.Build;

import java.util.Random;

import androidx.annotation.RequiresApi;

public class GameLevel extends Level{

    public enum Phase{
        Drop,
        Erace,
        Control,
        Gameover
    }

    public enum Direction{
        Up,
        Down,
        Left,
        Right
    }

    private Phase currentPhase;

    private Puyo[][] board;
    public static final int boardRowCount = 6;
    public static final int boardColumnCount = 12;
    public static final Vector2D boardSize = new Vector2D(Puyo.getSize().x * boardRowCount, Puyo.getSize().y * boardColumnCount);

    private Puyo centerPuyo;
    private Puyo movablePuyo;

    private Direction movablePuyoDirection;

    private final float minimumFlickVelocity = 10.0f;

    private ImageActor boardImage;
    private TouchPanel touchPanel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameLevel(String name){
        super(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initializeActors(){
        currentPhase = Phase.Control;

        board = new Puyo[boardRowCount][boardColumnCount];
        clearBoard();

        centerPuyo = null;
        movablePuyo = null;

        movablePuyoDirection = null;

        graphicsManager.addBitmap("images/board.png", "Board");
        graphicsManager.addBitmap("images/red-puyo.png", "Red Puyo");
        graphicsManager.addBitmap("images/green-puyo.png", "Green Puyo");
        graphicsManager.addBitmap("images/blue-puyo.png", "Blue Puyo");
        graphicsManager.addBitmap("images/magenta-puyo.png", "Magenta Puyo");
        graphicsManager.addBitmap("images/yellow-puyo.png", "Yellow Puyo");

        skipLoad();

        boardImage = new ImageActor(this, "Board Image");
        boardImage.getUITransformComponent().setSize(boardSize);
        boardImage.getUITransformComponent().setPosition(Game.getInstance().getScreenSize().x / 2, Game.getInstance().getScreenSize().y / 2);
        boardImage.getImageComponent().setBitmap(graphicsManager.getBitmap("Board"));
        boardImage.getImageComponent().setDrawOrder(0);

        touchPanel = new TouchPanel(this, "Touch Panel");
        touchPanel.getUiTransformComponent().setSize(Game.getInstance().getScreenSize());
        touchPanel.getUiTransformComponent().setPosition(Game.getInstance().getScreenSize().multiply(0.5f));
        touchPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.DoubleTap, this::rotatePuyoClockwise);
        touchPanel.getInteractableComponent().addTask(InteractableComponent.TouchAction.LongPress, this::dropPuyo);
        touchPanel.getInteractableComponent().setFlickTask(this::onFling);

        spawnPuyo();
    }

    @Override
    public void update(){
        switch(currentPhase){
            case Drop:
                if(checkAllPuyoFixed()){
                    currentPhase = Phase.Erace;
                }

                break;
            case Erace:
                if(checkAllPuyoEraced()){
                    spawnPuyo();
                }
                else{
                    dropAllPuyo();
                }

                break;
            case Control:

                break;
        }

        System.out.println("Phase: " + currentPhase);
    }

    public void dropAllPuyo(){
        currentPhase = Phase.Drop;

        for(int row = 0; row < boardRowCount; row++){
            boolean aboveSpace = false;

            for(int column = boardColumnCount - 1; 0 <= column; column--){
                if(board[row][column] == null){
                    aboveSpace = true;
                }
                else{
                    if(aboveSpace){
                        setPuyo(row, column, null);
                        board[row][column].setFixed(false);
                    }
                }
            }
        }
    }

    private boolean checkAllPuyoFixed(){
        if(!centerPuyo.isFixed() || !movablePuyo.isFixed()){
            return false;
        }

        for(int row = 0; row < boardRowCount; row++){
            for(int column = 0; column < boardColumnCount; column++){
                if(board[row][column] != null && !board[row][column].isFixed()){
                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkAllPuyoEraced(){
        return true;
    }

    private void EracePuyos(){


        dropAllPuyo();
    }

    private void spawnPuyo(){
        currentPhase = Phase.Control;

        centerPuyo = new Puyo(this, "Puyo");
        centerPuyo.getUiTransformComponent().setPosition(getPositionFromCoordinate(2, -1));
        centerPuyo.setColor(Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)]);
        centerPuyo.getImageComponent().setDrawOrder(1);

        movablePuyo = new Puyo(this, "Puyo");
        movablePuyo.getUiTransformComponent().setPosition(getPositionFromCoordinate(2, -2));
        movablePuyo.setColor(Puyo.Color.values()[new Random().nextInt(Puyo.Color.values().length)]);
        movablePuyo.getImageComponent().setDrawOrder(1);

        movablePuyoDirection = Direction.Up;
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
                if(checkAdjacentSpace(centerPuyo, Direction.Left) && checkAdjacentSpace(movablePuyo, Direction.Left)){
                    centerPuyo.getUiTransformComponent().translate(-Puyo.getSize().x, 0);
                    movablePuyo.getUiTransformComponent().translate(-Puyo.getSize().x, 0);
                }

                break;
            case Right:
                if(checkAdjacentSpace(centerPuyo, Direction.Right) && checkAdjacentSpace(movablePuyo, Direction.Right)){
                    centerPuyo.getUiTransformComponent().translate(Puyo.getSize().x, 0);
                    movablePuyo.getUiTransformComponent().translate(Puyo.getSize().x, 0);
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
                if(checkAdjacentSpace(centerPuyo, Direction.Right)){
                    movablePuyo.getUiTransformComponent().translate(Puyo.getSize().x, Puyo.getSize().y);

                    movablePuyoDirection = Direction.Right;
                }
                else if(checkAdjacentSpace(centerPuyo, Direction.Left)){
                    centerPuyo.getUiTransformComponent().translate(-Puyo.getSize().x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, Puyo.getSize().y);

                    movablePuyoDirection = Direction.Right;
                }

                break;
            case Right:
                if(checkAdjacentSpace(centerPuyo, Direction.Down)){
                    movablePuyo.getUiTransformComponent().translate(-Puyo.getSize().x, Puyo.getSize().y);

                    movablePuyoDirection = Direction.Down;
                }

                break;
            case Down:
                if(checkAdjacentSpace(centerPuyo, Direction.Left)){
                    movablePuyo.getUiTransformComponent().translate(-Puyo.getSize().x, -Puyo.getSize().y);

                    movablePuyoDirection = Direction.Left;
                }
                else if(checkAdjacentSpace(centerPuyo, Direction.Right)){
                    centerPuyo.getUiTransformComponent().translate(Puyo.getSize().x, 0);
                    movablePuyo.getUiTransformComponent().translate(0, -Puyo.getSize().y);

                    movablePuyoDirection = Direction.Left;
                }

                break;
            case Left:
                movablePuyo.getUiTransformComponent().translate(Puyo.getSize().x, -Puyo.getSize().y);

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

        dropAllPuyo();
    }

    private boolean checkAdjacentSpace(Puyo puyo, Direction direction){
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

    public static Vector2D getPositionFromCoordinate(Vector2D coordinate){
        Vector2D position = Game.getInstance().getScreenSize().multiply(0.5f).subtract(boardSize.multiply(0.5f));
        position = position.add(coordinate.multiply(Puyo.getSize()));
        position = position.add(Puyo.getSize().multiply(0.5f));

        return position;
    }
    public static Vector2D getPositionFromCoordinate(int x, int y){
        return getPositionFromCoordinate(new Vector2D(x, y));
    }

    public static Vector2D getCoordinateFromPosition(Vector2D position){
        Vector2D fixedPosition = position.subtract(Game.getInstance().getScreenSize().multiply(0.5f).subtract(boardSize.multiply(0.5f)));
        fixedPosition = fixedPosition.subtract(Puyo.getSize().multiply(0.5f));

        Vector2D coordinate = fixedPosition.devide(Puyo.getSize());
        coordinate.x = (float)Math.round(coordinate.x);
        coordinate.y = (float)Math.round(coordinate.y);

        return coordinate;
    }
    public static Vector2D getCoordinateFromPosition(float x, float y){
        return getCoordinateFromPosition(new Vector2D(x, y));
    }

    public Phase getCurrentPhase(){
        return currentPhase;
    }

    public Puyo[][] getBoard(){
        return board;
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
