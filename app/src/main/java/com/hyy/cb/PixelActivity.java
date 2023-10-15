package com.hyy.cb;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class PixelActivity extends Activity {
    private static PixelActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    public static void finishPixelActivity() {
        if (instance != null) {
            instance.finish();
            instance = null;
        }
    }
}
