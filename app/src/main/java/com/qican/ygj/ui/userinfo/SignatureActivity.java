/**
 * @Function：我的签名
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


public class SignatureActivity extends Activity implements View.OnClickListener {
    private CommonTools myTool;
    private LinearLayout llBack, llSave;
    private EditText edtAutograph;
    private SweetAlertDialog mDialog;
    private String signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signature);

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
        edtAutograph = (EditText) findViewById(R.id.edt_autograph);

        myTool = new CommonTools(this);
    }

    private void initData() {
        edtAutograph.setText(myTool.getSignature());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_save:
                setAutograph();
                break;
        }
    }

    private void setAutograph() {
        // 没有登录的话就直接返回了
        if (!myTool.isLoginWithDialog()) {
            return;
        }

        signature = edtAutograph.getText().toString();
        if (signature.equals(myTool.getSignature())) {
            myTool.showInfo("签名没有变化");
            return;
        }

        if (!"".equals(signature)) {

            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在修改···");
            mDialog.show();

            //更改服务器信息
            String url = ConstantValue.SERVICE_ADDRESS + "updateUserInformation";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("userId", myTool.getUserId())
                    .addParams("signature", signature)
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
                                    myTool.setAutograph(signature);
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
                            edtAutograph.setError("还没有输入昵称哦！");
                        }
                    })
                    .playOn(edtAutograph);
        }
    }
}
