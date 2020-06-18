package com.blackhoodie.puyopuyo;

public class Vector2D{

    public float x;
    public float y;

    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Vector2D(){
        this(0.0f, 0.0f);
    }

    public Vector2D add(Vector2D vector){
        return new Vector2D(this.x + vector.x, this.y + vector.y);
    }

    public Vector2D subtract(Vector2D vector){
        return new Vector2D(this.x - vector.x, this.y - vector.y);
    }

    public Vector2D multiply(float scalar){
        return new Vector2D(this.x * scalar, this.y * scalar);
    }
    public Vector2D multiply(Vector2D vector){
        return new Vector2D(this.x * vector.x, this.y * vector.y);
    }

}
