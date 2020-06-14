package com.blackhoodie.puyopuyo;

public class Configuration{

    private Configuration(){

    }

    public static final float FRAME_RATE = 200.0f;
    public static final float MINIMUM_FRAME_RATE = 10.0f;
    public static final float MINIMUM_FRAME_TIME = 1.0f / FRAME_RATE;
    public static final float MAXIMUM_FRAME_TIME = 1.0f / MINIMUM_FRAME_RATE;

}
