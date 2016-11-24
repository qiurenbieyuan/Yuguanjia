/**
 * @Function：我的个人信息页面
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.userinfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;


public class NickNameActivity extends Activity implements View.OnClickListener {
    private CommonTools myTool;
    private LinearLayout llBack, llSave;
    private EditText edtNickName;
    private String nickName;
    private SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        llSave.setOnClickListener(this);
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llSave = (LinearLayout) findViewById(R.id.ll_save);
        edtNickName = (EditText) findViewById(R.id.edt_nickname);

        myTool = new CommonTools(this);
    }

    private void initData() {
        edtNickName.setText(myTool.getNickName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_save:
                setNickName();
                break;
        }
    }

    private void setNickName() {
        // 没有登录的话就直接返回了
        if (!myTool.isLoginWithDialog()) {
            return;
        }

        nickName = edtNickName.getText().toString();
        if (nickName.equals(myTool.getNickName())) {
            myTool.showInfo("昵称没有变化");
            return;
        }

        if (!"".equals(nickName)) {

            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在修改···");
            mDialog.show();

            //更改服务器信息
            String url = ConstantValue.SERVICE_ADDRESS + "updateUserInformation";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("userId", myTool.getUserId())
                    .addParams("userName", nickName)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            mDialog.setTitleText("修改失败")
                                    .setContentText("修改失败，异常信息 e-->[" + e.toString() + "]")
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            switch (response) {
                                case "success":
                                    myTool.setNickName(nickName);
                                    mDialog.setTitleText("修改成功")
                                            .setConfirmText("好  的")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    new CountDownTimer(1000, 1000) {

                                                        @Override
                                                        public void onTick(long millisUntilFinished) {

                                                        }

                                                        @Override
                                                        public void onFinish() {
                                                            finish();
                                                        }
                                                    }.start();
                                                }
                                            }).changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    break;
                                case "error":
                                    mDialog.setTitleText("修改失败了！")
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                    break;
                            }
                        }


                    });


        } else {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .withListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            edtNickName.setError("还没有输入昵称哦！");
                        }
                    })
                    .playOn(edtNickName);
        }
    }

}
