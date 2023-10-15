package com.hyy.cb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // 屏幕锁屏时，启动像素 Activity
            Intent pixelIntent = new Intent(context, PixelActivity.class);
            pixelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(pixelIntent);
        } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            // 用户解锁时，销毁像素 Activity
            PixelActivity.finishPixelActivity();
        }
    }
}
