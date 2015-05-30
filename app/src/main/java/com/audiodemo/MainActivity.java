package com.audiodemo;

import com.audiodemo.adapter.RecordAdapter;
import com.audiodemo.listener.AudioListener;
import com.audiodemo.util.MediaManager;
import com.audiodemo.widget.AudioRecorderButton;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 * Created by apple on 5/29/15.
 */
public class MainActivity extends ActionBarActivity {
    private ListView lv;
    private ArrayAdapter<Recorder> adapter;
    private List<Recorder> list = new ArrayList<>();
    private AudioRecorderButton btn;
    private View animView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv);
        btn = (AudioRecorderButton)findViewById(R.id.arButton);

        btn.setAudioListener(new AudioListener() {
            @Override
            public void wellPrepared() {

            }

            @Override
            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds,filePath);
                list.add(recorder);
                adapter.notifyDataSetChanged();
                lv.setSelection(list.size()-1);
            }
        });

        adapter = new RecordAdapter(this, list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(animView != null){
                    animView.setBackgroundResource(R.mipmap.adj);
                    animView = null;
                }

                //播放动画
                animView = view.findViewById(R.id.vAnim);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable anim = (AnimationDrawable)animView.getBackground();
                anim.start();

                //播放音频
                MediaManager.playSound(list.get(position).filePath,
                        new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                animView.setBackgroundResource(R.mipmap.adj);
                            }
                        });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    public class Recorder{
        float time;
        String filePath;

        public Recorder(float time, String filePath) {
            super();
            this.time = time;
            this.filePath = filePath;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

}
