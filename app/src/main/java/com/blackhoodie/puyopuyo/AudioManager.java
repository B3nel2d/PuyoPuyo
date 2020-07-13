package com.blackhoodie.puyopuyo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class AudioManager{

    private Level owner;

    private SoundPool soundPool;
    private AudioAttributes audioAttributes;

    private List<Audio> audios;

    private float volume;

    public AudioManager(Level owner){
        this.owner = owner;
        audios = new ArrayList<Audio>();
    }

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addAudio(String name, String filePath){
        if(audios.stream().anyMatch(target -> target.getName() == name)){
            System.out.println("ERROR: Cannot add an audio. There is already one with same name.");
            return;
        }

        audios.add(new Audio(name, filePath));
    }

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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int playAudio(String name, boolean loop){
        return this.playAudio(name, loop, 1.0f);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int playAudio(String name, float rate){
        return this.playAudio(name, false, rate);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int playAudio(String name){
        return this.playAudio(name, false, 1.0f);
    }

    public void stopAudio(int streamId){
        soundPool.stop(streamId);
    }

    public void dispose(){
        soundPool.release();
        audios.clear();
    }

    public float getVolume(){
        return volume;
    }
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
