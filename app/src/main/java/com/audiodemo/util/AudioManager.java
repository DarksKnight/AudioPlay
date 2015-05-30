package com.audiodemo.util;

import com.audiodemo.listener.AudioListener;

import android.media.MediaRecorder;

import java.io.File;
import java.util.UUID;

/**
 * 音频管理类
 * Created by apple on 5/29/15.
 */
public class AudioManager {

    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;
    public AudioListener mListener;
    private boolean isPrepared = false;

    private static AudioManager mInstance;
    private AudioManager(String dir){
        mDir = dir;
    }
    public static AudioManager getInstance(String dir){
        if(mInstance == null){
            synchronized (AudioManager.class){
                if(mInstance == null){
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    /**
     * 准备录音
     */
    public void prepareAudio(){
        try{
            isPrepared = false;
            File dir = new File(mDir);
            if(!dir.exists()){
                dir.mkdir();
            }

            String fileName = generateFileName();
            File file = new File(dir, fileName);
            mCurrentFilePath = file.getAbsolutePath();
            mMediaRecorder = new MediaRecorder();
            //设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频编码
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isPrepared = true;
            if(mListener != null){
                mListener.wellPrepared();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取声音等级
     * @param maxLv
     * @return
     */
    public int getVoiceLv(int maxLv){
        if(isPrepared){
            try{
                return maxLv * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            }catch (Exception e){
                return 1;
            }
        }
        return 1;
    }

    /**
     * 释放录音
     */
    public void release(){
        try{
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 取消录音
     */
    public void cancel(){
        release();
        if(mCurrentFilePath != null){
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }

    }

    public String getCurrentFilePath(){
        return mCurrentFilePath;
    }

    private String generateFileName(){
        return UUID.randomUUID().toString() + ".amr";
    }

    public void setOnAudioListener(AudioListener listener){
        mListener = listener;
    }

}
