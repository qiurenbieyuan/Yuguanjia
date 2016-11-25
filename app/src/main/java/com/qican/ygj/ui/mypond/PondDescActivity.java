/**
 * @Function：我的签名
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.mypond;

import android.app.Activity;
import android.content.Intent;
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
import com.qican.ygj.bean.Pond;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;


public class PondDescActivity extends Activity implements View.OnClickListener {

    public static final String KEY_POND_DESC = "KEY_POND_DESC";
    private CommonTools myTool;
    private LinearLayout llBack, llSave;
    private EditText edtPondDesc;
    private SweetAlertDialog mDialog;
    private String pondDesc, sourcePondDesc;
    private Pond mPond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ponddesc);

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
        edtPondDesc = (EditText) findViewById(R.id.edt_pond_desc);

        myTool = new CommonTools(this);
    }

    private void initData() {
        mPond = new Pond();
        mPond = (Pond) myTool.getParam(mPond);
        sourcePondDesc = mPond.getDesc();

        edtPondDesc.setText(sourcePondDesc);
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

        pondDesc = edtPondDesc.getText().toString();
        if (pondDesc.equals(sourcePondDesc)) {
            myTool.showInfo("描述没有变化");
            return;
        }

        if (!"".equals(pondDesc)) {

            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在修改···");
            mDialog.show();

            //更改服务器信息
            String url = ConstantValue.SERVICE_ADDRESS + "updatePond";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("pondId", mPond.getId())
                    .addParams("pondDescrible", pondDesc)
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
                                    //修改成功装载新的池塘名称
                                    Intent intent = new Intent();
                                    intent.putExtra(KEY_POND_DESC, pondDesc);
                                    setResult(RESULT_OK, intent);

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
                            edtPondDesc.setError("还没有输入相关信息哦！");
                        }
                    })
                    .playOn(edtPondDesc);
        }
    }
}
