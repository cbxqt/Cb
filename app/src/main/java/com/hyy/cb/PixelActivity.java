package com.hyy.cb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class PixelActivity extends Activity {
    private static PixelActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("de", "启动");
        Window window = getWindow();
        //放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        //设置宽高
        params.width = 1;
        params.height = 1;
        //设置起始坐标
        params.x = 0;
        params.y = 0;
        window.setAttributes(params);
        instance = this;
    }

    public static void finishPixelActivity() {
        if (instance != null) {
            instance.finish();
            instance = null;
        }
    }
}
