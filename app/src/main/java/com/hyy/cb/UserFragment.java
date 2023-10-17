package com.hyy.cb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = requireContext();
        sharedPreferences = context.getSharedPreferences("CbPreferences", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initializeButton(view);
        return view;
    }

    private void initializeButton(View view) {
        ImageButton button_inform = view.findViewById(R.id.button_inform);
        ImageButton button_zhihu = view.findViewById(R.id.button_zhihu);
        ImageButton button_tieba = view.findViewById(R.id.button_tieba);
        ImageButton button_cloudmusic = view.findViewById(R.id.button_cloudmusic);
        ImageButton button_bili = view.findViewById(R.id.button_bili);
        ImageButton button_migu = view.findViewById(R.id.button_migu);
        ImageButton button_football = view.findViewById(R.id.button_football);
        //初始化数据
        initializeStatus(button_inform, "button_inform");
        initializeStatus(button_zhihu, "button_zhihu");
        initializeStatus(button_tieba, "button_tieba");
        initializeStatus(button_cloudmusic, "button_cloudmusic");
        initializeStatus(button_bili, "button_bili");
        initializeStatus(button_migu, "button_migu");
        initializeStatus(button_football, "button_football");
        //监听点击事件
        buttonClicked(button_inform, "button_inform");
        buttonClicked(button_zhihu, "button_zhihu");
        buttonClicked(button_tieba, "button_tieba");
        buttonClicked(button_cloudmusic, "button_cloudmusic");
        buttonClicked(button_bili, "button_bili");
        buttonClicked(button_migu, "button_migu");
        buttonClicked(button_football, "button_football");
    }

    private void initializeStatus(ImageButton imageButton, String buttonName) {
        if (sharedPreferences.getBoolean(buttonName, false)) {
            imageButton.setBackgroundResource(R.drawable.ic_button_background_selected);
        } else {
            imageButton.setBackgroundResource(R.drawable.ic_button_background_unselected);
        }
    }

    private void buttonClicked(ImageButton imageButton, String buttonName) {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sharedPreferences.getBoolean(buttonName, false)) {
                    sharedPreferences.edit().putBoolean(buttonName, true).apply();
                    imageButton.setBackgroundResource(R.drawable.ic_button_background_selected);
                } else {
                    sharedPreferences.edit().putBoolean(buttonName, false).apply();
                    imageButton.setBackgroundResource(R.drawable.ic_button_background_unselected);
                }
            }
        });
    }
}
