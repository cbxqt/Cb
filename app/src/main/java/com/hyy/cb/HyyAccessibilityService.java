package com.hyy.cb;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class HyyAccessibilityService extends AccessibilityService {
    private static final String TAG = "无障碍服务";
    //用于无障碍服务
    private boolean AlreadyPass = false; // 判定是否已经跳过

    private SharedPreferences sharedPreferences;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
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
            Log.d("打开成功", packageName);
            //判定是否回到主页面切换应用
            if (packageName.contains("launcher") && AlreadyPass) {
                AlreadyPass = false;
                Log.d("切换", "返回主页面");
            }

            //处理跳过操作
            if (!AlreadyPass) {
                // 获取当前页面的文字内容
                AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                if (rootNode == null) return;

                //处理不同应用
                switch (packageName) {
                    case "com.zhihu.android":
                        //知乎
                        if (sharedPreferences.getBoolean("button_zhihu", false)) {
                            operateClickable(rootNode);
                        } else {
                            Log.d(TAG, "未打开跳过知乎");
                        }
                        break;
                    case "com.baidu.tieba":
                        //百度贴吧
                        if (sharedPreferences.getBoolean("button_tieba", false)) {
                            operateEnable(rootNode);
                        } else {
                            Log.d(TAG, "未打开跳过贴吧");
                        }
                        break;
                    case "com.netease.cloudmusic":
                        //网易云音乐
                        if (sharedPreferences.getBoolean("button_cloudmusic", false)) {
                            operateClickable(rootNode);
                        } else {
                            Log.d(TAG, "未打开跳过网易云");
                        }
                        break;
                    case "tv.danmaku.bili":
                        //b站
                        if (sharedPreferences.getBoolean("button_bili", false)) {
                            operateClickable(rootNode);
                        } else {
                            Log.d(TAG, "未打开跳过b站");
                        }
                        break;
                    case "com.cmcc.cmvideo":
                        //咪咕视频
                        if (sharedPreferences.getBoolean("button_migu", false)) {
                            operateClickable(rootNode);
                        } else {
                            Log.d(TAG, "未打开跳过咪咕视频");
                        }
                        break;
//                    case "com.dongqiudi.news":
//                        //懂球帝
//                        if (sharedPreferences.getBoolean("button_football", false)) {
//                            // operateEnable(rootNode);
//                        } else {
//                            Log.d(TAG, "未打开跳过懂球帝");
//                        }
//                        break;
                }
            }
        } else {
            Log.d(TAG, "电源未打开");
        }


    }

    @Override
    public void onInterrupt() {
        //服务中断的处理
        Log.d(TAG, "服务中断");
    }

    private void operateClickable(AccessibilityNodeInfo node) {
        if (node == null) return;
        // 遍历节点树，查找包含"跳过"文本的节点并执行点击操作
        if (node.isClickable() && node.getText() != null) {
            String text = node.getText().toString();
            if (text.contains("跳过")) {
                // 如果文本包含"跳过"，则点击元素
                Log.d(TAG, "完成");
                Toast.makeText(context, "跳过完成", Toast.LENGTH_SHORT).show();
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                AlreadyPass = true;
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            operateClickable(node.getChild(i));
        }
    }

    private void operateEnable(AccessibilityNodeInfo node) {
        if (node == null) return;
        if (node.isEnabled() && node.getText() != null) {
            String text = node.getText().toString();
            if (text.contains("跳过")) {
                if (node.isClickable()) {
                    // 如果目标节点是可点击的，直接执行点击操作
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.d(TAG, "点击完成");
                } else {
                    // 如果目标节点不可点击，模拟点击操作
                    Rect nodeRect = new Rect();
                    node.getBoundsInScreen(nodeRect); //点击这个node
                    int centerX = nodeRect.left + nodeRect.width() / 2;
                    int centerY = nodeRect.top + nodeRect.height() / 2;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里发送手势
                            // 创建一个Path对象
                            Path path = new Path();

                            // 修改路径，将手势的起点和终点位置进行微调
                            path.moveTo(centerX, centerY);
                            path.lineTo(centerX, centerY);

                            // 创建GestureDescription.StrokeDescription对象，指定路径和延迟
                            int gestureDuration = 500; // 延迟时间，单位为毫秒
                            GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(path, 0, gestureDuration);

                            // 创建手势描述
                            GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
                            gestureBuilder.addStroke(strokeDescription);

                            // 构建手势描述
                            GestureDescription gesture = gestureBuilder.build();
                            dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
                                @Override
                                public void onCompleted(GestureDescription gestureDescription) {
                                    Log.d(TAG, "模拟点击完成");
                                    Toast.makeText(context, "模拟点击完成", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(GestureDescription gestureDescription) {
                                    Log.d(TAG, "模拟点击被取消");
                                    Toast.makeText(context, "模拟点击被取消", Toast.LENGTH_SHORT).show();
                                }
                            }, new Handler());
                        }
                    }, 3000); // 1000毫秒延迟示例，可以根据需要调整


                }
                Log.d(TAG, "跳过完成");
                Toast.makeText(context, "跳过完成", Toast.LENGTH_SHORT).show();
            }
        }


        for (int i = 0; i < node.getChildCount(); i++) {
            operateEnable(node.getChild(i));
        }
    }

}
