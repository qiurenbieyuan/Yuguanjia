/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.login;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class RegisterActivity extends Activity implements View.OnClickListener {
    private CommonTools myTool;
    private LinearLayout llBack;
    private EditText edtUserName, edtPassword, edtConPwd;
    private ImageView ivDelUserName, ivDelPassword, ivDelConPwd;
    private Button btnRegister;
    private String userName, password, confirmPwd;
    private SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        ivDelUserName.setOnClickListener(this);
        ivDelPassword.setOnClickListener(this);
        ivDelConPwd.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        setTextListener();
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        edtUserName = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConPwd = (EditText) findViewById(R.id.edt_conpwd);
        ivDelUserName = (ImageView) findViewById(R.id.iv_del_username);
        ivDelPassword = (ImageView) findViewById(R.id.iv_del_password);
        ivDelConPwd = (ImageView) findViewById(R.id.iv_del_conpwd);

        btnRegister = (Button) findViewById(R.id.btn_register);

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
            case R.id.iv_del_username:
                edtUserName.setText("");
                break;
            case R.id.iv_del_password:
                edtPassword.setText("");
                break;
            case R.id.iv_del_conpwd:
                edtConPwd.setText("");
                break;
            case R.id.btn_register:
                attempRegister();//登录
                break;
        }
    }

    private void attempRegister() {

        userName = edtUserName.getText().toString().trim();
        password = edtPassword.getText().toString();
        confirmPwd = edtConPwd.getText().toString();

        if ("".equals(userName)) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtUserName.requestFocus();
                    edtUserName.setError("请输入手机号码！");
                }
            })
                    .playOn(edtUserName);
            return;
        }
        if (userName.length() != 11) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtUserName.requestFocus();
                    edtUserName.setError("格式有误，11位手机号码！");
                }
            })
                    .playOn(edtUserName);
            return;
        }
        if (password.length() < 6) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtPassword.requestFocus();
                    edtPassword.setError("密码在6位以上！");
                }
            })
                    .playOn(edtPassword);
            return;
        }
        if (!password.equals(confirmPwd)) {
            YoYo.with(Techniques.Shake)
                    .duration(700).withListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    edtConPwd.requestFocus();
                    edtConPwd.setError("两次密码不一致！");
                }
            })
                    .playOn(edtConPwd);
            return;
        }

//        YoYo.with(Techniques.Bounce)
//                .duration(1000)
//                .withListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        myTool.showInfo("注册成功！");
//                        finish();
//                    }
//                })
//                .playOn(btnRegister);

        mDialog = null;
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在注册···");
        mDialog.show();

        String url = ConstantValue.SERVICE_ADDRESS + "register";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", userName)
                .addParams("userPwd", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        toOtherCase(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        switch (response) {
                            case "success":
                                toLoginSuccess(response);
                                break;
                            case "error":
                                mDialog.setTitleText("错误")
                                        .setContentText("用户已存在或其他系统错误！")
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                break;
                            default:
                                toOtherCase(response);
                                break;
                        }
                    }
                });
    }

    private void toOtherCase(String response) {
        mDialog.setTitleText("提示")
                .setContentText("服务器返回的信息：" + response + "！")
                .changeAlertType(SweetAlertDialog.WARNING_TYPE);
    }

    private void toLoginSuccess(String response) {
        mDialog.setTitleText("注册成功")
                .setContentText("欢迎注册鱼管家，从此开启智能养鱼Style！[" + response + "]")
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
    }

    private void setTextListener() {
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showIvDelByEdt(s, ivDelUserName);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showIvDelByEdt(s, ivDelPassword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtConPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showIvDelByEdt(s, ivDelConPwd);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 根据输入信息显示删除控件
     *
     * @param s
     * @param ivDel
     */
    private void showIvDelByEdt(CharSequence s, final ImageView ivDel) {
        if (s.length() != 0) {
            //没有显示时就不做操作
            if (ivDel.getVisibility() == View.GONE) {
                ivDel.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.ZoomIn)
                        .duration(700)
                        .playOn(ivDel);
            }
        } else {
            YoYo.with(Techniques.ZoomOut)
                    .duration(700)
                    .withListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ivDel.setVisibility(View.GONE);
                        }
                    })
                    .playOn(ivDel);
        }
    }
}
