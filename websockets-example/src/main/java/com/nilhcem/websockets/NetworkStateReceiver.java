package com.nilhcem.websockets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkStateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Network connectivity change");

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkIsOn = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        Intent broadcastIntent = new Intent(WebSocketsService.ACTION_NETWORK_STATE_CHANGED);
        broadcastIntent.putExtra(WebSocketsService.ACTION_NETWORK_STATE_CHANGED, networkIsOn);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
    }
}
