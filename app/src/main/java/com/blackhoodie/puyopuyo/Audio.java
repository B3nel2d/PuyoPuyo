package com.blackhoodie.puyopuyo;

/**
 * 音声（効果音）のクラス
 */
public class Audio{

    /** 名前 */
    private String name;
    /** ファイルパス */
    private String filePath;
    /** サウンドID */
    private int soundId;
    /** ロードされているか */
    private boolean loaded;

    /**
     * コンストラクタ
     * @param name 名前
     * @param filePath ファイルパス
     */
    public Audio(String name, String filePath){
        this.name = name;
        this.filePath = filePath;
        this.soundId = 0;
        this.loaded = false;
    }

    /**
     * 名前を取得する
     * @return 名前
     */
    public String getName(){
        return name;
    }

    /**
     * ファイルパスを取得する
     * @return ファイルパス
     */
    public String getFilePath(){
        return filePath;
    }

    /**
     * サウンドIDを取得する
     * @return サウンドID
     */
    public int getSoundId(){
        return soundId;
    }
    /**
     * サウンドIDを設定する
     * @param soundId 設定する値
     */
    public void setSoundId(int soundId){
        this.soundId = soundId;
    }

    /**
     * ロードされているか取得する
     * @return ロードされているか
     */
    public boolean isLoaded(){
        return loaded;
    }
    /**
     * ロード済みか設定する
     * @param value 設定する値
     */
    public void setLoaded(boolean value){
        this.loaded = value;
    }

}
