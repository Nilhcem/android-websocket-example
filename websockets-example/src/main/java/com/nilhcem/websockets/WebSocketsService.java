package com.nilhcem.websockets;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.codebutler.android.websockets.WebSocketClient;

import java.net.URI;
import java.nio.charset.Charset;

public class WebSocketsService extends Service implements WebSocketClient.Listener {

    public static final String ACTION_MSG_RECEIVED = "msgReceived";
    public static final String ACTION_NETWORK_STATE_CHANGED = "networkStateChanged";

    private static final String TAG = WebSocketsService.class.getSimpleName();
    private static final String WS_URL = "ws://YOUR_IP:3000/ws/websocket";

    private final IBinder mBinder = new WebSocketsBinder();
    private WebSocketClient mWebSocketClient;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean networkIsOn = intent.getBooleanExtra(ACTION_NETWORK_STATE_CHANGED, false);
            if (networkIsOn) {
                startSocket();
            } else {
                stopSocket();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(WebSocketsService.ACTION_NETWORK_STATE_CHANGED));
        startSocket();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        stopSocket();
        return false;
    }

    @Override
    public void onConnect() {
        Log.i(TAG, "Websocket onConnect()");
    }

    @Override
    public void onMessage(String message) {
        Log.i(TAG, "Websocket onMessage()");
        Log.i(TAG, "Message (String) received: " + message);
        sendMessageReceivedEvent();
    }

    @Override
    public void onMessage(byte[] data) {
        Log.i(TAG, "Websocket onMessage()");
        Log.i(TAG, "Message (byte[]) received: " + new String(data, Charset.defaultCharset()));
        sendMessageReceivedEvent();
    }

    @Override
    public void onDisconnect(int code, String reason) {
        Log.i(TAG, "Websocket onDisconnect()");
        Log.i(TAG, "Code: " + code + " - Reason: " + reason);
    }

    @Override
    public void onError(Exception error) {
        Log.i(TAG, "Websocket onError()");
        if (mWebSocketClient != null) {
            Log.e(TAG, "Error (connection may be lost)", error);
        }
    }

    private void startSocket() {
        mWebSocketClient = new WebSocketClient(URI.create(WS_URL), this, null);
        mWebSocketClient.connect();
    }

    private void stopSocket() {
        if (mWebSocketClient != null) {
            mWebSocketClient.disconnect();
            mWebSocketClient = null;
        }
    }

    private void sendMessageReceivedEvent() {
        Intent intent = new Intent(ACTION_MSG_RECEIVED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public final class WebSocketsBinder extends Binder {
        public WebSocketsService getService() {
            return WebSocketsService.this;
        }
    }
}
