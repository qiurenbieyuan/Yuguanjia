/**
 * 欢迎界面
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.qican.ygj.MainActivity;
import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;


public class WelcomeActivity extends Activity {
    private RelativeLayout mRlSkip;
    private TextView mTvDownTime;
    private CountDownTimer mTimer;
    private CommonTools myTool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 页面设置为沉浸式状态栏风格
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        initView();
        initEvent();

        toMain();
//        if (myTool.isFirstUse()) {
//            startActivity(new Intent(this, GuidePageActivity.class));
//            myTool.setFirstUse(false);
//            finish();
//        } else {
//            toMain();
//        }
    }


    private void toMain() {
        findViewById(R.id.iv_welcome).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_alog).setVisibility(View.GONE);
        //动画
        YoYo.with(Techniques.Landing)
                .withListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        findViewById(R.id.iv_alog).setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.DropOut)
                                .duration(1000)
                                .playOn(findViewById(R.id.iv_alog));
                    }
                })
                .duration(1000)
                .playOn(findViewById(R.id.iv_welcome));

        mTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvDownTime.setText(Long.toString(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
                // 设置淡入淡出效果
//                int VERSION=Integer.parseInt(android.os.Build.VERSION.SDK);
//                if(VERSION >= 5){
//                    WelcomeActivity.this.overridePendingTransition(R.anim.alpha_out,R.anim.alpha_in);
//                }
            }
        }.start();
    }

    private void initEvent() {
        mRlSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.cancel(); // 关闭倒计时并跳转
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initView() {
        mRlSkip = (RelativeLayout) findViewById(R.id.rl_skip);
        mTvDownTime = (TextView) findViewById(R.id.tv_countdowntime);

        myTool = new CommonTools(this);
    }
}
