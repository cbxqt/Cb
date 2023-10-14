package com.hyy.cb;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class HyyAccessibilityService extends AccessibilityService {
    private boolean AlreadyPass = false; // 判定是否已经跳过
    private String PrimaryPackageName = null; //判定是否新打开应用
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        //处理无障碍事件
        if (accessibilityEvent == null) return;


        // 获取当前应用包名
        String packageName = accessibilityEvent.getPackageName().toString();
        Log.d("打开成功", packageName);

        if (!packageName.equals(PrimaryPackageName)) {
            AlreadyPass = false;
        }

        if (packageName.equals("com.zhihu.android") && !AlreadyPass) {
            Log.d("知乎", "打开成功");
            // 获取当前页面的文字内容
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            if (rootNode == null) return;

            // 遍历节点树，查找包含"跳过"文本的节点并执行点击操作
            findClickableElements(rootNode, packageName);
        }
    }

    @Override
    public void onInterrupt() {
        //服务中断的处理
        Log.d("服务未开启", "onInterrupt: ");
    }

    private void findClickableElements(AccessibilityNodeInfo node, String packageName) {
        if (node == null) return;

        if (node.isClickable() && node.getText() != null) {
            String text = node.getText().toString();
            if (!text.isEmpty()) {
                // 在这里处理获取到的文字内容
                // Log.d("AccessibilityDemo", "可点击元素的文字内容: " + text);
                if (text.contains("跳过")) {
                    // 如果文本包含"跳过"，则点击元素
                    Log.d("AccessibilityDemo", "包含'跳过'的元素被点击: " + text);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    AlreadyPass = true;
                    PrimaryPackageName = packageName;
                }
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            findClickableElements(node.getChild(i), packageName);
        }
    }
}
