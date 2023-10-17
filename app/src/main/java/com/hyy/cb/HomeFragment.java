package com.hyy.cb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private Context context;
    private ImageView imgPower;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            imgPower = view.findViewById(R.id.power);
            sharedPreferences = context.getSharedPreferences("CbPreferences", Context.MODE_PRIVATE);

            if (sharedPreferences.getBoolean("POWER", false) && mainActivity.isAccessibilityEnabled()) {
                //切换图片
                imgPower.setImageResource(R.drawable.ic_power_on);
            } else {
                imgPower.setImageResource(R.drawable.ic_power_off);
            }
            imgPower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!sharedPreferences.getBoolean("POWER", false)) {
                        //检查是否开启无障碍服务
                        if (mainActivity.isAccessibilityEnabled()) {
                            //开启标志
                            sharedPreferences.edit().putBoolean("POWER", true).apply();
                            //切换图片
                            imgPower.setImageResource(R.drawable.ic_power_on);
                        }

                    } else {
                        sharedPreferences.edit().putBoolean("POWER", false).apply();
                        imgPower.setImageResource(R.drawable.ic_power_off);
                    }

                }
            });
        }
        return view;
    }

}
