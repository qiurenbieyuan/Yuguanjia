/**
 * 秋人别院，侧滑菜单
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qican.ygj.R;
import com.qican.ygj.listener.OnFramentListener;
import com.qican.ygj.ui.login.LoginActivity;
import com.qican.ygj.ui.userinfo.MyInfoActivity;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.view.CircleImageView;
import com.videogo.openapi.EZOpenSDK;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SlideMenuFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "SlideMenuFragment";
    private CommonTools myTool;
    private OnFramentListener mCallBack;
    private RelativeLayout rlUserInfo, rlMyPond, rlMyCamera, rlAddPond, rlAddCamera, rlLinkToEZ, rlMyInfo;
    private CircleImageView civHeadImg;
    private ImageView ivMsg, ivSetting;
    private TextView tvNickName, tvSignature;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu, container, false);

        initView(view);
        initEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (!myTool.isLogin()) {
            civHeadImg.setImageBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.defaultheadpic));
            return;
        }
        //显示头像
        myTool.showImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=571129321,501172449&fm=21&gp=0.jpg", civHeadImg);
        tvNickName.setText(myTool.getNickName());
        tvSignature.setText(myTool.getSignature());
    }

    private void initEvent() {
        rlUserInfo.setOnClickListener(this);
        rlMyPond.setOnClickListener(this);
        rlMyCamera.setOnClickListener(this);
        rlAddPond.setOnClickListener(this);
        rlAddCamera.setOnClickListener(this);
        rlLinkToEZ.setOnClickListener(this);
        rlMyInfo.setOnClickListener(this);

        ivMsg.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        civHeadImg.setOnLongClickListener(this);
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
        rlMyInfo = (RelativeLayout) v.findViewById(R.id.rl_myinfo);

        civHeadImg = (CircleImageView) v.findViewById(R.id.civ_headpic);
        ivMsg = (ImageView) v.findViewById(R.id.iv_msg);
        ivSetting = (ImageView) v.findViewById(R.id.iv_setting);

        tvNickName = (TextView) v.findViewById(R.id.tv_nickname);
        tvSignature = (TextView) v.findViewById(R.id.tv_signature);

        myTool = new CommonTools(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userinfo:
                myTool.startActivity(MyInfoActivity.class);
                break;
            case R.id.rl_mypond:
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("我的池塘")
                        .setContentText("注销萤石账号？")
                        .setConfirmText("是的，注销！")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        EZOpenSDK.getInstance().logout();
                                    }
                                }.start();
                            }
                        })
                        .show();
                break;
            case R.id.rl_mycamera:
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("我的设备")
                        .setContentText("还在进一步完善当中!")
                        .setConfirmText("确  定!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
            case R.id.rl_addpond:
                startActivity(new Intent(getActivity(), AddPondActivity.class));
                break;
            case R.id.rl_addcamera:
                myTool.startActivity(AddCameraActivity.class);
                break;
            case R.id.iv_msg:
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("系统消息")
                        .setContentText("还在进一步完善当中!")
                        .setConfirmText("确  定!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
            case R.id.iv_setting:
                myTool.startActivity(SettingsActivity.class);
                break;
            case R.id.rl_linktoez:
                EZOpenSDK.getInstance().openLoginPage();
                break;
            case R.id.rl_myinfo:
                myTool.startActivity(MyInfoActivity.class);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.civ_headpic:
                myTool.startActivity(LoginActivity.class);
                break;
        }
        return false;
    }
}
