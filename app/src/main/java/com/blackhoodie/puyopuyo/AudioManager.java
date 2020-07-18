package com.blackhoodie.puyopuyo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import androidx.annotation.RequiresApi;

/**
 * レベルの効果音出力処理を担うクラス
 */
public class AudioManager{

    /** 所属するレベル */
    private Level owner;

    /** SoundPool */
    private SoundPool soundPool;
    /** SoundPool用の音声の属性 */
    private AudioAttributes audioAttributes;

    /** 効果音のリスト */
    private List<Audio> audios;

    /** 音量 */
    private float volume;

    /**
     * コンストラクタ
     * @param owner 所属するレベル
     */
    public AudioManager(Level owner){
        this.owner = owner;
        audios = new CopyOnWriteArrayList<Audio>();
    }

    /**
     * 初期化処理を行う
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initialize(){
        audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();

        if(audios == null){
            audios = new ArrayList<Audio>();
        }

        volume = 1.0f;

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status){
                if(status != 0){
                    return;
                }

                Audio loadedAudio = audios.stream().filter(target -> target.getSoundId() == sampleId).findFirst().orElse(null);
                if(loadedAudio != null){
                    loadedAudio.setLoaded(true);
                }

                int count = 0;
                for(Audio audio : audios){
                    if(audio.isLoaded()){
                        count++;
                    }
                }

                if(count == audios.size()){
                    owner.onLoadComplete();
                }
            }
        });

        for(Audio audio : audios){
            loadAudio(audio);
        }
    }

    /**
     * 効果音を追加する
     * @param filePath 追加する効果音のファイルパス（assets内）
     * @param name 追加する効果音の名前
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addAudio(String filePath, String name){
        if(audios.stream().anyMatch(target -> target.getName() == name)){
            System.out.println("ERROR: Could not add an audio. There is already one with same name.");
            return;
        }

        audios.add(new Audio(name, filePath));
    }

    /**
     * 効果音を読み込む
     * @param audio 読み込む効果音
     */
    private void loadAudio(Audio audio){
        AssetManager assetManager = Game.getInstance().getContext().getResources().getAssets();
        AssetFileDescriptor fileDescriptor = null;

        try{
            fileDescriptor = assetManager.openFd(audio.getFilePath());
        }
        catch(IOException exception){
            throw new IllegalStateException();
        }

        audio.setSoundId(soundPool.load(fileDescriptor, 1));
    }

    /**
     * 効果音を再生する
     * @param name 再生する効果音の名前
     * @param loop ループするかどうか
     * @param rate 再生速度の倍率（0.5 ～　2.0）
     * @return ストリームID
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int playAudio(String name, boolean loop, float rate){
        Audio audio = audios.stream().filter(target -> target.getName() == name).findFirst().orElse(null);
        int soundId;

        if(audio == null){
            System.out.println("ERROR: Cannot play an audio.");
            return -1;
        }
        else{
            soundId = audio.getSoundId();
        }

        return soundPool.play(soundId, volume, volume, 1, (loop ? -1 : 0), rate);
    }
    /**
     * 効果音を再生する
     * @param name 再生する効果音の名前
     * @param loop ループするかどうか
     * @return ストリームID
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int playAudio(String name, boolean loop){
        return this.playAudio(name, loop, 1.0f);
    }
    /**
     * 効果音を再生する
     * @param name 再生する効果音の名前
     * @param rate 再生速度の倍率（0.5 ～　2.0）
     * @return ストリームID
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int playAudio(String name, float rate){
        return this.playAudio(name, false, rate);
    }
    /**
     * 効果音を再生する
     * @param name 再生する効果音の名前
     * @return ストリームID
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int playAudio(String name){
        return this.playAudio(name, false, 1.0f);
    }

    /**
     * 効果音の再生を止める
     * @param streamId 再生を止める効果音のストリームID
     */
    public void stopAudio(int streamId){
        soundPool.stop(streamId);
    }

    /**
     * リソースを開放する
     */
    public void dispose(){
        soundPool.release();
        audios.clear();
    }

    /**
     * 音量を取得する
     * @return 音量
     */
    public float getVolume(){
        return volume;
    }
    /**
     * 音量を設定する
     * @param volume 設定する値（0.0 ～　1.0）
     */
    public void setVolume(float volume){
        this.volume = volume;

        if(this.volume < 0.0f){
            this.volume = 0.0f;
        }
        if(1.0f < this.volume){
            this.volume = 1.0f;
        }
    }

}
