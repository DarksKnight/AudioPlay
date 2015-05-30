package com.audiodemo.util;

import android.media.*;

/**
 * 音频播放管理类
 * Created by apple on 5/30/15.
 */
public class MediaManager {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPause = true;

    /**
     * 播放录音
     * @param filePath
     * @param listener
     */
    public static void playSound(String filePath, MediaPlayer.OnCompletionListener listener){
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        }else{
            mMediaPlayer.reset();
        }

        try{
            mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(listener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 暂停
     */
    public static void pause(){
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 重新开始
     */
    public static void resume(){
        if(mMediaPlayer != null && isPause){
            mMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 结束
     */
    public static void release(){
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
