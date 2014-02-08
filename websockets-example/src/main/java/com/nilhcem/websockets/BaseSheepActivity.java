package com.nilhcem.websockets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

abstract class BaseSheepActivity extends BaseSocketActivity implements MediaPlayer.OnCompletionListener {

    private View mSpeechBubble;
    private MediaPlayer mMediaPlayer;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSpeechBubble.setVisibility(View.VISIBLE);
            mMediaPlayer.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        mSpeechBubble = findViewById(R.id.speech_bubble);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMediaPlayer = MediaPlayer.create(this, R.raw.baasheep);
        mMediaPlayer.setOnCompletionListener(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(WebSocketsService.ACTION_MSG_RECEIVED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        onCompletion(mMediaPlayer);
    }

    @Override
    protected void onStop() {
        mMediaPlayer.release();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onStop();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mSpeechBubble.setVisibility(View.GONE);
    }

    abstract int getLayoutResID();
}
