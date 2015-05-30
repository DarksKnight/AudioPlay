package com.audiodemo.listener;

/**
 * 监听
 * 功能：
 * 准备完成
 * 结束录音
 * Created by apple on 5/29/15.
 */
public interface AudioListener {

    void wellPrepared();

    void onFinish(float seconds, String filePath);
}
