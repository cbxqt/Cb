package com.hyy.cb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView imgPower = view.findViewById(R.id.power);
        imgPower.setOnClickListener(new View.OnClickListener() {
            final MainActivity mainActivity = (MainActivity) getActivity();
            @Override
            public void onClick(View view) {
                if (mainActivity != null) {
                    if (imgPower.getContentDescription().equals("off")) {
                        //切换图片
                        imgPower.setImageResource(R.drawable.ic_power_on);
                        imgPower.setContentDescription("on");
                        mainActivity.startService();
                    } else {
                        imgPower.setImageResource(R.drawable.ic_power_off);
                        imgPower.setContentDescription("off");
                        mainActivity.stopService();
                    }
                }

            }
        });
        return view;
    }
}
