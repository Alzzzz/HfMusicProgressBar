package com.starunion.hfmusicprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.starunion.progress_lib.HfMusicProgressBar;
import com.starunion.progress_lib.OnProgressBarScrollListener;

public class MainActivity extends AppCompatActivity {
    private HfMusicProgressBar musicProgressBar;
    private float progress;
    private boolean playingMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicProgressBar = findViewById(R.id.pb_music);
        musicProgressBar.setProgressBackgroud(R.drawable.btn_jiancaiyinyue);
        musicProgressBar.setUnitLength(20000);
        musicProgressBar.setMaxLength(2 * 60 * 1000L);
        musicProgressBar.setOnProgressBarScrollListener(new OnProgressBarScrollListener() {
            @Override
            public void onScrollStart() {
                playingMusic = false;
                progress = 0;
                musicProgressBar.setProgress(progress);
            }

            @Override
            public void onScrollEnd(long startLength) {
                Log.d("MainActivity", "当前时间："+startLength);
                playingMusic = true;
            }
        });
        mHandler.sendEmptyMessageDelayed(0, 20);

    }

    Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (playingMusic){
                if (progress < 100){
                    progress += 0.1;
                    musicProgressBar.setProgress(progress);
                }
            }
            mHandler.sendEmptyMessageDelayed(0, 20);
        }
    };
}
