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
            ImageView imgPower = view.findViewById(R.id.power);
            SharedPreferences sharedPreferences = context.getSharedPreferences("CbPreferences", Context.MODE_PRIVATE);
            boolean POWER = sharedPreferences.getBoolean("POWER", false);
            if (POWER) {
                mainActivity.startService();
                //切换图片
                imgPower.setImageResource(R.drawable.ic_power_on);
                imgPower.setContentDescription("on");
            } else {
                mainActivity.stopService();
                imgPower.setImageResource(R.drawable.ic_power_off);
                imgPower.setContentDescription("off");
            }

            imgPower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imgPower.getContentDescription().equals("off")) {
                        mainActivity.startService();
                        //切换图片
                        imgPower.setImageResource(R.drawable.ic_power_on);
                        imgPower.setContentDescription("on");

                    } else {
                        mainActivity.stopService();
                        imgPower.setImageResource(R.drawable.ic_power_off);
                        imgPower.setContentDescription("off");
                    }

                }
            });
        }

        return view;
    }
}
