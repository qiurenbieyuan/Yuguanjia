/**
 * 通用工具类
 *
 * @时间：2016-7-6
 */
package com.qican.ygj.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qican.ygj.R;
import com.qican.ygj.bean.Pump;

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

        Glide.with(mContext).load(url).centerCrop().error(R.drawable.defaultimage)
                .crossFade().into(imageView);

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
}
