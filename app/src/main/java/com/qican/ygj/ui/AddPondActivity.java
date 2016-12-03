/**
 * 添加池塘
 */
package com.qican.ygj.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.qican.ygj.R;
import com.qican.ygj.listener.OnDialogListener;
import com.qican.ygj.ui.mypond.PreviewActivity;
import com.qican.ygj.ui.userinfo.PicChooseDialog;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class AddPondActivity extends TakePhotoActivity implements View.OnClickListener, OnDialogListener {
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final String TAG = "AddPondActivity";
    private LinearLayout llBack, llAddPond;
    private ImageView ivAddimg, ivClose;
    private CommonTools myTool;
    private String pondCoverAddress = null;
    private ImageView ivImgDesc;
    private EditText edtPondName, edtPondDesc;
    private String pondName, pondDesc;
    private SweetAlertDialog mDialog;

    private PicChooseDialog mPondDialog;
    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(200 * 1024).setMaxPixel(600).create();
    CropOptions cropOptions = new CropOptions.Builder().setAspectX(3).setAspectY(2).setWithOwnCrop(true).create();
    private TakePhoto takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpond);
        initView();
        initEvent();
        initImageSelCon();
    }

    private void initImageSelCon() {

    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llAddPond = (LinearLayout) findViewById(R.id.ll_add);

        ivAddimg = (ImageView) findViewById(R.id.iv_addimg);
        ivImgDesc = (ImageView) findViewById(R.id.iv_imgdesc);
        ivClose = (ImageView) findViewById(R.id.iv_close);

        edtPondName = (EditText) findViewById(R.id.edt_pond_name);
        edtPondDesc = (EditText) findViewById(R.id.edt_pond_desc);
        mPondDialog = new PicChooseDialog(this, R.style.Translucent_NoTitle);
        takePhoto = getTakePhoto();

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        llAddPond.setOnClickListener(this);

        ivAddimg.setOnClickListener(this);
        ivImgDesc.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        mPondDialog.setOnDialogListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_addimg:
                //添加图片
                mPondDialog.show();
                break;
            case R.id.iv_imgdesc:
                Bundle bundle = new Bundle();
                bundle.putString("path", pondCoverAddress);
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_close:
                ivImgDesc.setVisibility(View.GONE);
                ivClose.setVisibility(View.GONE);
                ivAddimg.setVisibility(View.VISIBLE);
                pondCoverAddress = null;
                break;
            case R.id.ll_add:
                //添加池塘
                addPond();
                break;
        }
    }

    private void addPond() {
        //没有登录就不能添加
        if (!myTool.isLoginWithDialog()) {
            return;
        }

        pondName = edtPondName.getText().toString().trim();
        pondDesc = edtPondDesc.getText().toString();

        if (pondName.isEmpty()) {
            //震动View
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(edtPondName);
            return;
        }

        if (pondDesc.isEmpty()) {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(edtPondDesc);
            return;
        }

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在添加···");
        mDialog.show();

        File pondFile = null;
        if (pondCoverAddress == null) {
            pondFile = myTool.getDefaultPondImg();
        } else {
            pondFile = new File(pondCoverAddress);
        }

        Log.i(TAG, "addPond: " + pondFile.toString());

        String url = ConstantValue.SERVICE_ADDRESS + "uploadPondHeadImage";
        Log.i(TAG, "addPond: pondName[" + pondName + "],pondDesc[" + pondDesc + "],userId[" + myTool.getUserName() + "]");

        //上传池塘头像
        OkHttpUtils
                .post()
                .url(url)
                .addParams("userId", myTool.getUserName())
                .addParams("pondName", pondName)
                .addParams("pondDescrible", pondDesc)
                .addFile("mFile", pondName + "封面.png", pondFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDialog.setTitleText("添加失败")
                                .setContentText("添加失败，异常信息 e-->[" + e.toString() + "]")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        switch (response) {
                            case "error":
                                //添加池塘信息,成功的提示对话框
                                mDialog.setTitleText("添加失败!")
                                        .setContentText("服务器出现了错误，请稍后再试!")
                                        .setConfirmText("好  的")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        }).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                break;
                            case "success":
                                //添加池塘信息,成功的提示对话框
                                mDialog.setTitleText("添加成功!")
                                        .setContentText("你的鱼塘尽在掌握之中,记得及时回来打理唷! ")
                                        .setConfirmText("完  成")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
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
                            default:
                                //添加池塘信息,成功的提示对话框
                                mDialog.setTitleText("其他信息：" + response)
                                        .setConfirmText("完  成")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        }).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                break;
                        }
                    }
                });
    }

    /**
     * 显示池塘封面图片到图片预览上
     *
     * @param path
     */
    private void showImageByPath(String path) {
        if (path != null) {
            ivImgDesc.setVisibility(View.VISIBLE);
            ivAddimg.setVisibility(View.GONE);
            ivClose.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivImgDesc.setImageBitmap(bitmap);
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
        pondCoverAddress = result.getImage().getPath();
        showImageByPath(pondCoverAddress);
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
