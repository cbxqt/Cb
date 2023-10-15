package com.hyy.cb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final Fragment HomeFragment = new HomeFragment();
    private final Fragment UserFragment = new UserFragment();
    private final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private boolean serviceStart = false;
    private Intent serviceInformIntent;
    private IntentFilter backgroundFilter;
    private ScreenReceiver backgroundReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBottomNavigationView();
        showFragment(HomeFragment); //默认主页面

        //防止杀后台
        backgroundFilter = new IntentFilter();
        backgroundFilter.addAction(Intent.ACTION_SCREEN_OFF);
        backgroundFilter.addAction(Intent.ACTION_USER_PRESENT);
        backgroundReceiver = new ScreenReceiver();

        //常驻通知栏
        serviceInformIntent = new Intent(this, HyyForegroundService.class);

    }

    @Override
    protected void onPause() {
        super.onPause();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 检查是否已经添加了同样的Fragment
        Fragment existingFragment = fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());
        if (existingFragment != null) {
            // 如果已经添加了同样的Fragment，直接显示它
            transaction.show(existingFragment);
        } else {
            // 如果没有添加同样的Fragment，则添加新的Fragment
            transaction.add(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        }

        // 隐藏其他已添加的Fragment
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList != null) {
            for (Fragment f : fragmentList) {
                if (f != existingFragment) {
                    transaction.hide(f);
                }
            }
        }

        transaction.commit();
    }

    public void startService() {
        //开启常驻通知栏
        startService(serviceInformIntent);
        //开启广播
        registerReceiver(backgroundReceiver, backgroundFilter);
        serviceStart = true;
    }

    public void stopService() {
        if (serviceStart) {
            //关闭通知栏
            stopService(serviceInformIntent);
            //关闭广播
            unregisterReceiver(backgroundReceiver);
        }
    }

}