package com.hyy.cb;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashMap;

public class HyyAccessibilityService extends AccessibilityService {
    private static final String TAG = "无障碍服务";
    private final HashMap<String, String> APP_PACKAGE_NAME = new HashMap<>();//存储跳过应用的包名和控制按钮的开关
    private SharedPreferences sharedPreferences;
    //用于无障碍服务
    private boolean AlreadyPass = false; // 判定是否已经跳过
    private String LastKnownPackageName = null; //最后打开的AppName

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this;

        saveAppPackageName();
        sharedPreferences = context.getSharedPreferences("CbPreferences", Context.MODE_PRIVATE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        //处理无障碍事件
        if (accessibilityEvent == null) return;
        boolean POWER = sharedPreferences.getBoolean("POWER", false);
        if (POWER) {
            // 获取当前应用包名
            if (accessibilityEvent.getPackageName() == null) return;

            String packageName = accessibilityEvent.getPackageName().toString();

            //判定是否回到主页面切换应用
            if (packageName.contains("launcher") || !packageName.equals(LastKnownPackageName)) {
                AlreadyPass = false;
                LastKnownPackageName = packageName;
                // Log.d(TAG, "打开了新任务");
            }

            main(packageName);

        } else {
            Log.d(TAG, "电源未打开");
        }


    }

    @Override
    public void onInterrupt() {
        //服务中断的处理
        Log.d(TAG, "服务中断");
    }

    private void main(String packageName){
        //处理跳过操作
        if (!AlreadyPass) {
            // 获取当前页面的文字内容
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if (rootNode == null) return;

            if (APP_PACKAGE_NAME.containsKey(packageName)) {
                String ButtonName = APP_PACKAGE_NAME.get(packageName);
                if (sharedPreferences.getBoolean(ButtonName, false)) {
                    operateEnable(rootNode);
                    Log.d(TAG, "跳过"+packageName);
                } else {
                    Log.d(TAG, "开关未打开" + packageName);
                }
            } else {
                if (sharedPreferences.getBoolean("button_switch", false)) {
                    operateEnable(rootNode);
                } else {
                    Log.d(TAG, "未打开其他应用跳过");
                }
            }
        }
    }

    private void operateEnable(AccessibilityNodeInfo node) {
        if (node == null) return;
        if (node.getText() != null) {
            String text = node.getText().toString();
            if (text.contains("跳过")) {
                // 如果文本包含"跳过"，则点击元素
                Log.d(TAG, "跳过完成");
                // Toast.makeText(context, "跳过完成", Toast.LENGTH_SHORT).show();
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                AlreadyPass = true;
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            operateEnable(node.getChild(i));
        }
    }

    private void saveAppPackageName() {
        APP_PACKAGE_NAME.put("com.zhihu.android", "button_zhihu");
        APP_PACKAGE_NAME.put("com.netease.cloudmusic", "button_cloudmusic");
        APP_PACKAGE_NAME.put("tv.danmaku.bili", "button_bili");
        APP_PACKAGE_NAME.put("com.cmcc.cmvideo", "button_migu");
        APP_PACKAGE_NAME.put("com.baidu.tieba", "button_tieba");
    }

}
