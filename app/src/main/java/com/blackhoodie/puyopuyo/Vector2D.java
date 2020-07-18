package com.blackhoodie.puyopuyo;

/**
 * 二次元ベクトルを定義するクラス
 */
public class Vector2D{

    /** x成分 */
    public float x;
    /** y成分 */
    public float y;

    /**
     * コンストラクタ
     * @param x x成分
     * @param y y成分
     */
    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * コンストラクタ
     */
    public Vector2D(){
        this(0.0f, 0.0f);
    }

    /**
     * ベクトルの加算を行う
     * @param vector 加算するベクトル
     * @return 計算後のベクトル
     */
    public Vector2D add(Vector2D vector){
        return new Vector2D(this.x + vector.x, this.y + vector.y);
    }

    /**
     * ベクトルの加算を行う
     * @param x 加算するベクトルのx成分
     * @param y 加算するベクトルのy成分
     * @return 計算後のベクトル
     */
    public Vector2D add(float x, float y){
        return this.add(new Vector2D(x, y));
    }

    /**
     * ベクトルの減算を行う
     * @param vector 減算するベクトル
     * @return 計算後のベクトル
     */
    public Vector2D subtract(Vector2D vector){
        return new Vector2D(this.x - vector.x, this.y - vector.y);
    }
    /**
     * ベクトルの減算を行う
     * @param x 減算するベクトルのx成分
     * @param y 減算するベクトルのy成分
     * @return 計算後のベクトル
     */
    public Vector2D subtract(float x, float y){
        return this.subtract(new Vector2D(x, y));
    }

    /**
     * ベクトルの乗算を行う
     * @param vector 乗算するベクトル
     * @return 計算後のベクトル
     */
    public Vector2D multiply(Vector2D vector){
        return new Vector2D(this.x * vector.x, this.y * vector.y);
    }
    /**
     * ベクトルの乗算を行う
     * @param x 乗算するベクトルのx成分
     * @param y 乗算するベクトルのy成分
     * @return 計算後のベクトル
     */
    public Vector2D multiply(float x, float y){
        return this.multiply(new Vector2D(x, y));
    }
    /**
     * ベクトルの乗算を行う
     * @param scalar 掛ける値
     * @return
     */
    public Vector2D multiply(float scalar){
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    /**
     * ベクトルの加算を行う
     * @param vector 除算するベクトル
     * @return 計算後のベクトル
     */
    public Vector2D devide(Vector2D vector){
        return new Vector2D(this.x / vector.x, this.y / vector.y);
    }
    /**
     * ベクトルの除算を行う
     * @param x 除算するベクトルのx成分
     * @param y 除算するベクトルのy成分
     * @return 計算後のベクトル
     */
    public Vector2D devide(float x, float y){
        return this.devide(new Vector2D(x, y));
    }
    /**
     * ベクトルの除算を行う
     * @param scalar 割る値
     * @return
     */
    public Vector2D devide(float scalar){
        return new Vector2D(this.x / scalar, this.y / scalar);
    }

    /**
     * ベクトルの絶対値を取得する
     * @return ベクトルの絶対値
     */
    public float getMagnitude(){
        return (float)Math.sqrt(getSquaredMagnitude());
    }

    /**
     * ベクトルの絶対値の2乗を取得する
     * @return ベクトルの絶対値の2乗
     */
    public float getSquaredMagnitude(){
        return x * x + y * y;
    }

}
