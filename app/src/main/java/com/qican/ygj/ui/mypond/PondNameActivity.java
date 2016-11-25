/**
 * @Function：我的个人信息页面
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.mypond;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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


public class PondNameActivity extends Activity implements View.OnClickListener {
    public static final String KEY_PONDNAME = "KEY_PONDNAME";
    private CommonTools myTool;
    private LinearLayout llBack, llSave;
    private EditText edtPondName;
    private Pond mPond;
    private String mPondName, sourcePondName;
    private SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pondname);
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
        edtPondName = (EditText) findViewById(R.id.edt_pond_name);

        myTool = new CommonTools(this);
    }

    private void initData() {
        //获取上一页面传过来的池塘信息
        mPond = new Pond();
        mPond = (Pond) myTool.getParam(mPond);

        sourcePondName = mPond.getName();
        edtPondName.setText(sourcePondName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_save:
                setPondName();
                break;
        }
    }

    private void setPondName() {
        // 没有登录的话就直接返回了
        if (!myTool.isLoginWithDialog()) {
            return;
        }

        mPondName = edtPondName.getText().toString();
        if (mPondName.equals(sourcePondName)) {
            myTool.showInfo("池塘名没有变化！");
            return;
        }

        if (!"".equals(mPondName)) {

            mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在修改···");
            mDialog.show();

            //更改服务器信息
            String url = ConstantValue.SERVICE_ADDRESS + "updatePond";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("pondId", mPond.getId())
                    .addParams("pondName", mPondName)
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
                                    intent.putExtra(KEY_PONDNAME, mPondName);
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
                            edtPondName.setError("还没有输入昵称哦！");
                        }
                    })
                    .playOn(edtPondName);
        }
    }
}
