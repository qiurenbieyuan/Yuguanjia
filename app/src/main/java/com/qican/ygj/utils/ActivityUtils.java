/* 
 * @ProjectName VideoGo
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName UIUtils.java
 * @Description 这里对文件进行描述
 * 
 * @author chenxingyf1
 * @data 2015-4-23
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.qican.ygj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.videogo.exception.BaseException;
import com.videogo.openapi.EZGlobalSDK;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZAreaInfo;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;

import java.io.File;
import java.util.List;

//import com.videogo.login.LoginActivity;
//import com.videogo.login.LoginAgainActivity;
//import com.videogo.login.VerifyHardwareSignatresActivity;
//import com.videogo.main.CustomApplication;
//import com.videogo.main.EmptyActivity;
//import com.videogo.main.MainTabActivity;
//import com.videogo.personal.UserTerminalActivity;
//import com.videogo.stat.HikAction;

/**
 * 界面跳转
 *
 * @author chenxingyf1
 * @data 2015-4-23
 */
public class ActivityUtils {
    /**
     * 处理session过期的错误
     *
     * @throws
     */
    public static void handleSessionException(Activity activity) {
        LocalInfo localInfo = LocalInfo.getInstance();
//        if (TextUtils.isEmpty(localInfo.getUserName())) {
//            return;
//        }

        goToLoginAgain(activity);

    }

    public static void goToLoginAgain(Activity activity) {
//        Intent intent = new Intent(activity, LoginAgainActivity.class);
//        activity.startActivity(intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EZAreaInfo> areaList = EZGlobalSDK.getInstance().getAreaList();
                    if (areaList != null) {
                        LogUtil.debugLog("application", "list count: " + areaList.size());

                        EZAreaInfo areaInfo = areaList.get(0);
                        EZGlobalSDK.getInstance().openLoginPage(areaInfo.getId());
                    }
                } catch (BaseException e) {
                    e.printStackTrace();

                    EZOpenSDK.getInstance().openLoginPage();
                }
            }
        }).start();
    }

    public static void goToLogin(Activity activity, boolean autoLogin) {
        // CustomApplication application = (CustomApplication) activity.getApplication();
        // application.finishAllSingleActivitys();
        // if (application.getMainTabActivity() != null) {
        // application.getMainTabActivity().finish();
        // }
//        Intent intent = new Intent(activity, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra(IntentConsts.EXTRA_AUTO_LOGIN, autoLogin);
//        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.alpha_fake_fade);
    }

    public static void goToLogin(Activity activity) {
        goToLogin(activity, false);
    }

    /**
     * 处理硬件特征码错误的情况
     *
     * @throws
     */
    public static void handleHardwareError(Context context, Bundle bundle) {
//        Intent intent = new Intent(context, VerifyHardwareSignatresActivity.class);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(R.anim.fade_up, R.anim.alpha_fake_fade);
    }

    public static void handleLoginTerminalSuperLimit(final Activity activity, final boolean gotoLogin) {/*
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setMessage(R.string.login_terminal_super_limit)
                .setPositiveButton(R.string.certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HikStat.onEvent(activity, HikAction.ACTION_user_terminal_limit_confirm);
                        Intent intent = new Intent(activity, UserTerminalActivity.class);
                        intent.putExtra(UserTerminalActivity.USER_TERMINAL_ACTION,
                                UserTerminalActivity.USER_TERMINAL_CHECK_MODE);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.fade_up, R.anim.alpha_fake_fade);
                        activity.finish();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HikStat.onEvent(activity, HikAction.ACTION_user_terminal_limit_cancel);
                        if (gotoLogin) {
                            goToLogin(activity);
                            activity.finish();
                        }
                    }
                }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        // TODO Auto-generated method stub
                        return true;
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if (!activity.isFinishing()) {
            try {
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    */
    }

    public static void goToMainTab(Activity activity) {
//        if (((CustomApplication) activity.getApplication()).getMainTabActivity() != null) {
//            Intent intent = new Intent(activity, EmptyActivity.class);
//            activity.startActivity(intent);
//        } else {
//            Intent intent = new Intent(activity, MainTabActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            activity.startActivity(intent);
//        }
    }

    public static void pictureFromCamera(Activity activity, int resultCode, String targetFileName) {
        File filePath = new File(LocalInfo.getInstance().getFilePath(), "/Temp");
        if (!filePath.exists())
            filePath.mkdirs();
        File file = new File(filePath, targetFileName);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        activity.startActivityForResult(intent, resultCode);
    }

    public static void cropPicture(Activity activity, int resultCode, File srcFile, String targetFileName,
                                   int cropWidth, int cropHeight) {
        File filePath = new File(LocalInfo.getInstance().getFilePath(), "/Temp");
        if (!filePath.exists())
            filePath.mkdirs();
        File file = new File(filePath, targetFileName);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(srcFile), "image/*"); // 获取任意类型的图片
        intent.putExtra("crop", "true"); // 滑动选中图片区域
        intent.putExtra("circleCrop", "false");
        intent.putExtra("aspectX", 1); // 表示剪切框的比例为1:1
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", cropWidth); // 指定输出图片的大小
        intent.putExtra("outputY", cropHeight);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        activity.startActivityForResult(intent, resultCode);
    }

    public static void pictureFromAlbum(Activity activity, int resultCode, String targetFileName, int cropWidth,
                                        int cropHeight) {
        File filePath = new File(LocalInfo.getInstance().getFilePath(), "/Temp");
        if (!filePath.exists())
            filePath.mkdirs();
        File file = new File(filePath, targetFileName);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.setType("image/*"); // 获取任意类型的图片
        intent.putExtra("crop", "true"); // 滑动选中图片区域
        intent.putExtra("aspectX", 1); // 表示剪切框的比例为1:1
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", cropWidth); // 指定输出图片的大小
        intent.putExtra("outputY", cropHeight);
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, resultCode);
    }

    public static void pictureFromAlbum(Activity activity, int resultCode, String targetFileName) {
        File filePath = new File(LocalInfo.getInstance().getFilePath(), "/Temp");
        if (!filePath.exists())
            filePath.mkdirs();
        File file = new File(filePath, targetFileName);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.setType("image/*"); // 获取任意类型的图片
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, resultCode);
    }
}