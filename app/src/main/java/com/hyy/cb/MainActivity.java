package com.hyy.cb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "主任务";
    private final Fragment HomeFragment = new HomeFragment();
    private final Fragment UserFragment = new UserFragment();
    private SharedPreferences sharedPreferences;
    private Intent serviceInformIntent;
    private IntentFilter backgroundFilter;
    private ScreenReceiver backgroundReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBottomNavigationView(); //底部导航栏
        showFragment(HomeFragment); //默认主页面

        //加载数据库
        sharedPreferences = this.getSharedPreferences("CbPreferences", Context.MODE_PRIVATE);

        //防止杀后台
        backgroundFilter = new IntentFilter();
        backgroundFilter.addAction(Intent.ACTION_SCREEN_OFF);
        backgroundFilter.addAction(Intent.ACTION_USER_PRESENT);
        backgroundReceiver = new ScreenReceiver();

        //常驻通知栏
        serviceInformIntent = new Intent(this, HyyForegroundService.class);

    }

    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // 设置底部导航栏的选择监听器
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //选中home页面
                        showFragment(HomeFragment);
                        break;
                    case R.id.navigation_user:
                        //选中user页面
                        showFragment(UserFragment);
                        break;
                }
                return true; // 表示已处理项目选择
            }
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public boolean isAccessibilityEnabled() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager != null) {
            boolean isEnabled = accessibilityManager.isEnabled();
            if (!isEnabled) {
                Log.d(TAG, "无障碍服务未打开");
                Toast.makeText(this, "无障碍服务未打开", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Log.d(TAG, "无障碍服务已打开");
                sharedPreferences.edit().putBoolean("POWER", true).apply();
                return true;
            }
        } else {
            return false;
        }
    }


    public void checkService(boolean buttonOn, String buttonName) {
        if (buttonName.equals("button_inform")) {
            if (buttonOn) {
                //开启常驻通知栏
                startService(serviceInformIntent);
                sharedPreferences.edit().putBoolean("serviceInform", true).apply();
            } else if (sharedPreferences.getBoolean("serviceInform", false)) {
                //关闭通知栏
                stopService(serviceInformIntent);
                sharedPreferences.edit().putBoolean("serviceInform", false).apply();
            }
        } else if (buttonName.equals("button_preventkill")) {
            if (buttonOn) {
                //开启防杀后台
                registerReceiver(backgroundReceiver, backgroundFilter);
                sharedPreferences.edit().putBoolean("backgroundReceiver", true).apply();
            } else if (sharedPreferences.getBoolean("backgroundReceiver", false)) {
                //关闭防杀后台
                unregisterReceiver(backgroundReceiver);
                sharedPreferences.edit().putBoolean("backgroundReceiver", false).apply();
            }
        }

    }

}