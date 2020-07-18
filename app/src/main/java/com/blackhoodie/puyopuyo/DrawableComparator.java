package com.blackhoodie.puyopuyo;

import java.util.Comparator;

public class DrawableComparator implements Comparator<DrawableComponent>{

    public int compare(DrawableComponent drawable1, DrawableComponent drawable2){
        if(drawable1.getDrawOrder() < drawable2.getDrawOrder()){
            return -1;
        }
        else if(drawable1.getDrawOrder() == drawable2.getDrawOrder()){
            return 0;
        }
        else{
            return 1;
        }
    }

}
