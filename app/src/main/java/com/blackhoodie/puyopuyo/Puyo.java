package com.blackhoodie.puyopuyo;

import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 *
 */
public class Puyo extends Actor{

    public enum Color{
        Red,
        Green,
        Blue,
        Yellow,
        Magenta,
    }

    private Color color;

    private float dropSpeed;
    private final float dropAcceleration = 3.0f;
    private final float fallSpeed = 5.0f;

    private boolean fixed;

    public static final Vector2D size = new Vector2D(125, 125);

    private UITransformComponent uiTransformComponent;
    private ImageComponent imageComponent;

    public Puyo(Level owner, String name){
        super(owner, name);
    }

    @Override
    public void initialize(){
        color = null;
        dropSpeed = 0.0f;
        fixed = false;

        uiTransformComponent = new UITransformComponent(this);
        uiTransformComponent.setSize(size);

        imageComponent = new ImageComponent(this, uiTransformComponent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(){
        drop();
        fall();
        ground();
    }

    private void drop(){
        if(fixed || ((GameLevel)owner).getCurrentPhase() != GameLevel.Phase.Drop){
            return;
        }

        dropSpeed += dropAcceleration;
        uiTransformComponent.translate(0, dropSpeed);
    }

    private void fall(){
        if(fixed || ((GameLevel)owner).getCurrentPhase() != GameLevel.Phase.Control){
            return;
        }

        uiTransformComponent.translate(0, fallSpeed);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ground(){
        if(fixed){
            return;
        }

        int row = (int)((GameLevel)owner).getCoordinateFromPosition(uiTransformComponent.getPosition()).x;
        int groundHeight = GameLevel.boardColumnCount - 1;

        for(int column = GameLevel.boardColumnCount - 1; -1 <= column; column--){
            if(((GameLevel)owner).getPuyo(row, column) == null){
                groundHeight = column;
                break;
            }
        }

        if(((GameLevel)owner).getPositionFromCoordinate(row, groundHeight).y <= uiTransformComponent.getPosition().y){
            if(0 <= groundHeight){
                uiTransformComponent.setPosition(((GameLevel)owner).getPositionFromCoordinate(row, groundHeight));
                ((GameLevel)owner).setPuyo(row, groundHeight, this);

                fixed = true;

                owner.getAudioManager().playAudio("Puyo Ground");
            }
            else{
                ((GameLevel)owner).erasePuyo(this);
            }

            resetDropSpeed();
        }
    }

    public Color getColor(){
        return color;
    }
    public void setColor(Color color){
        this.color = color;
        imageComponent.setBitmap(owner.getGraphicsManager().getBitmap(color + " Puyo"));
    }

    public void resetDropSpeed(){
        dropSpeed = 0.0f;
    }

    public boolean isFixed(){
        return fixed;
    }
    public void setFixed(boolean value){
        fixed = value;
    }

    public UITransformComponent getUiTransformComponent(){
        return uiTransformComponent;
    }

    public ImageComponent getImageComponent(){
        return imageComponent;
    }

}
