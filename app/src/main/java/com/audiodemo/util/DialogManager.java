package com.audiodemo.util;

import com.audiodemo.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 弹窗管理类
 * Created by apple on 5/29/15.
 */
public class DialogManager {

    private Dialog mDialog;
    private Context mContext;

    private ImageView ivIcon, ivVoice;
    private TextView mLabel;

    public DialogManager(Context context){
        mContext = context;
    }

    /**
     * 显示录音弹窗
     */
    public void showRecordingDialog(){
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_recorder, null);
        mDialog.setContentView(view);

        ivIcon = (ImageView)mDialog.findViewById(R.id.ivIcon);
        ivVoice = (ImageView)mDialog.findViewById(R.id.ivVoice);
        mLabel = (TextView)mDialog.findViewById(R.id.tvRecorder);

        mDialog.show();
    }

    /**
     * 录音中
     */
    public void recording(){
        if(mDialog != null && mDialog.isShowing()){
            ivIcon.setVisibility(View.VISIBLE);
            ivVoice.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.VISIBLE);

            ivIcon.setImageResource(R.mipmap.recorder);
            mLabel.setText("手指上滑，取消发送");
        }
    }

    /**
     * 取消发送
     */
    public void wantToCancel(){
        if(mDialog != null && mDialog.isShowing()){
            ivIcon.setVisibility(View.VISIBLE);
            ivVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);

            ivIcon.setImageResource(R.mipmap.cancel);
            mLabel.setText("松开手指，取消发送");
        }
    }

    /**
     * 录音过短
     */
    public void tooShort(){
        if(mDialog != null && mDialog.isShowing()){
            ivIcon.setVisibility(View.VISIBLE);
            ivVoice.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);

            ivIcon.setImageResource(R.mipmap.voice_to_short);
            mLabel.setText("录音时间过短");
        }
    }

    /**
     * 关闭弹窗
     */
    public void dissmissDialog(){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 更新声音等级
     * @param lv
     */
    public void updateVoiceLabelLv(int lv){
        if(mDialog != null && mDialog.isShowing()){
            int resId = mContext.getResources().getIdentifier("v" + lv, "mipmap" , mContext.getPackageName());
            ivVoice.setImageResource(resId);
        }
    }

}
