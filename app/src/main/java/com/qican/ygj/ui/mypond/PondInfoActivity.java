package com.qican.ygj.ui.mypond;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.qican.ygj.R;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.listener.BeanCallBack;
import com.qican.ygj.listener.OnDialogListener;
import com.qican.ygj.ui.userinfo.PicChooseDialog;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;


public class PondInfoActivity extends TakePhotoActivity implements View.OnClickListener, OnDialogListener {

    private static final String TAG = "PondInfoActivity";
    private static final int REQUEST_POND_NAME = 1;//修改池塘名
    private static final int REQUEST_POND_DESC = 2;//修改池塘描述

    private CommonTools myTool;
    private TextView tvPondInfo, tvPondName, tvPondDesc;
    private RelativeLayout rlPondInfo, rlBack, rlPondName, rlPondDesc, rlModifyCover;
    private Pond mPond;
    private File pondFile;
    private ImageView ivPondImg;
    private String pondCover;
    private ProgressBar pbUpdateCover;

    private PicChooseDialog mPondDialog;
    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(200 * 1024).setMaxPixel(600).create();
    CropOptions cropOptions = new CropOptions.Builder().setAspectX(3).setAspectY(2).setWithOwnCrop(true).create();
    private TakePhoto takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pondinfo);

        initView();
        initData();
        initEvent();
    }

    private void initData() {
        rlBack.setVisibility(View.GONE);
        //获得上一页传过来的参数
        mPond = new Pond();
        mPond = (Pond) myTool.getParam(mPond);

        tvPondInfo.setText(mPond.getName() + "：" + mPond.getDesc());
        tvPondName.setText(mPond.getName());
        tvPondDesc.setText(mPond.getDesc());

        myTool.showImage(mPond.getImgUrl(), ivPondImg);
    }

    private void initView() {
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);

        tvPondInfo = (TextView) findViewById(R.id.tv_pond_info);
        tvPondName = (TextView) findViewById(R.id.tv_pond_name);
        tvPondDesc = (TextView) findViewById(R.id.tv_pond_desc);

        rlPondInfo = (RelativeLayout) findViewById(R.id.rl_pond_info);
        rlPondName = (RelativeLayout) findViewById(R.id.rl_pond_name);
        rlPondDesc = (RelativeLayout) findViewById(R.id.rl_pond_desc);
        rlModifyCover = (RelativeLayout) findViewById(R.id.rl_modify_cover);
        pbUpdateCover = (ProgressBar) findViewById(R.id.pb_updatecover);

        takePhoto = getTakePhoto();

        ivPondImg = (ImageView) findViewById(R.id.iv_pond_img);
        mPondDialog = new PicChooseDialog(this, R.style.Translucent_NoTitle);
        mPondDialog.setOnDialogListener(this);

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        rlPondInfo.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        rlPondName.setOnClickListener(this);
        rlPondDesc.setOnClickListener(this);
        rlModifyCover.setOnClickListener(this);
    }


    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.rl_pond_info:
                showBackMenu();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_pond_name:
                myTool.startActivityForResult(mPond, PondNameActivity.class, REQUEST_POND_NAME);
                break;
            case R.id.rl_pond_desc:
                myTool.startActivityForResult(mPond, PondDescActivity.class, REQUEST_POND_DESC);
                break;
            case R.id.rl_modify_cover:
                mPondDialog.show();
                break;
        }
    }

    //显示退出菜单
    private void showBackMenu() {

        if (rlBack.getVisibility() == View.VISIBLE) {
            //重新开始计时
            hideBackTimer.cancel();
            hideBackTimer.start();
            return;
        }

        rlBack.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(rlBack);
        hideBackTimer.start();
    }

    private CountDownTimer hideBackTimer = new CountDownTimer(3000, 3000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            YoYo.with(Techniques.FadeOut)
                    .duration(1000)
                    .withListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            rlBack.setVisibility(View.GONE);
                        }
                    })
                    .playOn(rlBack);
        }
    };

    private void uploadPondCover() {

        if (pondCover == null) {
            myTool.showInfo("图片选择失败，请重新选取！");
            return;
        }
        pbUpdateCover.setVisibility(View.VISIBLE);
        pondFile = new File(pondCover);

        String url = ConstantValue.SERVICE_ADDRESS + "uploadPondHeadImage";
        Log.i(TAG, "addPond: userId[" + myTool.getUserId() + "]");

//        myTool.showInfo("pondId[" + mPond.getId() + "],url:[" + mPond.getImgUrl() + "]");

        //上传池塘头像
        OkHttpUtils
                .post()
                .url(url)
                .addParams("pondId", mPond.getId())
                .addFile("mFile", "pond_" + mPond.getId() + ".png", pondFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pbUpdateCover.setVisibility(View.GONE);
                        myTool.showInfo("response[" + e.toString() + "]");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        switch (response) {
                            case "success":
                                //重新查询封面
                                showImageFromService();
                                break;
                            case "error":
                                pbUpdateCover.setVisibility(View.GONE);
                                myTool.showInfo("上传失败，稍后从事！");
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POND_NAME && resultCode == RESULT_OK) {
            String pondName = data.getExtras().getString(PondNameActivity.KEY_PONDNAME);
            mPond.setName(pondName);
            tvPondName.setText(pondName);
            tvPondInfo.setText(pondName + "：" + mPond.getDesc());
        }
        if (requestCode == REQUEST_POND_DESC && resultCode == RESULT_OK) {
            String pondDesc = data.getExtras().getString(PondDescActivity.KEY_POND_DESC);
            mPond.setDesc(pondDesc);
            tvPondDesc.setText(pondDesc);
            tvPondInfo.setText(mPond.getName() + "：" + pondDesc);
        }
    }

    @Override
    public void dialogResult(Dialog dialog, String msg) {
        switch (msg) {
            case PicChooseDialog.FORM_CAMERA:
                takePhoto.onEnableCompress(compressConfig, false);
                takePhoto.onPickFromCaptureWithCrop(myTool.getPondFileUri(), cropOptions);
                break;
            case PicChooseDialog.FROM_FILE:
                takePhoto.onEnableCompress(compressConfig, false);
                takePhoto.onPickFromDocumentsWithCrop(myTool.getPondFileUri(), cropOptions);
                break;
        }
    }

    @Override
    public void takeSuccess(final TResult result) {
        super.takeSuccess(result);
        Log.i(TAG, "takeSuccess,图片路径： " + result.getImage().getPath());
        pondCover = result.getImage().getPath();
        uploadPondCover();
    }

    /**
     * 显示池塘封面图片到图片预览上
     */
    private void showImageFromService() {

        String url = ConstantValue.SERVICE_ADDRESS + "selectPond";
        Log.i(TAG, "addPond: userId[" + myTool.getUserId() + "]");

        OkHttpUtils
                .post()
                .url(url)
                .addParams("pondId", mPond.getId())
                .build()
                .execute(new BeanCallBack<com.qican.ygj.beanfromzhu.Pond>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pbUpdateCover.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(com.qican.ygj.beanfromzhu.Pond pond, int id) {
                        Log.i(TAG, "onResponse: URL--> " + pond.getPondImageUrl());
                        pbUpdateCover.setVisibility(View.GONE);
                        if (pond != null) {
                            myTool.showImage(pond.getPondImageUrl(), ivPondImg);
                        }
                    }
                });
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        myTool.showInfo("选择失败:" + msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        myTool.showInfo("取消选择");
    }
}
