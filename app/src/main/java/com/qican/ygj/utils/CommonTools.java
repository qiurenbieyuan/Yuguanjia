/**
 * 通用工具类
 *
 * @时间：2016-7-6
 */
package com.qican.ygj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.qican.ygj.R;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.bean.Pump;
import com.qican.ygj.listener.LoggingListener;
import com.qican.ygj.task.CommonTask;
import com.qican.ygj.ui.SettingsActivity;
import com.qican.ygj.ui.login.LoginActivity;
import com.qican.ygj.ui.mypond.PondInfoActivity;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.File;
import java.io.Serializable;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class CommonTools {

    //    private final static String ALBUM_PATH
//            = Environment.getExternalStorageDirectory() + "/download_test/";
    public static final String PACKAGE_NAME = "com.qican.ygj";

    public static final String USER_FILE_PATH = Environment.getExternalStorageDirectory() + "/"
            + PACKAGE_NAME; //用户文件

    public static final String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/"
            + PACKAGE_NAME;

    private Context mContext;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private CropHelper mCropHelper;

    public CommonTools(Context context) {
        this.mContext = context;

        sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * Toast消息
     *
     * @param msg
     * @param duration
     */
    public void showInfo(String msg, int duration) {
        Toast.makeText(mContext, msg, duration).show();
    }

    public void showInfo(String msg) {
        showInfo(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 根据运行状态，显示增氧机
     *
     * @param runningState
     * @param imageView
     * @return
     */
    public CommonTools showPumpByState(String runningState, ImageView imageView, final ImageView fans) {
        Bitmap pumpOnImg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pump_on);
        Bitmap pumpOFFImg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pump_off);
        final RotateAnimation fansAnim = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.pump_running);

        //设置性别图标
        switch (runningState) {
            case Pump.PUMP_ON:
                imageView.setImageBitmap(pumpOnImg);
                //风扇转起来
                fans.setVisibility(View.VISIBLE);
                fans.startAnimation(fansAnim);
                break;
            case Pump.PUMP_OFF:
                imageView.setImageBitmap(pumpOFFImg);
                fans.setVisibility(View.GONE);
                fans.clearAnimation();
                break;
        }
        return this;
    }

    /**
     * 从网络平滑加载图片到指定ImageView上
     *
     * @param url
     * @param imageView
     */
    public CommonTools showImage(String url, ImageView imageView) {
//        Picasso.with(mContext).load(url).into(imageView);
        Glide.with(mContext).load(url).listener(new LoggingListener<String, GlideDrawable>()).centerCrop()
                .crossFade().into(imageView);
//        Glide.with(mContext).load(url).centerCrop().error(R.drawable.defaultimage)
//                .crossFade().into(imageView);
        return this;
    }

    public CommonTools showImage(String url, ImageView imageView, @DrawableRes int errRes) {
        Glide.with(mContext)
                .load(url)
                .listener(new LoggingListener<String, GlideDrawable>())
                .centerCrop().error(errRes)
                .crossFade().into(imageView);
        return this;
    }

    public CommonTools showImageByOkHttp(String url, final ImageView imageView) {
        OkHttpUtils
                .get()//
                .url(url)//
                .build()//
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showInfo("异常：" + e.toString());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        imageView.setImageBitmap(bitmap);
                    }
                });
        return this;
    }

    /**
     * 从网络平滑加载图片到指定ImageView上
     *
     * @param url
     * @param imageView
     */
    public CommonTools showImageWithoutCrop(String url, ImageView imageView) {

        Glide.with(mContext).load(url).error(R.drawable.defaultimage)
                .crossFade().into(imageView);

        return this;
    }

    /**
     * 从路径中得到bitmap
     */
    public Bitmap getBitmapByPath(String path) {

        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(path, null);

        return bitmap;
    }

    /**
     * 启动activity
     *
     * @param activity
     */
    public void startActivity(Class<?> activity) {
        try {
            mContext.startActivity(new Intent(mContext, activity));
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    /**
     * 带参数的启动
     *
     * @param o
     * @param activity
     * @return
     */
    public CommonTools startActivity(Serializable o, Class<?> activity) {
        try {
            //跳转到详细信息界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(o.getClass().getName(), o);
            Intent intent = new Intent(mContext, activity);
            intent.putExtras(bundle);

            mContext.startActivity(intent);
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return this;
    }

    /**
     * 得到上一页面传递过来的参数
     *
     * @param o
     * @return
     */
    public Serializable getParam(@NonNull Serializable o) {
        Bundle bundle = ((Activity) mContext).getIntent().getExtras();
        return (Serializable) bundle.get(o.getClass().getName());
    }

    /**
     * 启动activity,得到返回结果
     *
     * @param activity
     */
    public void startActivityForResult(Class<?> activity, int requestCode) {
        try {
            ((Activity) mContext).startActivityForResult(new Intent(mContext, activity), requestCode);
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
    }

    /**
     * 带参数的启动
     *
     * @param o
     * @param activity
     * @return
     */
    public CommonTools startActivityForResult(Serializable o, Class<?> activity, int requestCode) {
        try {
            //跳转到详细信息界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(o.getClass().getName(), o);
            Intent intent = new Intent(mContext, activity);
            intent.putExtras(bundle);

            ((Activity) mContext).startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("异常！")
                    .setContentText("启动Activity失败！[e:" + e.toString() + "]")
                    .setConfirmText("确  定!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return this;
    }

    /**
     * 设置用户登录标志
     *
     * @param isLogin
     * @return
     */
    public CommonTools setLoginFlag(boolean isLogin) {
        editor.putBoolean(ConstantValue.KEY_ISLOGIN, isLogin);
        editor.commit();
        return this;
    }

    /**
     * 判断用户是否登录
     *
     * @return true-已登录，false-未登录
     */
    public boolean isLogin() {
        return sp.getBoolean(ConstantValue.KEY_ISLOGIN, false);
    }

    public CommonTools setUserName(String userName) {
        editor.putString(ConstantValue.KEY_USERNAME, userName);
        editor.commit();
        return this;
    }

    public String getUserName() {
        return sp.getString(ConstantValue.KEY_USERNAME, "");
    }

    public CommonTools setNickName(String nickName) {
        editor.putString(ConstantValue.KEY_NICKNAME, nickName);
        editor.commit();
        return this;
    }

    public String getNickName() {
        return sp.getString(ConstantValue.KEY_NICKNAME, "小鱼_001");
    }

    public CommonTools setUserSex(String userSex) {
        editor.putString(ConstantValue.KEY_SEX, userSex);
        editor.commit();
        return this;
    }

    public String getUserSex() {
        return sp.getString(ConstantValue.KEY_SEX, "小鱼_001");
    }

    public CommonTools setUserHeadURL(String url) {
        editor.putString(ConstantValue.KEY_HEADURL, url);
        editor.commit();
        return this;
    }

    public String getUserHeadURL() {
        return sp.getString(ConstantValue.KEY_HEADURL, "");
    }

    /**
     * 用户签名
     *
     * @param autograph
     * @return
     */
    public CommonTools setAutograph(String autograph) {
        editor.putString(ConstantValue.KEY_AUTOGRAPH, autograph);
        editor.commit();
        return this;
    }

    public String getSignature() {
        return sp.getString(ConstantValue.KEY_AUTOGRAPH, "这个家伙很懒，没有留下什么！");
    }

    /**
     * 带有提示框的登录状态检测
     *
     * @return
     */
    public boolean isLoginWithDialog() {
        // 未登录则提示
        if (!isLogin()) {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("未登录")
                    .setContentText("您还没有登录唷,亲!")
                    .setConfirmText("立即登录")
                    .setCancelText("取  消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            startActivity(LoginActivity.class);
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        }
        return isLogin();
    }

    /**
     * 获取默认池塘封面
     *
     * @return
     */
    public File getDefaultPondImg() {

        mCropHelper = new CropHelper((Activity) mContext, OSUtils.getSdCardDirectory() + "/pond_img.png");

        File file = null;
        Bitmap pondBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_pond);

        if (BitmapUtils.saveFile(pondBitmap, USER_FILE_PATH + "/img/default_pond_img.png")) {
            file = new File(USER_FILE_PATH + "/img/default_pond_img.png");
        } else {
            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("失败")
                    .setContentText("默认封面设置失败!")
                    .show();
            file = new File("");
        }
        return file;
    }

    public String getUserId() {
        return getUserName();
    }
}
