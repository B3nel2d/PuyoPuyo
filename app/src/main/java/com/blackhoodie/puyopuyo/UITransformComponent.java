package com.blackhoodie.puyopuyo;

public class UITransformComponent extends Component{

    private Vector2D size;
    private Vector2D position;
    private float rotation;

    public UITransformComponent(Actor owner, int updateOrder){
        super(owner, updateOrder);

        size = new Vector2D(1.0f, 1.0f);
        position = new Vector2D(0.0f, 0.0f);
        rotation = 0.0f;
    }
    public UITransformComponent(Actor owner){
        this(owner, 0);
    }

    public void update(){

    }

    public void translate(Vector2D translation){
        position.x += translation.x;
        position.y += translation.y;
    }
    public void translate(float x, float y){
        translate(new Vector2D(x, y));
    }

    public Vector2D getSize(){
        return size;
    }
    public void setSize(Vector2D size){
        this.size = size;

        if(size.x < 0){
            this.size.x = 0;
        }
        if(size.y < 0){
            this.size.y = 0;
        }
    }
    public void setSize(float x, float y){
        setSize(new Vector2D(x, y));
    }

    public Vector2D getPosition(){
        return position;
    }
    public void setPosition(Vector2D position){
        this.position = position;
    }
    public void setPosition(float x, float y){
        setPosition(new Vector2D(x, y));
    }

    public float getRotation(){
        return rotation;
    }
    public void setRotation(float rotation){
        this.rotation = rotation;

        while(rotation < 0){
            this.rotation += 360;
        }
        while(360 < rotation){
            this.rotation -= 360;
        }
    }
}
