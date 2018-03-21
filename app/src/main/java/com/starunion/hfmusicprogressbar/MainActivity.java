package com.starunion.hfmusicprogressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.starunion.progress_lib.HfMusicProgressBar;

public class MainActivity extends AppCompatActivity {
    private HfMusicProgressBar musicProgressBar;
    private float progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicProgressBar = findViewById(R.id.pb_music);
        musicProgressBar.setProgressBackgroud(R.drawable.btn_jiancaiyinyue);
        mHandler.sendEmptyMessageDelayed(0, 20);

    }

    Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (progress < 100){
                progress += 0.1;
                mHandler.sendEmptyMessageDelayed(0, 20);
                musicProgressBar.setProgress(progress);
            }
        }
    };
}
