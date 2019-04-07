package com.ez08.compass.ui.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ez08.compass.R;
import com.ez08.compass.auth.AuthModule;
import com.ez08.compass.ui.base.BaseActivity;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MediaPlayerActivity extends BaseActivity {

    MediaPlayer mediaPlayer;
    public IjkMediaPlayer ijkplayer;
//    final String url = "http://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/gear0/prog_index.m3u8";
        String url = "http://imapp.compass.cn:8000/hls/WavMain.exe_rooms_290_20190407.m3u8";
    Button mediaPlayerButton;
    Button ijkPlayerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        mediaPlayerButton = findViewById(R.id.button1);
        mediaPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                // 通过异步的方式装载媒体资源
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // 装载完毕回调
                        mediaPlayer.start();
                    }
                });

            }
        });

        ijkPlayerButton = findViewById(R.id.button2);
        ijkPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ijkplayer = new IjkMediaPlayer();
                try {
                    ijkplayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ijkplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                ijkplayer.prepareAsync();
                ijkplayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(IMediaPlayer iMediaPlayer) {
                        ijkplayer.start();
                    }
                });
            }
        });
    }
}
