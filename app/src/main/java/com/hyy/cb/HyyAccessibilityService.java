package com.hyy.cb;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

public class HyyAccessibilityService extends AccessibilityService {
    private boolean AlreadyPass = false; // 判定是否已经跳过

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        //处理无障碍事件
        if (accessibilityEvent == null) return;


        // 获取当前应用包名
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
            if (packageName.equals("com.zhihu.android")) {
                //知乎
                operateZhihu(rootNode);

            } else if (packageName.equals("com.baidu.tieba")) {
                //百度贴吧
                operateTieba(rootNode);
            }
        }
    }

    @Override
    public void onInterrupt() {
        //服务中断的处理
        Log.d("服务未开启", "onInterrupt: ");
    }

    private void operateZhihu(AccessibilityNodeInfo node) {
        if (node == null) return;
        // 遍历节点树，查找包含"跳过"文本的节点并执行点击操作
        if (node.isClickable() && node.getText() != null) {
            String text = node.getText().toString();
            if (text.contains("跳过")) {
                // 如果文本包含"跳过"，则点击元素
                Log.d("AccessibilityDemo", "包含'跳过'的元素被点击: " + text);
                Toast.makeText(getApplicationContext(), "跳过完成", Toast.LENGTH_SHORT).show();
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                AlreadyPass = true;
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            operateZhihu(node.getChild(i));
        }
    }

    private void operateTieba(AccessibilityNodeInfo node) {
        if (node == null) return;
        if (node.isEnabled() && node.getText() != null) {
            String text = node.getText().toString();
            if (text.contains("跳过")) {
                if (node.isClickable()) {
                    // 如果目标节点是可点击的，直接执行点击操作
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    // 如果目标节点不可点击，模拟点击操作
                    Bundle arguments = new Bundle();
                    arguments.putBoolean("ACTION_ARGUMENT_CLICKABLE", true);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK, arguments);
                }
                Toast.makeText(getApplicationContext(), "跳过完成", Toast.LENGTH_SHORT).show();
            }
        }


        for (int i = 0; i < node.getChildCount(); i++) {
            operateTieba(node.getChild(i));
        }
    }
}
