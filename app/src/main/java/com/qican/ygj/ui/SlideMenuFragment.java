/**
 * 秋人别院，侧滑菜单
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qican.ygj.R;
import com.qican.ygj.bean.Camera;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.listener.OnFramentListener;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.view.CircleImageView;
import com.videogo.main.EzvizWebViewActivity;
import com.videogo.openapi.EZOpenSDK;

import java.util.ArrayList;
import java.util.List;


public class SlideMenuFragment extends Fragment implements View.OnClickListener {
    private CommonTools myTool;
    private static final String TAG = "SlideMenuFragment";

    private OnFramentListener mCallBack;
    private RelativeLayout rlUserInfo, rlMyPond, rlMyCamera, rlAddPond, rlAddCamera, rlLinkToEZ;
    private CircleImageView civHeadImg;
    private ImageView ivMsg, ivSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu, container, false);

        initView(view);
        initEvent();
        initData();


        return view;
    }

    private void initData() {
        //显示头像
        myTool.showImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=571129321,501172449&fm=21&gp=0.jpg", civHeadImg);
    }

    private void initEvent() {
        rlUserInfo.setOnClickListener(this);
        rlMyPond.setOnClickListener(this);
        rlMyCamera.setOnClickListener(this);
        rlAddPond.setOnClickListener(this);
        rlAddCamera.setOnClickListener(this);
        rlLinkToEZ.setOnClickListener(this);

        ivMsg.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mCallBack == null) {
            mCallBack = (OnFramentListener) activity;
        }
    }

    private void initView(View v) {
        rlUserInfo = (RelativeLayout) v.findViewById(R.id.rl_userinfo);
        rlMyPond = (RelativeLayout) v.findViewById(R.id.rl_mypond);
        rlMyCamera = (RelativeLayout) v.findViewById(R.id.rl_mycamera);
        rlAddPond = (RelativeLayout) v.findViewById(R.id.rl_addpond);
        rlAddCamera = (RelativeLayout) v.findViewById(R.id.rl_addcamera);
        rlLinkToEZ = (RelativeLayout) v.findViewById(R.id.rl_linktoez);

        civHeadImg = (CircleImageView) v.findViewById(R.id.civ_headpic);
        ivMsg = (ImageView) v.findViewById(R.id.iv_msg);
        ivSetting = (ImageView) v.findViewById(R.id.iv_setting);

        myTool = new CommonTools(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userinfo:
                myTool.showInfo("我的信息");
                break;
            case R.id.rl_mypond:
                myTool.showInfo("我的池塘");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        EZOpenSDK.getInstance().logout();
                    }
                }.start();
                break;
            case R.id.rl_mycamera:
                myTool.showInfo("我的设备");
                break;
            case R.id.rl_addpond:
                startActivity(new Intent(getActivity(), AddPondActivity.class));
                break;
            case R.id.rl_addcamera:
                myTool.showInfo("添加监控设备");
                break;
            case R.id.iv_msg:
                myTool.showInfo("系统消息");
                break;
            case R.id.iv_setting:
                myTool.showInfo("系统设置");
                break;
            case R.id.rl_linktoez:
                EZOpenSDK.getInstance().openLoginPage();
                break;
        }
    }
}
