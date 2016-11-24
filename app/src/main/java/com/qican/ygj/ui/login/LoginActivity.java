/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.qican.ygj.bean.PostResult;
import com.qican.ygj.listener.OnTaskListener;
import com.qican.ygj.task.CommonTask;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class LoginActivity extends Activity implements View.OnClickListener, OnTaskListener {

    private static final int TASKID_LOGIN = 1;
    private static final int REQUESR_FOR_USERNAME = 001;//注册成功返回用户名和密码

    private CommonTools myTool;
    private LinearLayout llBack;
    private EditText edtUserName, edtPassword;
    private ImageView ivDelUserName, ivDelPassword, ivHeadImg;
    private Button btnLogin, btnRegister;
    private SweetAlertDialog mLoginDialog;
    //登录任务
    private CommonTask<String> mLoginTask;
    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();

        initEvent();
    }

    private void initData() {
        //如果有存用户名则显示出来
        if (!"".equals(myTool.getUserName())) {
            edtUserName.setText(myTool.getUserName());
            ivDelUserName.setVisibility(View.VISIBLE);
            edtPassword.requestFocus();
        }
        if (!"".equals(myTool.getUserHeadURL())) {
            myTool.showImage(myTool.getUserHeadURL(), ivHeadImg);
        }
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        ivDelUserName.setOnClickListener(this);
        ivDelPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        setTextListener();
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        edtUserName = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        ivDelUserName = (ImageView) findViewById(R.id.iv_del_username);
        ivDelPassword = (ImageView) findViewById(R.id.iv_del_password);
        ivHeadImg = (ImageView) findViewById(R.id.iv_headimg);

        btnLogin = (Button) findViewById(R.id.btn_login);
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
            case R.id.btn_login:
                attempLogin();//登录
                break;
            case R.id.btn_register:
                myTool.startActivityForResult(RegisterActivity.class, REQUESR_FOR_USERNAME);
                break;
        }
    }

    private void attempLogin() {

        userName = edtUserName.getText().toString().trim();
        password = edtPassword.getText().toString();

        if ("".equals(edtUserName.getText().toString().trim())) {
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
        if (edtUserName.getText().toString().trim().length() != 11) {
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
        if (edtPassword.getText().toString().length() < 6) {
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
        Map<String, String> map = new HashMap<>();

        mLoginTask = new CommonTask<String>(this, TASKID_LOGIN) {
            @Override
            public PostResult getResult(Map inputPara) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return new PostResult<String>().setResult("success");
            }
        };
        mLoginTask.setOnTaskFinishListener(this);
//        mLoginTask.execute(map);//开始执行

        mLoginDialog = null;
        mLoginDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在登录···");
        mLoginDialog.show();

        String url = ConstantValue.SERVICE_ADDRESS + "login";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("userId", userName)
                .addParams("userPwd", password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mLoginDialog.setTitleText("登录失败")
                                .setContentText("登录失败，异常信息 e-->[" + e.toString() + "]")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        mLoginDialog = null;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        loginResponse(response);
                    }


                });

    }

    private void loginResponse(String result) {
        switch (result) {
            case "success":
                //保存一些设置
                myTool.setLoginFlag(true);
                myTool.setUserName(edtUserName.getText().toString().trim());
                myTool.setUserHeadURL("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=571129321,501172449&fm=21&gp=0.jpg");

                mLoginDialog.setTitleText("登录成功")
                        .setContentText("登录成功，鱼管家欢迎您！")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                            }
                        })
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                new CountDownTimer(6000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        mLoginDialog.setConfirmText("立即开启（" + millisUntilFinished / 1000 + "s）");
                    }

                    @Override
                    public void onFinish() {
                        finish();
                    }
                }.start();
                break;
            case "error":
                mLoginDialog.setTitleText("错误")
                        .setContentText("用户已存在或其他系统错误！")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                break;
            default:
                mLoginDialog.setTitleText("提示")
                        .setContentText("服务器返回的信息：" + result + "！")
                        .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                break;
        }
    }

    private void setTextListener() {
        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    //没有显示时就不做操作
                    if (ivDelUserName.getVisibility() == View.GONE) {
                        ivDelUserName.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.ZoomIn)
                                .duration(700)
                                .playOn(findViewById(R.id.iv_del_username));
                    }
                } else {
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ivDelUserName.setVisibility(View.GONE);
                                }
                            })
                            .playOn(findViewById(R.id.iv_del_username));
                }
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
                if (s.length() != 0) {
                    //没有显示时就不做操作
                    if (ivDelPassword.getVisibility() == View.GONE) {
                        ivDelPassword.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.ZoomIn)
                                .duration(700)
                                .playOn(findViewById(R.id.iv_del_password));
                    }
                } else {
                    YoYo.with(Techniques.ZoomOut)
                            .duration(700)
                            .withListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ivDelPassword.setVisibility(View.GONE);
                                }
                            })
                            .playOn(findViewById(R.id.iv_del_password));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void taskFailed(CommonTask t, Exception e) {
        mLoginTask = null;
        switch (t.getTaskID()) {
            case TASKID_LOGIN:
                mLoginDialog.setTitleText("登录失败")
                        .setContentText("登录失败，异常信息 e-->[" + e.toString() + "]")
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                mLoginDialog = null;
                break;
        }
    }

    @Override
    public void taskSuccess(CommonTask t, Object result) {
        mLoginTask = null;
        switch (t.getTaskID()) {
            case TASKID_LOGIN:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册成功返回用户名和密码
        if (requestCode == REQUESR_FOR_USERNAME) {

        }
    }
}
