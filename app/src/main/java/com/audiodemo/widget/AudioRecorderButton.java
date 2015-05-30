package com.audiodemo.widget;

import com.audiodemo.R;
import com.audiodemo.listener.AudioListener;
import com.audiodemo.util.AudioManager;
import com.audiodemo.util.DialogManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * 自定义录音按钮
 * Created by apple on 5/29/15.
 */
public class AudioRecorderButton extends Button implements AudioListener {
    private static final int DISTANCE_Y_CANCEL = 50;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_CANCEL = 3;

    private int mCurState = STATE_NORMAL;
    private boolean isRecording = false;
    private DialogManager mDialogManager;
    private AudioManager mAudioManager;
    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGED = 0X111;
    private static final int MSG_DIALOG_DISSMISS = 0X112;
    private float mTime = 0;
    private boolean mReady;

    private AudioListener mListener;

    private Runnable mGetVoiceLvRunnable = new Runnable() {
        @Override
        public void run() {
            while(isRecording){
                try{
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_AUDIO_PREPARED:
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLvRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLabelLv(mAudioManager.getVoiceLv(7));
                    break;
                case MSG_DIALOG_DISSMISS:
                    mDialogManager.dissmissDialog();
                    break;
            }
        }
    };

    public AudioRecorderButton(Context context) {
        super(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        mDialogManager = new DialogManager(getContext());

        String dir = "mnt/sdcard/demo_audios";
        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mReady = true;
                mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isRecording = true;
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:
                if(isRecording){
                    if(wantToCancel(x, y)){
                        changeState(STATE_WANT_CANCEL);
                    }else{
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!mReady){
                    reset();
                    return super.onTouchEvent(event);
                }
                if(!isRecording || mTime < 0.6f){
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISSMISS, 1300);
                }else if(mCurState == STATE_RECORDING){
                    mDialogManager.dissmissDialog();
                    mAudioManager.release();

                    if(mListener != null){
                        mListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
                    }
                }else if(mCurState == STATE_WANT_CANCEL){
                    mDialogManager.dissmissDialog();
                    mAudioManager.cancel();
                }
                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 重置
     */
    private void reset(){
        mTime = 0;
        mReady = false;
        isRecording = false;
        changeState(STATE_NORMAL);
    }

    /**
     * 取消发送
     * @param x
     * @param y
     * @return
     */
    private boolean wantToCancel(int x, int y){
        if(x < 0 || x > getWidth()){
            return true;
        }
        if( y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL){
            return true;
        }
        return false;
    }

    /**
     * 状态改变
     * @param state
     */
    private void changeState(int state){
        if(mCurState != state){
            mCurState = state;
            switch(state){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_rec_normal);
                    setText(R.string.str_rec_normal);
                    break;
                case STATE_WANT_CANCEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_rec_want_cancel);
                    mDialogManager.wantToCancel();
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_rec_recording);

                    if(isRecording){
                        mDialogManager.recording();
                    }
                    break;
            }
        }
    }

    public void setAudioListener(AudioListener listener){
        mListener = listener;
    }

    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public void onFinish(float seconds, String filePath) {

    }
}
