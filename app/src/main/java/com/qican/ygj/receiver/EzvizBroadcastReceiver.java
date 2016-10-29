package com.qican.ygj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class EzvizBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("com.videogo.action.OAUTH_SUCCESS_ACTION".equals(action)) {
            Toast.makeText(context, "已关联成功！", Toast.LENGTH_SHORT).show();
        } else if ("action:android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            Toast.makeText(context, "网络状态发生变化！", Toast.LENGTH_SHORT).show();
        }
        Log.i("EzvizBroadcastReceiver", "action:" + action);
    }
}
