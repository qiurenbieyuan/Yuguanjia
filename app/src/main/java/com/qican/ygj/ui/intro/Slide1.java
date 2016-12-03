package com.qican.ygj.ui.intro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.qican.ygj.R;

public class Slide1 extends Fragment implements ISlideBackgroundColorHolder {
    private View view;
    private String TAG = "Slide1";
    private ImageView ivImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_slide1, container, false);

        ivImg = (ImageView) view.findViewById(R.id.iv_img);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            ivImg.setVisibility(View.GONE);
        } else {
            ivImg.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.Landing)
                    .duration(700)
                    .playOn(ivImg);
        }
    }

    @Override
    public int getDefaultBackgroundColor() {
        return Color.parseColor("#f99401");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        // Set the background color of the view within your slide to which the transition should be applied.
        Log.i(TAG, "setBackgroundColor: " + backgroundColor);
        if (view != null) {
            view.setBackgroundColor(backgroundColor);
        }
    }
}
