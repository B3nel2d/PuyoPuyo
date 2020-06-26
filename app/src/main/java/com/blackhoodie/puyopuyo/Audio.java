package com.blackhoodie.puyopuyo;

public class Audio{

    private String name;
    private String filePath;
    private int soundId;
    private boolean loaded;

    public Audio(String name, String filePath){
        this.name = name;
        this.filePath = filePath;
        this.soundId = 0;
        this.loaded = false;
    }

    public String getName(){
        return name;
    }

    public String getFilePath(){
        return filePath;
    }

    public int getSoundId(){
        return soundId;
    }
    public void setSoundId(int soundId){
        this.soundId = soundId;
    }

    public boolean isLoaded(){
        return loaded;
    }
    public void setLoaded(boolean value){
        this.loaded = value;
    }

}
