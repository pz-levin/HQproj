package com.example.hqproj;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class MusicService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra("action");
        if ("play".equals(action)) {
            playMusic();
        } else if ("pause".equals(action)) {
            pauseMusic();
        } else if ("stop".equals(action)) {
            stopMusic();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private MediaPlayer player;

    //播放
    private void playMusic() {
        if (player == null) {
            player = MediaPlayer.create(this,R.raw.summer);
        }
        player.start();
    }

    //暂停
    private void pauseMusic() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    //停止
    private void stopMusic() {
        if (player != null) {
            player.stop();
            player.reset();
            player.release();
            player = null;
        }
    }

    //退出
    private void exitMusic() {
        stopMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
}