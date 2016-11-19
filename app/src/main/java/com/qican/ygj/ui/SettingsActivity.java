/**
 * @Function：鱼管家设置
 * @Author：残阳催雪
 * @Time：2016-11-15
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qican.ygj.R;
import com.qican.ygj.ui.setting.AboutActivity;
import com.qican.ygj.utils.CommonTools;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsActivity extends Activity implements View.OnClickListener {
    private CommonTools myTool;
    private LinearLayout llBack;
    private RelativeLayout rlFeedback, rlAbout, rlService, rlComQuestion;
    private RelativeLayout rlExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        rlFeedback.setOnClickListener(this);
        rlExit.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        rlComQuestion.setOnClickListener(this);
        rlService.setOnClickListener(this);
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        rlFeedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rlExit = (RelativeLayout) findViewById(R.id.rl_exit);
        rlAbout = (RelativeLayout) findViewById(R.id.rl_about);
        rlComQuestion = (RelativeLayout) findViewById(R.id.rl_comquestion);
        rlService = (RelativeLayout) findViewById(R.id.rl_service);

        myTool = new CommonTools(this);
    }

    /**
     * 点击事件
     *
     * @param v：所点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_feedback:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("意见反馈")
                        .setContentText("还在进一步完善当中!")
                        .setConfirmText("确  定!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
//                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.rl_comquestion:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("常见问题")
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
            case R.id.rl_service:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("客服在线")
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
            case R.id.rl_exit:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("退出")
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
            case R.id.rl_about:
                myTool.startActivity(AboutActivity.class);
                break;
        }
    }
}